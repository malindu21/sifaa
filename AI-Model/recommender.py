from operator import le
from contextlib import nullcontext
from replay_buffer import PriorityExperienceReplay
import tensorflow as tf
import numpy as np
from tensorflow.python.ops.gen_math_ops import Exp

from actor import Actor
from critic import Critic
from replay_memory import ReplayMemory
from embedding import FloralGenreEmbedding, UserFloralEmbedding
from state_representation import DRRAveStateRepresentation
import json
import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity

from flask import Flask,request,jsonify
from flask_cors import CORS

import matplotlib.pyplot as plt

import wandb

import envs as eenv


hi= []
hi2 =[]
favplant = []
episodeSplit = 90

class DRRAgent:
    
    def __init__(self, env, users_num, items_num, state_size, is_test=False, use_wandb=False):
        
        self.env = env

        self.users_num = users_num
        self.items_num = items_num
        
        self.embedding_dim = 100
        self.actor_hidden_dim = 128
        self.actor_learning_rate = 0.001
        self.critic_hidden_dim = 128
        self.critic_learning_rate = 0.001
        self.discount_factor = 0.9
        self.tau = 0.001

        self.replay_memory_size = 1000000
        self.batch_size = 32
        
        self.actor = Actor(self.embedding_dim, self.actor_hidden_dim, self.actor_learning_rate, state_size, self.tau)
        self.critic = Critic(self.critic_hidden_dim, self.critic_learning_rate, self.embedding_dim, self.tau)
        
        # self.m_embedding_network = FloralGenreEmbedding(items_num, 19, self.embedding_dim)
        # self.m_embedding_network([np.zeros((1,)),np.zeros((1,))])
        # self.m_embedding_network.load_weights('/home/diominor/Workspace/DRR/save_weights/m_g_model_weights.h5')

        self.embedding_network = UserFloralEmbedding(users_num, items_num, self.embedding_dim)
        self.embedding_network([np.zeros((1,)),np.zeros((1,))])
        # self.embedding_network = UserFloralEmbedding(users_num, self.embedding_dim)
        # self.embedding_network([np.zeros((1)),np.zeros((1,100))])
        self.embedding_network.load_weights('save_weights/user_floral_embedding_case4.h5')

        self.srm_ave = DRRAveStateRepresentation(self.embedding_dim)
        self.srm_ave([np.zeros((1, 100,)),np.zeros((1,state_size, 100))])

        # PER
        self.buffer = PriorityExperienceReplay(self.replay_memory_size, self.embedding_dim)
        self.epsilon_for_priority = 1e-6

        # greedy exploration hyperparameter
        self.epsilon = 1.
        self.epsilon_decay = (self.epsilon - 0.1)/500000
        self.std = 1.5

        self.is_test = is_test
        

        # wandb
        self.use_wandb = use_wandb
        if use_wandb:
            wandb.init(project="drr", 
            entity="diominor",
            config={'users_num':users_num,
            'items_num' : items_num,
            'state_size' : state_size,
            'embedding_dim' : self.embedding_dim,
            'actor_hidden_dim' : self.actor_hidden_dim,
            'actor_learning_rate' : self.actor_learning_rate,
            'critic_hidden_dim' : self.critic_hidden_dim,
            'critic_learning_rate' : self.critic_learning_rate,
            'discount_factor' : self.discount_factor,
            'tau' : self.tau,
            'replay_memory_size' : self.replay_memory_size,
            'batch_size' : self.batch_size,
            'std_for_exploration': self.std})

    def calculate_td_target(self, rewards, q_values, dones):
        y_t = np.copy(q_values)
        for i in range(q_values.shape[0]):
            y_t[i] = rewards[i] + (1 - dones[i])*(self.discount_factor * q_values[i])
        return y_t

    def recommend_item(self, action, recommended_items, top_k=False, items_ids=None):
        if items_ids == None:
            items_ids = np.array(list(set(i for i in range(self.items_num)) - recommended_items))

        items_ebs = self.embedding_network.get_layer('floral_embedding')(items_ids)
        # items_ebs = self.m_embedding_network.get_layer('floral_embedding')(items_ids)
        action = tf.transpose(action, perm=(1,0))
        if top_k:
            item_indice = np.argsort(tf.transpose(tf.keras.backend.dot(items_ebs, action), perm=(1,0)))[0][-top_k:]
            return items_ids[item_indice]
        else:
            item_idx = np.argmax(tf.keras.backend.dot(items_ebs, action))
            return items_ids[item_idx]
        
    def train(self, max_episode_num, top_k=False, load_model=False):
        # Initialize target networks
        self.actor.update_target_network()
        self.critic.update_target_network()

        if load_model:
            self.load_model("save_weights/critic_50000.h5")
            print('Completely load weights!')

        episodic_precision_history = []

        for episode in range(max_episode_num):
            # episodic reward
            episode_reward = 0
            correct_count = 0
            steps = 0
            q_loss = 0
            mean_action = 0
            favplant.clear()
            # Environment
            user_id, items_ids, done = self.env.reset()
            print(f'user_id : {user_id}, rated_items_length:{len(self.env.user_items)}')
            print('user items : ', self.env.get_items_names(items_ids))
            print('user items : ', len(items_ids))
            for x in range(10):
                favplant.append({'fav':str(items_ids[x])})
            #print(json.dumps({"items ": (self.env.get_items_names(items_ids))}, indent=1))
            plantid=[]
            plant=[]
            
            hi.clear()
           
            # favplant.append((str(items_ids[0]),str(items_ids[0])))
            while not done:
                
                # Observe current state & Find action
                ## Embedding
                user_eb = self.embedding_network.get_layer('user_embedding')(np.array(user_id))
                items_eb = self.embedding_network.get_layer('floral_embedding')(np.array(items_ids))
                # items_eb = self.m_embedding_network.get_layer('floral_embedding')(np.array(items_ids))
                ## SRM으로 state
                state = self.srm_ave([np.expand_dims(user_eb, axis=0), np.expand_dims(items_eb, axis=0)])

                ## Action(ranking score)
                action = self.actor.network(state)

                ## ε-greedy exploration
                if self.epsilon > np.random.uniform() and not self.is_test:
                    self.epsilon -= self.epsilon_decay
                    action += np.random.normal(0,self.std,size=action.shape)

                ## Item 
                def get_title_from_index(index):
	                return florals_df[florals_df.MovieID == index]["Title"].values[0]

           
                #df = pd.read_csv("florals.dat")

                datContent = [i.strip().split("::") for i in open("./florals.dat").readlines()]
                florals_df = pd.DataFrame(datContent, columns = ['MovieID', 'Title', 'Genres'])
                florals_df['MovieID'] = florals_df['MovieID'].apply(pd.to_numeric)

                florals_id_to_florals = {floral[0]: floral[1:] for floral in datContent}

                #print(florals_id_to_florals)

                recommended_item = self.recommend_item(action, self.env.recommended_items, top_k=top_k)
                #print('items : ', {recommended_item})
                # print(f'recommended items ids : {recommended_item} 'f'Name:  {get_title_from_index(recommended_item)}')
                #newValue = get_title_from_index(recommended_item)
                #print(newValue)
                # print(f'recommened items : \n {np.array(self.env.get_items_names(recommended_item), dtype=object)}')
                ###### helper functions. Use them when needed #######


                

                ##Step 5: Compute the Cosine Similarity based on the count_matrix
                
              

                ## Step 6: Get index of this floral from its title
                
                floral_index = get_title_from_index(recommended_item)
                #floral_index2 = get_title_from_index(items_ids)

                #floral_index = pd.merge(get_title_from_index(recommended_item), florals_df, on="Index")
          
                
                
                plant.append(floral_index)
                plantid.append(recommended_item)
                
                #print(floral_index2)
                   
                #print(plant.to_json(orient="records"))
                
                #json_data = json.dumps(floral_index)
                #print(json_data)

                # Calculate reward & observe new state (in env)
                ## Step
                next_items_ids, reward, done, _ = self.env.step(recommended_item, top_k=top_k)
                if top_k:
                    reward = np.sum(reward)

                # get next_state
                next_items_eb = self.embedding_network.get_layer('floral_embedding')(np.array(next_items_ids))
                # next_items_eb = self.m_embedding_network.get_layer('floral_embedding')(np.array(next_items_ids))
                next_state = self.srm_ave([np.expand_dims(user_eb, axis=0), np.expand_dims(next_items_eb, axis=0)])

                # buffer에 저장
                self.buffer.append(state, action, reward, next_state, done)
                
                if self.buffer.crt_idx > 1 or self.buffer.is_full:
                    # Sample a minibatch
                    batch_states, batch_actions, batch_rewards, batch_next_states, batch_dones, weight_batch, index_batch = self.buffer.sample(self.batch_size)

                    # Set TD targets
                    target_next_action= self.actor.target_network(batch_next_states)
                    qs = self.critic.network([target_next_action, batch_next_states])
                    target_qs = self.critic.target_network([target_next_action, batch_next_states])
                    min_qs = tf.raw_ops.Min(input=tf.concat([target_qs, qs], axis=1), axis=1, keep_dims=True) # Double Q method
                    td_targets = self.calculate_td_target(batch_rewards, min_qs, batch_dones)
        
                    # Update priority
                    for (p, i) in zip(td_targets, index_batch):
                        self.buffer.update_priority(abs(p[0]) + self.epsilon_for_priority, i)

                    # print(weight_batch.shape)
                    # print(td_targets.shape)
                    # raise Exception
                    # Update critic network
                    q_loss += self.critic.train([batch_actions, batch_states], td_targets, weight_batch)
                    
                    # Update actor network
                    s_grads = self.critic.dq_da([batch_actions, batch_states])
                    self.actor.train(batch_states, s_grads)
                    self.actor.update_target_network()
                    self.critic.update_target_network()

                
                items_ids = next_items_ids
                episode_reward += reward
                mean_action += np.sum(action[0])/(len(action[0]))
                steps += 1

                if reward > 0:
                    correct_count += 1
                
                print(f'recommended items : {len(self.env.recommended_items)},  epsilon : {self.epsilon:0.3f}, reward : {reward:+}', end='\r')
                
                #print(eenv.OfflineEnv.get_items_names(nullcontext,self.env.recommended_items))
                if done:
                    print()
                    precision =  int(correct_count/steps * 100)
                    print(f'{episode}/{max_episode_num}, precision : {precision:2}%, total_reward:{episode_reward}, q_loss : {q_loss/steps}, mean_action : {mean_action/steps}')
                    if self.use_wandb:
                        wandb.log({'precision':precision, 'total_reward':episode_reward, 'epsilone': self.epsilon, 'q_loss' : q_loss/steps, 'mean_action' : mean_action/steps})
                    episodic_precision_history.append(precision)
             
            if (episode+1)%50 == 0:
                plt.plot(episodic_precision_history)
                plt.savefig(f'/training_precision_%_top_5.png')

            if (episode+1)%1000 == 0:
                self.save_model(f'save_weights/actor_{episode+1}_fixed.h5',
                                f'save_weights/critic_{episode+1}_fixed.h5')

            # for x in range(10):
            #     favplant.append({'id':str(self.env.user_items)})
            
            for x in range(20):
                hi.append({'id':str(plantid[x]), 'name':str(plant[x])})

            #hi.append({'id':str(plantid[0]), 'name':str(plant[0])})
            #print(len(favplant))
            # hi2.append(str(favplant))
            #print(json.dumps(hi2))



    def save_model(self, actor_path, critic_path):
        self.actor.save_weights(actor_path)
        self.critic.save_weights(critic_path)
        
    def load_model(self, actor_path, critic_path):
        self.actor.load_weights(actor_path)
        self.critic.load_weights(critic_path)