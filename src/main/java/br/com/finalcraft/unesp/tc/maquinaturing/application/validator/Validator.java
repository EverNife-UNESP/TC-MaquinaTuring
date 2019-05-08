package br.com.finalcraft.unesp.tc.maquinaturing.application.validator;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Aresta;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Vertice;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog.HistoryLog;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Edge;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Graph;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;

import java.util.ArrayList;
import java.util.List;

public class Validator {


    public static List<Aresta> todasAsArestas = new ArrayList<Aresta>();
    public static List<Vertice> todosOsVertices = new ArrayList<Vertice>();

    public static Vertice verticeInicial = null;

    public static void loadGraph() {
        todasAsArestas.clear();
        todosOsVertices.clear();
        verticeInicial = null;

        Graph graph = GraphController.getGraph();

        for (Edge edge : graph.getEdges()) {
            if (edge.arestaOne.isValid()) {
                todasAsArestas.add(edge.arestaOne);
            }
            if (edge.isBidiretec() && edge.arestaTwo.isValid()) {
                todasAsArestas.add(edge.arestaTwo);
            }
        }

        for (Vertex vertex : graph.getAllVertex()) {
            Vertice vertice = Vertice.encapsulate(vertex);
            vertice.extractOwnVertices(todasAsArestas);
            todosOsVertices.add(vertice);
            if (vertice.isInitial) {
                verticeInicial = vertice;
            }
        }

        System.out.println("Todos os vertices: \n");

        todosOsVertices.forEach(vertice -> {
            System.out.println("\nq" + vertice.id + " :");
            vertice.arestasList.forEach(aresta -> {
                System.out.println("   q" + vertice.id + " --> q" + aresta.targetId + " == " + aresta.getGramarsString());
            });
        });

        System.out.println("\n\n");

    }

    public static Integer expressionPointer;

    public static boolean validadeString(String expression) {
        HistoryLog result = validadeStringWithLog(expression);
        return  result!= null ? result.match : false;
    }

    public static HistoryLog validadeStringWithLog(String expression) {
        if (verticeInicial == null) {
            return null;
        }

        HistoryLog historyLog = walk(new HistoryLog(expression,verticeInicial), verticeInicial);

        System.out.println("[" + historyLog.time + " iterations] resultSucces==" + historyLog.match + "  for the expression --> " + historyLog.expression);
        return historyLog;
    }

    public static HistoryLog walk(HistoryLog previousLog, Vertice vertice) {

        previousLog.addTime();

        char checkChar = previousLog.getCharToRead();

        for (Aresta aresta : vertice.arestasList) {
            List<PontoDeFita> possibleFutures = aresta.getPossibleFutures(checkChar);

            for (PontoDeFita pontoDeFita : possibleFutures){
                try {
                    if (pontoDeFita.getRead() == checkChar){
                        System.out.println("[reading '" + checkChar + "']Checking target  -:> q" + aresta.getTargetId() + " with logic [" + pontoDeFita + "]" + " %3s for the expression --> " + previousLog.expression);
                        //Passa uma cópia do historyLog atual
                        HistoryLog walkedHistoryLog = previousLog.clone();
                        Vertice targetVertice = aresta.getTargetVertice();
                        walkedHistoryLog.readAndWrite(pontoDeFita, targetVertice);
                        HistoryLog resultHistoryLog = walk(walkedHistoryLog, targetVertice);
                        if (resultHistoryLog.match) {
                            return resultHistoryLog;
                        }
                    }
                }catch (Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }

        //Se ele for final, e nao tem mais nada para ler, ja retorna pq deu certo
        if (vertice.isFinale) {
            System.out.println("Final <--");
            previousLog.match = true;
        }
        return previousLog;
    }

    public static void loadValidatorToGraph(){

        GraphController.clear();

        for (Vertice vertice : todosOsVertices){

            if (vertice.getArestasList().isEmpty() && getArestasThatReaches(vertice.getId()).isEmpty()){
                continue;
            }

            Vertex createdVertex = GraphController.getGraph().addCustomVertex(vertice.getId());

            createdVertex.setFinale(vertice.isFinale());
            createdVertex.setInitial(vertice.isInitial());
            if (vertice.getVertex() != null){
                createdVertex.getCircle().setCenterX(vertice.getVertex().getCircle().getCenterX());
                createdVertex.getCircle().setCenterY(vertice.getVertex().getCircle().getCenterY());
                if (vertice.getVertex().getCustomName() != ("q"+vertice.getId())) createdVertex.setCustomName(vertice.getVertex().getCustomName());
            }
            if (!vertice.customIdentifier.isEmpty()) createdVertex.setCustomName(vertice.customIdentifier);
        }

        for (Aresta aresta : todasAsArestas){

            Edge createdEdge = GraphController.getGraph().getOrCreateEdge(aresta.sourceId,aresta.targetId);

            Aresta edgeAresta = createdEdge.getOrCreateAresta(GraphController.getGraph().getVertex(aresta.getSourceId()));
            edgeAresta.getPontosDeFita().addAll(aresta.getPontosDeFita());
        }


        GraphController.getGraph().reloadPage();
    }

    public static List<Vertice> getFutureVertices(Vertice vertice){
        List<Vertice> verticeList = new ArrayList<Vertice>();
        for (Aresta aresta : vertice.arestasList){
            verticeList.add(getVerticeFromID(aresta.targetId));
        }
        return verticeList;
    }

    public static List<Vertice> getFutureNonSelfVertices(Vertice vertice){
        List<Vertice> verticeList = new ArrayList<Vertice>();
        for (Vertice aFutureVertice : getFutureVertices(vertice)){
            if (vertice != aFutureVertice){
                verticeList.add(aFutureVertice);
            }
        }
        return verticeList;
    }

    public static Vertice getVerticeFromID(int theId){
        for (Vertice vertice : todosOsVertices){
            if (vertice.id == theId){
                return vertice;
            }
        }
        return null;
    }

    public static Aresta getArestaBetween(int sourceId, int targetId){
        for (Aresta aresta : todasAsArestas){
            if (aresta.sourceId == sourceId && aresta.targetId == targetId){
                return aresta;
            }
        }
        return null;
    }

    public static List<Aresta> getArestasThatReaches(int targetId){
        List<Aresta> list = new ArrayList<>();
        for (Aresta aresta : todasAsArestas){
            if (aresta.targetId == targetId){
                list.add(aresta);
            }
        }
        return list;
    }




}