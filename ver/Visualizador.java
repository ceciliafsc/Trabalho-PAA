package ver;

import model.Placa;
import model.Peca;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class Visualizador extends JFrame {

    private List<Placa> placas;
    private Map<Integer, Color> cores;

    public Visualizador(List<Placa> placas, List<Peca> porao1, List<Peca> porao2) {
        this.placas = placas;
        this.cores = new HashMap<>();
        setTitle("Visualizador de Corte de Placas e Particionamento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // ABA 1: Placas (Parte 1)
        JPanel painelPlacas = new JPanel();
        painelPlacas.setLayout(new BoxLayout(painelPlacas, BoxLayout.Y_AXIS));
        JScrollPane scrollPlacas = new JScrollPane(painelPlacas);
        scrollPlacas.getVerticalScrollBar().setUnitIncrement(16);

        for (int i = 0; i < placas.size(); i++) {
            Placa p = placas.get(i);
            JPanel painelPlaca = new PainelPlaca(p, i + 1);
            painelPlacas.add(painelPlaca);
            painelPlacas.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        tabbedPane.addTab("Parte 1: Placas", scrollPlacas);

        // ABA 2: Porões (Parte 2)
        JPanel painelPoroes = new JPanel(new GridLayout(1, 2, 10, 0));
        painelPoroes.add(criarPainelPorao("Porão 1", porao1));
        painelPoroes.add(criarPainelPorao("Porão 2", porao2));
        tabbedPane.addTab("Parte 2: Porões", painelPoroes);

        add(tabbedPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
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
            Random rand = new Random(id * 1000); // Seed fixa para mesma cor sempre
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            cores.put(id, new Color(r, g, b));
        }
        return cores.get(id);
    }

    class PainelPlaca extends JPanel {
        private Placa placa;
        private int numero;
        private int escala = 2; // Fator de escala para visualização

        public PainelPlaca(Placa placa, int numero) {
            this.placa = placa;
            this.numero = numero;
            setPreferredSize(new Dimension(Placa.LARGURA * escala + 50, Placa.ALTURA * escala + 50));
            setBorder(BorderFactory.createTitledBorder("Placa " + numero));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int[][] grid = placa.getPlacaGrid();

            // Desenha o fundo da placa
            g.setColor(Color.WHITE);
            g.fillRect(20, 20, Placa.LARGURA * escala, Placa.ALTURA * escala);
            g.setColor(Color.BLACK);
            g.drawRect(20, 20, Placa.LARGURA * escala, Placa.ALTURA * escala);

            // Desenha as peças
            for (int i = 0; i < Placa.ALTURA; i++) {
                for (int j = 0; j < Placa.LARGURA; j++) {
                    int id = grid[i][j];
                    if (id != 0) {
                        g.setColor(getCor(id));
                        g.fillRect(20 + j * escala, 20 + i * escala, escala, escala);
                    }
                }
            }
        }
    }
}
