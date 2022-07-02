package xyz.fantastixus.socnet.transformers;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import xyz.fantastixus.socnet.LinkTransformer;

public class OriginalEdgeBasedLinkTransformer<V, E> implements LinkTransformer<V> {

    private UndirectedSparseGraph<V, E> graph;

    public OriginalEdgeBasedLinkTransformer(UndirectedSparseGraph<V, E> g) {
        this.graph = g; 
    }

    @Override
    public int transform(V x, V y) {
        if (graph.findEdge(x, y) != null) return 1; 
        else return -1;
    }
    
}
