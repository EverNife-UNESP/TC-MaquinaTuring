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

public class LoaderController {


    private static LoaderController instance;
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

        Scene newSceneWindow1 = new Scene(MyFXMLs.load_from_file);
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
    void onLoadFile(ActionEvent event) {
        if (!fileName.getText().isEmpty()){
            if (XMLFileManipulator.readFromXML(fileName.getText())){
                resultLabel.setText("Arquivo carregado com sucesso!");
                new Sleeper(){
                    @Override
                    public void andDo() {
                        resultLabel.setText("...");
                        dialog.close();
                    }
                }.runAfter(0050L);
            }else {
                resultLabel.setText("Erro ao carregar arquivo T.T");
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
