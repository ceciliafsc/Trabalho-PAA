package algoritmos_parte1;

import java.util.ArrayList;
import java.util.List;
import model.Peca;
import model.Placa;

/**
 * Implementação da heurística First Fit Decreasing para o problema de corte.
 */
public class Heuristica {
    private List<Peca> pecas;
    private List<Placa> melhorPlacas;

    public Heuristica(List<Peca> pecas) {
        this.pecas = new ArrayList<>(pecas);
        this.melhorPlacas = new ArrayList<>();
    }

    public void executar() {
        // Ordena as peças por área (decrescente) - Estratégia Decreasing
        pecas.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));

        List<Placa> placas = new ArrayList<>();
        placas.add(new Placa());

        for (Peca p : pecas) {
            boolean alocou = false;
            // Tenta encaixar na primeira placa que couber (First Fit)
            for (Placa placa : placas) {
                if (placa.adicionarPeca(p)) {
                    alocou = true;
                    break;
                }
            }
            // Se não couber em nenhuma, cria uma nova placa
            if (!alocou) {
                Placa novaPlaca = new Placa();
                novaPlaca.adicionarPeca(p);
                placas.add(novaPlaca);
            }
        }
        this.melhorPlacas = placas;
    }

    public List<Placa> getMelhorPlacas() {
        return melhorPlacas;
    }
}
