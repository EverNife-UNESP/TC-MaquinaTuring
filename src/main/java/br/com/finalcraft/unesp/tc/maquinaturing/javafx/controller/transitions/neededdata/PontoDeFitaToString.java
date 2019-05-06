package br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.transitions.neededdata;

import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class PontoDeFitaToString {

    String value;

    public static ObservableList<PontoDeFitaToString> observableList(List<PontoDeFita> characterList){
        ObservableList<PontoDeFitaToString> observableList = FXCollections.observableArrayList();
        for (PontoDeFita character : characterList){
            observableList.add(new PontoDeFitaToString(character));
        }
        return observableList;
    }

    public PontoDeFitaToString(PontoDeFita pontoDeFita) {
        this.value = pontoDeFita.getFirst() + "|" + pontoDeFita.getSecond() + "|" + pontoDeFita.getOrientation();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
