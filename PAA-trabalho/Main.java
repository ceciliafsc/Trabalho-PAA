import model.*;
import algoritmos.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args){
        String arquivo = "entrada.txt";
        List<Peca> pecas = lerArquivo(arquivo);

        System.out.println("Peças lidas:");
        for (Peca p : pecas) {
            System.out.println(p);
        }
        //se for rodr o froça bruta
        ForcaBruta solver = new ForcaBruta(pecas);
        solver.executar();

        //aqui é onde cria a interface grafica?
    }

    public static List<Peca> lerArquivo(String caminho) {
        List <Peca> pecas = new ArrayList<>();
        try(Scanner sc = new Scanner(new File (caminho))) {
            int n = sc.nextInt();
            for(int i =0; i<n; i++) {
                int altura = sc.nextInt();
                int largura = sc.nextInt();
                pecas.add(new Peca (i+1, altura, largura));
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo:" + e.getMessage());
        }
        return pecas;
    }
}