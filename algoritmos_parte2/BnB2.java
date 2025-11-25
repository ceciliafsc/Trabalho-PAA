package algoritmos_parte2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Peca;

/**
 * Branch and Bound para o problema de distribuição de carga.
 */
public class BnB2 {
    private int diferencaMinima = Integer.MAX_VALUE;
    private List<Peca> melhorPorao1 = new ArrayList<>();
    private List<Peca> melhorPorao2 = new ArrayList<>();
    private List<Peca> poraoAtual1 = new ArrayList<>();
    private List<Peca> poraoAtual2 = new ArrayList<>();

    public void executar(List<Peca> pecas) {
        List<Peca> listaOrdenada = new ArrayList<>(pecas);
        listaOrdenada.sort((p1, p2) -> Integer.compare(p2.getPeso(), p1.getPeso()));
        permutar(listaOrdenada, 0);
    }

    private void permutar(List<Peca> lista, int inicio) {
        if (inicio == lista.size()) {
            int diferenca = calcularDistribuicaoGulosa(lista);
            if (diferenca < diferencaMinima) {
                diferencaMinima = diferenca;
                melhorPorao1 = new ArrayList<>(poraoAtual1);
                melhorPorao2 = new ArrayList<>(poraoAtual2);
            }
            return;
        }

        if (diferencaMinima == 0)
            return; // Solução ótima encontrada

        for (int i = inicio; i < lista.size(); i++) {
            Collections.swap(lista, inicio, i);
            // Heurística simples para poda (se a estimativa for ruim, não continua)
            int diferencaEstimada = calcularDistribuicaoGulosa(lista);
            if (diferencaEstimada < diferencaMinima) {
                permutar(lista, inicio + 1);
            }
            Collections.swap(lista, inicio, i);
        }
    }

    private int calcularDistribuicaoGulosa(List<Peca> pecas) {
        poraoAtual1.clear();
        poraoAtual2.clear();
        int peso1 = 0;
        int peso2 = 0;

        for (Peca p : pecas) {
            if (peso1 <= peso2) {
                peso1 += p.getPeso();
                poraoAtual1.add(p);
            } else {
                peso2 += p.getPeso();
                poraoAtual2.add(p);
            }
        }
        return Math.abs(peso1 - peso2);
    }

    public List<Peca> getMelhorPorao1() {
        return melhorPorao1;
    }

    public List<Peca> getMelhorPorao2() {
        return melhorPorao2;
    }

    public int getDiferencaMinima() {
        return diferencaMinima;
    }
}
