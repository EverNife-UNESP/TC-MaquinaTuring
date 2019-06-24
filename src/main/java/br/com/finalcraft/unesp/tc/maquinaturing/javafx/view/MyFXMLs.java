package br.com.finalcraft.unesp.tc.maquinaturing.javafx.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class MyFXMLs {

    public static Parent main_screen;
    public static Parent state_editor;
    public static Parent change_tapes;

    public static Parent select_edit_trasition;
    public static Parent add_transition;
    public static Parent edit_transition_sideonly;

    public static Parent finiteautomation_tester;
    public static Parent finiteautomation_tester_direct;
    public static Parent finiteautomation_tester_steped;
    public static Parent finiteautomation_tester_multiple;

    public static Parent save_to_file;
    public static Parent load_from_file;

    public static void loadUpFiles() throws IOException {
        main_screen = FXMLLoader.load(MyFXMLs.class.getResource("/assets/main_screen.fxml"));
        state_editor = FXMLLoader.load(MyFXMLs.class.getResource("/assets/editors/state_editor.fxml"));
        change_tapes = FXMLLoader.load(MyFXMLs.class.getResource("/assets/tapes/change_tapes.fxml"));
        select_edit_trasition = FXMLLoader.load(MyFXMLs.class.getResource("/assets/editors/select_edit_trasition.fxml"));
        add_transition = FXMLLoader.load(MyFXMLs.class.getResource("/assets/editors/add_transition.fxml"));
        edit_transition_sideonly = FXMLLoader.load(MyFXMLs.class.getResource("/assets/editors/sideonly_edit_transition.fxml"));
        finiteautomation_tester = FXMLLoader.load(MyFXMLs.class.getResource("/assets/testers/finiteautomation_tester.fxml"));
        finiteautomation_tester_direct = FXMLLoader.load(MyFXMLs.class.getResource("/assets/testers/finiteautomation_tester_direct.fxml"));
        finiteautomation_tester_steped = FXMLLoader.load(MyFXMLs.class.getResource("/assets/testers/finiteautomation_tester_steped.fxml"));
        finiteautomation_tester_multiple = FXMLLoader.load(MyFXMLs.class.getResource("/assets/testers/finiteautomation_tester_multiple.fxml"));
        save_to_file = FXMLLoader.load(MyFXMLs.class.getResource("/assets/file/save_to_file.fxml"));
        load_from_file = FXMLLoader.load(MyFXMLs.class.getResource("/assets/file/load_from_file.fxml"));
    }
}
