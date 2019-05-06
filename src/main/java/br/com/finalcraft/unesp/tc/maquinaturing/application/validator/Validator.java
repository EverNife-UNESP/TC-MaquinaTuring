package br.com.finalcraft.unesp.tc.maquinaturing.application.validator;

import br.com.finalcraft.unesp.tc.DEBUGGER;
import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Aresta;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Vertice;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Edge;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Graph;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public static String theExpression;

    public static boolean validadeString(String expression) {
        theExpression = expression;

        if (verticeInicial == null) {
            return false;
        }

        onTimeChange(); // Reinicia o infiniteLoopPreventer
        HistoryLog historyLog = walk(new HistoryLog(), verticeInicial);

        System.out.println("[" + historyLog.time + "]  --> " + historyLog.path);
        return historyLog.math;
    }

    public static HistoryLog validadeStringWithLog(String expression) {
        theExpression = expression;

        if (verticeInicial == null) {
            return null;
        }

        onTimeChange(); // Reinicia o infiniteLoopPreventer
        HistoryLog historyLog = walk(new HistoryLog(), verticeInicial);

        System.out.println("[" + historyLog.time + "]  --> " + historyLog.path);
        return historyLog;
    }

    public static List<Integer> infiniteLoopPreventer = new ArrayList<Integer>();

    public static void onTimeChange() {
        infiniteLoopPreventer.clear();
    }

    public static HistoryLog walk(HistoryLog previousLog, Vertice vertice) {

        //Se ele for final, ja retorna pq deu certo
        previousLog.addPath(vertice.id);
        if (vertice.isFinale && previousLog.time + 1 == theExpression.length()) {
            System.out.println("Final <--");
            previousLog.math = true;
            return previousLog;
        }

        //Verifica os caminhos com vazio
        for (Aresta aresta : vertice.arestasList) {
            try {
                if (aresta.getPontosDeFita().contains('\u03B5') && !infiniteLoopPreventer.contains(aresta.getTargetId())) {
                    infiniteLoopPreventer.add(aresta.getTargetId());
                    System.out.println("Null Dislock  -:> " + aresta.getTargetId());
                    HistoryLog previousPrevious = previousLog.clone();
                    previousPrevious.addNullPathPrefix();

                    HistoryLog historyLog = walk(previousPrevious, getVerticeFromID(aresta.getTargetId()));
                    if (historyLog.math) {
                        return historyLog;
                    }
                }
            } catch (Exception ignored) {
            }
        }

        //Atualiza o tempo do estado
        previousLog.addTime();
        onTimeChange(); // Reinicia o infiniteLoopPreventer

        //
        for (Aresta aresta : vertice.arestasList) {
            try {
                char checkChar = theExpression.charAt(previousLog.time);
                if (aresta.getPontosDeFita().contains(checkChar)) {
                    System.out.println("[" + checkChar + "]Checking target  -:> " + aresta.getTargetId());

                    //Passa uma cópia do historilog atual
                    HistoryLog historyLog = walk(previousLog.clone(), getVerticeFromID(aresta.getTargetId()));
                    if (historyLog.math) {
                        return historyLog;
                    }
                }
            } catch (Exception ignored) {
            }
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

    /*

    public static boolean convertToAFD() {

        if (verticeInicial == null){
            return false;
        }

        Map<Aresta,List<PontoDeFita>> valuesToBeRemovedAtTheEnd = new HashMap<Aresta,List<PontoDeFita>>();

        for (Aresta aresta : todasAsArestas){
            List<PontoDeFita> selfVerticesGRammars = new ArrayList<>();
            selfVerticesGRammars.addAll(aresta.getPontosDeFita());
            valuesToBeRemovedAtTheEnd.put(aresta,selfVerticesGRammars);
        }


        List<Character> allPossibleGrammars = new ArrayList<>();
        for (Aresta aresta : todasAsArestas){
            for (PontoDeFita pontoDeFita : aresta.getPontosDeFita()){
                if (!allPossibleGrammars.contains(character)){
                    allPossibleGrammars.add(character);
                }
            }
        }

        System.out.print("\n-----------------------\n\n\n All possible grammars :"); allPossibleGrammars.forEach(character -> System.out.print(character + "|"));
        System.out.println("\n");

        List<Vertice> interestingVertices = new ArrayList<>();

        interestingVertices.add(verticeInicial);

        boolean needAnotherLoop;
        int largerId = 0;
        Map<String,Integer> mapOfVerticesByCustomName = new HashMap<String, Integer>();
        for (Vertice vertice : todosOsVertices){
            vertice.customIdentifier = "q" + vertice.getId();
            mapOfVerticesByCustomName.put(vertice.customIdentifier,vertice.id);
            if (largerId <= vertice.getId()) largerId = vertice.getId() + 1;
        }


        int internalLimit = 10;

        do {
            needAnotherLoop = false;

            internalLimit--;
            if (internalLimit == 0){
                break;
            }



            List<Vertice> newInterestingVertices = new ArrayList<>();

            for (Vertice sourceVertice : interestingVertices){

                if (sourceVertice.alreadyChecked){
                    continue;
                }
                sourceVertice.alreadyChecked = true;

                System.out.println("\n\nLooking for vertice :" + sourceVertice.customIdentifier);

                List<Vertice> splitedVertices = new ArrayList<>();
                if (sourceVertice.customIdentifier.length() > 2){
                    String[] ids = sourceVertice.customIdentifier.split(",");
                    for (String id : ids){
                        splitedVertices.add(getVerticeFromID(mapOfVerticesByCustomName.get(id)));
                    }
                }else {
                    splitedVertices.add(sourceVertice);
                }

                todosOsVertices.forEach(forEacVertice -> forEacVertice.extractOwnVertices(todasAsArestas));
                for (Character grammar : allPossibleGrammars){

                    StringBuilder newRule = new StringBuilder();
                    //List<Aresta> arestasToRemove = new ArrayList<>();

                    for (Vertice splitedVertice : splitedVertices){
                        for (Aresta aresta : splitedVertice.getArestasList()){
                            if (aresta.getPontosDeFita().contains(grammar)){
                                String partOfTheNewVerticeRule = getVerticeFromID(aresta.getTargetId()).customIdentifier;
                                if (!newRule.toString().contains(partOfTheNewVerticeRule)){
                                    newRule.append(getVerticeFromID(aresta.getTargetId()).customIdentifier + ",");
                                }
                                //if (!sourceVertice.alreadyChecked) aresta.getPontosDeFita().remove(grammar);
                            }
                        }
                    }

                    if (newRule.length() >= 2){
                        // todasAsArestas.removeAll(arestasToRemove);
                        //sourceVertice.getArestasList().removeAll(arestasToRemove);

                        if (newRule.length() >= 3){
                            newRule.setLength(newRule.length() - 1);
                        }
                        String theRule = newRule.toString();

                        int futureVerticeId = mapOfVerticesByCustomName.getOrDefault(theRule,-1);

                        System.out.print("    Rule for " + grammar + " is " + newRule.toString());

                        Vertice futureVertice;
                        if (futureVerticeId == -1){
                            futureVertice = new Vertice();
                            futureVertice.id = largerId; largerId++;
                            futureVertice.customIdentifier = theRule;
                            mapOfVerticesByCustomName.put(futureVertice.customIdentifier,futureVertice.id);
                            todosOsVertices.add(futureVertice);
                            System.out.println("         [Adding + " + theRule);
                        }else {
                            futureVertice = getVerticeFromID(futureVerticeId);
                        }

                        Aresta aresta = getArestaBetween(sourceVertice.getId(),futureVertice.getId());
                        if (aresta == null){
                            aresta = new Aresta(sourceVertice.getId(),futureVertice.getId());
                            todasAsArestas.add(aresta);
                        }

                        aresta.getPontosDeFita().add(grammar);


                        if (!interestingVertices.contains(futureVertice) && !newInterestingVertices.contains(futureVertice)){
                            newInterestingVertices.add(futureVertice);
                            needAnotherLoop = true;
                        }
                    }
                }
            }

            interestingVertices.addAll(newInterestingVertices);
        }while (needAnotherLoop);


        for (Vertice vertice : interestingVertices){
            List<Vertice> splitedVertices = new ArrayList<>();
            if (vertice.customIdentifier.length() > 2){
                String[] ids = vertice.customIdentifier.split(",");
                for (String id : ids){
                    splitedVertices.add(getVerticeFromID(mapOfVerticesByCustomName.get(id)));
                }
            }else {
                splitedVertices.add(vertice);
            }

            for (Vertice splitedVertice : splitedVertices){
                if (splitedVertice.isFinale){
                    vertice.isFinale = true;
                }
            }

        }

        List<Vertice> markedToBeRemoved = new ArrayList<>();
        for (Vertice vertice : todosOsVertices){
            if (!interestingVertices.contains(vertice)){
                markedToBeRemoved.add(vertice);
            }
        }


        for (Aresta aresta : valuesToBeRemovedAtTheEnd.keySet()){
            List<Character> grammarsToBeRemoved = valuesToBeRemovedAtTheEnd.get(aresta);
            for (Character character : grammarsToBeRemoved){
                aresta.getPontosDeFita().remove(character);
            }
        }


        System.out.println("Todos os vertices: \n");

        todosOsVertices.forEach(vertice -> {
            System.out.println("\nq" + vertice.id + " :");
            vertice.arestasList.forEach(aresta -> {
                System.out.println("   q" + vertice.id + " --> q" + aresta.targetId + " == " + aresta.getGramarsString());
            });
        });

        Validator.loadValidatorToGraph();

        for (Vertice vertice : markedToBeRemoved){
            GraphController.getGraph().removeVertex(vertice.getId());
        }


        GraphController.getGraph().reloadPage();

        return true;
    }

    public static void removeNullEdges(){

        boolean atLeastOneIsNull;
        do {
            boolean doubleBreak = false;
            Vertice sourceVertice = null;
            Vertice targetVertice = null;

            atLeastOneIsNull = false;
            for (Vertice vertice : todosOsVertices){
                doubleBreak = false;

                for (Aresta aresta : vertice.getArestasList()){

                    if (aresta.getSourceId() == aresta.getTargetId()) {
                        if (aresta.getPontosDeFita().contains('ε')) {
                            aresta.getPontosDeFita().removeIf(character -> character == 'ε');
                        }
                        continue; // Ignora caso seja uma aresta apontando para o próprio vertice!
                    }
                    if (aresta.getPontosDeFita().contains('ε')){
                        aresta.getPontosDeFita().forEach(character -> {
                            DEBUGGER.info(aresta.getIdentifier() + " connections : " + character);
                        });

                        sourceVertice = getVerticeFromID(aresta.getSourceId());
                        targetVertice = getVerticeFromID(aresta.getTargetId());


                        for (int i = 0; i < aresta.getPontosDeFita().size() ; i++){
                            if (aresta.getPontosDeFita().get(i) == 'ε'){
                                aresta.getPontosDeFita().remove(i);
                                break;
                            }
                        }

                        boolean hasMoreConnections = false;
                        if (aresta.getPontosDeFita().size() == 0){
                            sourceVertice.getArestasList().remove(aresta);
                            todasAsArestas.remove(aresta);
                        }else {
                            hasMoreConnections = true; // Algo do tipo q1 --> a2 == a | ε (algo conecta os dois vertices alem do vazio)
                        }

                        if (true){
                            //Pego todas as arestas do vertice futuro e CLONO elas, alterando a origem para o vertice anterior
                            for (Aresta inAresta : targetVertice.getArestasList()){

                                Aresta newAresta = inAresta.cloneAresta();
                                newAresta.sourceId = sourceVertice.getId();

                                boolean foundAresta = false;
                                for (Aresta sourceAresta : sourceVertice.getArestasList()){
                                    if (sourceAresta.isSameTransaction(newAresta)){
                                        sourceAresta.getPontosDeFita().addAll(newAresta.getPontosDeFita());
                                        foundAresta = true;
                                        break;
                                    }
                                }

                                if (!foundAresta){
                                    todasAsArestas.add(newAresta);
                                    sourceVertice.getArestasList().add(newAresta);
                                }
                            }
                        }

                        if (targetVertice.isInitial()) sourceVertice.isInitial = true;
                        if (targetVertice.isFinale()) sourceVertice.isFinale = true;

                        DEBUGGER.info("DoubleBreak on " + sourceVertice.getId() + " to " + targetVertice.getId());
                        doubleBreak = true;
                        atLeastOneIsNull = true;
                        break;
                    }
                }

                if (doubleBreak){
                    todosOsVertices.forEach(forEacVertice -> forEacVertice.extractOwnVertices(todasAsArestas));
                    break;
                }
            }


        }while (atLeastOneIsNull);

        //Deletar arestas vazias
        todasAsArestas.removeIf(aresta -> {
            return (aresta.getPontosDeFita().size() == 0);
        });

        System.out.println("\n\nTodos os vertices remanejados: \n");

        todosOsVertices.forEach(vertice -> {
            System.out.println("\nq" + vertice.id + " :");
            vertice.arestasList.forEach(aresta -> {
                System.out.println("   q" + vertice.id + " --> q" + aresta.targetId + " == " + aresta.getGramarsString() );
            });
        });

        System.out.println("\n\n");
    }
    */


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