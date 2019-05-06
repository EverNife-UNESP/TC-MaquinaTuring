package br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data;

import br.com.finalcraft.unesp.tc.maquinaturing.javafx.controller.transitions.neededdata.PontoDeFitaToString;

public class PontoDeFita implements Comparable<PontoDeFita>{

    public Character first;
    public Character second;
    public Orientation orientation;

    public PontoDeFita(char first, char second, Orientation orientation) {
        this.first = first;
        this.second = second;
        this.orientation = orientation;
    }

    public PontoDeFita(char first, char second,char orientation) {
        this.first = first;
        this.second = second;
        this.orientation = Orientation.getByName(orientation);
    }

    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
    }

    public Orientation getOrientation(){
        return this.orientation;
    }

    public Character getFirst() {
        return first;
    }

    public Character getSecond() {
        return second;
    }

    public boolean match(PontoDeFita other) {
        return this.getFirst() == other.getFirst()
                && this.getSecond() == other.getSecond()
                && this.getOrientation() == other.getOrientation();
    }

    @Override
    public int compareTo(PontoDeFita o) {
       return this.getSecond().compareTo(o.getSecond());
    }

    public static enum Orientation{
        RIGHT("R"),
        LEFT("L"),
        STAY("S");


        String alias;
        Orientation(String alias) {
            this.alias = alias;
        }

        @Override
        public String toString() {
            return alias;
        }

        public static Orientation getByName(char name){
            switch (name){
                case 'R':
                    return RIGHT;
                case 'L':
                    return LEFT;
                case 'S':
                    return STAY;
            }
            return null;
        }
    }
}
