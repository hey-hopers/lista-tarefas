import javax.swing.*;
import java.awt.*;

public class Main {
    private JFrame frame;
    private JTabbedPane tabbedPane;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Sistema de Tarefas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Configurar o JTabbedPane
        tabbedPane = new JTabbedPane();

        // Adicionar o MenuPanel
        MenuPanel menuPanel = new MenuPanel(this);
        frame.add(menuPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void adicionarAbaConsultar() {
        ListaDeAfazeres listaDeAfazeres = new ListaDeAfazeres();  // Certifique-se de inicializar corretamente sua lista
        ConsultarPanel consultarPanel = new ConsultarPanel(listaDeAfazeres);
        tabbedPane.addTab("Consultar", consultarPanel);
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    public void adicionarAbaAdicionar() {
        AdicionarPanel adicionarPanel = new AdicionarPanel();
        tabbedPane.addTab("Adicionar", adicionarPanel);
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }
}
