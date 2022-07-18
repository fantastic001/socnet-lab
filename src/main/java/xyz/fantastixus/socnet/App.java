package xyz.fantastixus.socnet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */

 import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import xyz.fantastixus.socnet.transformers.OriginalEdgeBasedLinkTransformer;

public class App 
{
    public static void main( String[] args )
    {
        String mode = args[0];
        if (args.length != 2) {
            System.out.println("Usage:  run.sh mode path where mode is either CD or KCore");
            return;
        } 
        String path = args[1];
        Scanner scanner;
        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(1);
            return;
        }
        HashSet<Integer> vertices = new HashSet<>();
        UndirectedSparseGraph<Integer, Pair<Integer>> g = new UndirectedSparseGraph<>();
        while (scanner.hasNext()) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            if (! vertices.contains(u)) {
                g.addVertex(u);
                // vertices.add(u);
            }
            if (! vertices.contains(v)) {
                g.addVertex(v);
                // vertices.add(v);
            }
            if (mode.equals("CD")) {
                int w = scanner.nextInt();
                if (w == 1) g.addEdge(new Pair<Integer>(u, v), v, u);
            }
            else {
                g.addEdge(new Pair<Integer>(u, v), u, v);
            }

        }
        if(mode.equals("CD")) {
            CoalitionDetector<Integer, Pair<Integer>> cd = new CoalitionDetector<Integer, Pair<Integer>>(
                g, 
                new OriginalEdgeBasedLinkTransformer<Integer, Pair<Integer>>(g)
            );
            List<UndirectedSparseGraph<Integer, Pair<Integer>>> coalitions;
            if (! cd.isClusterable()) {
                var links = cd.getLinksToBeRemoved();
                System.out.println("Coalitions cannot be created, there are links to be removed:");
                for (var link : links) {
                    System.out.println(link);
                    g.removeEdge(link);
                }
            }
            coalitions = cd.detect(); 
            System.out.println("Number of coalitions: " + coalitions.size());
            for (var c : coalitions) {
                System.out.println(c);
            }
        }
        else {
            KCoreDecomposition<Integer, Pair<Integer>> kcore = new KCoreDecomposition<>(g);
            kcore.decompose();
            var cores = kcore.getSubnetworks();
            int i = 0;
            for (var core : cores) {
                if (core.getVertexCount() == 0) continue;
                System.out.println("Core: " + i);
                for (int v : core.getVertices()) {
                    System.out.println(v);
                }
                i++;
            }
        }


        System.out.println("Mode: " + mode);
    }
}
