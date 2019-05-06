package br.com.finalcraft.unesp.tc.maquinaturing.application.xml.encapsulation;

import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;

import java.util.ArrayList;
import java.util.List;

public class VertexToXML {

    public static List<String> generateStringLines(Vertex vertex){
        List<String> list = new ArrayList<>();

        list.add("\t\t<state id=\"" + vertex.getID() + "\" name=\"" + vertex.getCustomName() + "\">;");
        list.add("\t\t\t<x>" + vertex.getCircle().getCenterX() + "</x>;");
        list.add("\t\t\t<y>" + vertex.getCircle().getCenterY() + "</y>;");
        if (vertex.isInitial()) list.add("\t\t\t<initial/>;");
        if (vertex.isFinale()) list.add("\t\t\t<final/>;");
        list.add("\t\t</state>;");

        return list;
    }


}
