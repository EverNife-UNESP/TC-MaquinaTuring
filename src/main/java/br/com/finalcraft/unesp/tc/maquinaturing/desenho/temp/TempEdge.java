/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.finalcraft.unesp.tc.maquinaturing.desenho.temp;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempEdge {

    public static Map<Integer,TempEdge> tempEdges = new HashMap<Integer, TempEdge>();

    private Color color = Color.valueOf("ffA0ff"); //Cor da aresta
    private Vertex source; //primeiro vetice da aresta
    private Line line;
    private Group arrow;
    private boolean bidiretec = false;
    private Text text;
    private Circle c2;

    DoubleProperty mousex;
    DoubleProperty mousey;

    private List<Character> grammarChars = new ArrayList<Character>();

    // ================================================================================================================= //

    public static TempEdge getOrCreateOne(Vertex source, double mouseX, double mouseY) {
        return tempEdges.getOrDefault(source.getID(),new TempEdge(source,mouseX,mouseY));
    }

    public TempEdge(Vertex source, double mouseX, double mouseY) {
        this.source = source;
        connect(source.getCircle(),mouseX,mouseY);
        tempEdges.put(source.getID(),this);
        GraphController.getGraph().addTempEdge(this);
        //connect(source.getCircle());
    }

    // para direcionado
    private float r;
    private float cos;
    private float sen;

    public Line getConnect(){
        return this.line;
    }

    public void clear(){
        line.setOpacity(1.0f);
        line.setStrokeWidth(6.0f); //def 3

        //arrow.getChildren().add(line3);
        //arrow.getChildren().add(line4);
    }

    public Group connect(Circle c1,double mouseX, double mouseY){
        arrow = new Group();
        line = new Line();

        c2 = new Circle();
        c2.setCenterX(mouseX);
        c2.setCenterX(mouseY);

        this.clear();
        //Color.rgb(200, 253, 255, 1);
        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());

        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());

        line.setStroke(color);

        line.toBack();

        arrow.getChildren().add(line);
        return arrow;
    }

    public Group getArrow() {
        return arrow;
    }

    public void delete() {
        GraphController.getGraph().removeAllTempEdges();
    }

    public void updateOnMouseMouve(double mouseX, double mouseY){
        this.c2.setCenterX(mouseX);
        this.c2.setCenterY(mouseY);
    }

    public Vertex getSource(){
        return this.source;
    }
}
