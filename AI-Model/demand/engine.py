import pandas as pd
import numpy as np
pd.set_option('display.max_columns', None)  
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

import seaborn as sns
import plotly.io as pio
import plotly.graph_objects as go
from plotly.offline import init_notebook_mode, iplot
from sklearn.preprocessing import StandardScaler
import warnings
warnings.filterwarnings('ignore')

data=pd.read_csv('train.csv')
center=pd.read_csv('fulfilment_center_info.csv')
meal=pd.read_csv('meal_info.csv')
test=pd.read_csv('test_QoiMO9B.csv')

test['num_orders']=123456 ### Assigning random number for Target Variable of Test Data.

data=pd.concat([data,test],axis=0)
data=data.merge(center,on='center_id',how='left')
data=data.merge(meal,on='meal_id',how='left')

train=data[data['week'].isin(range(1,146))]
test=data[data['week'].isin(range(146,156))]

def get_recommendations(id):    
    
    
    import json
    hi = data.groupby('center_id').num_orders.sum().sort_values(ascending=False).reset_index()
    result = hi.to_json(orient="records")
    parsed = json.loads(result)
    balamu = json.dumps(parsed) 
    #orders_for_product = orders[orders.product_id == id].order_id.unique();
    
    recommended_products = "hi"


    
    return balamu
    