import sys 
import pandas as pd 
import networkx as nx 
import matplotlib.pyplot as plt 
import os.path as path 


data = sys.argv[1]
report = sys.argv[2]
n = 0
def image(desc):
    global n 
    n += 1
    img_name, img_ext = path.splitext(sys.argv[3])
    name = "%s-%d.%s" % (img_name, n, img_ext)
    if not name.startswith("./"):
        name = "./" + name
    plt.savefig(name)
    print()
    print(desc)
    print()
    print("[[%s]]" % name)
    print()

g = nx.Graph()
for line in open(data):
    g.add_edge(line.strip().split()[0],line.strip().split()[1])




kcore = {}
curr = -1
for line in open(report):
    if line.startswith("Core:"):
        curr = int(line.strip().split()[1])
    elif line.startswith("Mode:"):
        curr = -1
    else:
        if curr >= 0:
            kcore[line.strip()] = curr

nodes = list(kcore.keys())
CB = nx.betweenness_centrality(g)
D = nx.degree(g)
table = pd.DataFrame({
    "Node":nodes, 
    "Shell index": [kcore[x] for x in nodes], 
    "Degree": [D[x] for x in nodes],
    "Betweeness": [CB[x] for x in nodes]
})



# print(table.to_markdown())

print("Broj čvorova po shell indeksima")

print(table.groupby("Shell index").count()["Node"].to_markdown())
print("Korelacija shell indeksa i ostalih metrika:")
print(table.corr("spearman").to_markdown())

for metric in ["Degree", "Betweeness"]:
    table.plot.scatter(x="Shell index", y=metric)
    image("Grafik gde je na x osi shell indeks a na y osi %s je prikazan na sledećem grafiku:" % metric)

macro = []
for k in range(curr+1):
    gg: nx.Graph = g.copy()
    gg.remove_nodes_from([x for x in nodes if kcore[x] > k])
    macro.append({
        "K": k,
        "Diameter": nx.diameter(gg),
        "Average shortest path length": nx.average_shortest_path_length(gg)
    })
print(pd.DataFrame(macro).to_markdown())