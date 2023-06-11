import pandas as pd
import scipy.sparse as sp
from sklearn.feature_extraction.text import CountVectorizer, TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity


def get_data():
        floral_data = pd.read_csv('dataset/floral_data.csv.zip')
        floral_data['original_plant'] = floral_data['original_plant'].str.lower()
        return floral_data

def combine_data(data):
        data_recommend = data.drop(columns=['floral_id', 'original_plant','plot'])
        data_recommend['combine'] = data_recommend[data_recommend.columns[0:2]].apply(
                                                                        lambda x: ','.join(x.dropna().astype(str)),axis=1)
        
        data_recommend = data_recommend.drop(columns=[ 'cast','cat'])
        return data_recommend
        
def transform_data(data_combine, data_plot):
        count = CountVectorizer(stop_words='english')
        count_matrix = count.fit_transform(data_combine['combine'])

        tfidf = TfidfVectorizer(stop_words='english')
        tfidf_matrix = tfidf.fit_transform(data_plot['plot'])

        combine_sparse = sp.hstack([count_matrix, tfidf_matrix], format='csr')
        cosine_sim = cosine_similarity(combine_sparse, combine_sparse)
        
        return cosine_sim


def recommend_florals(title, data, combine, transform):
        indices = pd.Series(data.index, index = data['original_plant'])
        index = indices[title]

        sim_scores = list(enumerate(transform[index]))
        sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
        sim_scores = sim_scores[1:21]

        floral_indices = [i[0] for i in sim_scores]

        floral_id = data['floral_id'].iloc[floral_indices]
        floral_title = data['original_plant'].iloc[floral_indices]
        floral_cat = data['cat'].iloc[floral_indices]

        recommendation_data = pd.DataFrame(columns=['Movie_Id','Name', 'Genres'])

        recommendation_data['Movie_Id'] = floral_id
        recommendation_data['Name'] = floral_title
        recommendation_data['Genres'] = floral_cat

        return recommendation_data

def results(floral_name):
        floral_name = floral_name.lower()

        find_floral = get_data()
        combine_result = combine_data(find_floral)
        transform_result = transform_data(combine_result,find_floral)

        if floral_name not in find_floral['original_plant'].unique():
                return 'Movie not in Database'

        else:
                recommendations = recommend_florals(floral_name, find_floral, combine_result, transform_result)
                return recommendations.to_dict('records')
