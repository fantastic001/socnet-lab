package xyz.fantastixus.socnet;

/**
 * Hello world!
 *
 */

 import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class App 
{
    public static void main( String[] args )
    {
        Graph<String, String> g = new UndirectedSparseGraph<String, String>();
        g.addVertex("ana");
        g.addVertex("mika");
        g.addEdge("friendship", "ana", "mika");
        System.out.println( g);
    }
}
