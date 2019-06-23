package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog.HistoryLog;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.Validator;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog.HistoryMove;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;
import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.modules.DirectTestController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.modules.MultipleTestController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.modules.StepedTestController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.tc.myown.Sleeper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TesterController {

    private static TesterController instance;
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

        Scene newSceneWindow1 = new Scene(MyFXMLs.finiteautomation_tester);
        dialog.setScene(newSceneWindow1);
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing, repainting all things");

                GraphController.getGraph().reloadPage();
            }
        });

        DirectTestController.setUp();
        StepedTestController.setUp();
        MultipleTestController.setUp();
    }

    public static void show(){
        Validator.loadGraph();
        dialog.show();
    }

    @FXML
    void onDireto(ActionEvent event) {
        DirectTestController.show();
    }

    @FXML
    void onMultiplo(ActionEvent event) {
        MultipleTestController.show();
    }

    @FXML
    void onPassoAPasso(ActionEvent event) {
        StepedTestController.show();
    }

}
