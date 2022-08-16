
import sys 
import networkx as nx
from numpy import append 

mode = sys.argv[1]

if mode in ["help", "-h", "--help"]:
    print("Usage: generate_random.py [LABELED|STANDARD]")
    exit(0)

G = nx.configuration_model([20 for x in range(100)])
G.remove_edges_from(nx.selfloop_edges(G))

visited = set()

if mode in ["LABELED", "labeled"]:
    for x,y,w in G.edges:
        if (x,y) in visited or (y,x) in visited:
            continue
        else: visited.add((x,y))
        sign = -1
        if x % 2 == y % 2:
            sign = 1 
        print("%d %d %d" % (x,y, sign))  
else:
    for x,y,w in G.edges:
        if (x,y) in visited or (y,x) in visited:
            continue
        else: visited.add((x,y))
        print("%d %d" % (x,y))