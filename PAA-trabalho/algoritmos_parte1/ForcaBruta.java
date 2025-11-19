package algoritmos_parte1;
import model.*;
import java.util.*;

public class ForcaBruta {

    private List<Peca> pecas; // lista de peças
    private double min = Double.MAX_VALUE; // mínimo global
    public List<Peca> melhorLista = new ArrayList<>(); // melhor lista encontrada
    public List<Placa> melhorPlacas = new ArrayList<>(); // melhor disposição de placas

    public ForcaBruta(List<Peca> pecas) {
        this.pecas = pecas;
    }

    public List<Peca> executar() {
        System.out.println("\nExecutando algoritmo de força bruta");

        long inicio = System.nanoTime(); // INÍCIO DO TEMPO

        permutar(new ArrayList<>(pecas), 0);

        long fim = System.nanoTime(); // FIM DO TEMPO

        double tempoMs = (fim - inicio) / 1_000_000.0; // milissegundos
        double tempoSeg = (fim - inicio) / 1_000_000_000.0; // segundos

        System.out.println("Melhor custo: " + min);
        System.out.printf("Tempo total: %.3f ms (%.4f segundos)\n", tempoMs, tempoSeg);
        return melhorLista;
    }

    // Gera todas as permutações (força bruta)
    private void permutar(List<Peca> lista, int inicio) {

        if (inicio == lista.size()) { // fim da permutação
            List<Placa> placas = montarPlacas(lista);
            double custo = calcularCustoTotal(placas);

            if (custo < min) {// guaradr melhor
                min = custo;
                melhorLista = new ArrayList<>(lista);
                melhorPlacas = copiarPlacas(placas);
            }
            return;
        }

        for (int i = inicio; i < lista.size(); i++) { // troca todas as posições
            Collections.swap(lista, inicio, i);
            permutar(lista, inicio + 1);
            Collections.swap(lista, inicio, i);
        }
    }

    // Monta placas a partir da lista de peças na ordem dada
    private List<Placa> montarPlacas(List<Peca> lista) {

        List<Placa> placas = new ArrayList<>(); // lista de placas
        placas.add(new Placa());

        for (Peca p : lista) {

            // se peça é maior que a placa, impossível
            if (p.getAltura() > Placa.ALTURA || p.getLargura() > Placa.LARGURA) {
                throw new IllegalArgumentException("Peça (id=" + p.getId() + ") maior que a placa: " +
                        p.getAltura() + "x" + p.getLargura());
            }

            Placa atual = placas.get(placas.size() - 1); // pega a última placa adicionada

            boolean adicionou = atual.adicionarPeca(p);// tenta adicionar na placa atual
            if (!adicionou) {// não conseguiu, cria uma placa nova
                Placa nova = new Placa();
                boolean adicionouNaNova = nova.adicionarPeca(p);
                if (!adicionouNaNova) {
                    throw new IllegalStateException(
                            "Não foi possível adicionar peça em placa vazia (id=" + p.getId() + ")");
                }
                placas.add(nova);
            }
        }

        return placas;
    }

    // Calcula custo total
    private double calcularCustoTotal(List<Placa> placas) {

        double custoPlacas = placas.size() * Placa.CUSTO_PLACA; // 1000* numero de placas

        double custoLaser = 0.0;
        for (Placa pl : placas) {
            for (Peca p : pl.getPecas()) {
                custoLaser += pl.calcularCustoCorte(p);
            }
        }

        return custoPlacas + custoLaser; // custo total
    }

    // Cópia profunda das placas (clona grade e lista de peças)
    private List<Placa> copiarPlacas(List<Placa> placas) {
        List<Placa> copia = new ArrayList<>();
        for (Placa p : placas) {
            copia.add(new Placa(p));
        }
        return copia;
    }
}
