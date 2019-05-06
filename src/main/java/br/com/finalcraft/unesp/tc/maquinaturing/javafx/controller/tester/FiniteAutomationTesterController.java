package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog.HistoryLog;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.Validator;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;
import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.tc.myown.Sleeper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FiniteAutomationTesterController {

    private static FiniteAutomationTesterController instance;
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
    }

    public static void show(){
        Validator.loadGraph();

        instance.onStepBackButton.setOpacity(0.3);
        instance.onStepBackButton.setDisable(true);
        instance.onStepForwardButton.setOpacity(0.3);
        instance.onStepForwardButton.setDisable(true);

        dialog.show();
    }

    @FXML
    private TextField diretoTextField;

    @FXML
    private Label diretoResult;

    @FXML
    private TextArea multiploTextArea;

    @FXML
    private TextArea multiploResult;

    @FXML
    private Button onStepBackButton;

    @FXML
    private Button onStepForwardButton;

    @FXML
    private TextField passoApassoTextField;

    @FXML
    private Label passoApassoResult;

    @FXML
    void onDireto(ActionEvent event) {
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

    @FXML
    void onMultiplo(ActionEvent event) {
        String[] todasAsExpressoes = multiploTextArea.getText().split("\n");

        for (int i = 0; i < todasAsExpressoes.length; i++){

            if (Validator.validadeString(todasAsExpressoes[i])){
                todasAsExpressoes[i] = " ✔ Termo  Aceito  ✔ [ " +todasAsExpressoes[i] + " ]";
            }else {
                todasAsExpressoes[i] = " ✘ Termo Recusado ✘ [ " +todasAsExpressoes[i] + " ]";
            }
        }

        multiploResult.setText(String.join("\n",todasAsExpressoes));


    }


    private static HistoryLog historyLog;
    @FXML
    void onPassoAPasso(ActionEvent event) {
        String termoASerValidado = passoApassoTextField.getText();


        historyLog = Validator.validadeStringWithLog(termoASerValidado);


        if (historyLog != null && historyLog.match){
            niddle = 0;
            realMovesAmout = historyLog.time;
            onMoveNiddle();
            passoApassoResult.setText("✔✔✔✔ Termo Aceito ✔✔✔✔");

            new Sleeper(){
                @Override
                public void andDo() {
                    //passoApassoResult.setText(String.join(" > ",historyLog.expression.replaceAll("\u03B5","").split("")) + "    em [" + (historyLog.time + 1) + "] passo(s)");
                }
            }.runAfter(1000L);
        }else {
            instance.onStepBackButton.setOpacity(0.3);
            instance.onStepBackButton.setDisable(true);
            instance.onStepForwardButton.setOpacity(0.3);
            instance.onStepForwardButton.setDisable(true);

            passoApassoResult.setText("✘✘✘✘ Termo Recusado ✘✘✘✘");
            new Sleeper(){
                @Override
                public void andDo() {
                    passoApassoResult.setText("...");
                }
            }.runAfter(3000L);

        }

    }


    private static int niddle = 0;
    private static int realMovesAmout;

    private static void onMoveNiddle(){
        if (niddle == 0){
            instance.onStepBackButton.setOpacity(0.3);
            instance.onStepBackButton.setDisable(true);
        }else {
            instance.onStepBackButton.setOpacity(1);
            instance.onStepBackButton.setDisable(false);
        }

        if (niddle == realMovesAmout){
            instance.onStepForwardButton.setOpacity(0.3);
            instance.onStepForwardButton.setDisable(true);
        }else{
            instance.onStepForwardButton.setOpacity(1);
            instance.onStepForwardButton.setDisable(false);
        }

        //int stageID = Integer.parseInt(historyLog.expression.replaceAll("\u03B5","").charAt(niddle)+"");
        int stageID = 0;
        GraphController.getGraph().getAllVertex().forEach(anVertex -> {
            if (!anVertex.isFinale()){
                anVertex.changeTempColor(Color.WHITE);
            }else {
                anVertex.repaint();
            }
        });
        try{
            Vertex vertex = GraphController.getGraph().getVertex(stageID);
            instance.passoApassoResult.setText(vertex.getCustomName());
            vertex.changeTempColor(Color.LIGHTGREEN);
        }catch (Exception ignored){}
    }

    @FXML
    void onStepBack(ActionEvent event) {
        niddle--;
        onMoveNiddle();
    }

    @FXML
    void onStepForward(ActionEvent event) {
        niddle++;
        onMoveNiddle();
    }

}
