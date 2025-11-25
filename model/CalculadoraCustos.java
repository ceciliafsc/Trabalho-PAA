package model;

import java.util.List;

/**
 * Classe utilitária para calcular os custos do projeto.
 */
public class CalculadoraCustos {

    /**
     * Calcula o custo total de uma lista de placas usadas.
     * Custo Total = (Custo das Placas) + (Custo dos Cortes)
     */
    public static double calcularCustoTotal(List<Placa> placas) {
        if (placas == null || placas.isEmpty()) {
            return 0.0;
        }

        double custoPlacas = placas.size() * Placa.CUSTO_PLACA;
        double custoCortes = 0.0;

        for (Placa p : placas) {
            for (Peca peca : p.getPecas()) {
                custoCortes += p.calcularCustoCorte(peca);
            }
        }

        return custoPlacas + custoCortes;
    }

    /**
     * Retorna uma string formatada com o detalhamento dos custos.
     */
    public static String getDetalhamento(List<Placa> placas) {
        if (placas == null || placas.isEmpty()) {
            return "Nenhuma placa utilizada.";
        }

        double custoPlacas = placas.size() * Placa.CUSTO_PLACA;
        double custoCortes = 0.0;

        for (Placa p : placas) {
            for (Peca peca : p.getPecas()) {
                custoCortes += p.calcularCustoCorte(peca);
            }
        }

        return String.format(
                "Placas (%d): R$ %.2f | Cortes: R$ %.2f | TOTAL: R$ %.2f",
                placas.size(), custoPlacas, custoCortes, custoPlacas + custoCortes);
    }
}
