package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa a placa de material onde as peças serão cortadas.
 * Gerencia o posicionamento das peças e valida se elas cabem.
 */
public class Placa {

    // Constantes do problema
    public static final double CUSTO_PLACA = 1000.0;
    public static final double CUSTO_CORTE_CM = 0.01;
    public static final int MARGEM = 10;
    public static final int ALTURA_TOTAL = 300;
    public static final int LARGURA_TOTAL = 300;

    // Área útil descontando a margem
    public static final int ALTURA_UTIL = ALTURA_TOTAL - MARGEM;
    public static final int LARGURA_UTIL = LARGURA_TOTAL - MARGEM;

    private List<Peca> pecasAlocadas;
    private int[][] grid; // Matriz que representa a placa (0 = vazio, >0 = id da peça)

    public Placa() {
        this.pecasAlocadas = new ArrayList<>();
        this.grid = new int[ALTURA_UTIL][LARGURA_UTIL];
        limpar();
    }

    // Construtor de cópia
    public Placa(Placa outra) {
        Objects.requireNonNull(outra, "Placa para copiar não pode ser nula");
        this.pecasAlocadas = new ArrayList<>(outra.pecasAlocadas);
        this.grid = new int[ALTURA_UTIL][LARGURA_UTIL];

        // Copia o grid
        for (int i = 0; i < ALTURA_UTIL; i++) {
            System.arraycopy(outra.grid[i], 0, this.grid[i], 0, LARGURA_UTIL);
        }
    }

    /**
     * Limpa a placa, removendo todas as peças.
     */
    public void limpar() {
        this.pecasAlocadas.clear();
        for (int i = 0; i < ALTURA_UTIL; i++) {
            for (int j = 0; j < LARGURA_UTIL; j++) {
                grid[i][j] = 0;
            }
        }
    }

    /**
     * Tenta adicionar uma peça na placa na primeira posição disponível.
     * 
     * @param peca A peça a ser adicionada.
     * @return true se a peça foi adicionada com sucesso, false se não couber.
     */
    public boolean adicionarPeca(Peca peca) {
        // Verificação rápida de limites
        if (peca.getAltura() > ALTURA_UTIL || peca.getLargura() > LARGURA_UTIL) {
            return false;
        }

        // Percorre a matriz procurando um espaço livre (Estratégia First Fit na matriz)
        for (int linha = 0; linha <= ALTURA_UTIL - peca.getAltura(); linha++) {
            for (int coluna = 0; coluna <= LARGURA_UTIL - peca.getLargura(); coluna++) {
                if (podePosicionar(peca, linha, coluna)) {
                    posicionar(peca, linha, coluna);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verifica se a peça cabe na posição (linha, coluna).
     */
    private boolean podePosicionar(Peca peca, int linha, int coluna) {
        // Verifica limites
        if (linha + peca.getAltura() > ALTURA_UTIL || coluna + peca.getLargura() > LARGURA_UTIL) {
            return false;
        }

        // Verifica colisão com outras peças
        for (int i = 0; i < peca.getAltura(); i++) {
            for (int j = 0; j < peca.getLargura(); j++) {
                if (grid[linha + i][coluna + j] != 0) {
                    return false; // Espaço ocupado
                }
            }
        }
        return true;
    }

    /**
     * Posiciona a peça na matriz e adiciona à lista.
     */
    private void posicionar(Peca peca, int linha, int coluna) {
        int id = (peca.getId() == 0) ? 1 : peca.getId(); // Garante que não seja 0 (vazio)

        for (int i = 0; i < peca.getAltura(); i++) {
            for (int j = 0; j < peca.getLargura(); j++) {
                grid[linha + i][coluna + j] = id;
            }
        }
        pecasAlocadas.add(peca);
    }

    public List<Peca> getPecas() {
        return pecasAlocadas;
    }

    public int[][] getGrid() {
        return grid;
    }

    public double calcularCustoCorte(Peca peca) {
        // O custo é o perímetro da peça * custo por cm
        // Perímetro = 2 * (altura + largura)
        return 2.0 * (peca.getAltura() + peca.getLargura()) * CUSTO_CORTE_CM;
    }
}
