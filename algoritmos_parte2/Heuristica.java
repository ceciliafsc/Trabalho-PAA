package algoritmos_parte2;

import java.util.ArrayList;
import java.util.List;
import model.Peca;

/**
 * Heurística LPT (Longest Processing Time) para distribuição de carga.
 */
public class Heuristica {
    private List<Peca> melhorPorao1 = new ArrayList<>();
    private List<Peca> melhorPorao2 = new ArrayList<>();
    private int diferencaMinima = Integer.MAX_VALUE;

    public void executar(List<Peca> pecas) {
        List<Peca> ordenadas = new ArrayList<>(pecas);
        // Ordena por peso decrescente (LPT)
        ordenadas.sort((p1, p2) -> Integer.compare(p2.getPeso(), p1.getPeso()));

        List<Peca> porao1 = new ArrayList<>();
        List<Peca> porao2 = new ArrayList<>();
        int peso1 = 0;
        int peso2 = 0;

        // Distribuição gulosa
        for (Peca p : ordenadas) {
            if (peso1 <= peso2) {
                porao1.add(p);
                peso1 += p.getPeso();
            } else {
                porao2.add(p);
                peso2 += p.getPeso();
            }
        }

        this.melhorPorao1 = porao1;
        this.melhorPorao2 = porao2;
        this.diferencaMinima = Math.abs(peso1 - peso2);
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
