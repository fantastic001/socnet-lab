package xyz.fantastixus.socnet;

import java.util.HashMap;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public interface ComponentDetector<V, E> {
    public HashMap<V, Integer>  detectComponents(UndirectedSparseGraph<V, E> graph);
}
