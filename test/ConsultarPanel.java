import javax.swing.*;
import java.awt.*;

public class ConsultarPanel extends JPanel {
    private ListaDeAfazeres listaDeAfazeres;

    public ConsultarPanel(ListaDeAfazeres listaDeAfazeres) {
        this.listaDeAfazeres = listaDeAfazeres;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JButton btnAtualizar = new JButton("Atualizar");

        // Ação do botão atualizar
        btnAtualizar.addActionListener(e -> updateConsultarPanel());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnAtualizar);

        JPanel consultaPanel = new JPanel(new BorderLayout());
        consultaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(consultaPanel);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Atualizar a visualização inicial
        SwingUtilities.invokeLater(this::updateConsultarPanel);
    }

    private void updateConsultarPanel() {
        removeAll(); // Limpar o painel antes de atualizar

        JPanel naoIniciadoPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel emProgressoPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel concluidoPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        if (!listaDeAfazeres.consultarTarefa(naoIniciadoPanel, emProgressoPanel, concluidoPanel)) {
            JOptionPane.showMessageDialog(this, "Erro ao consultar tarefas.");
        }

        JTabbedPane tabbedPane = (JTabbedPane) getParent().getParent(); // Voltamos duas vezes para alcançar o JTabbedPane
        tabbedPane.revalidate(); // Revalidar o JTabbedPane após atualizar

        JPanel consultarPanel = new JPanel(new BorderLayout());
        consultarPanel.add(new JScrollPane(new JPanel()), BorderLayout.CENTER); // Para garantir a rolagem

        add(consultarPanel, BorderLayout.CENTER);
        revalidate(); // Revalidar o painel de consulta após atualizar
        repaint(); // Repaint to make sure everything is rendered properly
    }
}
