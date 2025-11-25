import algoritmos_parte1.BranchAndBound;
import algoritmos_parte1.ForcaBruta;
import algoritmos_parte1.Heuristica;
import algoritmos_parte2.BnB2;
import algoritmos_parte2.ForaBruta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import model.CalculadoraCustos;
import model.Peca;
import model.Placa;
import util.LeitorArquivo;
import ver.Visualizador;

/**
 * Sistema de Otimização - PAA
 * Ponto de entrada da aplicação.
 */
public class Main extends JFrame {

    // --- COMPONENTES DA INTERFACE ---
    private JComboBox<String> comboArquivo;
    private JComboBox<String> comboAlgoritmoCorte;
    private JComboBox<String> comboAlgoritmoDistribuicao;
    private JTextArea areaLog;
    private Visualizador visualizador;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main();
        });
    }

    public Main() {
        configurarJanela();
        inicializarComponentes();
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Sistema de Otimização - PAA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Ignora
        }
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // --- PAINEL LATERAL ---
        JPanel painelLateral = new JPanel();
        painelLateral.setLayout(new BoxLayout(painelLateral, BoxLayout.Y_AXIS));
        painelLateral.setBorder(new EmptyBorder(10, 10, 10, 10));
        painelLateral.setPreferredSize(new Dimension(280, getHeight()));
        painelLateral.setBackground(new Color(245, 245, 245));

        JLabel lblConfig = new JLabel("Configurações");
        lblConfig.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblConfig.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelLateral.add(lblConfig);
        painelLateral.add(Box.createRigidArea(new Dimension(0, 20)));

        adicionarLabel(painelLateral, "Arquivo de Entrada:");
        comboArquivo = new JComboBox<>(listarArquivosEntrada());
        adicionarComponente(painelLateral, comboArquivo);

        adicionarLabel(painelLateral, "Algoritmo de Corte (Parte 1):");
        String[] algsCorte = { "Heurística (First Fit)", "Branch & Bound", "Força Bruta" };
        comboAlgoritmoCorte = new JComboBox<>(algsCorte);
        adicionarComponente(painelLateral, comboAlgoritmoCorte);

        adicionarLabel(painelLateral, "Algoritmo de Distribuição (Parte 2):");
        String[] algsDist = { "Heurística (LPT)", "Branch & Bound", "Força Bruta" };
        comboAlgoritmoDistribuicao = new JComboBox<>(algsDist);
        adicionarComponente(painelLateral, comboAlgoritmoDistribuicao);

        painelLateral.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton btnExecutar = new JButton("Executar Simulação");
        estilizarBotao(btnExecutar, new Color(70, 130, 180));
        btnExecutar.addActionListener(e -> executarSimulacao());
        painelLateral.add(btnExecutar);

        painelLateral.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnComparar = new JButton("Comparar Todos");
        estilizarBotao(btnComparar, new Color(100, 100, 100));
        btnComparar.addActionListener(e -> compararTodos());
        painelLateral.add(btnComparar);

        add(painelLateral, BorderLayout.WEST);

        // --- CENTRO ---
        visualizador = new Visualizador();
        add(visualizador, BorderLayout.CENTER);

        // --- RODAPÉ ---
        areaLog = new JTextArea(8, 50);
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Log de Execução"));
        add(scrollLog, BorderLayout.SOUTH);
    }

    private void adicionarLabel(JPanel p, String text) {
        JLabel l = new JLabel(text);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void adicionarComponente(JPanel p, JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(260, 30));
        p.add(c);
        p.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private void estilizarBotao(JButton btn, Color cor) {
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(260, 40));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private String[] listarArquivosEntrada() {
        File dir = new File("entradas");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            return new String[] { "Nenhum arquivo encontrado" };
        }
        return Arrays.stream(files).map(File::getName).toArray(String[]::new);
    }

    private void log(String msg) {
        SwingUtilities.invokeLater(() -> {
            areaLog.append(msg + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }

    private void executarSimulacao() {
        areaLog.setText("");
        new Thread(() -> {
            try {
                String arquivo = (String) comboArquivo.getSelectedItem();
                String algCorte = (String) comboAlgoritmoCorte.getSelectedItem();
                String algDist = (String) comboAlgoritmoDistribuicao.getSelectedItem();

                log("=== Iniciando Simulação ===");
                log("Arquivo: " + arquivo);

                List<Peca> pecas = LeitorArquivo.lerArquivo("entradas/" + arquivo);
                log("Peças lidas: " + pecas.size());

                // PARTE 1
                log("\n>> Executando Corte: " + algCorte);
                List<Placa> resultadoPlacas = null;
                long inicio = System.nanoTime();

                if (algCorte.contains("Heurística")) {
                    Heuristica h = new Heuristica(pecas);
                    h.executar();
                    resultadoPlacas = h.getMelhorPlacas();
                } else if (algCorte.contains("Branch")) {
                    BranchAndBound bb = new BranchAndBound(pecas);
                    bb.executar();
                    resultadoPlacas = bb.getMelhorPlacas();
                } else {
                    ForcaBruta fb = new ForcaBruta(pecas);
                    fb.executar();
                    resultadoPlacas = fb.getMelhorPlacas();
                }
                long fim = System.nanoTime();
                log(CalculadoraCustos.getDetalhamento(resultadoPlacas));
                log(String.format("Tempo Corte: %.3f ms", (fim - inicio) / 1_000_000.0));

                // PARTE 2
                log("\n>> Executando Distribuição: " + algDist);
                List<Peca> pecasParaParticao = new ArrayList<>();
                for (Placa p : resultadoPlacas) {
                    pecasParaParticao.addAll(p.getPecas());
                }

                List<Peca> porao1 = null;
                List<Peca> porao2 = null;
                int diferenca = 0;

                inicio = System.nanoTime();
                if (algDist.contains("Heurística")) {
                    algoritmos_parte2.Heuristica h2 = new algoritmos_parte2.Heuristica();
                    h2.executar(pecasParaParticao);
                    porao1 = h2.getMelhorPorao1();
                    porao2 = h2.getMelhorPorao2();
                    diferenca = h2.getDiferencaMinima();
                } else if (algDist.contains("Branch")) {
                    BnB2 bb2 = new BnB2();
                    bb2.executar(pecasParaParticao);
                    porao1 = bb2.getMelhorPorao1();
                    porao2 = bb2.getMelhorPorao2();
                    diferenca = bb2.getDiferencaMinima();
                } else {
                    ForaBruta fb2 = new ForaBruta();
                    fb2.executar(pecasParaParticao);
                    porao1 = fb2.getMelhorPorao1();
                    porao2 = fb2.getMelhorPorao2();
                    diferenca = fb2.getDiferencaMinima();
                }
                fim = System.nanoTime();

                log("Diferença de Peso: " + diferenca);
                log(String.format("Tempo Distribuição: %.3f ms", (fim - inicio) / 1_000_000.0));
                log("\nSimulação concluída!");

                final List<Peca> p1 = porao1;
                final List<Peca> p2 = porao2;
                final List<Placa> rPlacas = resultadoPlacas;

                SwingUtilities.invokeLater(() -> {
                    visualizador.atualizarResultados(rPlacas, p1, p2);
                });

            } catch (Exception ex) {
                log("ERRO: " + ex.getMessage());
                ex.printStackTrace();
            }
        }).start();
    }

    private void compararTodos() {
        areaLog.setText("");
        new Thread(() -> {
            try {
                String arquivo = (String) comboArquivo.getSelectedItem();
                log("\n=== RELATÓRIO COMPARATIVO: " + arquivo + " ===");
                List<Peca> pecas = LeitorArquivo.lerArquivo("entradas/" + arquivo);

                log(String.format("%-20s | %-12s | %-12s", "Algoritmo", "Custo (R$)", "Tempo (ms)"));
                log("-------------------------------------------------------");

                long ini = System.nanoTime();
                Heuristica h = new Heuristica(new ArrayList<>(pecas));
                h.executar();
                long fim = System.nanoTime();
                double custoH = CalculadoraCustos.calcularCustoTotal(h.getMelhorPlacas());
                log(String.format("%-20s | %12.2f | %12.3f", "Heurística", custoH, (fim - ini) / 1e6));

                if (pecas.size() <= 15) {
                    ini = System.nanoTime();
                    BranchAndBound bb = new BranchAndBound(new ArrayList<>(pecas));
                    bb.executar();
                    fim = System.nanoTime();
                    double custoBB = CalculadoraCustos.calcularCustoTotal(bb.getMelhorPlacas());
                    log(String.format("%-20s | %12.2f | %12.3f", "Branch & Bound", custoBB, (fim - ini) / 1e6));
                } else {
                    log(String.format("%-20s | %12s | %12s", "Branch & Bound", "N/A (>15)", "-"));
                }

                if (pecas.size() <= 8) {
                    ini = System.nanoTime();
                    ForcaBruta fb = new ForcaBruta(new ArrayList<>(pecas));
                    fb.executar();
                    fim = System.nanoTime();
                    double custoFB = CalculadoraCustos.calcularCustoTotal(fb.getMelhorPlacas());
                    log(String.format("%-20s | %12.2f | %12.3f", "Força Bruta", custoFB, (fim - ini) / 1e6));
                } else {
                    log(String.format("%-20s | %12s | %12s", "Força Bruta", "N/A (>8)", "-"));
                }
                log("-------------------------------------------------------");

            } catch (Exception ex) {
                log("Erro: " + ex.getMessage());
            }
        }).start();
    }
}
