package api;

import tools.MatrizDeCusto;

/**
 * Created by Lucas Vieira on 25/05/17.
 */
public class Aresta {


    private  Vertice verticeOrigem;
    private  Vertice verticeDestino;
    private  int mCusto;

    public Aresta(Vertice verticeOrigem, Vertice verticeDestino, int custo) {
        this.verticeOrigem = verticeOrigem;
        this.verticeDestino = verticeDestino;
        this.mCusto = custo;
    }


    public Vertice getVerticeOrigem() {
        return verticeOrigem;
    }

    public Vertice getVerticeDestino() {
        return verticeDestino;
    }


    public int getCusto() {
        return mCusto;
    }

    public void setVerticeOrigem(Vertice verticeOrigem) {
        this.verticeOrigem = verticeOrigem;
    }

    public void setVerticeDestino(Vertice verticeDestino) {
        this.verticeDestino = verticeDestino;
    }

    public void recalculeCusto(MatrizDeCusto matrizDeCusto) {
       mCusto = matrizDeCusto.getCusto(verticeOrigem.getId(),verticeDestino.getId());
    }
    public void print (){
        System.out.print(verticeOrigem.getId()+"->"+verticeDestino.getId());
    }
}