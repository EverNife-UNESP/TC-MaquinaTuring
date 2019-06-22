package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog.HistoryLog;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.Validator;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog.HistoryMove;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;
import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.modules.DirectTestController;
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

    public static int currentStackSize = 1;

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
    private HBox passoApassoResult;

    @FXML
    private Label passoApassoResult2;

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

            passoApassoResult.getChildren().clear();
            passoApassoResult.getChildren().add(new Label( "✔✔✔✔ Termo Aceito ✔✔✔✔" ));
            passoApassoResult2.setText("✔✔✔✔ Termo Aceito ✔✔✔✔");

            new Sleeper(){
                @Override
                public void andDo() {
                    passoApassoResult.getChildren().clear();
                    passoApassoResult.getChildren().add(new Label( "(" + historyLog.getHistoryOfActions() + ")" ));
                    passoApassoResult2.setText( "Aceito em [" + (historyLog.time) + "] passo(s)");
                }
            }.runAfter(700L);
        }else {
            instance.onStepBackButton.setOpacity(0.3);
            instance.onStepBackButton.setDisable(true);
            instance.onStepForwardButton.setOpacity(0.3);
            instance.onStepForwardButton.setDisable(true);

            passoApassoResult.getChildren().clear();
            passoApassoResult.getChildren().add(new Label( "✘✘✘✘ Termo Recusado ✘✘✘✘" ));
            passoApassoResult2.setText("");
            new Sleeper(){
                @Override
                public void andDo() {
                    passoApassoResult.getChildren().clear();
                    passoApassoResult.getChildren().add(new Label( "✘✘✘✘ Termo Recusado ✘✘✘✘" ));
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
        HistoryMove historyMove = historyLog.getHistoryMoveOf(niddle);
        int stageID = historyMove.getVertice().getId();

        GraphController.getGraph().getAllVertex().forEach(anVertex -> {
            if (!anVertex.isFinale()){
                anVertex.changeTempColor(Color.WHITE);
            }else {
                anVertex.repaint();
            }
        });
        try{
            Vertex vertex = GraphController.getGraph().getVertex(stageID);

            String firstPart = historyMove.getCurrentExpression().substring(0,historyMove.pointer);
            String theChar = historyMove.getCurrentExpression().charAt(historyMove.pointer) + "";
            boolean secondPartEnabled = true;
            String secondPart;
            try {
                secondPart = historyMove.getCurrentExpression().substring(historyMove.pointer + 1);
            }catch (Exception errorTaking){
                errorTaking.printStackTrace();
                secondPartEnabled = false;
                secondPart = historyMove.getCurrentExpression().substring(historyMove.pointer);

            }

            instance.passoApassoResult.getChildren().clear();
            instance.passoApassoResult.getChildren().add(new Label( firstPart ));
            Label label = new Label( theChar );
            label.setBackground( new Background( new BackgroundFill( Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY ) ) );
            instance.passoApassoResult.getChildren().add(label);

            if (secondPartEnabled){
                instance.passoApassoResult.getChildren().add(new Label( secondPart ));
            }

            instance.passoApassoResult2.setText("Ultima operação lida: " + historyMove.getPontoDeFita().toString());
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
