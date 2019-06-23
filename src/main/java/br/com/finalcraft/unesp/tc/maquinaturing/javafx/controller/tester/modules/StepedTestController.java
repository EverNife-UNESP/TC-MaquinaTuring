package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.modules;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.Validator;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog.HistoryLog;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog.HistoryMove;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.MainController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.StackChangeListener;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.tc.myown.Sleeper;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StepedTestController implements StackChangeListener {

    private static StepedTestController instance;
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

        Scene newSceneWindow1 = new Scene(MyFXMLs.finiteautomation_tester_steped);
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
    private TextField textField2;

    @FXML
    private TextField textField3;

    @FXML
    private TextField textField4;

    @FXML
    private TextField textField5;

    @FXML
    private Button onStepBackButton;

    @FXML
    private Button onStepForwardButton;

    @FXML
    private Label passoApassoResult2;

    @FXML
    private HBox passoAPassoTape1;

    @FXML
    private HBox passoAPassoTape2;

    @FXML
    private HBox passoAPassoTape3;

    @FXML
    private HBox passoAPassoTape4;

    @FXML
    private HBox passoAPassoTape5;

    private static HistoryLog[] historyLogs;
    @FXML
    void onPassoAPasso() {

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

        historyLogs = Validator.validadeStringWithLogMark2(expressions);

        if (historyLogs != null && historyLogs[0].match){
            niddle = 0;
            realMovesAmout = historyLogs[0].time;
            onMoveNiddle();

            passoAPassoTape1.getChildren().clear();
            passoAPassoTape1.getChildren().add(new Label( "✔✔✔✔ Termo Aceito ✔✔✔✔" ));
            passoApassoResult2.setText("✔✔✔✔ Termo Aceito ✔✔✔✔");

            new Sleeper(){
                @Override
                public void andDo() {
                    passoAPassoTape1.getChildren().clear();
                    passoAPassoTape1.getChildren().add(new Label( "(" + historyLogs[0].getHistoryOfActions() + ")" ));
                    passoApassoResult2.setText( "Aceito em [" + (historyLogs[0].time) + "] passo(s)");
                }
            }.runAfter(700L);
        }else {
            instance.onStepBackButton.setOpacity(0.3);
            instance.onStepBackButton.setDisable(true);
            instance.onStepForwardButton.setOpacity(0.3);
            instance.onStepForwardButton.setDisable(true);

            passoAPassoTape1.getChildren().clear();
            passoAPassoTape1.getChildren().add(new Label( "✘✘✘✘ Termo Recusado ✘✘✘✘" ));
            passoApassoResult2.setText("");
            new Sleeper(){
                @Override
                public void andDo() {
                    passoAPassoTape1.getChildren().clear();
                    passoAPassoTape1.getChildren().add(new Label( "✘✘✘✘ Termo Recusado ✘✘✘✘" ));
                }
            }.runAfter(3000L);

        }

    }

    private HBox getHBOXTape(int stackNumber){
        switch (stackNumber){
            case 0:
                return passoAPassoTape1;
            case 1:
                return passoAPassoTape2;
            case 2:
                return passoAPassoTape3;
            case 3:
                return passoAPassoTape4;
            case 4:
                return passoAPassoTape5;
        }
        return null;
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

        //int stageID = Integer.parseInt(historyLogs.expression.replaceAll("\u03B5","").charAt(niddle)+"");
        HistoryMove mainHistoryMove = historyLogs[0].getHistoryMoveOf(niddle);
        int stageID = mainHistoryMove.getVertice().getId();

        GraphController.getGraph().getAllVertex().forEach(anVertex -> {
            if (!anVertex.isFinale()){
                anVertex.changeTempColor(Color.WHITE);
            }else {
                anVertex.repaint();
            }
        });

        for (int i = 0; i < historyLogs.length; i++) {
            try{
                HistoryMove historyMove = historyLogs[i].getHistoryMoveOf(niddle);

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

                HBox hBoxTape = instance.getHBOXTape(i);

                hBoxTape.getChildren().clear();
                hBoxTape.getChildren().add(new Label( firstPart ));
                Label label = new Label( theChar );
                label.setBackground( new Background( new BackgroundFill( Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY ) ) );
                hBoxTape.getChildren().add(label);

                if (secondPartEnabled){
                    hBoxTape.getChildren().add(new Label( secondPart ));
                }

                if (i == 0){
                    Vertex vertex = GraphController.getGraph().getVertex(stageID);
                    instance.passoApassoResult2.setText("Ultima operação lida: " + historyMove.getPontoDeFita().toString());
                    vertex.changeTempColor(Color.LIGHTGREEN);
                }
            }catch (Exception ignored){}
        }
    }

    @FXML
    void onStepBack() {
        niddle--;
        onMoveNiddle();
    }

    @FXML
    void onStepForward() {
        niddle++;
        onMoveNiddle();
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
