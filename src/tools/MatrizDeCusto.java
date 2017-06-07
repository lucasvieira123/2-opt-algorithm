
package tools;

import java.util.ArrayList;

/**
 * Created by Lucas Vieira on 30/05/17.
 */

public  class MatrizDeCusto {
    public static final int custoInfinito = 999;
    private int[][] matrizDistancia;
    private String tipoValor, tipoNome;
    private int n;

    public MatrizDeCusto(int n) {
        this.n = n;
        matrizDistancia = new int[n][n];
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

    }

    public int getCusto(int i, int j) {
        return matrizDistancia[i][j];
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

    public ArrayList<Integer> construirTrilhaInicial() {
        ArrayList<Integer> cicloDeVertices = new ArrayList<>();


        for(int i =0; i<n; i++){
            cicloDeVertices.add(i);
        }

        return cicloDeVertices;
    }



    public int custoDoCiclo(ArrayList<Integer>ciclo ){
        //todo fazer uma soma do jeito que o professor disse, sem precisar passar por todo ciclo.
        int custoDoCiclo = 0;

        for(int i=0;i<ciclo.size();i++){
            if(i==ciclo.size()-1){
                int verticeAtual = ciclo.get(i);
                int verticeProximo = ciclo.get(0);
                custoDoCiclo=custoDoCiclo+this.getCusto(verticeAtual,verticeProximo);
            }else {
            int verticeAtual = ciclo.get(i);
            int verticeProximo = ciclo.get(i+1);
            custoDoCiclo=custoDoCiclo+this.getCusto(verticeAtual,verticeProximo);
            }
        }
        return custoDoCiclo;
    }


}

