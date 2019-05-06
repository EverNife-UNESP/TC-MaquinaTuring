package br.com.finalcraft.unesp.tc.maquinaturing.javafx.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class MyFXMLs {

    public static Parent main_screen;
    public static Parent state_editor;

    public static Parent select_edit_trasition;
    public static Parent add_transition;
    public static Parent edit_transition_sideonly;

    public static Parent finiteautomation_tester;

    public static Parent save_to_file;
    public static Parent load_from_file;

    public static void loadUpFiles() throws IOException {
        main_screen = FXMLLoader.load(MyFXMLs.class.getResource("/assets/main_screen.fxml"));
        state_editor = FXMLLoader.load(MyFXMLs.class.getResource("/assets/state_editor.fxml"));
        select_edit_trasition = FXMLLoader.load(MyFXMLs.class.getResource("/assets/select_edit_trasition.fxml"));
        add_transition = FXMLLoader.load(MyFXMLs.class.getResource("/assets/add_transition.fxml"));
        edit_transition_sideonly = FXMLLoader.load(MyFXMLs.class.getResource("/assets/sideonly_edit_transition.fxml"));
        finiteautomation_tester = FXMLLoader.load(MyFXMLs.class.getResource("/assets/finiteautomation_tester.fxml"));
        save_to_file = FXMLLoader.load(MyFXMLs.class.getResource("/assets/save_to_file.fxml"));
        load_from_file = FXMLLoader.load(MyFXMLs.class.getResource("/assets/load_from_file.fxml"));
    }
}
