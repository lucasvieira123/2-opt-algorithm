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
    private Vertice verticeOrigem;
    private Vertice verticeDestino;
    private MatrizDeCusto mMatrizDeCusto;
    private int mCustoAtual;
    private Grafo mGrafo;
    private ArrayList<Vertice> mTrilhaDeMudancas =new ArrayList<>();
    private ArrayList<Vertice> mTrilhaBackUp = new ArrayList<>();
    TreeMap mTrilhas = new TreeMap();
    private int mPossivelCusto;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        File arquivo = abrirChooserParaSelecionarArquivo();

        mMatrizDeCusto = criarMatrizDeCusto(arquivo);
        mGrafo = mMatrizDeCusto.getGrafo();

        mTrilhaBackUp = (ArrayList<Vertice>) mMatrizDeCusto.construirTrilhaInicial().clone();
        mTrilhaDeMudancas = (ArrayList<Vertice>) mTrilhaBackUp.clone();
        mCustoAtual = mMatrizDeCusto.getCustoInicial();
        System.out.println("Custo atual:"+mCustoAtual);

        printPercurso(mTrilhaDeMudancas);


        for(int i = 0; i<n; i++){

            if(i!=0){
                if( !mTrilhas.isEmpty()){
                    mTrilhaBackUp = (ArrayList<Vertice>) ((ArrayList<Vertice>) mTrilhas.firstEntry().getValue()).clone();
                    mCustoAtual = ((Integer) mTrilhas.firstKey()).intValue();
                }

                System.out.println("NOVA TRILHA DEFINIDA:");
                printPercurso(mTrilhaBackUp);
                mTrilhas.clear();
                System.out.println("NOVO CUSTO ATUAL DEFINIDO:"+mCustoAtual);
                mTrilhaDeMudancas = (ArrayList<Vertice>) mTrilhaBackUp.clone();
            }


            Aresta arestaDaTrilha = mTrilhaDeMudancas.get(i).getArestasSaindo().get(0);

            for(Aresta arestaPossivel : arestasNaoConsecutivas(arestaDaTrilha)){

                mTrilhaDeMudancas = realizarTroca( arestaDaTrilha, arestaPossivel);
                System.out.print("troca de "); arestaDaTrilha.print();
                System.out.print(" com "); arestaPossivel.print();
                System.out.println("\n");
                System.out.println("trilha com a troca");
                printPercurso(mTrilhaDeMudancas);

                mPossivelCusto = custoComAPossivelTroca(arestaDaTrilha, arestaPossivel);
                System.out.println("Custou com a troca: "+mPossivelCusto);
                boolean gerouCiclo = geraCiclo(mTrilhaDeMudancas);
                if(mPossivelCusto < mCustoAtual && gerouCiclo){
                    System.out.println(" custo aceito e não gerou ciclo");
                    mTrilhas.put(mPossivelCusto, mTrilhaDeMudancas);
                    mTrilhaDeMudancas = (ArrayList<Vertice>) mTrilhaBackUp.clone();
                //   printPercurso(mTrilhaDeMudancas);

                }else {
                    System.out.println("ou não formou ciclo ou "+ mPossivelCusto+">"+ mCustoAtual);
                    mTrilhaDeMudancas= (ArrayList<Vertice>) mTrilhaBackUp.clone();
                }
            }
        }
    }

    private ArrayList<Vertice> realizarTroca(Aresta arestaDaTrilha, Aresta arestaPossivel) {
        String trilhaString = transformarParaString(mTrilhaDeMudancas);
        String trilhacomAlteracoes= "";



        ArrayList<String> trilhaVerticesString =  new ArrayList<>(Arrays.asList(trilhaString.split(",")));

        for(String v : trilhaVerticesString){
            if(v.equals(String.valueOf(arestaDaTrilha.getVerticeOrigem().getId()))){
                trilhacomAlteracoes=trilhacomAlteracoes.concat(arestaPossivel.getVerticeOrigem().getId()+",");
                continue;
            }
            if(v.equals(String.valueOf(arestaDaTrilha.getVerticeDestino().getId()))){
                trilhacomAlteracoes=trilhacomAlteracoes.concat(arestaPossivel.getVerticeDestino().getId()+",");
                continue;
            }

            if(v.equals(String.valueOf(arestaPossivel.getVerticeOrigem().getId()))){
                trilhacomAlteracoes= trilhacomAlteracoes.concat(arestaDaTrilha.getVerticeOrigem().getId()+",");
                continue;
            }

            if(v.equals(String.valueOf(arestaPossivel.getVerticeDestino().getId()))){
                trilhacomAlteracoes= trilhacomAlteracoes.concat(arestaDaTrilha.getVerticeDestino().getId()+",");
                continue;
            }

            trilhacomAlteracoes= trilhacomAlteracoes.concat(v+",");
        }

        return transformarParaTrilha(trilhacomAlteracoes);
    }

    private ArrayList<Vertice> transformarParaTrilha(String trilhacomAlteracoes) {
        ArrayList<Vertice> vertices = new ArrayList<>();
        ArrayList<String> trilhaVerticesString =  new ArrayList<>(Arrays.asList(trilhacomAlteracoes.split(",")));
        Vertice verticeCabeca = null, verticeDestino,verticeOrigem;
        Vertice verticeAtual =null;

        for(int i = 0 ; i< trilhaVerticesString.size() ; i++){
            if(i==trilhaVerticesString.size()-1){
                verticeOrigem = getVerticeFromTrilha(trilhaVerticesString.get(i),vertices);
                verticeDestino = getVerticeFromTrilha(trilhaVerticesString.get(0),vertices);
                Aresta aresta = new Aresta(verticeOrigem,verticeDestino,mMatrizDeCusto.getCusto(verticeOrigem.getId(),verticeDestino.getId()));
                verticeOrigem.addArestaSaindo(aresta);
                verticeDestino.addArestaEntrando(aresta);
            }else {
                if(i==0){
                    verticeOrigem = new Vertice(Integer.valueOf(trilhaVerticesString.get(i)));
                    verticeDestino = new Vertice(Integer.valueOf(trilhaVerticesString.get(i+1)));
                    Aresta aresta = new Aresta(verticeOrigem,verticeDestino,mMatrizDeCusto.getCusto(verticeOrigem.getId(),verticeDestino.getId()));
                    verticeOrigem.addArestaSaindo(aresta);
                    verticeDestino.addArestaEntrando(aresta);
                    vertices.add(verticeOrigem);
                    vertices.add(verticeDestino);
                }else {
                    verticeOrigem = getVerticeFromTrilha(trilhaVerticesString.get(i),vertices);
                    verticeDestino = new Vertice(Integer.valueOf(trilhaVerticesString.get(i+1)));
                    Aresta aresta = new Aresta(verticeOrigem,verticeDestino,mMatrizDeCusto.getCusto(verticeOrigem.getId(),verticeDestino.getId()));
                    verticeOrigem.addArestaSaindo(aresta);
                    verticeDestino.addArestaEntrando(aresta);
                    vertices.add(verticeDestino);

                }

            }


        }

       /* for(int i = 0 ; i< trilhaVerticesString.size() ; i++){
            if(verticeAtual!=null){
                verticeOrigem=verticeAtual;
            }else {
                verticeOrigem = new Vertice(Integer.valueOf(trilhaVerticesString.get(i)));
            }

                if(i==0){
                    verticeCabeca = verticeOrigem;
                }
                if(i==trilhaVerticesString.size()-1){
                    verticeDestino= verticeCabeca;
                }else {
                     verticeDestino = new Vertice(Integer.valueOf(trilhaVerticesString.get(i+1)));
                     verticeAtual = verticeDestino;

                }

                Aresta aresta = new Aresta(verticeOrigem,verticeDestino,mMatrizDeCusto.getCusto(verticeOrigem.getId(),verticeDestino.getId()));
                verticeOrigem.addArestaSaindo(aresta);
                verticeDestino.addArestaEntrando(aresta);
                vertices.add(verticeOrigem);
                vertices.add(verticeDestino);
         //   }
*/
        return vertices;
        }

    private Vertice getVerticeFromTrilha(String s, ArrayList<Vertice> vertices) {
       for(Vertice vertice : vertices){
           if(vertice.getId()==Integer.valueOf(s)){
               return vertice;
           }
       }
       return null;
    }


    private String transformarParaString(ArrayList<Vertice> trilha) {
        String trilhaString = "";
        for(Vertice v :trilha){
            trilhaString = trilhaString.concat(String.valueOf(v.getId())+",");
        }
        return trilhaString.trim();
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




    private ArrayList<Aresta> arestasNaoConsecutivas(Aresta aresta) {
        ArrayList<Aresta> arestas = new ArrayList<>();
        for(Aresta a : mGrafo.getArestas()){
            if(a.getVerticeOrigem().getId() != aresta.getVerticeOrigem().getId() && a.getVerticeDestino().getId() != aresta.getVerticeOrigem().getId()){
                if(a.getVerticeOrigem().getId() != aresta.getVerticeDestino().getId() && a.getVerticeDestino().getId() != aresta.getVerticeDestino().getId()){
                    arestas.add(a);
                }
            }
        }
        return arestas;

    }



    private void printPercurso(ArrayList<Vertice> trilha) {
        int contador =0;
        for (Vertice v : trilha) {
            System.out.print(v.getId()+"->");
            contador++;
        }

        if(contador==trilha.size()){
            System.out.print(trilha.get(0).getId());
        }

        System.out.println("\n<><>");
    }


//    private Trilha aplicarTroca(Aresta arestaA, Aresta arestaB, Trilha trilhaBackUp) {
//        Trilha trilha = new Trilha();
//
//        for( Aresta aresta : trilhaBackUp.getTrilha()){
//
//        }
//
//
//    }





    private boolean saoArestasIguais(Aresta arestaA, Aresta arestaB) {
        if (arestaA.getVerticeOrigem().getId() == arestaB.getVerticeOrigem().getId()) {
            if (arestaA.getVerticeDestino().getId() == arestaB.getVerticeDestino().getId()) {
                return true;
            }
        }
        return false;
    }

    private int custoComAPossivelTroca(Aresta arestaA, Aresta arestaB) {
        int custo =0;
        for( int i=0; i< mTrilhaDeMudancas.size() ;i++){
            if(i==mTrilhaDeMudancas.size()-1){
               custo=custo+ mMatrizDeCusto.getCusto(mTrilhaDeMudancas.get(i).getId(),mTrilhaDeMudancas.get(0).getId());
            }else {
                custo=custo+ mMatrizDeCusto.getCusto(mTrilhaDeMudancas.get(i).getId(),mTrilhaDeMudancas.get(i+1).getId());
            }
        }

        /*int custoNovoA = mMatrizDeCusto.getCusto(arestaA.getVerticeDestino().getId(), arestaB.getVerticeDestino().getId());
        int custoNovoB = mMatrizDeCusto.getCusto(arestaA.getVerticeOrigem().getId(), arestaB.getVerticeOrigem().getId());
        int custoComTroca = mCustoAtual - arestaA.getCusto() - arestaB.getCusto() + custoNovoA + custoNovoB;
        return custoComTroca;*/
        return custo;
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

    }


}
