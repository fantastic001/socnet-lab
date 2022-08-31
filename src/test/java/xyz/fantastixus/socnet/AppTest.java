package xyz.fantastixus.socnet;

import java.util.Collection;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import xyz.fantastixus.socnet.transformers.AllNegativeLinkTransformer;
import xyz.fantastixus.socnet.transformers.AllPositiveLnikTransformer;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public void testClusterableSimple() {
        UndirectedSparseGraph<Integer, String> g = new UndirectedSparseGraph<>();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        assertTrue(
            "Simple graph with all positive edges is clusterable", 
            new CoalitionDetector<>(g, new AllPositiveLnikTransformer<>()).isClusterable()
        );
        assertTrue(
            "Simple graph with all negative edges is clusterable", 
            new CoalitionDetector<>(g, new AllNegativeLinkTransformer<>()).isClusterable()
        );
    }

    public void testDetectSimple() {
        UndirectedSparseGraph<Integer, String> g = new UndirectedSparseGraph<>();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        assertEquals(
            3, 
            new CoalitionDetector<>(g, new AllNegativeLinkTransformer<>()).detect().size()
        );
        assertEquals(
            3, 
            new CoalitionDetector<>(g, new AllPositiveLnikTransformer<>()).detect().size()
        );
        g.addEdge("a", 1, 2);
        g.addEdge("b", 1, 3);

        assertEquals(
            1, 
            new CoalitionDetector<>(g, new AllPositiveLnikTransformer<>()).detect().size()
        );
    }

    public void testKCoreDecompositionSimple() {
        UndirectedSparseGraph<Integer, Integer> g = new UndirectedSparseGraph<>();
        g.addVertex(1);
        g.addVertex(2);
        g.addEdge(1, 1, 2);
        KCoreDecomposition<Integer, Integer> decomposition = new KCoreDecomposition<>(g);
        decomposition.decompose();
        assertEquals(1, decomposition.getShellIndex(1));
        assertEquals(1, decomposition.getShellIndex(2));
        assertEquals(2, decomposition.getSubnetworks().size());

        g = new UndirectedSparseGraph<>();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        g.addEdge(2, 1, 2);
        g.addEdge(3, 1, 3);
        g.addEdge(4, 1, 4);
        g.addEdge(5, 1, 5);
        decomposition = new KCoreDecomposition<>(g);
        decomposition.decompose();
        assertEquals(1, decomposition.getShellIndex(2));
        assertEquals(1, decomposition.getShellIndex(3));
        assertEquals(1, decomposition.getShellIndex(4));
        assertEquals(1, decomposition.getShellIndex(5));
        assertEquals(1, decomposition.getShellIndex(1));

        g = new UndirectedSparseGraph<>();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addEdge(1, 1, 2);
        g.addEdge(2, 1, 3);
        g.addEdge(3, 3, 2);
        decomposition = new KCoreDecomposition<>(g);
        decomposition.decompose();
        assertEquals(2, decomposition.getShellIndex(1));
        assertEquals(2, decomposition.getShellIndex(2));
        assertEquals(2, decomposition.getShellIndex(3));
        assertEquals(2, decomposition.getSubnetworks().get(2).degree(1));

        // 0-core is equal to entire graph 
        assertEquals(3, decomposition.getSubnetworks().get(0).getVertexCount());
        assertEquals(3, decomposition.getSubnetworks().get(0).getEdgeCount());
    }

    public void testKCoreWithReferenceImplementation() 
    {
        UndirectedSparseGraph<Integer, Integer> g = new UndirectedSparseGraph<>();
        KCoreDecomposition<Integer, Integer> decomposition = new KCoreDecomposition<>(g);
        for (int i = 0; i<50; i++) g.addVertex(i);
        g.addEdge(2, 1, 2);
        g.addEdge(3, 1, 3);
        g.addEdge(4, 1, 4);
        g.addEdge(5, 1, 5);
        decomposition = new KCoreDecomposition<>(g);
        decomposition.decompose();
        assertEquals(getKCoreSimple(g, 2).getVertexCount(), decomposition.getSubnetworks().get(2).getVertexCount());

    }

    private UndirectedSparseGraph<Integer, Integer> getKCoreSimple(UndirectedSparseGraph<Integer, Integer> g, int k) 
    {
        UndirectedSparseGraph<Integer, Integer> graph = new UndirectedSparseGraph<>();
        for (Integer v : g.getVertices()) 
        {
            graph.addVertex(v);
        }
        for (Integer e : g.getEdges()) 
        {
            graph.addEdge(e, g.getEndpoints(e).getFirst(), g.getEndpoints(e).getSecond());
        }
        boolean finished = false; 
        while (! finished) 
        {
            finished = true;
            Collection<Integer> vertices = graph.getVertices().stream().toList();
            for (Integer v : vertices) 
            {
                if (graph.degree(v) < k) 
                {
                    graph.removeVertex(v);
                    finished = false;
                }
            }
        }
        return graph;
    }
}
