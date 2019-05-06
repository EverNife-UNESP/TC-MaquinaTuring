package br.com.finalcraft.unesp.tc.maquinaturing;

import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Graph;

public class GraphController {

    // Reference to the main application.
    private static Graph graph = new Graph();

    public static Graph getGraph() {
        return graph;
    }

    public static void clear(){
        graph.clearAll();
    }

}
    
 