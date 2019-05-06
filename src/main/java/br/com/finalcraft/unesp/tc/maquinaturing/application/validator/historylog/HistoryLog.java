package br.com.finalcraft.unesp.tc.maquinaturing.application.validator.historylog;

import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.PontoDeFita;
import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data.Vertice;

import java.util.ArrayList;
import java.util.List;

public class HistoryLog{

    public StringBuilder expression = new StringBuilder();
    public List<HistoryMove> pathWalked = new ArrayList<HistoryMove>();
    public int pointer = 0;
    public int time = -1;
    public boolean match = false;

    public HistoryLog() {
    }

    public HistoryLog(String expression) {
        this.expression.append(expression);
    }

    public HistoryLog(String expression, Vertice verticeInicial) {
        this.expression.append(expression);
        pathWalked.add(new HistoryMove(PontoDeFita.Orientation.STAY,verticeInicial));
    }

    public HistoryLog clone(){
        HistoryLog historyLog = new HistoryLog(expression.toString());
        historyLog.pathWalked = this.pathWalked;
        historyLog.pointer = this.pointer;
        historyLog.time = this.time;
        historyLog.match = this.match;
        return historyLog;
    }

    public void addTime(){
        this.time++;
    }

    public char getCharToRead(){
        //Se estiver fora da expressão atual, então, retorna "
        return expression.charAt(pointer);
    }

    public void readAndWrite(PontoDeFita pontoDeFita, Vertice targetVertice){
        expression.setCharAt(pointer,pontoDeFita.getWrite());
        pathWalked.add(new HistoryMove(pontoDeFita.getOrientation(),targetVertice));
        switch (pontoDeFita.getOrientation()){
            case LEFT:
                pointer--;
                if (pointer < 0){
                    pointer++;
                    expression.insert(0,PontoDeFita.EMPTY_CHAR);
                }
                break;
            case RIGHT:
                pointer++;
                if (pointer > (expression.length() - 1)){
                    expression.append(PontoDeFita.EMPTY_CHAR);
                }
                break;
            case STAY:
                //DoNothing
        }
    }
}
