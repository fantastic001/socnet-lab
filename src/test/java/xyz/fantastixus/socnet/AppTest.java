package xyz.fantastixus.socnet;

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
            1, 
            new CoalitionDetector<>(g, new AllPositiveLnikTransformer<>()).detect().size()
        );
    }
}
