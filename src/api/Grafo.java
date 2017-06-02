package api;

import java.util.ArrayList;

/**
 * Created by Lucas Vieira on 02/06/17.
 */
public class Grafo {

    ArrayList<Aresta> mArestas = new ArrayList<>();
    ArrayList<Vertice> mVertices = new ArrayList<>();

    public ArrayList<Aresta> getArestas() {
        return mArestas;
    }

    public ArrayList<Vertice> getVertices() {
        return mVertices;
    }

    public void addVertice (Vertice vertice){
        mVertices.add(vertice);
    }

    public void addAresta(Aresta aresta){
        mArestas.add(aresta);
    }
}
