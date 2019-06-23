package br.com.finalcraft.unesp.tc.maquinaturing.application.validator;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Aresta;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Vertice;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog.HistoryLog;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Edge;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Graph;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.MainController;

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

    public static boolean validadeString(String expression) {
        HistoryLog result = validadeStringWithLog(expression);
        return  result!= null ? result.match : false;
    }

    public static HistoryLog validadeStringWithLog(String expression) {
        if (verticeInicial == null) {
            return null;
        }

        if (expression.isEmpty()){
            expression = "" + PontoDeFita.EMPTY_CHAR;
        }

        HistoryLog historyLog = walk(new HistoryLog(expression,verticeInicial), verticeInicial);

        System.out.println("[" + historyLog.time + " iterations] resultSucces==" + historyLog.match + "  for the expression --> " + historyLog.expression);
        return historyLog;
    }

    public static boolean validadeStringMark2(String[] expressions) {
        HistoryLog[] result = validadeStringWithLogMark2(expressions);
        return  result[0]!= null ? result[0].match : false;
    }

    public static HistoryLog[] validadeStringWithLogMark2(String[] expressions) {
        if (verticeInicial == null) {
            return null;
        }

        HistoryLog[] historyLogs = new HistoryLog[expressions.length];
        for (int i = 0; i < expressions.length; i++) {
            if (expressions[i].isEmpty()) expressions[i] = "" + PontoDeFita.EMPTY_CHAR;
            historyLogs[i] = new HistoryLog(expressions[i],verticeInicial);
        }

        historyLogs = walkMark2(historyLogs, verticeInicial);

        System.out.println("[" + historyLogs[0].time + " iterations] resultSucces==" + historyLogs[0].match + "  for the expression --> " + historyLogs[0].expression);
        return historyLogs;
    }

    public static HistoryLog[] walkMark2(HistoryLog[] previousLogs, Vertice vertice) {

        //int count001 = 1;
        for (HistoryLog historyLog : previousLogs){
            historyLog.addTime();
            //count001++;
        }

        char checkChar = previousLogs[0].getCharToRead();
        //System.out.println("Checking char [" + checkChar + "]...");
        for (Aresta aresta : vertice.arestasList) {
            List<PontoDeFita> possibleFuturesForFirstTape = aresta.getPossibleFutures(checkChar);
            System.out.println(possibleFuturesForFirstTape.size() + " possibleFutures...");
            for (PontoDeFita pontoDeFita : possibleFuturesForFirstTape){
                //System.out.println(" Checking in on " + pontoDeFita + " - Order: " + pontoDeFita.getOrder());
                try {
                    List<PontoDeFita> sequentialPontosDeFita = aresta.getPontosDeFita(pontoDeFita.getOrder());

                    //System.out.println("Found " + sequentialPontosDeFita.size() + " sequentialPontosDeFita");
                    boolean canGoThisWay = true;
                    for (int i = 1; i<previousLogs.length; i++){
                        if (previousLogs[i].getCharToRead() != sequentialPontosDeFita.get(i).getRead()){
                           // System.out.println("Cant go this way, sorry...");
                            canGoThisWay = false;
                            break;
                        }
                    }

                    if (!canGoThisWay) continue;

                    for (int i = 0; i < previousLogs.length; i++) {
                        PontoDeFita rightFita = sequentialPontosDeFita.get(i);
                        System.out.println("[reading '" + rightFita.getRead() + "']Checking target  -:> q" + aresta.getTargetId() + " with logic " + rightFita + " " + " for the expression --> " + previousLogs[i].expression);
                    }
                    System.out.println("  ");


                    //Passa uma cópia do historyLog atual
                    HistoryLog[] walkedHistoryLogs = new HistoryLog[previousLogs.length];

                    walkedHistoryLogs[0] = previousLogs[0].clone();
                    Vertice targetVertice = aresta.getTargetVertice();
                    walkedHistoryLogs[0].readAndWrite(pontoDeFita, targetVertice);

                    for (int i = 1; i < previousLogs.length; i++) {
                        walkedHistoryLogs[i] = previousLogs[i].clone();
                        walkedHistoryLogs[i].readAndWrite(sequentialPontosDeFita.get(i), targetVertice);
                    }

                    HistoryLog[] resultHistoryLog = walkMark2(walkedHistoryLogs, targetVertice);
                    if (resultHistoryLog[0].match) {
                        return resultHistoryLog;
                    }

                }catch (Exception ignored){
                    ignored.printStackTrace();
                }
            }
        }
        
        //Se ele for final, e nao tem mais nada para ler, ja retorna pq deu certo
        if (vertice.isFinale) {
            System.out.println("Final <--");
            previousLogs[0].match = true;
        }
        System.out.println("NonFinal <--");
        return previousLogs;
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
        System.out.println("NonFinal <--");
        return previousLog;
    }

    public static List<Vertice> getFutureVertices(Vertice vertice){
        List<Vertice> verticeList = new ArrayList<Vertice>();
        for (Aresta aresta : vertice.arestasList){
            verticeList.add(getVerticeFromID(aresta.targetId));
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
}