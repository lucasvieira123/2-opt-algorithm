import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tools.MatrizDeCusto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    private int mCustoAtual;
    private ArrayList<Integer> mCicloDeMudancas =new ArrayList<>();
    private ArrayList<Integer> mCicloBackUp = new ArrayList<>();
    private int mPossivelCusto;

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
        //todo temos que fazer um critério de parada para que ele n rode 100 vezes sem precisar já que o melhor custo encontrado é 159
    for( int y =0; y< 100 ; y++){
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
        int valorDaLinhaAtual = 0;

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

               /* n = Integer.valueOf(primeiraLinha.substring(2, 4));
                valorTipo = primeiraLinha.substring(11, 12).trim();
                tipoNome = primeiraLinha.substring(14).trim();*/
                matrizDeCusto = new MatrizDeCusto(n);

                linha = br.readLine();
            }

            while (linha != null && !linha.equals("")) {
                linha = linha.trim();

                String[] arrayDeCustosDaLinha = linha.trim().split("\\s+");
                ArrayList<String> listDeCustosDaLinha = new ArrayList<>(Arrays.asList(arrayDeCustosDaLinha));


                for (int valorDaColunaAtual = 0; valorDaColunaAtual < n; valorDaColunaAtual++) {
                    try {
                        int custoAresta = Integer.valueOf(listDeCustosDaLinha.get(valorDaColunaAtual));
                        matrizDeCusto.addCusto(valorDaLinhaAtual, valorDaColunaAtual, custoAresta);
                    }catch (IndexOutOfBoundsException e){

                        linha = br.readLine();
                        linha = linha.trim();
                        arrayDeCustosDaLinha = linha.trim().split("\\s+");
                        listDeCustosDaLinha.addAll(new ArrayList<>(Arrays.asList(arrayDeCustosDaLinha)));
                        int custoAresta = Integer.valueOf(listDeCustosDaLinha.get(valorDaColunaAtual));
                        matrizDeCusto.addCusto(valorDaLinhaAtual, valorDaColunaAtual, custoAresta);
                    }

                }

                linha = br.readLine();
                valorDaLinhaAtual++;

            }
            matrizDeCusto.print();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return matrizDeCusto;

    }


    private void printPercurso(ArrayList<Integer> trilha) {
        int contador =0;
        for (int v : trilha) {
            System.out.print(v+"->");
            contador++;
        }

        if(contador==trilha.size()){
            System.out.print(trilha.get(0));
        }
        System.out.println();

    }




    private boolean ehPrimeiraLinha(String linha) {
        return linha.contains("N");
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
