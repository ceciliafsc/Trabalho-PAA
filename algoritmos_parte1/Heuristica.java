package algoritmos_parte1;

import model.*;
import java.util.*;

public class Heuristica {

    private List<Peca> pecas;
    public List<Placa> melhorPlacas = new ArrayList<>();
    private double custoTotal = 0.0;

    public Heuristica(List<Peca> pecas) {
        this.pecas = new ArrayList<>(pecas);
    }

    public List<Peca> executar() {
        System.out.println("\nExecutando algoritmo Heurístico (First Fit Decreasing)");

        long inicio = System.nanoTime();

        // 1. Ordenar peças por área decrescente
        pecas.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));

        // 2. First Fit
        List<Placa> placas = new ArrayList<>();
        placas.add(new Placa()); // Começa com uma placa

        for (Peca p : pecas) {
            boolean alocou = false;

            // Tenta encaixar na primeira placa que couber
            for (Placa placa : placas) {
                if (placa.adicionarPeca(p)) {
                    alocou = true;
                    break;
                }
            }

            // Se não coube em nenhuma, cria nova placa
            if (!alocou) {
                Placa novaPlaca = new Placa();
                if (!novaPlaca.adicionarPeca(p)) {
                    throw new IllegalStateException("Peça " + p.getId() + " não cabe nem em uma placa vazia!");
                }
                placas.add(novaPlaca);
            }
        }

        melhorPlacas = placas;
        custoTotal = calcularCustoTotal(placas);

        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1_000_000.0;
        double tempoSeg = (fim - inicio) / 1_000_000_000.0;

        System.out.println("Custo Total: " + custoTotal);
        System.out.println("Número de placas usadas: " + placas.size());
        System.out.printf("Tempo total: %.3f ms (%.4f segundos)\n", tempoMs, tempoSeg);

        return pecas;
    }

    private double calcularCustoTotal(List<Placa> placas) {
        double custoPlacas = placas.size() * Placa.CUSTO_PLACA;
        double custoLaser = 0.0;

        for (Placa pl : placas) {
            for (Peca p : pl.getPecas()) {
                custoLaser += pl.calcularCustoCorte(p);
            }
        }
        return custoPlacas + custoLaser;
    }

    public List<Placa> getMelhorPlacas() {
        return melhorPlacas;
    }
}
