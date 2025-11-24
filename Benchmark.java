import model.*;
import algoritmos_parte1.*;
import algoritmos_parte2.*;
import java.util.*;
import java.io.*;

public class Benchmark {

    public static void main(String[] args) {
        String[] arquivos = { "entrada_pequena.txt", "entrada.txt", "entrada_media.txt", "entrada_grande.txt" };

        System.out.println("=== BENCHMARK DE PERFORMANCE ===\n");
        System.out.println("| Arquivo | N Peças | Algoritmo | Custo/Diferença | Tempo (ms) | Placas |");
        System.out.println("|---------|---------|-----------|-----------------|------------|--------|");

        for (String arquivo : arquivos) {
            try {
                List<Peca> pecas = lerArquivo(arquivo);
                int n = pecas.size();

                // Heurística
                Heuristica h = new Heuristica(new ArrayList<>(pecas));
                long inicio = System.nanoTime();
                h.executar();
                long fim = System.nanoTime();
                double tempoH = (fim - inicio) / 1_000_000.0;
                double custoH = calcularCusto(h.getMelhorPlacas());
                System.out.printf("| %s | %d | Heurística | R$ %.2f | %.3f | %d |\n",
                        arquivo, n, custoH, tempoH, h.getMelhorPlacas().size());

                // Branch and Bound (skip if too large)
                if (n <= 10) {
                    BranchAndBound bb = new BranchAndBound(new ArrayList<>(pecas));
                    inicio = System.nanoTime();
                    bb.executar();
                    fim = System.nanoTime();
                    double tempoBB = (fim - inicio) / 1_000_000.0;
                    double custoBB = calcularCusto(bb.melhorPlacas);
                    System.out.printf("| %s | %d | B&B | R$ %.2f | %.3f | %d |\n",
                            arquivo, n, custoBB, tempoBB, bb.melhorPlacas.size());
                }

                // Força Bruta (skip if too large)
                if (n <= 8) {
                    ForcaBruta fb = new ForcaBruta(new ArrayList<>(pecas));
                    inicio = System.nanoTime();
                    fb.executar();
                    fim = System.nanoTime();
                    double tempoFB = (fim - inicio) / 1_000_000.0;
                    double custoFB = calcularCusto(fb.melhorPlacas);
                    System.out.printf("| %s | %d | Força Bruta | R$ %.2f | %.3f | %d |\n",
                            arquivo, n, custoFB, tempoFB, fb.melhorPlacas.size());
                }

                System.out.println("|---------|---------|-----------|-----------------|------------|--------|");

            } catch (Exception e) {
                System.out.println("Erro ao processar " + arquivo + ": " + e.getMessage());
            }
        }
    }

    private static double calcularCusto(List<Placa> placas) {
        if (placas == null || placas.isEmpty())
            return Double.MAX_VALUE;
        double custoPlacas = placas.size() * Placa.CUSTO_PLACA;
        double custoLaser = 0.0;
        for (Placa pl : placas) {
            for (Peca p : pl.getPecas()) {
                custoLaser += pl.calcularCustoCorte(p);
            }
        }
        return custoPlacas + custoLaser;
    }

    private static List<Peca> lerArquivo(String caminho) {
        List<Peca> pecas = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(caminho))) {
            int n = sc.nextInt();
            for (int i = 0; i < n; i++) {
                int altura = sc.nextInt();
                int largura = sc.nextInt();
                pecas.add(new Peca(i + 1, altura, largura));
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
        return pecas;
    }
}
