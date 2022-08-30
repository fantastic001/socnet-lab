import sys 
import pandas as pd 
import networkx as nx 
import matplotlib.pyplot as plt 


data = sys.argv[1]
report = sys.argv[2]


g = nx.Graph()

for line in open(data):
    x = line.strip().split()[0]
    y = line.strip().split()[1]
    w = line.strip().split()[2]
    g.add_edge(x,y, weight=int(w))

nx.draw_networkx(g.to_undirected(), with_labels=False, node_size=5)
plt.savefig(sys.argv[3])

coals = [] 
rl = 0 
for line in open(report):
    if line.startswith("Vertices:"):
        coals.append(line.strip().split(":")[1].split(","))
    elif line.startswith("<") and line.strip().endswith(">"):
        rl += 1
print("Broj klastera: %d" % len(coals))
print("Broj uklonjenih linkova: %d" % rl)
print("Najveći klaster ima %d čvorova" % max(len(x) for x in coals))
print("Najmanji klaster ima %d čvorova" % min(len(x) for x in coals))


