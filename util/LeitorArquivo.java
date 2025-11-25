package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Peca;

/**
 * Classe utilitária para leitura de arquivos de entrada.
 */
public class LeitorArquivo {

    /**
     * Lê um arquivo de texto e retorna a lista de peças.
     * O formato esperado é:
     * Linha 1: Número de peças (N)
     * Linhas 2 a N+1: Altura e Largura de cada peça
     * 
     * @param caminhoArquivo Caminho absoluto ou relativo do arquivo.
     * @return Lista de objetos Peca.
     * @throws FileNotFoundException Se o arquivo não for encontrado.
     * @throws Exception             Se houver erro de formatação.
     */
    public static List<Peca> lerArquivo(String caminhoArquivo) throws Exception {
        List<Peca> pecas = new ArrayList<>();
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            throw new FileNotFoundException("Arquivo não encontrado: " + caminhoArquivo);
        }

        try (Scanner scanner = new Scanner(arquivo)) {
            if (!scanner.hasNextInt()) {
                throw new Exception("Arquivo vazio ou formato inválido.");
            }

            int numeroPecas = scanner.nextInt();

            for (int i = 0; i < numeroPecas; i++) {
                if (scanner.hasNextInt()) {
                    int altura = scanner.nextInt();
                    int largura = scanner.nextInt();
                    // ID sequencial começando de 1
                    pecas.add(new Peca(i + 1, altura, largura));
                }
            }
        }

        return pecas;
    }
}
