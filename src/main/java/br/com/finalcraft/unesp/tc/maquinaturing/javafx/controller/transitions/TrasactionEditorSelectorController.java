package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.transitions;

import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Edge;
import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.view.MyFXMLs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TrasactionEditorSelectorController {


    private static TrasactionEditorSelectorController instance;
    private static Stage dialog;
    private static Edge theEdge;


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

        Scene newSceneWindow1 = new Scene(MyFXMLs.select_edit_trasition);
        dialog.setScene(newSceneWindow1);
        dialog.setResizable(false);
    }

    public static void show(Edge edge){
        theEdge = edge;
        instance.identifierOne.setText("Estado q" + edge.arestaOne.getSourceId() + " para q" + edge.arestaOne.getTargetId());
        instance.identifierTwo.setText("Estado q" + edge.arestaTwo.getSourceId() + " para q" + edge.arestaTwo.getTargetId());
        instance.contentOne.setText(edge.arestaOne.getGramarsString());
        instance.contentTwo.setText(edge.arestaTwo.getGramarsString());

        dialog.show();
    }
    @FXML
    private Label identifierOne;

    @FXML
    private Label identifierTwo;

    @FXML
    private Label contentOne;

    @FXML
    private Label contentTwo;

    @FXML
    void onEditOne(ActionEvent event) {
        dialog.close();
        SideOnlyEditTransitionController.show(theEdge,1);
    }

    @FXML
    void onEditTwo(ActionEvent event) {
        dialog.close();
        SideOnlyEditTransitionController.show(theEdge,2);
    }

    @FXML
    void onCancel(ActionEvent event) {
        dialog.close();
    }



}
