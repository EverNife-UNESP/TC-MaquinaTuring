package br.com.finalcraft.unesp.tc.maquinaturing.desenho;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.temp.TempEdge;
import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;

public class Graph {
    protected ArrayList<Vertex> vertex = new ArrayList<Vertex>();
    protected ArrayList<Edge> edges = new ArrayList<Edge>();
    protected ArrayList<TempEdge> tempEdges = new ArrayList<TempEdge>();

    private Group group = new Group();

    private int nVertices = 0;


    public void clearAll(){
        vertex.clear();
        edges.clear();
        tempEdges.clear();
        nVertices = 0;
        runnedONce = false;
        reloadPage();
    }

    public boolean hasAnyInitial(){
        for (Vertex vertex : vertex){
            if (vertex.isInitial()){
                return true;
            }
        }
        return false;
    }

    public void addVertexAtMouse(float xCoord, float yCoord){
        Vertex v = new Vertex();
        v.setID(nVertices);
        nVertices++;
        v.setColor(Color.rgb(255, 186, 182, 1));
        v.setX(xCoord);
        v.setY(yCoord);
        this.vertex.add(v);


        //Quando for adicionar o primeiro vértice, é necessário criar os grupos e vincular eles ao painel
        if (runnedONce){
            Circle circle = v.createCircle();
            TextFlow idVertex = v.createText();
            group.getChildren().addAll(circle, idVertex);
        }else {
            this.addObjects();
        }
    }

    public void addVertex(){
        Vertex v = new Vertex();
        v.setID(nVertices);
        nVertices++;
        v.setColor(Color.rgb(255, 186, 182, 1));
        this.vertex.add(v);
        computeStartPositionVertex(v);

        //Quando for adicionar o primeiro vértice, é necessário criar os grupos e vincular eles ao painel
        if (runnedONce){
            Circle circle = v.createCircle();
            TextFlow idVertex = v.createText();
            group.getChildren().addAll(circle, idVertex);
        }else {
            this.addObjects();
        }
    }

    public Vertex addCustomVertex(int customId){
        Vertex v = new Vertex();
        v.setID(customId);
        if (nVertices <= customId) nVertices = customId + 1;
        v.setColor(Color.rgb(255, 186, 182, 1));
        this.vertex.add(v);
        computeStartPositionVertex(v);

        //Quando for adicionar o primeiro vértice, é necessário criar os grupos e vincular eles ao painel
        if (runnedONce){
            Circle circle = v.createCircle();
            TextFlow idVertex = v.createText();
            group.getChildren().addAll(circle, idVertex);
        }else {
            this.addObjects();
        }
        return v;
    }

    public void removeVertex(int id){
        Vertex theVertex = null;
        for (Vertex aVertex : this.vertex){
            if (aVertex.getID() == id){
                group.getChildren().removeAll(aVertex.getCircle(),aVertex.getText());
                aVertex.clear();
                theVertex = aVertex;
            }
        }
        if (theVertex!= null){
            theVertex.delete();
        }
    }

    public void computeStartPositionVertex(Vertex v){
        float X=0 + GraphController.getGraph().vertex.size() * 5;
        float Y=0 + GraphController.getGraph().vertex.size() * 5;
        v.setX(X);
        v.setY(Y);
    }

    public Graph() {
    }

    public Edge getOrCreateEdge(int sourceId, int targetId) {
        return getOrCreateEdge(getVertex(sourceId),getVertex(targetId));

    }


    public Edge getOrCreateEdge(Vertex source, Vertex target) {
        for (Edge aEdge : edges){
            if ((source == aEdge.getSource() && target == aEdge.getTarget())){
                return aEdge;
            }
            if (source == aEdge.getTarget() && target == aEdge.getSource()){
                aEdge.setBidiretec(true);
                reloadPage();
                return aEdge;
            }
        }
        Edge e = new Edge(source, target);
        addEdge(e);
        return e;
    }

    public void addEdge(Edge edge){

        for (Edge aEdge : edges){
            if ((edge.getSource() == aEdge.getSource() && edge.getTarget() == aEdge.getTarget())){
                return;
            }
            if (edge.getSource() == aEdge.getTarget() && edge.getTarget() == aEdge.getSource()){
                aEdge.setBidiretec(true);
                aEdge.updateArrow();
                return;
            }
        }

        this.edges.add(edge);
        Group line = edge.connect(edge.getSource().getCircle(), edge.getTarget().getCircle());
        group.getChildren().add(line);
        edge.getSource().getCircle().toFront();
        edge.getSource().getText().toFront();
        edge.getTarget().getCircle().toFront();
        edge.getTarget().getText().toFront();
    }

    public void computeCircledPosition(int ray){
        int nVert = this.vertex.size();
        int step = 360 / nVert;
        int deslocX = 100 + ray;
        int deslocY = 100 + ray;
        for (int i=0; i<nVert; i++){
            double ang = i * step;
            ang = ang * Math.PI / 180;//necessario em radianos
            float X = (float) Math.cos(ang);
            float Y = (float) Math.sin(ang);
            X = X * ray + deslocX;
            Y = Y * ray + deslocY;
            this.vertex.get(i).setX(X);
            this.vertex.get(i).setY(Y);
        }
    }

    public ArrayList<Vertex> getAllVertex() {
        return this.vertex;
    }

    public Vertex getVertex(int id) {
        for (Vertex vertex : this.vertex){
            if (vertex.getID() == id){
                return vertex;
            }
        }
        return  null;
    }

    public ArrayList<Edge> getEdges() {
        return this.edges;
    }

    public Edge getEdge(Vertex source, Vertex target) {
        for (Edge edge : this.edges){
            if (edge.getSource() == source){
                if (edge.getTarget() == target){
                    return edge;
                }
            }
            if (edge.isBidiretec() && edge.getTarget() == source && edge.getSource() == target){
                return edge;
            }
        }
        return null;
    }

    boolean runnedONce = false;



    public void addTempEdge(TempEdge tempEdge){
        Group line = tempEdge.getArrow();
        group.getChildren().add(line);
        tempEdge.getSource().getCircle().toFront();
        tempEdge.getSource().getText().toFront();
        tempEdges.add(tempEdge);
    }

    public void removeAllTempEdges(){
        for (TempEdge edge : tempEdges) {
            group.getChildren().removeAll(edge.getArrow());
            edge.getSource().getCircle().toFront();
            edge.getSource().getText().toFront();
        }
        tempEdges.clear();
    }

    public void reloadPage(){
        BorderPane bp = JavaFXMain.getBP();
        group.getChildren().clear();

        //Draw each vertice of the graph
        for (Vertex v : this.vertex) {
            group.getChildren().addAll(v.getCircle(), v.getIdVertex());
            v.repaint();
            v.idVertex.setLayoutX(v.getCircle().getCenterX() - 8);
            v.idVertex.setLayoutY(v.getCircle().getCenterY() - 6);
        }

        for (Edge edge : edges) {
            group.getChildren().add(edge.getConnect());
            group.getChildren().add(edge.getArrow());
            edge.calculateAndSetTextOne();
            edge.updateArrow();

            edge.getSource().getCircle().toFront();
            edge.getSource().getText().toFront();
            edge.getTarget().getCircle().toFront();
            edge.getTarget().getText().toFront();
        }
    }



    public void addObjects(){

        BorderPane bp = JavaFXMain.getBP();
        clearObjects();

        group.getChildren().clear();
        //Draw each vertice of the graph
        for (Vertex v : this.vertex) {
            Circle circle = v.createCircle();
            TextFlow idVertex = v.createText();
            group.getChildren().addAll(circle, idVertex);
        }

        for (Edge edge : edges) {
            Group line = edge.connect(edge.getSource().getCircle(), edge.getTarget().getCircle());
            group.getChildren().add(line);
            edge.getSource().getCircle().toFront();
            edge.getSource().getText().toFront();
            edge.getTarget().getCircle().toFront();
            edge.getTarget().getText().toFront();
        }

        if (!runnedONce){
            bp.setCenter(group);
            runnedONce = true;
        }
    }

    public Group getGroup(){
        return group;
    }

    public void clearObjects(){

        for (Vertex v : this.vertex) {
            v.clear();
        }

        for (Edge edge : edges) {
            edge.clear();
        }

        removeAllTempEdges();
    }

    public java.awt.Dimension getSize() {
        if (this.vertex.size() > 0) {
            float maxX = vertex.get(0).getX();
            float minX = vertex.get(0).getX();
            float maxY = vertex.get(0).getY();
            float minY = vertex.get(0).getY();

            //Encontra o maior e menor valores para X e Y
            for (Vertex v : this.vertex) {
                if (maxX < v.getX()) {
                    maxX = v.getX();
                } else {
                    if (minX > v.getX()) {
                        minX = v.getX();
                    }
                }

                if (maxY < v.getY()) {
                    maxY = v.getY();
                } else {
                    if (minY > v.getY()) {
                        minY = v.getY();
                    }
                }
            }

            int w = (int) (maxX + (this.vertex.get(0).getRay() * 5)) + 350;
            int h = (int) (maxY + (this.vertex.get(0).getRay() * 5));

            return new java.awt.Dimension(w, h);
        } else {
            return new java.awt.Dimension(0, 0);
        }
    }
}
