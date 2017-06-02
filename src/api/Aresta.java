package api;

/**
 * Created by Lucas Vieira on 25/05/17.
 */
public class Aresta {


    private  Vertice verticeOrigem;
    private  Vertice verticeDestino;
    private  int custo;

    public Aresta(Vertice verticeOrigem, Vertice verticeDestino, int custo) {
        this.verticeOrigem = verticeOrigem;
        this.verticeDestino = verticeDestino;
        this.custo = custo;
    }


    public Vertice getVerticeOrigem() {
        return verticeOrigem;
    }

    public Vertice getVerticeDestino() {
        return verticeDestino;
    }


    public int getCusto() {
        return custo;
    }
}
