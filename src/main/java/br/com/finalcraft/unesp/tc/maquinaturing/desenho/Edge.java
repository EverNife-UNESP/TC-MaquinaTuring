/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.finalcraft.unesp.tc.maquinaturing.desenho;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Aresta;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.MainController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.transitions.SideOnlyEditTransitionController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.transitions.TrasactionEditorSelectorController;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Edge {

    private Color color = Color.valueOf("82A0A1"); //Cor da aresta
    private Vertex source; //primeiro vetice da aresta
    private Vertex target; //segundo vertice da aresta
    private Line line;
    private Group arrow;
    private Line line1;
    private Line line2;
    private Line line3;
    private Line line4;
    private boolean bidiretec = false;
    private Text floatingText;

    private boolean selfArrow = false;

    //==================================================================================================================
    // Finite Automation Needs
    //==================================================================================================================

    public Aresta arestaOne;
    public Aresta arestaTwo;

    public boolean isBidiretec() {
        return bidiretec;
    }

    public void setBidiretec(boolean bidiretec) {
        this.bidiretec = bidiretec;
    }


    public void calculateAndSetTextOne(){
        StringBuilder stringBuilder = new StringBuilder("\n");

        if (bidiretec){
            stringBuilder.append("q" + arestaOne.getSourceId() + " --> q" + arestaOne.getTargetId() + " == ");
            for (PontoDeFita pontoDeFita : arestaOne.getPontosDeFita()){
                stringBuilder.append( pontoDeFita.getFirst() + "|" + pontoDeFita.getSecond() + "|" + pontoDeFita.getOrientation() + "  ");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);

            stringBuilder.append("\n");
            stringBuilder.append("q" + arestaTwo.getSourceId() + " --> q" + arestaTwo.getTargetId() + " == ");
            for (PontoDeFita pontoDeFita : arestaTwo.getPontosDeFita()){
                stringBuilder.append( pontoDeFita.getFirst() + "|" + pontoDeFita.getSecond() + "|" + pontoDeFita.getOrientation() + "  ");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
        }else {
            if (selfArrow){
                stringBuilder.append("\n\n");
            }
            for (PontoDeFita pontoDeFita : arestaOne.getPontosDeFita()){
                stringBuilder.append( pontoDeFita.getFirst() + "|" + pontoDeFita.getSecond() + "|" + pontoDeFita.getOrientation() + "\n");
            }
        }
        floatingText.setText(stringBuilder.toString());
    }

    public Aresta getOrCreateAresta(Vertex source) {
        if (arestaOne.getSourceId() == source.getID()){
            return arestaOne;
        }
        return arestaTwo;
    }

    public Group getArrow() {
        return arrow;
    }

    public boolean deleteIfInvalid(){
        if (!arestaOne.isValid()){
            delete();
            //Se a segunda parte for válida, entao, deve ser criado uma nova EDGE com o sentido contrário
            if (arestaTwo.isValid()){
                Edge edge = GraphController.getGraph().getOrCreateEdge(this.target,this.source);
                this.arestaTwo.getPontosDeFita().forEach(character -> edge.arestaOne.addPontoDeFita(character));
            }
            GraphController.getGraph().reloadPage();
            return true;
        }
        if ( isBidiretec() && !arestaTwo.isValid()){
            delete();
            Edge edge = GraphController.getGraph().getOrCreateEdge(this.source,this.target);
            this.arestaOne.getPontosDeFita().forEach(character -> edge.arestaOne.addPontoDeFita(character));
            GraphController.getGraph().reloadPage();
            return true;
        }
        return false;
    }

    public void delete(){
        GraphController.getGraph().edges.remove(this);
        GraphController.getGraph().reloadPage();
    }
    // ==================================================================================================================


    // ================================================================================================================= //



    public Edge(Vertex source, Vertex target) {
        this.source = source;
        this.target = target;
        this.arestaOne = new Aresta(source.getID(),target.getID());
        this.arestaTwo = new Aresta(target.getID(),source.getID());

        if (source == target){
            selfArrow = true;
        }
    }

    // para direcionado
    private float r;
    private float cos;
    private float sen;
    public Line getConnect(){
        return this.line;
    }


    public Group connect(Circle c1, Circle c2) {
        arrow = new Group();
        line = new Line();
        //Color.rgb(200, 253, 255, 1);

        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());

        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());

        line.setStroke(color);

        line.toBack();

        arrow.getChildren().add(line);

        TextFlow textFlow = new TextFlow();
        textFlow.setLayoutX((line.startXProperty().doubleValue() + line.endXProperty().doubleValue())/ 2);
        textFlow.setLayoutY((line.startYProperty().doubleValue() + line.endYProperty().doubleValue())/ 2);
        //
        floatingText = new Text();
        calculateAndSetTextOne();
        //
        floatingText.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        textFlow.getChildren().addAll(floatingText);
        arrow.getChildren().add(textFlow);

        this.clear();

        line.endXProperty().addListener((observable, oldvalue, newvalue)->{
            textFlow.setLayoutX((line.startXProperty().get() + newvalue.doubleValue())/2);

        });

        line.endYProperty().addListener((observable, oldvalue, newvalue)->{
            textFlow.setLayoutY((line.startYProperty().get() + newvalue.doubleValue())/2);

        });

        line.startXProperty().addListener((observable, oldvalue, newvalue)->{
            textFlow.setLayoutX((newvalue.doubleValue() + line.endXProperty().get())/2);
        });

        line.startYProperty().addListener((observable, oldvalue, newvalue)->{
            textFlow.setLayoutY((newvalue.doubleValue() + line.endYProperty().get())/2);
        });

        return arrow;
    }

    public void clear() {
        line.setOpacity(1.0f);
        line.setStrokeWidth(9.0f); //def 3

        arrow.getChildren().remove(line1);
        arrow.getChildren().remove(line2);
        arrow.getChildren().remove(line3);
        arrow.getChildren().remove(line4);

        line1 = new Line();
        line2 = new Line();
        line3 = new Line();
        line4 = new Line();

        arrow.getChildren().add(line1);
        arrow.getChildren().add(line2);
        arrow.getChildren().add(line3);
        arrow.getChildren().add(line4);

        arrow.getChildren().forEach(node -> node.setOnMousePressed((event) -> {
            switch (MainController.getPressedRadioButton()) {
                case EDITAR_TRANSICOES: {
                    if (event.getButton().equals(MouseButton.SECONDARY)) {
                        if (this.isBidiretec()) {
                            TrasactionEditorSelectorController.show(this);
                        } else {
                            SideOnlyEditTransitionController.show(this);
                        }
                    }
                }
            }
        }));

        line.endXProperty().addListener((observable, oldvalue, newvalue) -> {
            updateArrow();
        });

        line.endYProperty().addListener((observable, oldvalue, newvalue) -> {
            updateArrow();
        });

        line.startXProperty().addListener((observable, oldvalue, newvalue) -> {
            updateArrow();
        });

        line.startYProperty().addListener((observable, oldvalue, newvalue) -> {
            updateArrow();

        });
        updateArrow();
    }
    protected void updateArrow(){
        int size = 6;
        int deslocamento = 35;

        if (true) {
            double SourceX = line.startXProperty().get();
            double TargetX = line.endXProperty().get();
            double SourceY = line.startYProperty().get();
            double TargetY = line.endYProperty().get();

            this.r = (float) Math.sqrt(Math.pow(SourceX - TargetX, 2) + Math.pow(SourceY - TargetY, 2));
            this.cos = (float) ((TargetX - SourceX) / r);
            this.sen = (float) ((TargetY - SourceY) / r);

            int xAB = size + deslocamento;
            int yA = size;
            int yB = -size;


            if (selfArrow) {
                line1.setEndY(TargetY + Math.round( xAB * -sen + yA * -cos ) + 40);
                line1.setEndX(TargetX + Math.round( xAB * -cos - yA * -sen ) + 10);
                line1.setStrokeWidth(8.0f); //def 4
                line1.setStartY(TargetY + Math.round( deslocamento * -sen) + 30);
                line1.setStartX(TargetX + Math.round( deslocamento * -cos) - 10);

                line2.setEndY(TargetY + Math.round( xAB * -sen + yA * -cos ) + 40);
                line2.setEndX(TargetX + Math.round( xAB * -cos - yA * -sen ) - 10);
                line2.setStrokeWidth(8.0f); //def 4
                line2.setStartY(TargetY + Math.round( deslocamento * -sen) + 30);
                line2.setStartX(TargetX + Math.round( deslocamento * -cos) + 10);
            } else {
                line1.setEndY(TargetY + Math.round(xAB * -sen + yA * -cos));
                line1.setEndX(TargetX + Math.round(xAB * -cos - yA * -sen));
                line1.setStrokeWidth(8.0f); //def 4
                line1.setStartX(TargetX + Math.round(deslocamento * -cos));
                line1.setStartY(TargetY + Math.round(deslocamento * -sen));


                line2.setEndY(TargetY + Math.round(xAB * -sen + yB * -cos));
                line2.setEndX(TargetX + Math.round(xAB * -cos - yB * -sen));
                line2.setStrokeWidth(8.0f); //def 4
                line2.setStartX(TargetX + Math.round(deslocamento * -cos));
                line2.setStartY(TargetY + Math.round(deslocamento * -sen));
            }
        }

        if (bidiretec){
            double SourceX = line.endXProperty().get();
            double TargetX = line.startXProperty().get();
            double SourceY = line.endYProperty().get();
            double TargetY = line.startYProperty().get();

            this.r = (float) Math.sqrt(Math.pow(SourceX - TargetX, 2) + Math.pow(SourceY - TargetY, 2));
            this.cos = (float) ((TargetX - SourceX) / r);
            this.sen = (float) ((TargetY - SourceY) / r);

            int xAB = size + deslocamento;
            int yA = size;
            int yB = -size;

            line3.setEndY(TargetY + Math.round( xAB * -sen + yA * -cos ));
            line3.setEndX(TargetX + Math.round( xAB * -cos - yA * -sen ));
            line3.setStrokeWidth(8.0f); //def 4
            line3.setStartX(TargetX + Math.round( deslocamento * -cos));
            line3.setStartY(TargetY + Math.round( deslocamento * -sen));


            line4.setEndY(TargetY +Math.round( xAB * -sen + yB * -cos ));
            line4.setEndX(TargetX + Math.round( xAB * -cos - yB * -sen ));
            line4.setStrokeWidth(8.0f); //def 4
            line4.setStartX(TargetX + Math.round( deslocamento * -cos));
            line4.setStartY(TargetY + Math.round( deslocamento * -sen));

        }



    }

    public Vertex getSource()
    {
        return this.source;
    }

    public Vertex getTarget(){
        return this.target;
    }
}
