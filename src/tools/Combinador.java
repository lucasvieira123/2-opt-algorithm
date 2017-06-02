package tools;

import java.util.ArrayList;

/**
 * Created by Lucas Vieira on 30/05/17.
 */
public final class Combinador {



      static public  ArrayList<Combinado> gerarTodasCombinacoes(int tamanhoDoConjunto, int ordem){
         ArrayList<Combinado> conjuntoDeCombinacoes = new ArrayList<>();
           for(int i=0; i<tamanhoDoConjunto;i++){
               for(int j=0; j<tamanhoDoConjunto;j++){
                    if(i==j){
                         continue;
                    }

                    Combinado Combinado = new Combinado(i,j);
                    conjuntoDeCombinacoes.add(Combinado);
               }
          }
          return conjuntoDeCombinacoes;
     }




}
