package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.modules;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.Validator;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog.HistoryLog;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog.HistoryMove;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.StackDependencieController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.TesterController;
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

public class DirectTestController implements StackDependencieController{

    private static DirectTestController instance;
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
    }

    public static void show(){
        Validator.loadGraph();

        instance.textField2.setText("");
        instance.textField3.setText("");
        instance.textField4.setText("");
        instance.textField5.setText("");

        dialog.show();
    }

    @FXML
    private TextField textField1;

    @FXML
    private Label directResullt;

    @FXML
    private TextField textField2;

    @FXML
    private TextField textField3;

    @FXML
    private TextField textField4;

    @FXML
    private TextField textField5;

    @FXML
    void onDirectTest(ActionEvent event) {



        String termoASerValidado = diretoTextField.getText();

        if (Validator.validadeString(termoASerValidado)){
            diretoResult.setText("✔✔✔✔ Termo Aceito ✔✔✔✔");
        }else {
            diretoResult.setText("✘✘✘✘ Termo Recusado ✘✘✘✘");
        }

        new Sleeper(){
            @Override
            public void andDo() {
                diretoResult.setText("....");
            }
        }.runAfter(3000L);
    }


    //---------

    public void disableUnusedFields(){
        if (TesterController.currentStackSize >1 1) textField1

    }

    @Override
    public void onStackSizeChange(int newStackSize) {
        if (newStackSize >= 2) textField2.setDisable(false); else textField2.setDisable(true);
        if (newStackSize >= 3) textField3.setDisable(false); else textField3.setDisable(true);
        if (newStackSize >= 4) textField4.setDisable(false); else textField4.setDisable(true);
        if (newStackSize == 5) textField5.setDisable(false); else textField5.setDisable(true);
    }
}
