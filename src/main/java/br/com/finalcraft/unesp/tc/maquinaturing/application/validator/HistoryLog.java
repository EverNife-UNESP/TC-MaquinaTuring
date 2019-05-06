package br.com.finalcraft.unesp.tc.maquinaturing.application.validator;

public class HistoryLog{
    public String path = "";
    public int time = -1;
    public boolean math = false;

    public HistoryLog() {
    }

    public HistoryLog clone(){
        HistoryLog historyLog = new HistoryLog();
        historyLog.path = this.path;
        historyLog.time = this.time;
        historyLog.math = this.math;
        return historyLog;
    }

    public HistoryLog(String path, int time) {
        this.path = path;
        this.time = time;
    }

    public void addTime(){
        this.time++;
    }
    public void addNullPathPrefix(){
        path = path + '\u03B5';
    }

    public void addPath(int id){
        path = path + id;
    }

    public void undoPath(){
        this.path = path.substring(0,path.length() - 2);
    }

    public void undoWalk(){
        this.time --;
        this.path = path.substring(0,path.length() - 2);
    }
}
