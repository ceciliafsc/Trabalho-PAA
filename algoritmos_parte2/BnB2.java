package algoritmos_parte2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Peca;

public class BnB2 {
     private int min = Integer.MAX_VALUE; // diferença mínima de peso
    public List<Peca> melhorPorao1 = new ArrayList<>(); // melhor lista para porão 1
    public List<Peca> melhorPorao2 = new ArrayList<>(); // melhor lista para porão 2
    public List<Peca> poraoAtual1 = new ArrayList<>(); // melhor lista para porão 1
    public List<Peca> poraoAtual2 = new ArrayList<>(); // melhor lista para porão 2

     public BnB2(List<Peca> pecas) {
    }

     public void executar(List<Peca> lista) {
        System.out.println("\nExecutando algoritmo de Branch and Bound");

        long inicio = System.nanoTime(); // INÍCIO DO TEMPO

        permutar(new ArrayList<>(lista), 0);

        long fim = System.nanoTime(); // FIM DO TEMPO

        double tempoMs = (fim - inicio) / 1_000_000.0; // milissegundos
        double tempoSeg = (fim - inicio) / 1_000_000_000.0; // segundos

        System.out.println("Menor diferença de peso: " + min);
        System.out.printf("Tempo total: %.3f ms (%.4f segundos)\n", tempoMs, tempoSeg);
    }

      private void permutar(List<Peca> lista, int inicio) {

        if (inicio == lista.size()) { // fim da permutação
            int diferenca = montarPorao(lista, 0);

            if (diferenca < min) {// guarada melhor
                min = diferenca;
                melhorPorao1= new ArrayList<>(poraoAtual1);
                melhorPorao2= new ArrayList<>(poraoAtual2);
            }
            return;
        }

        for (int i = inicio; i < lista.size(); i++) { // troca todas as posições
            Collections.swap(lista, inicio, i);
             int diferenca = montarPorao(lista, 0);
                    if(diferenca < min){ //se custo atual for menor que o minimo global, continua
                         permutar(lista, inicio + 1);
                    }
            Collections.swap(lista, inicio, i);
        }
    }
    

    public int montarPorao(List<Peca> pecas, int index) {
     int peso1 = 0;
     int peso2 = 0;
        for(int i =0; i < pecas.size(); i++) {
            if( peso1 <= peso2){
               peso1 += pecas.get(i).getPeso();
               poraoAtual1.add(pecas.get(i));
            } else {
               peso2 += pecas.get(i).getPeso();
               poraoAtual2.add(pecas.get(i));
            }
        }
        return Math.abs(peso1 - peso2);//retorna a diferença de peso
    }
}
