#Dependencies
import pandas as pd
import numpy as np
import tensorflow as tf
import itertools
import matplotlib.pyplot as plt
import time
import envs
from envs import OfflineEnv
from recommender import DRRAgent


import os
ROOT_DIR = os.getcwd()
DATA_DIR = os.path.join(ROOT_DIR, 'floral-dataset/')
STATE_SIZE = 10
MAX_EPISODE_NUM = 1


if __name__ != "__main__":

    print('Data loading...')

    #Loading datasets
    ratings_list = [i.strip().split("::") for i in open(os.path.join(DATA_DIR,'ratings.dat'), 'r').readlines()]
    users_list = [i.strip().split("::") for i in open(os.path.join(DATA_DIR,'users.dat'), 'r').readlines()]
    florals_list = [i.strip().split("::") for i in open(os.path.join(DATA_DIR,'florals.dat'),encoding='latin-1').readlines()]
    ratings_df = pd.DataFrame(ratings_list, columns = ['UserID', 'FloralID', 'Rating', 'Timestamp'], dtype = np.uint32)
    florals_df = pd.DataFrame(florals_list, columns = ['FloralID', 'Title', 'Genres'])
    florals_df['FloralID'] = florals_df['FloralID'].apply(pd.to_numeric)

    print("Data loading complete!")
    print("Data preprocessing...")

    florals_id_to_florals = {floral[0]: floral[1:] for floral in florals_list}
    ratings_df = ratings_df.applymap(int)


    users_dict = np.load('data/user_dict.npy', allow_pickle=True)

 
    users_history_lens = np.load('data/users_histroy_len.npy')

    users_num = max(ratings_df["UserID"])+1
    items_num = max(ratings_df["FloralID"])+1

    # Training setting
    train_users_num = int(users_num * 0.8)
    train_items_num = items_num
    train_users_dict = {k:users_dict.item().get(k) for k in range(1, train_users_num+1)}
    train_users_history_lens = users_history_lens[:train_users_num]
    
    print('DONE!')
    time.sleep(2)

    def rec(id):
        env = OfflineEnv(train_users_dict, train_users_history_lens, florals_id_to_florals, STATE_SIZE)
        import envs
        envs.fromMobile = id
        recommender = DRRAgent(env, users_num, items_num, STATE_SIZE, use_wandb=False)
        #print(florals_id_to_florals)
        recommender.actor.build_networks()
        recommender.critic.build_networks()
        recommender.train(MAX_EPISODE_NUM, load_model=False)
        import recommender
        yep = recommender.hi
        yep2 = recommender.hi2
        import json
        new = json.dumps(yep)

        return str(new)

    def fav(id):

            
        env = OfflineEnv(train_users_dict, train_users_history_lens, florals_id_to_florals, STATE_SIZE)
        import envs
        envs.fromMobile = id
        recommender = DRRAgent(env, users_num, items_num, STATE_SIZE, use_wandb=False)
        #print(florals_id_to_florals)
        recommender.actor.build_networks()
        recommender.critic.build_networks()
        recommender.train(MAX_EPISODE_NUM, load_model=False)
        import recommender
        # yep = recommender.hi
        yep2 = recommender.favplant
        import json
        new = json.dumps(yep2)

        return str(new)