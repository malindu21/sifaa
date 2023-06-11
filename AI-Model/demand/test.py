import numpy as np
import pandas as pd
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

#print("The Shape of Demand dataset :",data.shape)
#print("The Shape of Fulmilment Center Information dataset :",center.shape)
#print("The Shape of Meal information dataset :",meal.shape)
#print("The Shape of Test dataset :",test.shape)

test['num_orders']=123456 ### Assigning random number for Target Variable of Test Data.

data=pd.concat([data,test],axis=0)
data=data.merge(center,on='center_id',how='left')
data=data.merge(meal,on='meal_id',how='left')

#Discount Amount
data['discount amount']=data['base_price']-data['checkout_price']

#Discount Percent
data['discount percent'] = ((data['base_price']-data['checkout_price'])/data['base_price'])*100

#Discount Y/N
data['discount y/n'] = [1 if x>0 else 0 for x in (data['base_price']-data['checkout_price'])]

data=data.sort_values(['center_id', 'meal_id', 'week']).reset_index()

#Compare Week Price
data['compare_week_price'] = data['checkout_price'] - data['checkout_price'].shift(1) 

data['compare_week_price'][data['week']==1]=0

data=data.sort_values(by='index').reset_index().drop(['level_0','index'],axis=1)

# Compare Week Price Y/N
data['compare_week_price y/n'] = [1 if x>0 else 0 for x in data['compare_week_price']]

train=data[data['week'].isin(range(1,146))]
test=data[data['week'].isin(range(146,156))]


#data=data.merge(center,on='date',how='left')
data['year'] = pd.DatetimeIndex(data['date']).year

#center['year'] = pd.DatetimeIndex(center['date']).year

import json

fig=plt.figure(figsize=(4,7))
plt.title('Total No. of Orders for Each Center type',fontdict={'fontsize':13})
sns.barplot(y='num_orders', x='payment_type', data=train.groupby('payment_type').sum()['num_orders'].reset_index(),palette='autumn');
plt.ylabel('No. of Orders',fontdict={'fontsize':12})
plt.xlabel('Payment type',fontdict={'fontsize':12})
sns.despine(bottom = True, left = True);
hi = train.groupby('payment_type').sum()['num_orders'].reset_index()
result = hi.to_json(orient="records")
parsed = json.loads(result)
print(json.dumps(parsed) )


def get_recomendations(id):
    id = id
    yoo = "hi"
    return yoo