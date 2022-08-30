import sys 
import pandas as pd 
import networkx as nx 
import matplotlib.pyplot as plt 


data = sys.argv[1]
report = sys.argv[2]

g_data = [(int(line.strip().split()[0]), int(line.strip().split()[1])) for line in open(data)]

g = nx.Graph()

for x,y in g_data:
    g.add_edge(x,y)


nx.draw_networkx(g.to_undirected(), with_labels=False, node_size=5)
plt.savefig(sys.argv[3])

kcore = {}
curr = -1
for line in open(report):
    if line.startswith("Core:"):
        curr = int(line.strip().split()[1])
    elif line.startswith("Mode:"):
        curr = -1
    else:
        if curr >= 0:
            kcore[int(line.strip())] = curr

k = list(kcore.keys())

table = pd.DataFrame({"Node":k, "Shell index": [kcore[x] for x in k]})

print(table.to_markdown())

print("Broj čvorova po shell indeksima")

print(table.groupby("Shell index").count().to_markdown())

