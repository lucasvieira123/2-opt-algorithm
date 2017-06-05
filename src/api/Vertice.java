package api;

import java.util.ArrayList;

/**
 * Created by Lucas Vieira on 25/05/17.
 */
public class Vertice {

    private int mId;
    private ArrayList<Aresta> arestasEntrando = new ArrayList<>();
    private ArrayList<Aresta> arestasSaindo = new ArrayList<>();


    public Vertice(int id) {
        this.mId = id;
    }


    public int getId() {
        return mId;
    }

    public ArrayList<Aresta> getArestasSaindo() {
        return arestasSaindo;
    }

    public ArrayList<Aresta> getArestasEntrando() {
        return arestasEntrando;
    }

    public void setId (int id){
        mId = id;
    }


    public void addArestaSaindo(Aresta arestaSaindo) {
        arestasSaindo.add(arestaSaindo);
    }

    public void addArestaEntrando(Aresta arestaEntrando) {
        arestasEntrando.add(arestaEntrando);
    }
}
