import javax.swing.*;
import java.awt.*;

public class AdicionarPanel extends JPanel {
    private JTextField tituloField;
    private JTextField descricaoField;
    private JButton btnSalvar;

    public AdicionarPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel adicionarPanel = new JPanel();
        adicionarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        adicionarPanel.setLayout(new GridLayout(0, 2, 10, 10));

        // Adicionando os campos de texto e os rótulos
        JLabel tituloLabel = new JLabel("Título:");
        tituloField = new JTextField();

        JLabel descricaoLabel = new JLabel("Descrição:");
        descricaoField = new JTextField();

        adicionarPanel.add(tituloLabel);
        adicionarPanel.add(tituloField);

        adicionarPanel.add(descricaoLabel);
        adicionarPanel.add(descricaoField);

        // Botão de salvar
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarTarefa());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnSalvar);

        add(adicionarPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void salvarTarefa() {
        String titulo = tituloField.getText();
        String descricao = descricaoField.getText();

        if (titulo.isEmpty() || descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Aqui você pode adicionar a lógica para salvar a tarefa na lista de afazeres
        // Supondo que exista um método salvarTarefa(String titulo, String descricao) em ListaDeAfazeres

        // Exemplo:
        // listaDeAfazeres.salvarTarefa(titulo, descricao);

        JOptionPane.showMessageDialog(this, "Tarefa salva com sucesso!");

        // Limpar os campos após salvar
        tituloField.setText("");
        descricaoField.setText("");
    }
}
