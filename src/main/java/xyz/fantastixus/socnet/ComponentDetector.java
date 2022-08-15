package xyz.fantastixus.socnet;

import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class ComponentDetector<V, E> {
    private LinkTransformer<V> transformer; 
     public ComponentDetector(LinkTransformer<V> transfoermer) {
        this.transformer = transfoermer;
    }
    public HashMap<V, Integer>  detectComponents(UndirectedSparseGraph<V, E> graph) {
        int componentNumber = 0;
        HashMap<V, Integer> result = new HashMap<>();
        for (V vertex : graph.getVertices()) {
            if (! result.containsKey(vertex)) {
                // we are starting to create new component 
                Stack<V> s = new Stack<>();
                s.push(vertex);
                while (!s.isEmpty()) {
                    V x = s.pop();
                    if (! result.containsKey(x)) {
                        result.put(x, componentNumber);
                        for (V y : graph.getNeighbors(x)) {
                            if (transformer.transform(x, y) > 0) {
                                s.push(y);
                            }
                        }
                    }
                }
                componentNumber++;
            }
        }
        return result;
    }
}
