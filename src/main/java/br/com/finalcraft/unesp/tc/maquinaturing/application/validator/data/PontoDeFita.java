package br.com.finalcraft.unesp.tc.maquinaturing.application.validator.data;

public class PontoDeFita implements Comparable<PontoDeFita>{

    public static final char EMPTY_CHAR = '▢';

    public Character read;
    public Character write;
    public Orientation orientation;

    public PontoDeFita(char read, char write, Orientation orientation) {
        this.read = read;
        this.write = write;
        this.orientation = orientation;
    }

    public PontoDeFita(char read, char write, char orientation) {
        this.read = read;
        this.write = write;
        this.orientation = Orientation.getByName(orientation);
    }

    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
    }

    public Orientation getOrientation(){
        return this.orientation;
    }

    public Character getRead() {
        return read;
    }

    public Character getWrite() {
        return write;
    }

    public boolean match(PontoDeFita other) {
        return this.toString().equals(other.toString());
    }

    @Override
    public int compareTo(PontoDeFita o) {
       return this.getWrite().compareTo(o.getWrite());
    }

    @Override
    public String toString() {
        return this.getRead() + "|" + this.getWrite() + "|" + this.getOrientation();
    }

    public static enum Orientation{
        RIGHT("R"),
        LEFT("L"),
        STAY("S"),
        BEGINNING("B");


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
                case 'B':
                    return BEGINNING;
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
