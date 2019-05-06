package br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog;

import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Vertice;

import java.util.ArrayList;
import java.util.List;

public class HistoryMove {

    PontoDeFita.Orientation orientation;
    Vertice vertice;

    public HistoryMove(PontoDeFita.Orientation orientation, Vertice targetVertice) {
        this.orientation = orientation;
        this.vertice = vertice;
    }

}
