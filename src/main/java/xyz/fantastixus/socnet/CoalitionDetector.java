package xyz.fantastixus.socnet;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Graphs;

public class CoalitionDetector<V, E> {
    private UndirectedSparseGraph<V, E> graph; 
    private ComponentDetector<V, E> component_detector; 
    private LinkTransformer<E> transformer;
    public CoalitionDetector(UndirectedSparseGraph<V, E> graph, ComponentDetector<V, E> cd, LinkTransformer<E> t) {
        this.graph = graph;
        this.component_detector = cd; 
        this.transformer = t; 
    }
    public List<UndirectedSparseGraph<V, E>> detect() {
        return null;
    }
    public boolean isClusterable() {
        return getLinksToBeRemoved().isEmpty();
    }

    public List<E> getLinksToBeRemoved() {
        ArrayList<E> result = new ArrayList<E>();
        HashMap<V, Integer> components = this.component_detector.detectComponents(this.graph);
        for (E edge : graph.getEdges()) {
            V source = graph.getSource(edge);
            V target = graph.getDest(edge);
            if (components.get(source) == components.get(target) && ! isPositive(edge)) {
                result.add(edge);
            }
        }
        return result;
    }

    private boolean isPositive(E link) {
        return this.transformer.transform(link) > 0;
    }

    private boolean isPositive(V x, V y) {
        E edge = this.graph.findEdge(x, y)
        if (edge == null) return false; 
        else return isPositive(edge);
    }
}
