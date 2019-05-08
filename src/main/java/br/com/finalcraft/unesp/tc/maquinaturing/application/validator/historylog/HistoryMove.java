package br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog;

import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Vertice;

public class HistoryMove {

    PontoDeFita pontoDeFita;
    Vertice vertice;
    String currentExpression;

    public HistoryMove(PontoDeFita pontoDeFita, Vertice vertice, String currentExpression) {
        this.pontoDeFita = pontoDeFita;
        this.vertice = vertice;
        this.currentExpression = currentExpression;
    }

    public HistoryMove(Vertice vertice, String currentExpression) {
        this.pontoDeFita = new PontoDeFita(' ', ' ', 'B');
        this.vertice = vertice;
        this.currentExpression = currentExpression;
    }

    public String getCurrentExpression() {
        return currentExpression;
    }

    public PontoDeFita getPontoDeFita() {
        return pontoDeFita;
    }

    public Vertice getVertice() {
        return vertice;
    }
}
