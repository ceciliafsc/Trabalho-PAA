package algoritmos_parte1;

import model.*;
import java.util.*;

public class BranchAndBound {

    private List<Peca> pecas; // lista de peças
    private double min = Double.MAX_VALUE; // mínimo global
    public List<Peca> melhorLista = new ArrayList<>(); // melhor lista encontrada
    public List<Placa> melhorPlacas = new ArrayList<>(); // melhor disposição de placas
    public int areaPecasTotal = 0;
    public int areaAlocada = 0;

    // soma constante do custo de corte (perímetros * custo/cm)
    private double custoLaserTotal = 0.0;

    public BranchAndBound(List<Peca> pecas) {
        this.pecas = pecas;
    }

    public void executar() {
        System.out.println("\nExecutando algoritmo de branch and bound");

        // Ordena e calcula área total e custo de corte total
        List<Peca> ordenadas = ordenarPecas(pecas);

        // calcula custo de corte total 
        custoLaserTotal = 0.0;
        for (Peca p : ordenadas) {
            custoLaserTotal += 2.0 * (p.getAltura() + p.getLargura()) * Placa.CUSTO_CORTE_CM;
        }

        // criar a primeira placa e iniciar BnB 
        List<Placa> placasIniciais = new ArrayList<>();
        Placa primeira = new Placa();
        placasIniciais.add(primeira);
        int areaLivreInicial = primeira.getArea();

        long inicio = System.nanoTime(); // INÍCIO DO TEMPO

        // chamar bnb
        bnb(ordenadas, 0, areaPecasTotal, 0, areaLivreInicial, placasIniciais);

        long fim = System.nanoTime(); // FIM DO TEMPO

        double tempoMs = (fim - inicio) / 1_000_000.0; // milissegundos
        double tempoSeg = (fim - inicio) / 1_000_000_000.0; // segundos

        System.out.println("Melhor custo: " + min);
        System.out.printf("Tempo total: %.3f ms (%.4f segundos)\n", tempoMs, tempoSeg);
    }

    public List<Peca> ordenarPecas(List<Peca> pecas) {
        // Ordena por área decrescente
        Collections.sort(pecas, new Comparator<Peca>() {
            @Override
            public int compare(Peca a, Peca b) {
                return Integer.compare(b.getArea(), a.getArea()); // decrescente
            }
        });

        // calcula a área total das peças
        areaPecasTotal = 0;
        for (Peca p : pecas) {
            areaPecasTotal += p.getArea();
        }
        return pecas;
    }

    /**
     * bnb:
     * - pecas: lista ordenada de peças
     * - i: índice da peça atual a alocar
     * - areaPecasTotal: área total de todas as peças (constante)
     * - areaAlocada: área já alocada nas placas do estado atual
     * - areaLivre: área livre total nas placas do estado atual
     * - nPlacas: lista de placas do estado atual (backtracking usa e desfaz modificações)
     */
    private void bnb(List<Peca> pecas, int i, int areaPecasTotal, int areaAlocada, int areaLivre, List<Placa> nPlacas) {

        //adicionou todas as peças
        if (i == pecas.size()) {

            // Custo final = número de placas atuais * custo placa + custo de corte total 
            double custo = nPlacas.size() * Placa.CUSTO_PLACA + custoLaserTotal;

            if (custo < min) { // guarda melhor
                min = custo;
                melhorLista = new ArrayList<>(pecas);
                melhorPlacas = copiarPlacas(nPlacas);
            }
            return;
        }

        // limite por área livre e área necessária
        int areaFaltante = areaPecasTotal - areaAlocada; // área das peças que ainda faltam
        int areaNecessaria = Math.max(0, areaFaltante - areaLivre); // área extra que precisa de placas novas
        int minPlacasAdicionais = (int) Math.ceil(areaNecessaria / (double) Placa.AREA); // mínimo de placas adicionais
        int minPlacasTotal = nPlacas.size() + minPlacasAdicionais; // mínimo de placas total

        double lowerBound = minPlacasTotal * Placa.CUSTO_PLACA + custoLaserTotal; // custo mínimo possível a partir do estado

        // se lower bound mair que mínimo poda
        if (lowerBound >= min) {
            return;
        }

        Peca p = pecas.get(i);

        // se peça é maior que a placa -> erro
        if (p.getAltura() > Placa.ALTURA || p.getLargura() > Placa.LARGURA) {
            throw new IllegalArgumentException(
                    "Peça (id=" + p.getId() + ") maior que a placa: " +
                    p.getAltura() + "x" + p.getLargura());
        }

        // tenta colocar peça nas placas existentes
        for (int j = 0; j < nPlacas.size(); j++) {
            Placa atual = nPlacas.get(j);

            // tenta adicionar na placa j
            boolean adicionou = atual.adicionarPeca(p);

            if (adicionou) {//se conseguiu
                //atualiza áreas
                int novaAreaLivre = areaLivre - p.getArea();
                int novaAreaAlocada = areaAlocada + p.getArea();

                // chama proxima placa
                bnb(pecas, i + 1, areaPecasTotal, novaAreaAlocada, novaAreaLivre, nPlacas);

                //remover peça
                atual.removerPeca(p);
            }
        }

        //abrir mais uma placa (não coube)
        Placa nova = new Placa();
        boolean adicionouNova = nova.adicionarPeca(p);//adiciona peça na nova placa
        if (!adicionouNova) {
            throw new IllegalStateException(
                    "Não foi possível adicionar a peça em uma placa vazia (id=" + p.getId() + ")");
        }

        nPlacas.add(nova);
        //atualiza áreas
        int novaAreaLivre = areaLivre + nova.getArea() - p.getArea();
        int novaAreaAlocada = areaAlocada + p.getArea();

        bnb(pecas, i + 1, areaPecasTotal, novaAreaAlocada, novaAreaLivre, nPlacas);

        //remove placa e peca
        nova.removerPeca(p);
        nPlacas.remove(nPlacas.size() - 1);
    }

    // Cópia das placas
    private List<Placa> copiarPlacas(List<Placa> placas) {
        List<Placa> copia = new ArrayList<>();
        for (Placa p : placas) {
            copia.add(new Placa(p));
        }
        return copia;
    }
}
