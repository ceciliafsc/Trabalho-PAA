package model;

import java.util.ArrayList;
import java.util.List;

public class Placa {

    public static final double CUSTO_PLACA = 1000.0;
    public static final double CUSTO_CORTE_CM = 0.01;
    public static final int MARGEM = 10;
    public static final int ALTURA = 300 - MARGEM;
    public static final int LARGURA = 300 - MARGEM;

    private List<Peca> pecas;
    private int[][] placaGrid;// Representação da placa

    public Placa() {
        this.pecas = new ArrayList<>();
        this.placaGrid = new int[ALTURA][LARGURA];

        // Inicia toda a placa como vazia
        for (int i = 0; i < ALTURA; i++) {
            for (int j = 0; j < LARGURA; j++) {
                placaGrid[i][j] = 0;
            }
        }
    }
    }

    // retorna true se a peça cabe na placa
    public boolean cabePeca(Peca peca) {
        if (peca.getAltura() > this.alturaDisponivel || peca.getLargura() > this.larguraDisponivel) {
            return false;
        }
        return true;
    }

    public boolean cabePeca(Peca peca, int linha, int coluna){
        if(peca.getAltura() > ALTURA || peca.getLargura() > LARGURA){//maior que a placa
            return false;
        }
    }

    //(perímetro * custo por cm)
    public double calcularCustoCorte(Peca peca) {
        return 2.0 * (peca.getAltura() + peca.getLargura()) * CUSTO_CORTE_CM;
    }


    // Adiciona peça na placa e atualiza o espaço disponível
    public void adicionarPeca(Peca peca) {
        pecas.add(peca);
        this.alturaDisponivel -= (peca.getAltura());
        this.larguraDisponivel -= (peca.getLargura());
    }

    public void novaPlaca() {
        this.alturaDisponivel = ALTURA;
        this.larguraDisponivel = LARGURA;
        this.pecas.clear();
    }

    public List<Peca> getPecas() {
        return pecas;
    }
}
