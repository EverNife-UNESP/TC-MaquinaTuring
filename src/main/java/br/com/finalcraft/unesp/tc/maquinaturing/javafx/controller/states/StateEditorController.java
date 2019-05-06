package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.states;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
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

public class StateEditorController {


    private static StateEditorController instance;
    private static Stage dialog;
    private static Vertex theVertex;


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

        Scene newSceneWindow1 = new Scene(MyFXMLs.state_editor);
        dialog.setScene(newSceneWindow1);
    }

    public static void show(Vertex vertex){
        theVertex = vertex;
        instance.stateIdentifier.setText("Identificador do Estado: q" + theVertex.getID());
        instance.state_name.setText(theVertex.getCustomName());
        instance.initial.setSelected(theVertex.isInitial());
        instance.finale.setSelected(theVertex.isFinale());

        if (!theVertex.isInitial() && GraphController.getGraph().hasAnyInitial()){
            instance.initial.setOpacity(0.3);
            instance.initial.setDisable(true);
        }else {
            instance.initial.setOpacity(1);
            instance.initial.setDisable(false);
        }
        dialog.show();
    }

    @FXML
    private TextField state_name;

    @FXML
    private Label stateIdentifier;

    @FXML
    private CheckBox initial;

    @FXML
    private CheckBox finale;

    @FXML
    void onSaveChanges(ActionEvent event) {
        theVertex.setCustomName(state_name.getText());
        theVertex.setInitial(initial.isSelected());
        theVertex.setFinale(finale.isSelected());
        dialog.close();
    }

    @FXML
    void onStateDelete(ActionEvent event) {
        GraphController.getGraph().removeVertex(theVertex.getID());
        dialog.close();
    }

    @FXML
    void stateName(ActionEvent event) {

    }


}
