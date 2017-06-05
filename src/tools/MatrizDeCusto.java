
package tools;

import api.Aresta;
import api.Grafo;
import api.Vertice;
import java.util.ArrayList;

/**
 * Created by Lucas Vieira on 30/05/17.
 */

public  class MatrizDeCusto {
    public static final int custoInfinito = 999;
    private int[][] matrizDistancia;
    private String tipoValor, tipoNome;
    private int n;
    private Grafo mGrafo;

    public MatrizDeCusto(int n) {
        this.n = n;
        matrizDistancia = new int[n][n];
        mGrafo = new Grafo();
    }

    public final String getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(String tipoValor) {
        this.tipoValor = tipoValor;
    }

    public String getTipoNome() {
        return tipoNome;
    }

    public void setTipoNome(String tipoNome) {
        this.tipoNome = tipoNome;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void addCusto(int i, int j, int custo) {
        matrizDistancia[i][j] = custo;
        Vertice verticeOrigem;
        Vertice verticeDestino;
        boolean saoIguais = false;

        if (i == j) {
            saoIguais = true;
        }

        if (mGrafo.contain(i)) {
            verticeOrigem = mGrafo.getVertice(i);
        } else {
            verticeOrigem = new Vertice(i);
            mGrafo.addVertice(verticeOrigem);
        }

        if (mGrafo.contain(j)) {
            verticeDestino = mGrafo.getVertice(j);
        } else {
            verticeDestino = new Vertice(j);
            mGrafo.addVertice(verticeDestino);
        }



        if (!saoIguais) {
            Aresta aresta = new Aresta(verticeOrigem, verticeDestino, custo);
            mGrafo.addAresta(aresta);
            verticeOrigem.addArestaSaindo(aresta);
            verticeDestino.addArestaEntrando(aresta);
        }

    }

    public int getCusto(int i, int j) {
        return matrizDistancia[i][j];
    }

    public Grafo getGrafo() {
        return mGrafo;
    }


    public int getCustoInicial() {
        int soma = 0;
        for (int i = 0; i < n; i++) {
            if (i == matrizDistancia.length - 1) {
                soma += matrizDistancia[i][0];
            } else {
                soma += matrizDistancia[i][i + 1];
            }

        }
        return soma;
    }


    public void print() {
        for (int i = 0; i < matrizDistancia.length; i++) {
            for (int j = 0; j < matrizDistancia[i].length; j++) {
                System.out.print(matrizDistancia[i][j] + " ");
            }
            System.out.println();
        }
    }

    public ArrayList<Vertice> construirTrilhaInicial() {
        ArrayList<Vertice> trilhaVertices = new ArrayList<>();


        for(int i =0; i<n; i++){
            trilhaVertices.add(new Vertice(i));
        }

        return atribuirAsArestas(trilhaVertices);
    }

    private ArrayList<Vertice> atribuirAsArestas(ArrayList<Vertice> trilhaVertices) {
        Vertice verticeOrigem, verticeDestino;
        Aresta aresta;

        for(int i =0; i<trilhaVertices.size(); i++){
            if(i==trilhaVertices.size()-1){
                verticeOrigem = trilhaVertices.get(i);
                verticeDestino = trilhaVertices.get(0);
                 aresta = new Aresta(verticeOrigem,verticeDestino, getCusto(i,0));
            }else {
                verticeOrigem = trilhaVertices.get(i);
                verticeDestino = trilhaVertices.get(i+1);
                 aresta = new Aresta(verticeOrigem,verticeDestino, getCusto(i,i+1));
            }

            verticeOrigem.addArestaSaindo(aresta);
            verticeDestino.addArestaEntrando(aresta);


        }
        return trilhaVertices;
    }


}

