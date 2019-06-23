package br.com.finalcraft.unesp.tc.maquinaturing.application.xml.encapsulation;

import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Aresta;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Edge;
import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.MainController;

import java.util.ArrayList;
import java.util.List;

public class EdgeToXML {

    public static List<String> generateStringLines(Edge edge){
        List<String> list = new ArrayList<>();

        Aresta aresta = edge.arestaOne;

        int checks = 0;
        while (aresta != null && checks != 2){
            checks ++;
            for (PontoDeFita pontoDeFita : aresta.getPontosDeFita()){
                list.add("\t\t<transition>;");
                list.add("\t\t\t<from>" + aresta.getSourceId() + "</from>;");
                list.add("\t\t\t<to>" + aresta.getTargetId() + "</to>;");

                for (int i = 1; i<= MainController.currentStackSize; i++){
                    if (pontoDeFita.getRead() == PontoDeFita.EMPTY_CHAR){
                        list.add("\t\t\t<read tape=\"" + i + "\"/>;");
                    }else {
                        list.add("\t\t\t<read tape=\"" + i + "\">" + pontoDeFita.getRead()  + "</read>;");
                    }
                    if (pontoDeFita.getWrite() == PontoDeFita.EMPTY_CHAR){
                        list.add("\t\t\t<write tape=\"" + i + "\"/>;");
                    }else {
                        list.add("\t\t\t<write tape=\"" + i + "\">" + pontoDeFita.getWrite()  + "</write>;");
                    }
                    list.add("\t\t\t<move tape=\"" + i + "\">" + pontoDeFita.getOrientation()  + "</move>;");
                }
                list.add("\t\t</transition>;");
            }
            aresta = edge.arestaTwo;
        }

        return list;
    }
}
