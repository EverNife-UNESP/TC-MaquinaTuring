package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.modules;

import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.Validator;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.MainController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.StackChangeListener;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.tc.myown.Sleeper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class MultipleTestController implements StackChangeListener {

    private static MultipleTestController instance;
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

        Scene newSceneWindow1 = new Scene(MyFXMLs.finiteautomation_tester_multiple);
        dialog.setScene(newSceneWindow1);
    }

    public static void show(){
        dialog.show();
    }

    @FXML
    private TextArea multiploTextArea;

    @FXML
    private TextArea multiploResult;

    @FXML
    void onMultiplo() {
        String[] todasAsExpressoes = multiploTextArea.getText().split("\n");

        for (int i = 0; i < todasAsExpressoes.length; i++){

            String[] expressions = new String[MainController.currentStackSize];

            String[] existingExpressions = todasAsExpressoes[i].split(Pattern.quote("|"));

            for (int j = 0; j < expressions.length; j++){
                if (j < existingExpressions.length){
                    expressions[j] = existingExpressions[j];
                }else {
                    expressions[j] = "";
                }
            }

            if (Validator.validadeStringMark2(expressions)){
                todasAsExpressoes[i] = " ✔ Termo  Aceito  ✔ [ " +todasAsExpressoes[i] + " ]";
            }else {
                todasAsExpressoes[i] = " ✘ Termo Recusado ✘ [ " +todasAsExpressoes[i] + " ]";
            }
        }

        multiploResult.setText(String.join("\n",todasAsExpressoes));
    }

    //---------

    @Override
    public void onStackSizeChange(int newStackSize) {
    }
}
