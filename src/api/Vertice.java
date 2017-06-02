package api;

import java.util.ArrayList;

/**
 * Created by Lucas Vieira on 25/05/17.
 */
public class Vertice {

    private int id;
    private ArrayList<Aresta> arestasEntrando = new ArrayList<>();
    private ArrayList<Aresta> arestasSaindo = new ArrayList<>();


    public Vertice(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public ArrayList<Aresta> getArestasSaindo() {
        return arestasSaindo;
    }

    public ArrayList<Aresta> getArestasEntrando() {
        return arestasEntrando;
    }


}
