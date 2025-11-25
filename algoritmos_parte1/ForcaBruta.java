package algoritmos_parte1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.CalculadoraCustos;
import model.Peca;
import model.Placa;

/**
 * Algoritmo de Força Bruta para o problema de corte.
 * Testa todas as permutações possíveis de entrada das peças.
 */
public class ForcaBruta {
    private List<Peca> pecas;
    private double custoMinimo = Double.MAX_VALUE;
    private List<Placa> melhorPlacas = new ArrayList<>();

    public ForcaBruta(List<Peca> pecas) {
        this.pecas = new ArrayList<>(pecas);
    }

    public void executar() {
        permutar(new ArrayList<>(pecas), 0);
    }

    private void permutar(List<Peca> lista, int inicio) {
        if (inicio == lista.size()) {
            // Avalia a permutação completa
            List<Placa> placas = montarPlacas(lista);
            double custo = CalculadoraCustos.calcularCustoTotal(placas);

            if (custo < custoMinimo) {
                custoMinimo = custo;
                melhorPlacas = placas;
            }
            return;
        }

        for (int i = inicio; i < lista.size(); i++) {
            Collections.swap(lista, inicio, i);
            permutar(lista, inicio + 1);
            Collections.swap(lista, inicio, i); // Backtrack
        }
    }

    private List<Placa> montarPlacas(List<Peca> listaOrdenada) {
        List<Placa> placas = new ArrayList<>();
        placas.add(new Placa());

        for (Peca p : listaOrdenada) {
            Placa ultimaPlaca = placas.get(placas.size() - 1);
            if (!ultimaPlaca.adicionarPeca(p)) {
                Placa novaPlaca = new Placa();
                novaPlaca.adicionarPeca(p);
                placas.add(novaPlaca);
            }
        }
        return placas;
    }

    public List<Placa> getMelhorPlacas() {
        return melhorPlacas;
    }
}
