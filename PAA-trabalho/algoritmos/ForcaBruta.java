package algoritmos;

import model.*;
import java.util.*;

public class ForcaBruta {

    private List<Peca> pecas;//lista de peças
    private double min = Double.MAX_VALUE; //mínimo global
    public List<Peca> melhorLista = new ArrayList<>();//melhor lista encontrada
    public List<Placa> melhorPlacas = new ArrayList<>();//mínimo de placas

    public ForcaBruta(List<Peca> pecas) {
        this.pecas = pecas;
    }

    public void executar() {
        System.out.println("\nExecutando algoritmo de força bruta");
        permutar(new ArrayList<>(pecas), 0);
        System.out.println("Melhor custo: " + min);
    }

    //Gerar todas as permutações
    private void permutar(List<Peca> lista, int inicio) {

        if (inicio == lista.size()) {//fim da permutação
            List<Placa> placas = montarPlacas(lista);
            double custo = calcularCustoTotal(placas, lista);

            if (custo < min) {
                min = custo;
                melhorLista = new ArrayList<>(lista);
                melhorPlacas = copiarPlacas(placas);
            }
            return;
        }

        for (int i = inicio; i < lista.size(); i++) {//troca todas as posições
            Collections.swap(lista, inicio, i);
            permutar(lista, inicio + 1);
            Collections.swap(lista, inicio, i);
        }
    }

    //monta como as placas vão ser cortadas a partir da lista de peças
    private List<Placa> montarPlacas(List<Peca> lista) {

        List<Placa> placas = new ArrayList<>();//lista de placas
        placas.add(new Placa());//adiciona a primeira placa

        for (Peca p : lista) {
            Placa atual = placas.get(placas.size() - 1); //pega a última placa adicionada

            if (!atual.cabePeca(p)) { //se a peça não cabe na placa atual
                atual = new Placa(); //cria uma nova placa
                placas.add(atual);
            }

            atual.adicionarPeca(p); //adiciona a peça na placa 
                //adicionar peça ja atualiza o espaço disponível
        }

        return placas;
    }

   
    private double calcularCustoTotal(List<Placa> placas, List<Peca> pecas) {

        double custoPlacas = placas.size() * Placa.CUSTO_PLACA;//1000 para cada placa

        double custoLaser = 0;
        Placa temp = new Placa();

        for (Peca p : pecas)
            custoLaser += temp.calcularCustoCorte(p);//custo corte das peças

        return custoPlacas + custoLaser; //custo total
    }

   //copiar placas
    private List<Placa> copiarPlacas(List<Placa> placas) {
        List<Placa> copia = new ArrayList<>();
        for (Placa p : placas)
            copia.add(p);
        return copia;
    }
}
