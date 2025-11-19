package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Placa {

    public static final double CUSTO_PLACA = 1000.0;
    public static final double CUSTO_CORTE_CM = 0.01;
    public static final int MARGEM = 10;
    public static final int ALTURA = 300 - MARGEM;
    public static final int LARGURA = 300 - MARGEM;

    private List<Peca> pecas;
    private int[][] placaGrid; // Representação da placa

    public Placa() {
        this.pecas = new ArrayList<>();
        this.placaGrid = new int[ALTURA][LARGURA];

        // Inicia toda a placa como vazia (0)
        for (int i = 0; i < ALTURA; i++) {
            for (int j = 0; j < LARGURA; j++) {
                placaGrid[i][j] = 0;
            }
        }
    }

    public Placa(Placa other) {
        Objects.requireNonNull(other, "Placa para copiar não pode ser null");
        this.pecas = new ArrayList<>(other.pecas);

        this.placaGrid = new int[ALTURA][LARGURA];
        for (int i = 0; i < ALTURA; i++) {
            System.arraycopy(other.placaGrid[i], 0, this.placaGrid[i], 0, LARGURA);
        }
    }

    // -------------------------
    // VERIFICA SE CABE NA MATRIZ (em algum lugar)
    // -------------------------
    public boolean cabePeca(Peca peca) {
        // tenta encontrar qualquer posição livre onde caiba
        return cabeEmAlgumLugar(peca);
    }

    private boolean cabeEmAlgumLugar(Peca peca) {
        // PEÇA MAIOR QUE Placa
        if (peca.getAltura() > ALTURA || peca.getLargura() > LARGURA) {
            return false;
        }

        for (int i = 0; i <= ALTURA - peca.getAltura(); i++) {
            for (int j = 0; j <= LARGURA - peca.getLargura(); j++) {
                if (cabePeca(peca, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean cabePeca(Peca peca, int linha, int coluna) {

        if (linha + peca.getAltura() > ALTURA || coluna + peca.getLargura() > LARGURA) {
            return false; // passa dos limites da placa
        }

        // Verifica se todos os espaços necessários estão livres
        for (int i = 0; i < peca.getAltura(); i++) {
            for (int j = 0; j < peca.getLargura(); j++) {
                if (placaGrid[linha + i][coluna + j] != 0) { // DÚVIDA -------------
                    return false;
                }
            }
        }

        return true;
    }

    // (perímetro * custo por cm)
    public double calcularCustoCorte(Peca peca) {
        return 2.0 * (peca.getAltura() + peca.getLargura()) * CUSTO_CORTE_CM;
    }

    public boolean adicionarPeca(Peca peca) {

        if (peca.getAltura() > ALTURA || peca.getLargura() > LARGURA) {
            return false; // não cabe nem em placa vazia
        }

        // percorre a matriz procurando a 1ª posição possível
        for (int linha = 0; linha <= ALTURA - peca.getAltura(); linha++) {
            for (int coluna = 0; coluna <= LARGURA - peca.getLargura(); coluna++) {

                if (cabePeca(peca, linha, coluna)) {

                    // Preenche a matriz (marca com id da peça)
                    int id = peca.getId() == 0 ? 1 : peca.getId();// evita marcar com 0 (vazio)
                    for (int i = 0; i < peca.getAltura(); i++) {
                        for (int j = 0; j < peca.getLargura(); j++) {
                            placaGrid[linha + i][coluna + j] = id;
                        }
                    }

                    pecas.add(peca);
                    return true;
                }
            }
        }
        return false; // não coube
    }

    public void novaPlaca() {
        this.pecas.clear();

        for (int i = 0; i < ALTURA; i++) {
            for (int j = 0; j < LARGURA; j++) {
                placaGrid[i][j] = 0;// marca como vazio
            }
        }
    }

    public List<Peca> getPecas() {
        return pecas;
    }

    // mostrar placa preenchida
    public int[][] getPlacaGrid() {
        return placaGrid;
    }
}
