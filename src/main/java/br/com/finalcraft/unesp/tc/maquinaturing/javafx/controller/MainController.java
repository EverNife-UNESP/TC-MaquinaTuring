package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller;

import br.com.finalcraft.unesp.tc.maquinaturing.GraphController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.filemanager.LoaderController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.filemanager.SaverController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tapes.ChangeTapesController;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.tester.TesterController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    public static int currentStackSize = 1;

    private static List<StackChangeListener> stackChangeListeners = new ArrayList<>();
    public static void addStackChangeListener(StackChangeListener listener){
        stackChangeListeners.add(listener);
    }

    public static void changeStackSizeTo(int newSize){
        System.out.println("Change tapes size to " + newSize);
        GraphController.clear();
        currentStackSize = newSize;
        stackChangeListeners.forEach(listener -> listener.onStackSizeChange(newSize));
    }

    public enum PressedButton{
        EDITAR_ESTADOS,
        EDITAR_TRANSICOES;
    }

    private static MainController instance;

    public static PressedButton getPressedRadioButton(){
        if (instance.mainmenu.getSelectedToggle().equals(instance.editStates)){
            return PressedButton.EDITAR_ESTADOS;
        }else {
            return PressedButton.EDITAR_TRANSICOES;
        }
    }

    public static boolean doubleClickIsEnabled(){
        return instance.doubleClickCheck.isSelected();
    }



    @FXML
    void initialize() {
        instance = this;
    }

    @FXML
    Button addStateButton;

    @FXML
    ToggleGroup mainmenu;

    @FXML
    RadioButton editStates;

    @FXML
    RadioButton editTransactions;

    @FXML
    CheckBox doubleClickCheck;

    @FXML
    void onAddState(ActionEvent event) {
        if (getPressedRadioButton() == PressedButton.EDITAR_ESTADOS){
            GraphController.getGraph().addVertex();
        }
        //GraphController.defaultValues();
    }

    @FXML
    void openSaver() {
        SaverController.show();
    }

    @FXML
    void openImporter() {
        LoaderController.show();
    }

    @FXML
    void onStateChange() {
        GraphController.getGraph().getAllVertex().forEach(vertex -> vertex.repaint());
        if (getPressedRadioButton() == PressedButton.EDITAR_TRANSICOES){
            addStateButton.setOpacity(0.3);
            addStateButton.setDisable(true);
        }else {
            addStateButton.setOpacity(1);
            addStateButton.setDisable(false);

        }
        // :D
    }

    @FXML
    void onAlterarFitas() {
        ChangeTapesController.show();
    }

    @FXML
    void onTestarAutomato() {
        TesterController.show();
    }

}
