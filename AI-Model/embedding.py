import tensorflow as tf
import numpy as np

class FloralGenreEmbedding(tf.keras.Model):
    def __init__(self, len_florals, len_genres, embedding_dim):
        super(FloralGenreEmbedding, self).__init__()
        self.m_g_input = tf.keras.layers.InputLayer(name='input_layer', input_shape=(2,))
        # embedding
        self.m_embedding = tf.keras.layers.Embedding(name='floral_embedding', input_dim=len_florals, output_dim=embedding_dim)
        self.g_embedding = tf.keras.layers.Embedding(name='genre_embedding', input_dim=len_genres, output_dim=embedding_dim)
        # dot product
        self.m_g_merge = tf.keras.layers.Dot(name='floral_genre_dot', normalize=True, axes=1)
        # output
        self.m_g_fc = tf.keras.layers.Dense(1, activation='sigmoid')
        
    def call(self, x):
        x = self.m_g_input(x)
        memb = self.m_embedding(x[0])
        gemb = self.g_embedding(x[1])
        m_g = self.m_g_merge([memb, gemb])
        return self.m_g_fc(m_g)

# class UserFloralEmbedding(tf.keras.Model):
#     def __init__(self, len_users, embedding_dim):
#         super(UserFloralEmbedding, self).__init__()
#         self.m_u_input = tf.keras.layers.InputLayer(name='input_layer', input_shape=(2,))
#         # embedding
#         self.u_embedding = tf.keras.layers.Embedding(name='user_embedding', input_dim=len_users, output_dim=embedding_dim)
#         # dot product
#         self.m_u_merge = tf.keras.layers.Dot(name='floral_user_dot', normalize=False, axes=1)
#         # output
#         self.m_u_fc = tf.keras.layers.Dense(1, activation='sigmoid')
        
#     def call(self, x):
#         x = self.m_u_input(x)
#         uemb = self.u_embedding(x[0])
#         m_u = self.m_u_merge([x[1], uemb])
#         return self.m_u_fc(m_u)


class UserFloralEmbedding(tf.keras.Model):
    def __init__(self, len_users, len_florals, embedding_dim):
        super(UserFloralEmbedding, self).__init__()
        self.m_u_input = tf.keras.layers.InputLayer(name='input_layer', input_shape=(2,))
        # embedding
        self.u_embedding = tf.keras.layers.Embedding(name='user_embedding', input_dim=len_users, output_dim=embedding_dim)
        self.m_embedding = tf.keras.layers.Embedding(name='floral_embedding', input_dim=len_florals, output_dim=embedding_dim)
        # dot product
        self.m_u_merge = tf.keras.layers.Dot(name='floral_user_dot', normalize=False, axes=1)
        # output
        self.m_u_fc = tf.keras.layers.Dense(1, activation='sigmoid')
        
    def call(self, x):
        x = self.m_u_input(x)
        uemb = self.u_embedding(x[0])
        memb = self.m_embedding(x[1])
        m_u = self.m_u_merge([memb, uemb])
        return self.m_u_fc(m_u)

# class UserFloralEmbedding(tf.keras.Model):
#     def __init__(self, len_users, len_florals, embedding_dim):
#         super(UserFloralEmbedding, self).__init__()
#         self.m_u_input = tf.keras.layers.InputLayer(name='input_layer', input_shape=(2,))
#         # embedding
#         self.u_embedding = tf.keras.layers.Embedding(name='user_embedding', input_dim=len_users, output_dim=embedding_dim)
#         self.m_embedding = tf.keras.layers.Embedding(name='floral_embedding', input_dim=len_florals, output_dim=embedding_dim)
#         # dot product
#         self.m_u_concat = tf.keras.layers.Concatenate(name='floral_user_concat', axis=1)
#         # output
#         self.m_u_fc = tf.keras.layers.Dense(1, activation='sigmoid')
        
#     def call(self, x):
#         x = self.m_u_input(x)
#         uemb = self.u_embedding(x[0])
#         memb = self.m_embedding(x[1])
#         m_u = self.m_u_concat([memb, uemb])
#         return self.m_u_fc(m_u)