package br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data;

import br.com.finalcraft.unesp.tc.maquinaturing.desenho.Vertex;

import java.util.ArrayList;
import java.util.List;

public class Vertice {

    public int id;
    public boolean isInitial = false;
    public boolean isFinale = false;
    public List<Aresta> arestasList = new ArrayList<Aresta>();
    public List<Aresta> correctFutureArestasList = new ArrayList<Aresta>();
    public Vertex vertex = null;
    public String customIdentifier = "";


    public boolean alreadyChecked = false;

    public Vertice() {
    }

    public static Vertice encapsulate(Vertex vertex){
        Vertice vertice = new Vertice();
        vertice.id = vertex.getID();
        vertice.isInitial = vertex.isInitial();
        vertice.isFinale = vertex.isFinale();
        vertice.vertex = vertex;
        return vertice;
    }

    public void extractOwnVertices(List<Aresta> allArestas){
        this.arestasList.clear();
        for (Aresta aresta : allArestas){
            if (aresta.getSourceId() == this.id){
                this.arestasList.add(aresta);
            }
        }
    }

    public Vertice cloneVertice(){
        Vertice newVertice = new Vertice();
        newVertice.id = this.id;
        newVertice.isInitial = this.isInitial();
        newVertice.isFinale = this.isFinale();
        newVertice.vertex = this.vertex;
        return newVertice;
    }


    public int getId() {
        return id;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public boolean isFinale() {
        return isFinale;
    }

    public List<Aresta> getArestasList() {
        return arestasList;
    }

    public Vertex getVertex() {
        return vertex;
    }
}
