
package tools;

import api.Aresta;
import api.Grafo;
import api.Vertice;

import java.util.ArrayList;

/**
 * Created by Lucas Vieira on 30/05/17.
 */

public class MatrizDeCusto {
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

    public String getTipoValor() {
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

        Vertice verticeOrigem = new Vertice(i);
        Vertice verticeDestino = new Vertice(j);
        mGrafo.addVertice(verticeOrigem);
        mGrafo.addVertice(verticeDestino);
        mGrafo.addAresta(new Aresta(verticeOrigem,verticeDestino,custo));
    }

    public int getCusto(int i, int j){
        return matrizDistancia[i][j];
    }

    public Grafo getGrafo (){
        return mGrafo;
    }



    public int getCustoInicial(){
        int soma = 0;
        for(int i = 0; i<n; i++){
            if(i==matrizDistancia.length-1){
                soma+= matrizDistancia[i][0];
            }else { soma+= matrizDistancia[i][i+1];}

        }
        return soma;
    }


    public void print(){
        for (int i = 0; i < matrizDistancia.length; i++) {
            for (int j = 0; j < matrizDistancia[i].length; j++) {
                System.out.print(matrizDistancia[i][j] + " ");
            }
            System.out.println();
        }
    }

    public ArrayList<Aresta> getTrilaInicial() {

        ArrayList<Aresta> trila = new ArrayList<>();
        Aresta aresta;
        for(int i =0; i<n; i++){
            if(i==matrizDistancia.length-1){

                aresta =new Aresta(new Vertice(i), new Vertice(0), getCusto(i,0));
            }else {
                aresta =new Aresta(new Vertice(i), new Vertice(i+1), getCusto(i,i+1));

            }
            trila.add(aresta);
        }
        return trila;
    }

    public int custoFromTrila(ArrayList<Aresta>trila){
        int custoTotal = 0;

       for(Aresta aresta : trila){
           custoTotal += aresta.getCusto();;
       }
       return custoTotal;
    }



}

