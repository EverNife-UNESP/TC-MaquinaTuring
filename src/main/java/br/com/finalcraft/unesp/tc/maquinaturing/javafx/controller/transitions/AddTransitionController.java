package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.transitions;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Aresta;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Edge;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;
import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.MainController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.StackChangeListener;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.tc.myown.Sleeper;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AddTransitionController implements StackChangeListener {


    private static AddTransitionController instance;
    private static Stage dialog;

    private static Edge edge;
    private static Vertex startVertex;
    private static Vertex endVertex;


    @FXML
    void initialize() {
        instance = this;
        MainController.addStackChangeListener(this);
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

        instance.transaction_read1.setText("");
        instance.transaction_read2.setText("");
        instance.transaction_read3.setText("");
        instance.transaction_read4.setText("");
        instance.transaction_read5.setText("");

        instance.transaction_write1.setText("");
        instance.transaction_write2.setText("");
        instance.transaction_write3.setText("");
        instance.transaction_write4.setText("");
        instance.transaction_write5.setText("");

        instance.transaction_move1.setText("");
        instance.transaction_move2.setText("");
        instance.transaction_move3.setText("");
        instance.transaction_move4.setText("");
        instance.transaction_move5.setText("");

        dialog.show();
    }

    @FXML
    private Label valueLabel;

    @FXML
    private Label stateIdentifier;

    @FXML
    private TextField transaction_read1;

    @FXML
    private TextField transaction_write1;

    @FXML
    private TextField transaction_move1;

    @FXML
    private TextField transaction_read2;

    @FXML
    private TextField transaction_write2;

    @FXML
    private TextField transaction_move2;

    @FXML
    private TextField transaction_read3;

    @FXML
    private TextField transaction_write3;

    @FXML
    private TextField transaction_move3;

    @FXML
    private TextField transaction_read4;

    @FXML
    private TextField transaction_write4;

    @FXML
    private TextField transaction_move4;

    @FXML
    private TextField transaction_read5;

    @FXML
    private TextField transaction_write5;

    @FXML
    private TextField transaction_move5;

    @FXML
    void onCancel() {
        dialog.close();
    }


    private TextField getRead(int stackNumber){
        switch (stackNumber){
            case 0:
                return transaction_read1;
            case 1:
                return transaction_read2;
            case 2:
                return transaction_read3;
            case 3:
                return transaction_read4;
            case 4:
                return transaction_read5;
        }
        return null;
    }

    private TextField getWrite(int stackNumber){
        switch (stackNumber){
            case 0:
                return transaction_write1;
            case 1:
                return transaction_write2;
            case 2:
                return transaction_write3;
            case 3:
                return transaction_write4;
            case 4:
                return transaction_write5;
        }
        return null;
    }

    private TextField getMove(int stackNumber){
        switch (stackNumber){
            case 0:
                return transaction_move1;
            case 1:
                return transaction_move2;
            case 2:
                return transaction_move3;
            case 3:
                return transaction_move4;
            case 4:
                return transaction_move5;
        }
        return null;
    }

    @FXML
    void onSaveChanges() {

        List<PontoDeFita> pontoDeFitaList = new ArrayList<PontoDeFita>();
        boolean shouldBeAdded = false;
        Edge edge = GraphController.getGraph().getOrCreateEdge(startVertex, endVertex);
        Aresta aresta = edge.getOrCreateAresta(startVertex);
        for (int stackIdentifier = 0; stackIdentifier < MainController.currentStackSize; stackIdentifier++){
            shouldBeAdded = false;
            TextField read = getRead(stackIdentifier);
            TextField write = getWrite(stackIdentifier);
            TextField move = getMove(stackIdentifier);

            String readValue    = read.getText();
            String writeValue   = write.getText();
            String moveValue    = move.getText();

            if (readValue.length() > 1){
                valueLabel.setText("O primeiro valor do ponto [" + stackIdentifier + "] precisa ser único!");
                new Sleeper(){
                    @Override
                    public void andDo() {
                        valueLabel.setText("Valor da Transição");
                    }
                }.runAfter(2000);
                return;
            }

            if (writeValue.length() > 1){
                valueLabel.setText("O segundo valor do ponto [" + stackIdentifier + "] precisa ser único!");
                new Sleeper(){
                    @Override
                    public void andDo() {
                        valueLabel.setText("Valor da Transição");
                    }
                }.runAfter(2000);
                return;
            }

            char theReadChar = readValue.length() == 1 ? readValue.charAt(0) : PontoDeFita.EMPTY_CHAR;
            char theWriteChar = writeValue.length() == 1 ? writeValue.charAt(0) : PontoDeFita.EMPTY_CHAR;
            char theMoveChar = moveValue.length() == 1 ? moveValue.charAt(0) : PontoDeFita.EMPTY_CHAR;

            switch (theMoveChar){
                case 'R':
                case 'L':
                case 'S':
                    break;
                default:
                    valueLabel.setText("O terceiro valor do ponto [" + stackIdentifier + "] precisa ser [ L | R | S ]");
                    new Sleeper(){
                        @Override
                        public void andDo() {
                            valueLabel.setText("Valor da Transição");
                        }
                    }.runAfter(2000);
                    return;
            }
            shouldBeAdded = true;
            pontoDeFitaList.add(new PontoDeFita(theReadChar,theWriteChar,theMoveChar,stackIdentifier));

        }

        if (shouldBeAdded){
            for (PontoDeFita pontoDeFita : pontoDeFitaList) {
                aresta.addPontoDeFita(pontoDeFita);
            }
            edge.calculateAndSetTextOne();
        }

        dialog.close();
    }

    @Override
    public void onStackSizeChange(int newStackSize) {
        if (newStackSize >= 2) {
            transaction_read2.setDisable(false);
            transaction_write2.setDisable(false);
            transaction_move2.setDisable(false);
        } else {
            transaction_read2.setDisable(true);
            transaction_write2.setDisable(true);
            transaction_move2.setDisable(true);
        }

        if (newStackSize >= 3) {
            transaction_read3.setDisable(false);
            transaction_write3.setDisable(false);
            transaction_move3.setDisable(false);
        } else {
            transaction_read3.setDisable(true);
            transaction_write3.setDisable(true);
            transaction_move3.setDisable(true);
        }

        if (newStackSize >= 4) {
            transaction_read4.setDisable(false);
            transaction_write4.setDisable(false);
            transaction_move4.setDisable(false);
        } else {
            transaction_read4.setDisable(true);
            transaction_write4.setDisable(true);
            transaction_move4.setDisable(true);
        }

        if (newStackSize >= 5) {
            transaction_read5.setDisable(false);
            transaction_write5.setDisable(false);
            transaction_move5.setDisable(false);
        } else {
            transaction_read5.setDisable(true);
            transaction_write5.setDisable(true);
            transaction_move5.setDisable(true);
        }
    }
}
