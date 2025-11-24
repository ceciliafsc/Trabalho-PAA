import model.*;
import java.util.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        String arquivo = "entrada.txt";
        List<Peca> pecas = lerArquivo(arquivo);

        System.out.println("Peças lidas:");
        for (Peca p : pecas) {
            System.out.println(p);
        }
        /*
         * //froça bruta
         * algoritmos_parte1.ForcaBruta solver = new
         * algoritmos_parte1.ForcaBruta(pecas);
         * solver.executar();
         * 
         * //branch and bound
         * algoritmos_parte1.BranchAndBound bbSolver = new
         * algoritmos_parte1.BranchAndBound(pecas);
         * bbSolver.executar();
         * 
         * //PARTE 2
         * algoritmos_parte2.ForaBruta fbSolver = new
         * algoritmos_parte2.ForaBruta(pecas);
         * fbSolver.executar(solver.executar());
         * 
         * algoritmos_parte2.BnB2 bnb = new algoritmos_parte2.BnB2(pecas);
         * bnb.executar(solver.executar());
         */

        // branch and bound
        // Heurística (First Fit Decreasing)
        algoritmos_parte1.Heuristica heuristicaSolver = new algoritmos_parte1.Heuristica(pecas);
        heuristicaSolver.executar();

        // Branch and Bound
        algoritmos_parte1.BranchAndBound bbSolver = new algoritmos_parte1.BranchAndBound(pecas);
        bbSolver.executar();

        // Força Bruta
        algoritmos_parte1.ForcaBruta fbSolver = new algoritmos_parte1.ForcaBruta(pecas);
        fbSolver.executar();

        // Selecionar o melhor resultado para visualizar
        List<model.Placa> melhorResultado = heuristicaSolver.getMelhorPlacas();
        double menorCusto = calcularCusto(melhorResultado);
        String algoritmo = "Heurística";

        // Compara com BnB
        double custoBnB = calcularCusto(bbSolver.melhorPlacas);
        if (custoBnB < menorCusto && !bbSolver.melhorPlacas.isEmpty()) {
            melhorResultado = bbSolver.melhorPlacas;
            menorCusto = custoBnB;
            algoritmo = "Branch and Bound";
        }

        // Compara com Força Bruta
        double custoFB = calcularCusto(fbSolver.melhorPlacas);
        if (custoFB < menorCusto && !fbSolver.melhorPlacas.isEmpty()) {
            melhorResultado = fbSolver.melhorPlacas;
            menorCusto = custoFB;
            algoritmo = "Força Bruta";
        }

        System.out.println("\nVisualizando resultado do algoritmo: " + algoritmo);

        // PARTE 2 - Particionamento
        System.out.println("\n--- INICIANDO PARTE 2 (Particionamento) ---");

        // Força Bruta Part 2
        algoritmos_parte2.ForaBruta fb2Solver = new algoritmos_parte2.ForaBruta(pecas);
        fb2Solver.executar(melhorResultado.stream().flatMap(p -> p.getPecas().stream()).toList());

        // Branch and Bound Part 2
        algoritmos_parte2.BnB2 bnb2Solver = new algoritmos_parte2.BnB2(pecas);
        bnb2Solver.executar(melhorResultado.stream().flatMap(p -> p.getPecas().stream()).toList());

        // Interface Gráfica
        ver.Visualizador visualizador = new ver.Visualizador(melhorResultado, bnb2Solver.melhorPorao1,
                bnb2Solver.melhorPorao2);
    }

    private static double calcularCusto(List<model.Placa> placas) {
        if (placas == null || placas.isEmpty())
            return Double.MAX_VALUE;
        double custoPlacas = placas.size() * model.Placa.CUSTO_PLACA;
        double custoLaser = 0.0;
        for (model.Placa pl : placas) {
            for (model.Peca p : pl.getPecas()) {
                custoLaser += pl.calcularCustoCorte(p);
            }
        }
        return custoPlacas + custoLaser;
    }

    public static List<Peca> lerArquivo(String caminho) {
        List<Peca> pecas = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(caminho))) {
            int n = sc.nextInt();
            for (int i = 0; i < n; i++) {
                int altura = sc.nextInt();
                int largura = sc.nextInt();
                pecas.add(new Peca(i + 1, altura, largura));
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo:" + e.getMessage());
        }
        return pecas;
    }
}