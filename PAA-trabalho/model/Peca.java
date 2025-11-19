package model;

public class Peca {
    private int altura;
    private int largura;
    private int id;
    private int peso;

    public Peca(int id, int altura, int largura) {
        this.id = id;
        this.altura = altura;
        this.largura = largura;
        this.peso = altura * largura;
    }

    public int getAltura() {return altura;}
    public int getLargura() {return largura;}
    public int getId() {return id;}
    public int getPeso() {return peso;}
    
    @Override
    public String toString(){
        return "Peça" + id + " ("+altura + "x" + largura +") - Peso: " + peso;
    }
}
