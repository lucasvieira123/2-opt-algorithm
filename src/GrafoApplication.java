import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tools.MatrizDeCusto;

import java.io.*;
import java.util.*;


/**
 * Created by Lucas Vieira on 29/05/17.
 */
public final class GrafoApplication extends Application {

    private File file;
    private Stage primaryStage;
    private int n;
    private String valorTipo;
    private String tipoNome;
    private MatrizDeCusto mMatrizDeCusto;
    private double mCustoAtual;
    private ArrayList<Integer> mCicloDeMudancas =new ArrayList<>();
    private ArrayList<Integer> mCicloBackUp = new ArrayList<>();
    private double mPossivelCusto;
    private boolean jaPassouPeloZero;
    private boolean zeroFoiAdicionado;
    private double custoAtualBackUp;
    private int countDeMesmoCusto;
    private ArrayList eixoX;
    private ArrayList eixoY;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        File arquivo = abrirChooserParaSelecionarArquivo();

        mMatrizDeCusto = criarMatrizDeCusto(arquivo);
        // mMatrizDeCusto.print();


        mCicloBackUp = (ArrayList<Integer>) mMatrizDeCusto.construirTrilhaInicial().clone();
        mCicloDeMudancas = clone(mCicloBackUp);
        mCustoAtual = mMatrizDeCusto.getCustoInicial();
        System.out.println("Custo atual:"+mCustoAtual);

        printPercurso(mCicloDeMudancas);
        long startTime = System.currentTimeMillis();

        for( int y =0; y< 100 ; y++){

            if(estaGerandoMesmoCustoCincoVezes()){
                long endTime   = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                System.out.println("TempoTotal:"+totalTime+"ms");

                System.exit(0);
            }

            for(int i = 1; i<n; i++){
                //  outloop:
                // for(int verticePossivel : verticesNaoConsecutivos(mCicloDeMudancas.get(i))){
                for(int j =i+1 ; j< mCicloDeMudancas.size();j++){
                    if(!saoConsecutivo(mCicloDeMudancas.get(i),mCicloDeMudancas.get(j))){
                        int verticePossivel = mCicloDeMudancas.get(j);

                        int verticeDoCiclo = mCicloDeMudancas.get(i);
                        realizarTroca(verticeDoCiclo, verticePossivel);
                        System.out.print("troca de "+verticeDoCiclo);
                        System.out.print(" com "+ verticePossivel);
                        System.out.println();
                        System.out.println("trilha com a troca");
                        printPercurso(mCicloDeMudancas);
                        mPossivelCusto = mMatrizDeCusto.custoDoCiclo(mCicloDeMudancas);
                        System.out.println("Custou com a troca: "+mPossivelCusto);
                        System.out.println("Custo atual:"+mCustoAtual);

                        if(mPossivelCusto < mCustoAtual){
                            System.out.print("Novo ciclo encontrado:");
                            printPercurso(mCicloDeMudancas);
                            System.out.println("Novo Custo:" +mPossivelCusto);
                            mCustoAtual= mPossivelCusto;

                            mCicloBackUp = clone(mCicloDeMudancas);
                            //   break outloop;

                        }else {
                            mCicloDeMudancas = clone(mCicloBackUp);
                            System.out.println("Volta para o ciclo");
                            printPercurso(mCicloBackUp);
                        }
                    }
                }
            }
            System.out.println("ciclo final:");
            printPercurso(mCicloBackUp);
            System.out.println("custo final:");
            System.out.println(mCustoAtual);

        }
    }

    private boolean estaGerandoMesmoCustoCincoVezes() {

        if(custoAtualBackUp==mCustoAtual && mCustoAtual!=0){
            countDeMesmoCusto++;
        }
        if(countDeMesmoCusto==5){
            return true;
        }

        custoAtualBackUp = mCustoAtual;
        return false;
    }

    private boolean saoConsecutivo(Integer vertice1, Integer vertice2) {
        if(vertice1==vertice2){
            return true;
        }
        int indexDoVertice1 = getIndexDesseElementoNoArrayList(vertice1,mCicloDeMudancas);

        if(indexDoVertice1==0){// elemento 0 do verto os consecultivos sao elemento 9 e o elemento 2
            if(mCicloDeMudancas.get(indexDoVertice1+1)==vertice2){
                return true;
            }
            if(mCicloDeMudancas.get(mCicloDeMudancas.size()-1)==vertice2){
                return true;
            }
        }

        if (indexDoVertice1==mCicloDeMudancas.size()-1){ // elemento 9 do verto os consecultivos sao elemento 0 e o elemento 8

            if(mCicloDeMudancas.get(0)==vertice2){
                return true;
            }
            if(mCicloDeMudancas.get(mCicloDeMudancas.size()-1)==vertice2){
                return true;
            }
        }

        return false;

    }
    //    }

    private ArrayList<Integer> clone(ArrayList<Integer> mCicloDeMudancas) {
        ArrayList<Integer>arrayList = new ArrayList<>();
        for(int vertice : mCicloDeMudancas){
            Integer i = new Integer(vertice);
            arrayList.add(i);
        }
        return arrayList;
    }

    private ArrayList<Integer> verticesNaoConsecutivos(int verticeAtual) {
        Integer indexVerticeAtual=getIndexDesseElementoNoArrayList(verticeAtual,mCicloDeMudancas);
        ArrayList<Integer> verticesNaoConsecutivos = clone(mCicloDeMudancas);
        Integer [] array = new Integer[verticesNaoConsecutivos.size()];
        array = verticesNaoConsecutivos.toArray(array);

        if(indexVerticeAtual==mCicloDeMudancas.size()-1){
            array[indexVerticeAtual-1]=null;
            array[indexVerticeAtual]=null;
            array[0]=null;


        } else if(indexVerticeAtual==0){
            array[mCicloDeMudancas.size()-1]=null;
            array[indexVerticeAtual]=null;
            array[indexVerticeAtual+1]=null;

        }else {
            array[indexVerticeAtual-1]=null;
            array[indexVerticeAtual]=null;
            array[indexVerticeAtual+1]=null;

        }
        ArrayList<Integer>arrayListSemElementosNull = new ArrayList<>(Arrays.asList(array));
        arrayListSemElementosNull.removeAll(Collections.singleton(null));
        return arrayListSemElementosNull;

    }

    Integer getIndexDesseElementoNoArrayList(int vertice, ArrayList<Integer> arrayList){
        for(int i =0; i<arrayList.size();i++){
            if(vertice==arrayList.get(i)){
                return i;
            }
        }
        return null;
    }

    private void realizarTroca(int verticeDoCiclo , int verticePossivel) {
        ArrayList<Integer> cicloComTroca = new ArrayList<>();
        int indexVerticeDoCiclo = getIndexDesseElementoNoArrayList(verticeDoCiclo,mCicloDeMudancas);
        int indexVerticePossivel = getIndexDesseElementoNoArrayList(verticePossivel,mCicloDeMudancas);
        Collections.swap(mCicloDeMudancas,indexVerticeDoCiclo,indexVerticePossivel);
    }



    private MatrizDeCusto criarMatrizDeCusto(File arquivo) throws FileNotFoundException {
        MatrizDeCusto matrizDeCusto = null;

        BufferedReader br = new BufferedReader(new FileReader(arquivo));

        try {
            String linha = br.readLine();

            if (ehPrimeiraLinha(linha)) {
                String primeiraLinha = linha;
                primeiraLinha = primeiraLinha.trim();

                if(primeiraLinha.contains("N=")){
                    int quantidadeDeCaracter = "N=".length();
                    int indexInicial= primeiraLinha.indexOf("N=");

                    n = Integer.valueOf(primeiraLinha.substring(indexInicial+quantidadeDeCaracter,
                            indexInicial+quantidadeDeCaracter+3).trim());
                }
                if(primeiraLinha.contains("Tipo=")){
                    int quantidadeDeCaracter = "Tipo=".length();
                    int indexInicial= primeiraLinha.indexOf("Tipo=");
                    valorTipo = primeiraLinha.substring(indexInicial+quantidadeDeCaracter,
                            indexInicial+quantidadeDeCaracter+1).trim();
                }

                matrizDeCusto = new MatrizDeCusto(n);

                linha = br.readLine();

            }

            if(valorTipo.contains("3")) {
                System.out.printf("Tipo 3");
                return criarMatrizDeCustoParaTipo3(linha,matrizDeCusto,br);

            }else if(valorTipo.contains("1")){
                System.out.println("Tipo 1");
                return criarMatrizDeCustoParaTipo1(linha,matrizDeCusto,br);

            }else if (valorTipo.contains("2")){
                System.out.println("Tipo 2");
                return criarMatrizDeCustoParaTipo2(linha,matrizDeCusto,br);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return matrizDeCusto;
    }





    private MatrizDeCusto criarMatrizDeCustoParaTipo3(String linha, MatrizDeCusto matrizDeCusto, BufferedReader br) throws IOException {

        int valorDaLinhaAtual = 0;

        while (linha != null && !linha.equals("")) {
            linha = linha.trim();

            String[] arrayDeCustosDaLinha = linha.trim().split("\\s+");
            ArrayList<String> listDeCustosDaLinha = new ArrayList<>(Arrays.asList(arrayDeCustosDaLinha));



            for (int valorDaColunaAtual = 0; valorDaColunaAtual < n; valorDaColunaAtual++) {
                try {
                    Double custoAresta = Double.valueOf(listDeCustosDaLinha.get(valorDaColunaAtual));
                    matrizDeCusto.addCusto(valorDaLinhaAtual, valorDaColunaAtual, custoAresta);
                } catch (IndexOutOfBoundsException e) {

                    linha = br.readLine();
                    linha = linha.trim();
                    arrayDeCustosDaLinha = linha.trim().split("\\s+");
                    listDeCustosDaLinha.addAll(new ArrayList<>(Arrays.asList(arrayDeCustosDaLinha)));
                    Double custoAresta = Double.valueOf(listDeCustosDaLinha.get(valorDaColunaAtual));
                    matrizDeCusto.addCusto(valorDaLinhaAtual, valorDaColunaAtual, custoAresta);
                }

            }

            linha = br.readLine();
            valorDaLinhaAtual++;

        }
        return matrizDeCusto;

    }

    private MatrizDeCusto criarMatrizDeCustoParaTipo2(String linha, MatrizDeCusto matrizDeCusto, BufferedReader br) throws IOException {
        eixoX = new ArrayList<>();
        eixoY = new ArrayList<>();

        System.out.println("Tipo 2");
        while (linha != null && !linha.equals("")) {
            String[] arrayDeCustosDaLinha = linha.trim().split("\\s+");
            ArrayList<String> listDeCustosDaLinha = new ArrayList<>(Arrays.asList(arrayDeCustosDaLinha));
                  /*  try {*/
            Double x = Double.valueOf(listDeCustosDaLinha.get(0));
            eixoX.add(x);
            Double y = Double.valueOf(listDeCustosDaLinha.get(1));
            eixoY.add(y);

            linha = br.readLine();

                    /*} catch (IndexOutOfBoundsException e) {
                        linha = br.readLine();
                        linha = linha.trim();
                        arrayDeCustosDaLinha = linha.trim().split("\\s+");
                        listDeCustosDaLinha.addAll(new ArrayList<>(Arrays.asList(arrayDeCustosDaLinha)));
                        int custoAresta = Integer.valueOf(listDeCustosDaLinha.get(contador));

                    }*/

        }
        for(int i = 0; i<n; i++){
            double varX1 = (double) eixoX.get(i);
            double varY1 = (double) eixoY.get(i);
            double varXn = 0;
            double varYn = 0;

            for(int j = 0; j<n; j++){
                varXn = (double) eixoX.get(j);
                varYn = (double) eixoY.get(j);
                matrizDeCusto.addCusto(i,j, distancianDePontos(varX1,varY1,varXn,varYn));
            }

        }
        matrizDeCusto.print();
        return matrizDeCusto;
    }



    private MatrizDeCusto criarMatrizDeCustoParaTipo1(String linha, MatrizDeCusto matrizDeCusto, BufferedReader br) throws IOException {

        System.out.printf("Tipo 1\n");
        while (linha != null && !linha.equals("")) {
            linha = linha.trim();

            String[] arrayDeCustosDaLinha = linha.trim().split("\\s+");
            ArrayList<String> listDeCustosDaLinha = new ArrayList<>(Arrays.asList(arrayDeCustosDaLinha));

            int quantidadeDeZeroPorLinha =1;
            for (int indexLinhaAtual = 0; indexLinhaAtual < n; indexLinhaAtual++) {

                int indexColunaAtual = 0;

                for(int index =0 ; index < listDeCustosDaLinha.size(); index++){
                    indexColunaAtual=quantidadeDeZeroPorLinha+index;

                    int custoAresta = Integer.valueOf(listDeCustosDaLinha.get(index));
                    matrizDeCusto.addCusto(indexLinhaAtual,indexColunaAtual,custoAresta);
                    matrizDeCusto.addCusto(indexColunaAtual,indexLinhaAtual,custoAresta);
                }
                quantidadeDeZeroPorLinha++;

                linha = br.readLine();
                if(linha == null){
                    return matrizDeCusto;
                }

                linha = linha.trim();
                arrayDeCustosDaLinha = linha.trim().split("\\s+");
                listDeCustosDaLinha=(new ArrayList<>(Arrays.asList(arrayDeCustosDaLinha)));
            }




        }
        return matrizDeCusto;

    }


    private void printPercurso(ArrayList<Integer> trilha) {
        int contador =0;
        for (int v : trilha) {
            System.out.print(v+1+"->");
            contador++;
        }

        if(contador==trilha.size()){
            System.out.print(trilha.get(0)+1);
        }
        System.out.println();

    }




    private boolean ehPrimeiraLinha(String linha) {
        return linha.contains("N");
    }

    private double distancianDePontos(double x1, double y1, double x2, double y2){
        double resul1 = Math.pow((x1-x2), 2);
        double resul2 = Math.pow((y1-y2), 2);
        double resul3 = Math.sqrt(resul1+resul2);
        double distancia = resul3;
        return   distancia;
    }

    public File abrirChooserParaSelecionarArquivo() {
        primaryStage.setTitle("Escolha o arquivo");
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha o arquivo");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Texto", "txt"));
        return fileChooser.showOpenDialog(primaryStage);
    }
/*
    private boolean geraCiclo(ArrayList<Vertice> trilha) {
        Aresta arestaAtual;
        Vertice verticeAtual;
        try {
            for (int i = 0; i < trilha.size(); i++) {
                verticeAtual = trilha.get(i);

                if (i == trilha.size() - 1) {

                    if (verticeAtual.getArestasSaindo().get(0).getVerticeDestino().getId() != trilha.get(0).getId()) {
                        return false;
                    }
                } else {
                    if (verticeAtual.getArestasSaindo().get(0).getVerticeDestino().getId() != trilha.get(i + 1).getId() ) {
                        return false;
                    }
                }


            }
        }catch (Exception e){
            return false;
        }


        return true;

    }*/


}