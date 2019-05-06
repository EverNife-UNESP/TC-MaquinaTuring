package br.com.finalcraft.unesp.tc.maquinaturing.application.xml.encapsulation;

import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Aresta;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Edge;

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
                list.add("\t\t\t<read>" + pontoDeFita.getFirst()  + "</read>;");
                list.add("\t\t\t<write>" + pontoDeFita.getSecond()  + "</write>;");
                list.add("\t\t\t<move>" + pontoDeFita.getOrientation()  + "</move>;");
                list.add("\t\t</transition>;");
            }
            aresta = edge.arestaTwo;
        }

        return list;
    }
}
