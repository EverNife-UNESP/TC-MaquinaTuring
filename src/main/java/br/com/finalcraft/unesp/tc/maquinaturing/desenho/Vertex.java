/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.finalcraft.unesp.tc.maquinaturing.desenho;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.temp.TempEdge;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.MainController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.states.StateEditorController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.transitions.AddTransitionController;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private float x;
    private float y;
    private int ray = 15;
    private Boolean selected = true;
    private Color color = Color.RED;
    private int ID;
    private String customName = "";
    private Circle vertex;
    private boolean initial = false;
    private boolean finale = false;
    TextFlow idVertex;

    private TempEdge tempEdge;

    //==================================================================================================================
    // Finite Automation Needs
    //==================================================================================================================

    public void repaint(){
        if (tempEdge != null){
            GraphController.getGraph().removeAllTempEdges();
            tempEdge = null;
        }

        if (initial){
            vertex.setStroke(Color.LIGHTGREEN);
            vertex.setStrokeWidth(6);
        }else {
            vertex.setStroke(Color.BLACK);
            vertex.setStrokeWidth(3);
        }

        if (finale){
            vertex.setFill(Color.RED);
        }else {
            vertex.setFill(Color.rgb(255, 186, 182, 1));
        }
    }


    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
        repaint();
    }

    public boolean isFinale() {
        return finale;
    }

    public void setFinale(boolean finale) {
        this.finale = finale;
        repaint();
    }


    public void setCustomName(String newName){
        this.customName = newName;
        updateText();
    }

    public String getCustomName(){
        if (customName.isEmpty()){
            return "q" + ID;
        }
        return customName;
    }

    public boolean isPointInCircle(double centerX, double centerY, double radius, double x, double y){
        if(isInRectangle(centerX, centerY, radius, x, y)){
            double dx = centerX - x;
            double dy = centerY - y;
            dx *= dx;
            dy *= dy;
            double distanceSquared = dx + dy;
            double radiusSquared = radius * radius;
            return distanceSquared <= radiusSquared;
        }
        return false;
    }

    boolean isInRectangle(double centerX, double centerY, double radius, double x, double y){
        return x >= centerX - radius && x <= centerX + radius &&
                y >= centerY - radius && y <= centerY + radius;
    }

    public void delete(){
        List<Edge> edgesToBeRemoved = new ArrayList<Edge>();
        for (Edge anEdge : GraphController.getGraph().getEdges()){
            if (!edgesToBeRemoved.contains(anEdge)){
                if (anEdge.getSource() == this || anEdge.getTarget() == this){
                    edgesToBeRemoved.add(anEdge);
                }
            }
        }

        for (Edge edge : edgesToBeRemoved){
            GraphController.getGraph().edges.remove(edge);
        }
        GraphController.getGraph().vertex.remove(this);
        GraphController.getGraph().reloadPage();
    }

    //==================================================================================================================




    private double orgSceneX, orgSceneY;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void clear(){
        try {
            if(this.selected){
                vertex.setOpacity(1.0f);
                vertex.setStrokeWidth(3.0f);
            }else{
                vertex.setOpacity(0.2f);
                vertex.setStrokeWidth(1.5f);
            }

            vertex.setStroke(Color.BLACK);

            this.changeColor();
        }catch (Exception ignored){

        }
    }

    public TextFlow createText(){
        idVertex = new TextFlow();
        idVertex.setLayoutX(vertex.getCenterX() - 8);
        idVertex.setLayoutY(vertex.getCenterY() - 6);
        Text text = new Text(getCustomName());
        text.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        text.setFill(Color.BLUEVIOLET);
        idVertex.getChildren().addAll(text);
        return idVertex;
    }

    public void updateText(){
        Text text = new Text(getCustomName());
        text.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        text.setFill(Color.BLUEVIOLET);
        idVertex.getChildren().clear();
        idVertex.getChildren().addAll(text);
    }

    public TextFlow getText(){
        return idVertex;
    }

    public Circle createCircle() {

        vertex = new Circle(((int) this.x), ((int) this.y), this.getRay() * 2, this.color);
        vertex.setId("q" + this.ID);

        //vertex = new Circle(((int) this.x) - this.getRay(), ((int) this.y)
        //       - this.getRay(), this.getRay() * 2, this.color);

        this.clear();

        vertex.setStroke(Color.BLACK);

        vertex.setCursor(Cursor.HAND);

        vertex.setOnMousePressed((event) -> {

            switch (MainController.getPressedRadioButton()){
                case EDITAR_ESTADOS:{
                    if (event.getButton().equals(MouseButton.SECONDARY)){
                        StateEditorController.show(this);
                        System.out.println("Mostrando editor de Estado!");
                    }
                    if (event.getButton().equals(MouseButton.PRIMARY)){
                        orgSceneX = event.getSceneX();
                        orgSceneY = event.getSceneY();

                        Circle c = (Circle) (event.getSource());
                        idVertex.setLayoutX(c.getCenterX() - 8);
                        idVertex.setLayoutY(c.getCenterY() - 6);
                        c.toFront();
                        idVertex.toFront();
                    }
                    break;
                }
                case EDITAR_TRANSICOES:{

                }
            }




        });
        vertex.setOnMouseReleased((event) -> {
            switch (MainController.getPressedRadioButton()){
                case EDITAR_TRANSICOES:{

                    if (event.getButton().equals(MouseButton.PRIMARY)){
                        for (Node node : GraphController.getGraph().getGroup().getChildren()){
                            if (node instanceof Circle){
                                Circle circle = (Circle) node;
                                if (isPointInCircle(circle.getCenterX(),circle.getCenterY(),circle.getRadius(),event.getX(),event.getY())){
                                    int vIni = this.getID(); //verticeInicial
                                    int vFim = Integer.valueOf(circle.getId().replaceAll("q","")); //verticeFinal

                                    Vertex vS = GraphController.getGraph().getVertex(vIni);
                                    Vertex vT = GraphController.getGraph().getVertex(vFim);

                                    AddTransitionController.show(vS,vT);

                                /* // Funcinal, but, comentando para testar add_transaction
                                Edge e = new Edge(vS, vT, 0, 0); //desenho
                                GraphController.graph.addEdge(e);    //desenho
                                e.changeText("\nq1-->q2  : a|b|c|d" +
                                        "\nq2-->q1  : b|2");
                                                                        */
                                    System.out.println("I am here: " + circle.getId());
                                    break;


                                }
                            }
                        }
                    }

                    repaint();
                    break;
                }
            }

        });

        vertex.setOnMouseDragged((event) -> {
            switch (MainController.getPressedRadioButton()){
                case EDITAR_ESTADOS:{
                    if (event.getButton().equals(MouseButton.SECONDARY)){
                        StateEditorController.show(this);
                        System.out.println("Mostrando editor de Estado!");
                    }

                    if (event.getButton().equals(MouseButton.PRIMARY)){

                        double offsetX = event.getSceneX() - orgSceneX;
                        double offsetY = event.getSceneY() - orgSceneY;

                        Circle c = (Circle) (event.getSource());

                        c.setCenterX(c.getCenterX() + offsetX);
                        c.setCenterY(c.getCenterY() + offsetY);
                        idVertex.setLayoutX(c.getCenterX() - 8);
                        idVertex.setLayoutY(c.getCenterY() - 6);

                        orgSceneX = event.getSceneX();
                        orgSceneY = event.getSceneY();
                    }
                    break;
                }
                case EDITAR_TRANSICOES:{

                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        if (tempEdge == null){
                            this.tempEdge = TempEdge.getOrCreateOne(this,event.getX(),event.getY());
                            System.out.println("criando tempdege");
                            vertex.setStroke(Color.YELLOW);
                        }else {
                            this.tempEdge.updateOnMouseMouve(event.getX(),event.getY());
                        }
                    }



                    //Vertex vS = GraphController.graph.;
                    /*
                    Vertex tempVertex = new Vertex();
                    tempVertex.setX(this.getX());
                    tempVertex.setY(this.getY());
                    GraphController.graph.getGroup().getChildren().add(tempVertex.createCircle());

                    Edge tempEdge = new Edge(this, this, 0, 1);
                    Group line = tempEdge.connect(tempEdge.getSource().getCircle(), tempEdge.getTarget().getCircle());
                    GraphController.graph.getGroup().getChildren().add(line);
                    tempEdge.getTarget().getCircle().toFront();
                    tempEdge.getTarget().getText().toFront();
                    */

                }
            }

        });
        return vertex;
    }

    public TextFlow getIdVertex(){
        return this.idVertex;
    }

    public Circle getCircle(){
        return this.vertex;
    }

    public void changeColor(Color color){
        vertex.setFill(color);
    }

    public void changeColor(){
        vertex.setFill(this.color);
    }

    public void changeTempColor(Color color){
        vertex.setFill(color);
    }

    public float getX() {
        return x;
    }

    public void setX(float X) {
        this.x = X;
    }

    public float getY() {
        return y;
    }

    public void setY(float Y) {
        this.y = Y;
    }

    public int getRay() {
        return ray;
    }

    public void setRay(int ray) {
        this.ray = ray;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean flag) {
        this.selected = flag;
    }
}
