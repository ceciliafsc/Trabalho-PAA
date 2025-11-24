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
        /*//froça bruta
        algoritmos_parte1.ForcaBruta solver = new algoritmos_parte1.ForcaBruta(pecas);
        solver.executar();

        //branch and bound
        algoritmos_parte1.BranchAndBound bbSolver = new algoritmos_parte1.BranchAndBound(pecas);
        bbSolver.executar();

        //PARTE 2
        algoritmos_parte2.ForaBruta fbSolver = new algoritmos_parte2.ForaBruta(pecas);
        fbSolver.executar(solver.executar());

        algoritmos_parte2.BnB2 bnb = new algoritmos_parte2.BnB2(pecas);
        bnb.executar(solver.executar());*/

        //branch and bound
        algoritmos_parte1.BranchAndBound bbSolver = new algoritmos_parte1.BranchAndBound(pecas);
        bbSolver.executar();
         algoritmos_parte1.ForcaBruta solver = new algoritmos_parte1.ForcaBruta(pecas);
        solver.executar();


        // aqui é onde cria a interface grafica?
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