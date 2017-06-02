import api.Aresta;
import api.Grafo;
import api.Vertice;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tools.MatrizDeCusto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


/**
 * Created by Lucas Vieira on 29/05/17.
 */
public final class GrafoApplication extends Application {


    private File file;
    private Stage primaryStage;
    private int n;
    private String valorTipo;
    private String tipoNome;
    private Vertice verticeOrigem;
    private Vertice verticeDestino;
    private MatrizDeCusto mMatrizDeCusto;
    private int mCustoAtual;
    private ArrayList<Aresta> mTrilhaAtual = new ArrayList<Aresta>();
    private Grafo mGrafo;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        File arquivo = abrirChooserParaSelecionarArquivo();

        mMatrizDeCusto = criarMatrizDeCusto(arquivo);
        mGrafo = mMatrizDeCusto.getGrafo();


        mCustoAtual = mMatrizDeCusto.getCustoInicial();
        mTrilhaAtual = mMatrizDeCusto.getTrilaInicial();
        printPercurso(mTrilhaAtual);


        int possivelNovoCusto = mMatrizDeCusto.getCustoInicial();
         ArrayList<Aresta> possivelTrilha = new ArrayList<Aresta>();



        for(Aresta arestaFromTrilha : mTrilhaAtual){
           for(Aresta arestaNaoConsecutiva : arestasNaoConsecutivas(arestaFromTrilha)){
               System.out.println("arestaFromTrilha:"+ arestaFromTrilha.getVerticeOrigem()+"->"+arestaFromTrilha.getVerticeDestino());
               System.out.println("arestaNaoConsecutiva:"+ arestaNaoConsecutiva.getVerticeOrigem()+"->"+arestaNaoConsecutiva.getVerticeDestino());
               System.out.println("custodaTroca:"+custoComAPossivelTroca(arestaFromTrilha,arestaNaoConsecutiva));
               if(custoComAPossivelTroca(arestaFromTrilha,arestaNaoConsecutiva)< possivelNovoCusto){
                   possivelTrilha.clear();
                   possivelTrilha.addAll(aplicarTroca(arestaFromTrilha,arestaNaoConsecutiva));
                   System.out.println("possivelTrilha:");
                   printPercurso(possivelTrilha);
                   if(geraCiclo(possivelTrilha)){
                       System.out.println("tem ciclo");
                       possivelNovoCusto = mMatrizDeCusto.custoFromTrila(possivelTrilha);
                       printPercurso(possivelTrilha);
                   }else {
                       possivelTrilha.clear();
                       possivelTrilha.addAll(mTrilhaAtual);
                   }

               }
           }

        }


    }



    private MatrizDeCusto criarMatrizDeCusto(File arquivo) throws FileNotFoundException {
        MatrizDeCusto matrizDeCusto = null;
        int valorDaLinhaAtual =0;

        BufferedReader br = new BufferedReader(new FileReader(arquivo));

        try {
            String linha = br.readLine();

            if (ehPrimeiraLinha(linha)) {
                String primeiraLinha = linha;
                primeiraLinha = primeiraLinha.trim();

                n = Integer.valueOf(primeiraLinha.substring(2, 4));
                valorTipo = primeiraLinha.substring(11, 12).trim();
                tipoNome = primeiraLinha.substring(14).trim();
                matrizDeCusto = new MatrizDeCusto(n);

                linha = br.readLine();
            }

            while (linha != null && !linha.equals("")) {

                String[] arrayDeCustosDaLinha = linha.trim().split("\\s+");

                for (int valorDaColunaAtual = 0; valorDaColunaAtual < n; valorDaColunaAtual++) {
                    int custoAresta = Integer.valueOf(arrayDeCustosDaLinha[valorDaColunaAtual]);
                    matrizDeCusto.addCusto(valorDaLinhaAtual, valorDaColunaAtual, custoAresta);
                }

                linha = br.readLine();
                valorDaLinhaAtual++;

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return matrizDeCusto;

    }

    private boolean geraCiclo(ArrayList<Aresta> trilha) {
       Aresta arestaAtual;
       Vertice verticeAtual;

        for(int i=0; i<trilha.size();i++){
            verticeAtual = trilha.get(i).getVerticeDestino();

            if(i==trilha.size()-1){

                if(verticeAtual != trilha.get(0).getVerticeOrigem()){
                    return false;
                }
            }else {
                if(verticeAtual != trilha.get(i+1).getVerticeOrigem()){
                    return false;
                }
            }


        }
        return true;

    }

    private ArrayList<Aresta> arestasNaoConsecutivas(Aresta arestaFromTrilha) {
        ArrayList<Aresta> arestas = new ArrayList<>();
        Vertice verticeOrigem = arestaFromTrilha.getVerticeOrigem();
        Vertice verticeDestino = arestaFromTrilha.getVerticeDestino();

        for(int i =0 ; i<n ; i++){
            if(i==verticeOrigem.getId() ||i== verticeDestino.getId()){ continue;}
            for(int j =0 ; j<n ; j++){
                if(j==verticeOrigem.getId() || j== verticeDestino.getId() || i==j){
                    continue;
                }else {
                    arestas.add(new Aresta(new Vertice(i), new Vertice(j), mMatrizDeCusto.getCusto(i,j)));
                }
            }
        }


    return arestas;
    }

    private void printPercurso(ArrayList<Aresta> possivelNovoCusto) {
        for(Aresta a : possivelNovoCusto){
            System.out.print(a.getVerticeOrigem()+"-"+a.getVerticeDestino()+"   ");
        }
        System.out.println("\n<><>");
    }

    private ArrayList<Aresta> aplicarTroca(Aresta arestaA, Aresta arestaB) {
        Integer indexA = null;
        Integer indexB = null;
        ArrayList<Aresta> trilha = new ArrayList<>();
        trilha.addAll(mTrilhaAtual);



        for(int i = 0; i< mTrilhaAtual.size(); i++){
            if(saoArestasIguais(mTrilhaAtual.get(i),arestaA)){
                indexA=i;
            }else if(saoArestasIguais(mTrilhaAtual.get(i),arestaB)){
                indexB=i;
            }
        }

        Aresta novaArestaA = new Aresta(arestaA.getVerticeOrigem(),arestaB.getVerticeOrigem(), mMatrizDeCusto
                .getCusto(arestaA.getVerticeOrigem().getId(),arestaB.getVerticeOrigem().getId()));

        Aresta novaArestaB = new Aresta(arestaA.getVerticeDestino(),arestaB.getVerticeDestino(), mMatrizDeCusto
                .getCusto(arestaA.getVerticeDestino().getId(),arestaB.getVerticeDestino().getId()));

        if(indexA!=null){
            trilha.set(indexA,novaArestaA);
        }

        if(indexB!=null){
            trilha.set(indexB,novaArestaB);
        }

        return trilha;




    }

    private boolean saoArestasIguais(Aresta arestaA, Aresta arestaB) {
        if(arestaA.getVerticeOrigem()==arestaB.getVerticeOrigem()){
            if(arestaA.getVerticeDestino()==arestaB.getVerticeDestino()){
                return true;
            }
        }
        return false;
    }

    private int custoComAPossivelTroca(Aresta arestaA, Aresta arestaB) {
        int custoNovoA = mMatrizDeCusto.getCusto(arestaA.getVerticeDestino().getId(),arestaB.getVerticeDestino().getId());
        int custoNovoB = mMatrizDeCusto.getCusto(arestaA.getVerticeOrigem().getId(),arestaB.getVerticeOrigem().getId());
        int custoComTroca = mCustoAtual - arestaA.getCusto() - arestaB.getCusto() + custoNovoA + custoNovoB;
        return custoComTroca;
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


}
