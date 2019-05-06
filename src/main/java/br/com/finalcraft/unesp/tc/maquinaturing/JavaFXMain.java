package br.com.finalcraft.unesp.tc.maquinaturing;

import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.filemanager.LoaderController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.filemanager.SaverController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.states.StateEditorController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.FiniteAutomationTesterController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.transitions.AddTransitionController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.transitions.SideOnlyEditTransitionController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.transitions.TrasactionEditorSelectorController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.view.MyFXMLs;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Random;

public class JavaFXMain extends Application {

    public static Stage thePrimaryStage;
    public static BorderPane rootLayout;

    public static BorderPane getBP(){
        return rootLayout;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        MyFXMLs.loadUpFiles();

        thePrimaryStage = primaryStage;
        thePrimaryStage.setTitle("Teoria da Computação - 2019");

        initRootLayout();

        //Iniciando todos as abas para deixar no grau!
        StateEditorController.setUp();
        TrasactionEditorSelectorController.setUp();
        AddTransitionController.setUp();
        SideOnlyEditTransitionController.setUp();
        FiniteAutomationTesterController.setUp();
        SaverController.setUp();
        LoaderController.setUp();
    }

    public void initRootLayout()  throws Exception{
        //Carrega o root layout do arquivo fxml.
        rootLayout = (BorderPane) MyFXMLs.main_screen;

        Text t = new Text();
        t.setText("Seja bem-vindo(a) ao trabalho de Teoria da Computação.\n                            Adicione um estado para continuar.");
        rootLayout.setCenter(t);

        // Mostra a scene (cena) contendo oroot layout.
        Scene scene = new Scene(rootLayout);
        thePrimaryStage.setScene(scene);

        //Ter certeza de que as janelas serão fechadas corretamente!
        thePrimaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        thePrimaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
