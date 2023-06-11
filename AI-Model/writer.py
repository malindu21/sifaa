import csv
from os import altsep, name
import random


# with open('test.dat', 'r') as input_file:
#     lines = input_file.readlines()
#     newLines = []
#     for line in lines:
#         newLine = line.strip('::').split()
#         newLines.append(newLine)

# with open('file.csv', 'w') as output_file:
#     file_writer = csv.writer(output_file)
#     file_writer.writerows(newLines)






# def get_wri(id):    
    
    
#     # writer = open('ml-1m/ratings.dat','a')
#     writer = open('floral-dataset/ratingsplus.dat','a')
#     writer.writelines("\r")
#     writer.writelines( (',').join(['%s'% (id)]))
#     response = "success"
# # names = ["test 1", "test 2", "test 3", " test 4", " test 5"]
    
#     return response 










# for x in range(3000):
#     writer = open('test.dat','a')
#     writer.writelines("\r")
#     for y in range(4):
#         writer.writelines( (',').join(['%s::%s::Roses|White'% (x,names[y])]))

#a_list = list(range(1, 450))
# #     writer = open('test.dat','a')
# #     writer.writelines("\r")
# adj = a_list

# Genres = ["Roses","Tulips","Orchids","Lilies","Sunflowers","Carnations","Poppies","Iris","Bougainvilleas"]
# Colors = ["Blue","White","Red","Yellow","Purple","Magenta","Ivory","Purple","Orange"]

# Roses = ['Climbing Roses','Hybrid Tea Roses','Grandiflora Roses','Floribunda Roses','Polyantha Roses','Polyantha Roses','Shrub Roses',
# 'Groundcover Roses','Alba Roses','Bourbon Roses','Centifolia Roses','David Austin Roses','China Roses','Gallica Roses','Damask Roses']

# RoseImage =[]

# Tulips = ['Darwin Hybrid Tulips',
# 'Triumph Tulips','Double Tulips','Fringed Tulips','Fosteriana Tulips','Greigii Tulips','Kaufmanniana Tulips','Lily-Flowered Tulips',
# 'Parrot Tulips','Single Early Tulips','Single Late Tulips', 'Single Late Tulips','Viridiflora Tulips','Species Tulips']

# Orchids = ['Cattleya Orchids','Brassia Orchids',
# 'Cymbidium Orchids','Vanda Orchids','Encyclia Orchids',
# 'Miltonia Orchids','Oncidium Orchids','Epidendrum Orchids','Odontoglossum Orchids','Zygopetalum Orchids'
# 'Phalaenopsis Orchids','Psychopsis Orchids']

# Lilies = ['Asiatic lilies','Oriental lilies','Trumpet lilies','Orienpet lilies','LA hybrid lilies','Turk Cap lilies','Canada lilies','Longiflorium lilies','Blue Water Lilies',
# 'Rosella’s Dream','Starlette','Martagon Mix','Nankeen']


# Sunflowers = ['Little Becka','Sunforest Mix','Elf','Strawberry Blonde','Soraya','American Giant','Pacino','Sunflower Zohar','Sunflower Elegance','Sunflower Bashful','Sunflower Russian Mammoth',
# 'Shock-O-Lat','Coconut Ice','Solar Flare','Lemon Queen','Ms. Mars' ,'Orange Sun','Irish Eyes','Chianti','Ring of Fire','Valentine','Goldie']

# Carnations = ['Dianthus Caryophyllus','Sweet William','Cheddar Pinks','Dianthus Chinensis','Dianthus Plumarius','Dianthus Chinensis Heddewigii','Dianthus Deltoides Maiden',
# 'Dianthus Superstar','Dianthus Armeria','Dianthus Pavonius','Dianthus superbus','Pinkball Wizard','Dianthus Neon Star','Dianthus American',
# 'Everlast Lavender Lace']

# Poppies = ['Corn Poppy','Oriental Poppy','Opium Poppy','California Poppy','Himalayan Poppy','Iceland poppy','Patty Plum','California Poppy',
# 'Helen Elizabeth Poppy','Tallulah Belle','Glow','Khedive','Goliath','Picotee','Ruffles','Turkish Delight','Turkenlouis']

# Iris = ['Dutch Iris','German Iris','Japanese Iris','Louisiana Iris','Siberian Iris','Bearded Iris','Dwarf Bearded Iris','Dwarf Crested Iris','Species Irises','Dutch Iris','Japanese Iris',
# 'Siberian Iris','Yellow Flag Iris','Louisiana Iris','Reticulata iris']

# Bougainvilleas = ['Camarillo Fiesta','Gold Rush','Stripe','Cherry Blossom','Queen','Delta Dawn','Ms. Alice',
# 'Madonna','Silhouette','Barbara Karst','Scarlett-O-Hara','Formosa','Raspberry-Ice']




# # for x in adj:
# #   for y in genres:
# #     print(x, y)
# combs = []
# const = 0
# for i in a_list:
#     for x in Genres:
#         for y in [1]:
#             # if x != y:
#             writer = open('test.dat','a')
#             writer.writelines("\r")
#             if x == 'Roses':
#                 writer.writelines( (',').join(['%s::%s::Roses|%s'% (const,random.choice(Roses),random.choice(Colors))]))
#             elif x == 'Tulips':
#                 writer.writelines( (',').join(['%s::%s::Tulips|%s'% (const,random.choice(Tulips),random.choice(Colors))]))
#             elif x == 'Orchids':
#                 writer.writelines( (',').join(['%s::%s::Orchids|%s'% (const,random.choice(Orchids),random.choice(Colors))]))
#             elif x == 'Lilies':
#                 writer.writelines( (',').join(['%s::%s::Lilies|%s'% (const,random.choice(Lilies),random.choice(Colors))]))
#             elif x == 'Sunflowers':
#                 writer.writelines( (',').join(['%s::%s::Sunflowers|%s'% (const,random.choice(Sunflowers),random.choice(Colors))]))
#             elif x == 'Carnations':
#                 writer.writelines( (',').join(['%s::%s::Carnations|%s'% (const,random.choice(Carnations),random.choice(Colors))])) 
#             elif x == 'Poppies':
#                 writer.writelines( (',').join(['%s::%s::Poppies|%s'% (const,random.choice(Poppies),random.choice(Colors))])) 
#             elif x == 'Iris':
#                 writer.writelines( (',').join(['%s::%s::Iris|%s'% (const,random.choice(Iris),random.choice(Colors))])) 
#             else:
#                 writer.writelines( (',').join(['%s::%s::Bougainvilleas|%s'% (const,random.choice(Bougainvilleas),random.choice(Colors))]))

#                 # print('%s::%s::%s'% (const,random.choice(select2),x))
        
#                 # print('%s::%s::%s'% (const,random.choice(select),x))
            
#             const = const + 1    
            
