package xyz.fantastixus.socnet;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

import javax.smartcardio.CommandAPDU;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.graph.util.Pair;

public class CoalitionDetector<V, E> {
    private UndirectedSparseGraph<V, E> graph; 
    private ComponentDetector<V, E> component_detector; 
    private LinkTransformer<V> transformer;
    private HashMap<V, Integer> components;
    public CoalitionDetector(UndirectedSparseGraph<V, E> graph, LinkTransformer<V> t) {
        this.graph = graph;
        this.component_detector = new ComponentDetector<V, E>(t);
        this.transformer = t; 
    }
    public List<UndirectedSparseGraph<V, E>> detect() {
        if (! isClusterable()) return null;
        int componentNumber = 0;
        boolean finished = false; 
        List<UndirectedSparseGraph<V, E>> result = new LinkedList<>();
        HashMap<Integer, LinkedList<V>> component_vertex_map = new HashMap<>();
        for (V v : graph.getVertices()) 
        {
            if (! component_vertex_map.containsKey(components.get(v))) 
            {
                component_vertex_map.put(components.get(v), new LinkedList<>());
            }
            component_vertex_map.get(components.get(v)).add(v);
        }
        while (!finished) {
            UndirectedSparseGraph<V, E> coalition = new UndirectedSparseGraph<>();
            LinkedList<V> vertices =  component_vertex_map.get(componentNumber);
            if (vertices == null) 
            {
                componentNumber++;
                if (componentNumber > 0) break;
            }
            for (V x : vertices) {
                if (components.get(x) == componentNumber) {
                    // add this node to newly created subgraph
                    coalition.addVertex(x);
                }
            }
            for (V x : vertices) {
                for (V y : vertices) {
                    if (! x.equals(y)) {
                        var edge = graph.findEdge(x, y);
                        if (edge != null) {
                            coalition.addEdge(edge, graph.getEndpoints(edge).getFirst(), graph.getEndpoints(edge).getSecond());
                        }
                    }
                }
            }
            if (coalition.getVertexCount()>0) {
                componentNumber++;
                result.add(coalition);
            }
            else {
                finished = true;
            }
        }
        return result;
    }
    public boolean isClusterable() {
        return getLinksToBeRemoved().isEmpty();
    }

    public List<Pair<V>> getLinksToBeRemoved() {
        LinkedList<Pair<V>> result = new LinkedList<>();
        components = this.component_detector.detectComponents(this.graph);
        for (V x : graph.getVertices()) {
            for (V y : graph.getNeighbors(x)) {
                if (!x.equals(y) && components.get(x) == components.get(y) && !isPositive(x, y)) {
                    result.add(new Pair<V>(x, y));
                }
            }
        }
        return result;
    }

    private boolean isPositive(V x, V y) {
        return transformer.transform(x, y) > 0;
    }
}
