package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tapes;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Aresta;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Edge;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;
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

public class ChangeTapesController {

    private static ChangeTapesController instance;
    private static Stage dialog;

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

        Scene newSceneWindow1 = new Scene(MyFXMLs.change_tapes);
        dialog.setScene(newSceneWindow1);
        dialog.setResizable(false);
    }

    public static void show(){
        dialog.show();
    }

    @FXML
    private Label valueLabel;

    @FXML
    void onCancel() {
        dialog.close();
    }

    @FXML
    void onSetFita1() {
        MainController.changeStackSizeTo(1);
        dialog.close();
    }

    @FXML
    void onSetFita2() {
        MainController.changeStackSizeTo(2);
        dialog.close();
    }

    @FXML
    void onSetFita3() {
        MainController.changeStackSizeTo(3);
        dialog.close();
    }

    @FXML
    void onSetFita4() {
        MainController.changeStackSizeTo(4);
        dialog.close();
    }

    @FXML
    void onSetFita5() {
        MainController.changeStackSizeTo(5);
        dialog.close();
    }
}
