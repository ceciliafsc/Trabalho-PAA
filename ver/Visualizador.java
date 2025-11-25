package ver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import model.Peca;
import model.Placa;

/**
 * Componente responsável pela visualização gráfica das placas e porões.
 */
public class Visualizador extends JPanel {
    private Map<Integer, Color> cores;
    private JTabbedPane abas;

    public Visualizador() {
        this.cores = new HashMap<>();
        setLayout(new BorderLayout());
        add(new JLabel("Selecione as opções ao lado e clique em 'Executar Simulação'.", SwingConstants.CENTER),
                BorderLayout.CENTER);
    }

    public void atualizarResultados(List<Placa> placas, List<Peca> porao1, List<Peca> porao2) {
        removeAll();
        abas = new JTabbedPane();

        // --- ABA 1: PLACAS (CORTE) ---
        JPanel painelPlacas = new JPanel();
        painelPlacas.setLayout(new BoxLayout(painelPlacas, BoxLayout.Y_AXIS));
        JScrollPane scrollPlacas = new JScrollPane(painelPlacas);
        scrollPlacas.getVerticalScrollBar().setUnitIncrement(16);

        if (placas != null) {
            for (int i = 0; i < placas.size(); i++) {
                Placa p = placas.get(i);
                JPanel painelPlaca = new PainelPlaca(p, i + 1);
                painelPlacas.add(painelPlaca);
                painelPlacas.add(Box.createRigidArea(new Dimension(0, 20)));
            }
        }
        abas.addTab("Parte 1: Corte (Placas)", scrollPlacas);

        // --- ABA 2: PORÕES (DISTRIBUIÇÃO) ---
        JPanel painelPoroes = new JPanel(new GridLayout(1, 2, 10, 0));
        if (porao1 != null && porao2 != null) {
            painelPoroes.add(criarPainelPorao("Porão 1", porao1));
            painelPoroes.add(criarPainelPorao("Porão 2", porao2));
        }
        abas.addTab("Parte 2: Distribuição (Porões)", painelPoroes);

        add(abas, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel criarPainelPorao(String titulo, List<Peca> pecas) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder(titulo));

        DefaultListModel<String> model = new DefaultListModel<>();
        int pesoTotal = 0;
        for (Peca p : pecas) {
            model.addElement(p.toString());
            pesoTotal += p.getPeso();
        }

        JList<String> lista = new JList<>(model);
        painel.add(new JScrollPane(lista), BorderLayout.CENTER);
        painel.add(new JLabel("Peso Total: " + pesoTotal, SwingConstants.CENTER), BorderLayout.SOUTH);

        return painel;
    }

    private Color getCor(int id) {
        if (!cores.containsKey(id)) {
            Random rand = new Random(id * 1000L);
            // Cores pastéis e agradáveis
            float r = (rand.nextFloat() / 2f) + 0.5f;
            float g = (rand.nextFloat() / 2f) + 0.5f;
            float b = (rand.nextFloat() / 2f) + 0.5f;
            cores.put(id, new Color(r, g, b));
        }
        return cores.get(id);
    }

    // --- CLASSE INTERNA PARA DESENHAR UMA PLACA ---
    class PainelPlaca extends JPanel {
        private Placa placa;
        private int numero;
        private final int ESCALA = 2; // Fator de escala para visualização

        public PainelPlaca(Placa placa, int numero) {
            this.placa = placa;
            this.numero = numero;
            setPreferredSize(new Dimension(Placa.LARGURA_TOTAL * ESCALA + 40, Placa.ALTURA_TOTAL * ESCALA + 40));
            setBorder(BorderFactory.createTitledBorder("Placa " + numero));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Desenha o fundo da placa
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(20, 20, Placa.LARGURA_TOTAL * ESCALA, Placa.ALTURA_TOTAL * ESCALA);

            // Desenha a área útil
            g.setColor(Color.WHITE);
            g.fillRect(20, 20, Placa.LARGURA_UTIL * ESCALA, Placa.ALTURA_UTIL * ESCALA);

            // Desenha as peças
            int[][] grid = placa.getGrid();
            for (int i = 0; i < Placa.ALTURA_UTIL; i++) {
                for (int j = 0; j < Placa.LARGURA_UTIL; j++) {
                    int id = grid[i][j];
                    if (id != 0) {
                        g.setColor(getCor(id));
                        g.fillRect(20 + j * ESCALA, 20 + i * ESCALA, ESCALA, ESCALA);
                    }
                }
            }

            // Desenha bordas das peças (opcional, para melhor visualização)
            g.setColor(Color.BLACK);
            g.drawRect(20, 20, Placa.LARGURA_UTIL * ESCALA, Placa.ALTURA_UTIL * ESCALA);
        }
    }
}
