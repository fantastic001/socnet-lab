package xyz.fantastixus.socnet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.event.GraphEvent.Vertex;
import edu.uci.ics.jung.graph.util.Pair;

public class KCoreDecomposition<V, E> {
    private UndirectedGraph<V, E> g;
    private HashMap<V, Integer> deg;
    private int md; 
    public KCoreDecomposition(UndirectedGraph<V, E> graph) {
        this.g = graph;
    }

    public void decompose() {
        int n, d, i, start, num;
        int du, pu, pw;
        V w;
        HashMap<V, Integer> pos;
        HashMap<Integer, V> vert = new HashMap<>();
        deg = new HashMap<>();
        vert = new HashMap<>();
        pos = new HashMap<>();
        n = g.getVertexCount();
        md = 0;
        for (V vertex : g.getVertices()) {
            deg.put(vertex, g.degree(vertex));
            if (md < g.degree(vertex)) {
                md = g.degree(vertex);
            }
        }
        // allocate array of degrees
        int[] bin = new int[md+1];
        for (i = 0; i<=md; i++) bin[i] = 0;
        for (V vertex : g.getVertices()) bin[g.degree(vertex)]++;
        start = 0; 
        for (d=0; d<=md; d++) {
            num = bin[d];
            bin[d] = start; 
            start += num;
        }
        for (V vertex : g.getVertices()) {
            pos.put(vertex, bin[g.degree(vertex)]);
            vert.put(pos.get(vertex), vertex);
            bin[g.degree(vertex)]++;
        }
        for (d=md; d>0; d--) bin[d] = bin[d-1];
        bin[0] = 0;
        for (i = 0; i<n; i++) {
            V vertex = vert.get(i);
            for (V u : g.getNeighbors(vertex)) {
                if (deg.get(u) > deg.get(vertex)) {
                    du = deg.get(u);
                    pu = pos.get(u);
                    pw = bin[du];
                    w = vert.get(pw);
                    if (! u.equals(w)) {
                        pos.put(u, pw);
                        vert.put(pu, w);
                        pos.put(w, pu);
                        vert.put(pw, u);
                    }
                    bin[du]++;
                    deg.put(u, deg.get(u)-1);

                }
            }
        }

    }

    public int getShellIndex(V v) {
        return deg.get(v);
    }

    public List<UndirectedSparseGraph<V, E>> getSubnetworks() {
        List<UndirectedSparseGraph<V, E>> result = new ArrayList<>();
        for (int d = 0; d<= md; d++) {
            UndirectedSparseGraph<V, E> sn = new UndirectedSparseGraph<>();
            for (V vertex : g.getVertices()) {
                if (deg.get(vertex) >= d) {
                    sn.addVertex(vertex);
                }
            }
            for (E edge : g.getEdges()) {
                Pair<V> pair = g.getEndpoints(edge);
                V x = pair.getFirst();
                V y = pair.getSecond();
                if (deg.get(y) >= d && deg.get(x) >= d) {
                    sn.addEdge(edge, x, y);
                }
            }
            result.add(sn);
        }
        return result;
    }
    
}
