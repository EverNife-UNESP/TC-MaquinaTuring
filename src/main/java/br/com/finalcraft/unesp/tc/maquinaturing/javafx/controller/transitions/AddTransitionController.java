package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.transitions;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Aresta;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Edge;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;
import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.view.MyFXMLs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddTransitionController {


    private static AddTransitionController instance;
    private static Stage dialog;

    private static Edge edge;
    private static Vertex startVertex;
    private static Vertex endVertex;


    @FXML
    void initialize() {
        instance = this;


    }

    public static void setUp(){
        dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        // Defines a modal window that blocks events from being
        // delivered to any other application window.
        dialog.initOwner(JavaFXMain.thePrimaryStage);

        Scene newSceneWindow1 = new Scene(MyFXMLs.add_transition);
        dialog.setScene(newSceneWindow1);
        dialog.setResizable(false);

    }

    public static void show(Vertex start, Vertex end){
        startVertex = start;
        endVertex = end;

        instance.stateIdentifier.setText("q" + startVertex.getID() + " --> " + "q" + endVertex.getID());
        instance.transaction_read.setText("");


        dialog.show();
    }

    @FXML
    private TextField transaction_read;

    @FXML
    private TextField transaction_write;

    @FXML
    private TextField transaction_move;

    @FXML
    private Label stateIdentifier;

    @FXML
    private CheckBox initial;

    @FXML
    private CheckBox finale;

    @FXML
    void onSaveChanges(ActionEvent event) {
        Edge edge = GraphController.getGraph().getOrCreateEdge(startVertex, endVertex);
        Aresta aresta = edge.getOrCreateAresta(startVertex);

        String readValue    = transaction_read.getText();
        String writeValue   = transaction_write.getText();
        String moveValue    = transaction_move.getText();

        if (writeValue.length() > 1 || moveValue.length() > 1 ){
            return;
        }

        char theWriteChar = writeValue.length() == 1 ? writeValue.charAt(0) : '▢';
        char theMoveChar = writeValue.length() == 1 ? moveValue.charAt(0) : '▢';

        char[] allChars = readValue.toCharArray();

        for (char aChar : allChars){
            aresta.addPontoDeFita(new PontoDeFita(aChar,theWriteChar,theMoveChar));
        }
        edge.calculateAndSetTextOne();
        dialog.close();
    }

    @FXML
    void onCancel(ActionEvent event) {
        dialog.close();
    }

    @FXML
    void stateName(ActionEvent event) {

    }


}
