package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.modules;

import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.Validator;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.MainController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.StackChangeListener;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.TesterController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.tc.myown.Sleeper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DirectTestController implements StackChangeListener {

    private static DirectTestController instance;
    private static Stage dialog;

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

        Scene newSceneWindow1 = new Scene(MyFXMLs.finiteautomation_tester_direct);
        dialog.setScene(newSceneWindow1);
    }

    public static void show(){
        instance.textField2.setText("");
        instance.textField3.setText("");
        instance.textField4.setText("");
        instance.textField5.setText("");
        dialog.show();
    }

    @FXML
    private TextField textField1;

    @FXML
    private Label directResult;

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

        String[] expressions = new String[MainController.currentStackSize];

        switch (expressions.length){
            case 5:
                expressions[4] = textField5.getText();
            case 4:
                expressions[3] = textField4.getText();
            case 3:
                expressions[2] = textField3.getText();
            case 2:
                expressions[1] = textField2.getText();
            case 1:
                expressions[0] = textField1.getText();
        }

        if (Validator.validadeStringMark2(expressions)){
            directResult.setText("✔✔✔✔ Termo Aceito ✔✔✔✔");
        }else {
            directResult.setText("✘✘✘✘ Termo Recusado ✘✘✘✘");
        }

        new Sleeper(){
            @Override
            public void andDo() {
                directResult.setText("....");
            }
        }.runAfter(3000L);
    }


    //---------

    @Override
    public void onStackSizeChange(int newStackSize) {
        if (newStackSize >= 2) textField2.setDisable(false); else textField2.setDisable(true);
        if (newStackSize >= 3) textField3.setDisable(false); else textField3.setDisable(true);
        if (newStackSize >= 4) textField4.setDisable(false); else textField4.setDisable(true);
        if (newStackSize == 5) textField5.setDisable(false); else textField5.setDisable(true);
    }
}
