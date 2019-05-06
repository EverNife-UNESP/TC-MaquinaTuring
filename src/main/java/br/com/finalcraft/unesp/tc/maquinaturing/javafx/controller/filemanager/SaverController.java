package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.filemanager;

import br.com.finalcraft.unesp.tc.maquinaturing.application.xml.XMLFileManipulator;
import br.com.finalcraft.unesp.tc.maquinaturing.JavaFXMain;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.tc.myown.Sleeper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SaverController {


    private static SaverController instance;
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

        Scene newSceneWindow1 = new Scene(MyFXMLs.save_to_file);
        dialog.setScene(newSceneWindow1);
    }

    public static void show(){
        dialog.show();
    }

    @FXML
    private TextField fileName;

    @FXML
    private Label resultLabel;

    @FXML
    void onSaveChanges(ActionEvent event) {
        if (!fileName.getText().isEmpty()){
            if (XMLFileManipulator.saveOnFile(fileName.getText())){
                resultLabel.setText("Arquivo salvo com sucesso!");
                new Sleeper(){
                    @Override
                    public void andDo() {
                        resultLabel.setText("...");
                        dialog.close();
                    }
                }.runAfter(0750L);
            }else {
                resultLabel.setText("Erro ao salvar arquivo T.T");
                new Sleeper(){
                    @Override
                    public void andDo() {
                        resultLabel.setText("....");
                    }
                }.runAfter(3000L);
            }
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        dialog.close();
    }
}
