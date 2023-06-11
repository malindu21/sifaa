import pandas as pd
import numpy as np
pd.set_option('display.max_columns', None)  
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import json
import seaborn as sns
import plotly.io as pio
import plotly.graph_objects as go
from plotly.offline import init_notebook_mode, iplot
from sklearn.preprocessing import StandardScaler
import warnings
warnings.filterwarnings('ignore')

data=pd.read_csv("demand/train.csv")
center=pd.read_csv("demand/fulfilment_center_info.csv")
floral=pd.read_csv("demand/floral_info.csv")
test=pd.read_csv("demand/test_QoiMO9B.csv")
Submission = pd.read_csv("demand/Result XG 2.csv")

test['num_orders']=123456 ### Assigning random number for Target Variable of Test Data.

data=pd.concat([data,test],axis=0)
data=data.merge(center,on='center_id',how='left')
data=data.merge(floral,on='floral_id',how='left')

train=data[data['week'].isin(range(1,146))]
test=data[data['week'].isin(range(146,156))]

def get_barChart(id):    
    
    
    import json
    hi = data.groupby('center_id').num_orders.sum().sort_values(ascending=False).reset_index()
    result = hi.to_json(orient="records")
    parsed = json.loads(result)
    android = json.dumps(parsed) 

    #orders_for_product = orders[orders.product_id == id].order_id.unique();
    
    recommended_products = "hi"
    return android

# def get_recommendations(id):    
    
    
#     import json
    # hi = data.groupby('center_id').num_orders.sum().sort_values(ascending=False).reset_index()
    # result = hi.to_json(orient="records")
    # parsed = json.loads(result)
    # balamu = json.dumps(parsed) 
    #orders_for_product = orders[orders.product_id == id].order_id.unique();






def get_recommendations(id):      
    import json
    hi = train.groupby(['cat']).num_orders.sum()
    result = hi.to_json(orient="columns")
    parsed = json.loads(result)
   

    return parsed

def get_original_demands(id):  
    predictedFlo = Submission[['floral_id', 'num_orders', 'week']].groupby(['floral_id'], as_index=False).agg({'week':'nunique', 
                                                                                            'num_orders':'sum'})                                                                                         
    florals = train[['floral_id', 'num_orders', 'week']].groupby(['floral_id'], as_index=False).agg({'week':'nunique', 
                                                                                            'num_orders':'sum'})
    florals['train'] = florals.num_orders/florals.week
    predictedFlo['predicted'] = predictedFlo.num_orders/florals.week

    fig,ax=plt.subplots(figsize=(10,6))
    ax1=fig.add_subplot(121)
    ax2=fig.add_subplot(122)

    florals1=florals.sort_values(by='train', ascending=False)[:5]
    florals2=predictedFlo.sort_values(by='predicted', ascending=False)[:5]

    florals1.plot.bar(x='floral_id', y='train', color='blue', ax=ax1, title='Real Demand')

    florals2.plot.bar(x='floral_id', y='predicted', color='orange', ax=ax2, title='Predicted Demand')

    final = florals1

    result = final.to_json(orient="records")
    parsed = json.loads(result)
    android = json.dumps(parsed) 
    
    return android






def get_self_item_demands(id):  

# 1062

    df_female = []
    for x in range(1,20):
        df_groupby_sex = data.groupby(['floral_id','week']).get_group((id,x)).num_orders.sum()
        df_female.append({'week':str(f"week {x}"), 'demand':str(df_groupby_sex)})

    hi = df_female


    android = json.dumps(df_female) 

    return str(android)

 

def get_predicted_demands(id):  

    predictedFlo = Submission[['floral_id', 'num_orders', 'week']].groupby(['floral_id'], as_index=False).agg({'week':'nunique', 
                                                                                            'num_orders':'sum'})                                                                                         
    florals = train[['floral_id', 'num_orders', 'week']].groupby(['floral_id'], as_index=False).agg({'week':'nunique', 
                                                                                            'num_orders':'sum'})
    florals['train'] = florals.num_orders/florals.week
    predictedFlo['predicted'] = predictedFlo.num_orders/florals.week

    fig,ax=plt.subplots(figsize=(10,6))
    ax1=fig.add_subplot(121)
    ax2=fig.add_subplot(122)

    florals1=florals.sort_values(by='train', ascending=False)[:5]
    florals2=predictedFlo.sort_values(by='predicted', ascending=False)[:5]

    florals1.plot.bar(x='floral_id', y='train', color='blue', ax=ax1, title='Real Demand')

    florals2.plot.bar(x='floral_id', y='predicted', color='orange', ax=ax2, title='Predicted Demand')

    final2 = florals2

    result2 = final2.to_json(orient="records")
    parsed2 = json.loads(result2)
    android = json.dumps(parsed2) 
    
    return android