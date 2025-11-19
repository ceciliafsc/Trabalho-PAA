package model;

public class Peca {
    private int altura;
    private int largura;
    private int id;

    public Peca(int id, int altura, int largura) {
        this.id = id;
        this.altura = altura;
        this.largura = largura;
    }

    public int getAltura() {return altura;}
    public int getLargura() {return largura;}
    public int getId() {return id;}
    
    @Override
    public String toString(){
        return "Peça" + id + " ("+altura + "x" + largura +")";
    }
}
