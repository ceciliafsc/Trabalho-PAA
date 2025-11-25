package model;

/**
 * Representa uma peça retangular que precisa ser cortada de uma placa.
 */
public class Peca {
    private int id;
    private int altura;
    private int largura;
    private int peso; // Usado para heurísticas de ordenação

    public Peca(int id, int altura, int largura) {
        this.id = id;
        this.altura = altura;
        this.largura = largura;
        this.peso = altura * largura; // Peso é a área
    }

    public int getId() {
        return id;
    }

    public int getAltura() {
        return altura;
    }

    public int getLargura() {
        return largura;
    }

    public int getArea() {
        return altura * largura;
    }

    public int getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return String.format("Peça %d (%dx%d) - Área: %d", id, altura, largura, getArea());
    }
}
