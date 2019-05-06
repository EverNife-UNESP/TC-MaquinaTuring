package br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data;

import br.com.finalcraft.unesp.tc.maquinaturing.application.validator.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aresta {

    public int sourceId;
    public int targetId;

    public List<PontoDeFita> pontoDeFitaList = new ArrayList<PontoDeFita>();

    public Aresta(int sourceId, int destinyId) {
        this.sourceId = sourceId;
        this.targetId = destinyId;
    }

    public boolean addPontoDeFita(PontoDeFita pontoDeFita){
        for (PontoDeFita registered : pontoDeFitaList){
            if (registered.match(pontoDeFita)){
                return false;
            }
        }
        pontoDeFitaList.add(pontoDeFita);
        Collections.sort(pontoDeFitaList);
        return true;
    }

    public List<PontoDeFita> getPontosDeFita() {
        return pontoDeFitaList;
    }

    public List<PontoDeFita> getPossibleFutures(char character) {
        List<PontoDeFita> possibleFutures = new ArrayList<PontoDeFita>();
        for (PontoDeFita pontoDeFita : pontoDeFitaList){
            if (pontoDeFita.getRead() == character){
                possibleFutures.add(pontoDeFita);
            }
        }
        return possibleFutures;
    }

    public String getGramarsString() {
        if (pontoDeFitaList.isEmpty()) return "Truly Empty";
        StringBuilder stringBuilder = new StringBuilder();
        for (PontoDeFita pontoDeFita : getPontosDeFita()){
            stringBuilder.append( pontoDeFita.getRead() + "|" + pontoDeFita.getWrite() + "|" + pontoDeFita.getOrientation());
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getTargetId() {
        return targetId;
    }

    public boolean isValid() {
        return pontoDeFitaList.size() > 0;
    }

    public String getIdentifier(){
        return "q" + sourceId + " --> q" + targetId;
    }

    public Aresta cloneAresta(){
        Aresta newAresta = new Aresta(sourceId,targetId);
        newAresta.getPontosDeFita().addAll(this.getPontosDeFita());
        return newAresta;
    }

    public boolean isSameTransaction(Aresta obj) {
        return ( this.sourceId == obj.sourceId && this.targetId == obj.targetId);
    }

    public Vertice getTargetVertice(){
        return Validator.getVerticeFromID(targetId);
    }
}
