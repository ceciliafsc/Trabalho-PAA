package algoritmos_parte1;

import java.util.ArrayList;
import java.util.List;
import model.Peca;
import model.Placa;

/**
 * Algoritmo Branch and Bound para o problema de corte.
 */
public class BranchAndBound {
    private List<Peca> pecas;
    private double custoMinimo = Double.MAX_VALUE;
    private List<Placa> melhorPlacas = new ArrayList<>();
    private double custoCorteTotalFixo = 0.0;

    public BranchAndBound(List<Peca> pecas) {
        this.pecas = new ArrayList<>(pecas);
    }

    public void executar() {
        // Ordena para tentar encontrar boas soluções mais rápido
        pecas.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));

        // Pré-calcula o custo de corte fixo (independe da posição)
        custoCorteTotalFixo = 0.0;
        int areaTotalPecas = 0;
        for (Peca p : pecas) {
            custoCorteTotalFixo += 2.0 * (p.getAltura() + p.getLargura()) * Placa.CUSTO_CORTE_CM;
            areaTotalPecas += p.getArea();
        }

        List<Placa> placasIniciais = new ArrayList<>();
        placasIniciais.add(new Placa());

        bnb(0, areaTotalPecas, 0, placasIniciais);
    }

    private void bnb(int indicePeca, int areaTotalPecas, int areaJaAlocada, List<Placa> placasAtuais) {
        // Caso base: todas as peças alocadas
        if (indicePeca == pecas.size()) {
            double custoAtual = placasAtuais.size() * Placa.CUSTO_PLACA + custoCorteTotalFixo;
            if (custoAtual < custoMinimo) {
                custoMinimo = custoAtual;
                melhorPlacas = copiarPlacas(placasAtuais);
            }
            return;
        }

        // Poda: se o custo atual já é maior que o melhor encontrado, desiste
        double custoAtualPlacas = placasAtuais.size() * Placa.CUSTO_PLACA + custoCorteTotalFixo;
        if (custoAtualPlacas >= custoMinimo) {
            return;
        }

        Peca pecaAtual = pecas.get(indicePeca);

        // Tenta colocar nas placas existentes
        for (int i = 0; i < placasAtuais.size(); i++) {
            Placa placa = placasAtuais.get(i);
            Placa copiaPlaca = new Placa(placa); // Copia para não afetar outros ramos

            if (copiaPlaca.adicionarPeca(pecaAtual)) {
                List<Placa> novasPlacas = new ArrayList<>(placasAtuais);
                novasPlacas.set(i, copiaPlaca);
                bnb(indicePeca + 1, areaTotalPecas, areaJaAlocada + pecaAtual.getArea(), novasPlacas);
            }
        }

        // Tenta colocar em uma nova placa
        Placa novaPlaca = new Placa();
        if (novaPlaca.adicionarPeca(pecaAtual)) {
            List<Placa> novasPlacas = new ArrayList<>(placasAtuais);
            novasPlacas.add(novaPlaca);
            bnb(indicePeca + 1, areaTotalPecas, areaJaAlocada + pecaAtual.getArea(), novasPlacas);
        }
    }

    private List<Placa> copiarPlacas(List<Placa> origem) {
        List<Placa> destino = new ArrayList<>();
        for (Placa p : origem) {
            destino.add(new Placa(p));
        }
        return destino;
    }

    public List<Placa> getMelhorPlacas() {
        return melhorPlacas;
    }
}
