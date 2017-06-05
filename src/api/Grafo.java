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

    public boolean contain(int idVertice) {
        for(Vertice v : mVertices){
            if(v.getId()==idVertice){
                return true;
            }
        }
        return false;
    }
    public boolean contain (Vertice vertice){
        return contain(vertice.getId());
    }
    public Vertice getVertice(int idVertice){
        Vertice vertice = null ;

        for(Vertice v : mVertices){
            if(v.getId()==idVertice){
               vertice = v;
               break;
            }
        }
        return vertice;
    }

    public Aresta getAresta(int i, int j) {
        Aresta aresta = null;
        for(Aresta a : mArestas){
            if(a.getVerticeOrigem().getId()==i && a.getVerticeDestino().getId()==j ){
                aresta=a;
            }
        }
        return aresta;
    }

    public Aresta getAresta(Vertice verticeOrigem, Vertice verticeDestino) {
        return getAresta(verticeOrigem.getId(),verticeDestino.getId());
    }
    public void printAresta (Aresta aresta){
        System.out.print(aresta.getVerticeOrigem().getId()+"->"+aresta.getVerticeDestino().getId()+"->");
    }
}
