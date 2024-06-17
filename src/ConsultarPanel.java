import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ConsultarPanel extends JPanel {
    private ListaDeAfazeres listaDeAfazeres;

    public ConsultarPanel(ListaDeAfazeres listaDeAfazeres) {
        this.listaDeAfazeres = listaDeAfazeres;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Criar os painéis para as categorias
        JPanel naoIniciadoPanel = createCategoryPanel("Não iniciado");
        JPanel emProgressoPanel = createCategoryPanel("Em progresso");
        JPanel concluidoPanel = createCategoryPanel("Concluído");

        // Consultar as tarefas e preencher os painéis
        List<Tarefa> tarefas = listaDeAfazeres.consultarTarefas();
        if (tarefas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma tarefa encontrada.");
        } else {
            preencherPainelComTarefas(tarefas, naoIniciadoPanel, emProgressoPanel, concluidoPanel);
        }

        // Adicionar os painéis de consulta ao painel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adiciona margem ao redor do painel principal

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Adiciona os painéis com as tarefas
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0; // Preencher verticalmente
        mainPanel.add(new JScrollPane(naoIniciadoPanel), gbc);

        gbc.gridx = 1;
        mainPanel.add(new JScrollPane(emProgressoPanel), gbc);

        gbc.gridx = 2;
        mainPanel.add(new JScrollPane(concluidoPanel), gbc);

        // Adiciona o painel principal ao ConsultarPanel
        add(mainPanel, BorderLayout.CENTER);
    }

    // Método para criar um painel de categoria com o título especificado
    private JPanel createCategoryPanel(String title) {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setBorder(new TitledBorder(title));
        return categoryPanel;
    }

    // Método para preencher os painéis com as tarefas
    private void preencherPainelComTarefas(List<Tarefa> tarefas, JPanel naoIniciadoPanel, JPanel emProgressoPanel, JPanel concluidoPanel) {
        for (Tarefa tarefa : tarefas) {
            JPanel taskPanel = new JPanel(new BorderLayout());
            taskPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            taskPanel.setBackground(new Color(240, 240, 240)); // Cor de fundo
            taskPanel.setMaximumSize(new Dimension(350, 200)); // Tamanho máximo

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);

            JLabel descricaoValue = new JLabel(tarefa.getDescricao());
            descricaoValue.setFont(new Font("Arial", Font.BOLD, 16));
            textPanel.add(descricaoValue);

            taskPanel.add(textPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            JButton exibirButton = new JButton("Exibir");
            JButton editarButton = new JButton("Editar");
            JButton removerButton = new JButton("Remover");

            exibirButton.addActionListener(e -> exibirTarefa(tarefa));
            editarButton.addActionListener(e -> editarTarefa(tarefa));
            removerButton.addActionListener(e -> removerTarefa(tarefa.getId()));

            buttonPanel.add(exibirButton);
            buttonPanel.add(editarButton);
            buttonPanel.add(removerButton);

            taskPanel.add(buttonPanel, BorderLayout.SOUTH);

            switch (tarefa.getStatus()) {
                case 1:
                    naoIniciadoPanel.add(taskPanel);
                    break;
                case 2:
                    emProgressoPanel.add(taskPanel);
                    break;
                case 3:
                    concluidoPanel.add(taskPanel);
                    break;
                default:
                    throw new IllegalStateException("Status desconhecido: " + tarefa.getStatus());
            }
        }
    }

    private void exibirTarefa(Tarefa tarefa) {
        String mensagem = "Descrição: " + tarefa.getDescricao() + "\n" +
                          "Prioridade: " + tarefa.getPrioridade() + "\n" +
                          "Status: " + tarefa.getStatus() + "\n" +
                          "Data de Criação: " + tarefa.getDataCriacao().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" +
                          "Data de Conclusão: " + (tarefa.getDataConclusao() != null ? tarefa.getDataConclusao().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A") + "\n" +
                          "Nota: " + tarefa.getNota();
        JOptionPane.showMessageDialog(this, mensagem, "Exibir Tarefa", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editarTarefa(Tarefa tarefa) {
        // Diálogo para editar tarefa
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField descricaoField = new JTextField(tarefa.getDescricao(), 20);
        JComboBox<String> prioridadeComboBox = new JComboBox<>(new String[]{"Baixa", "Média", "Alta"});
        prioridadeComboBox.setSelectedIndex(tarefa.getPrioridade() - 1);
        JTextField dataConclusaoField = new JTextField(tarefa.getDataConclusao() != null ? tarefa.getDataConclusao().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "", 20);
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Não iniciado", "Em progresso", "Concluído"});
        statusComboBox.setSelectedIndex(tarefa.getStatus() - 1);
        JTextArea notaTextArea = new JTextArea(tarefa.getNota(), 5, 20);
        JScrollPane notaScrollPane = new JScrollPane(notaTextArea);
        panel.add(new JLabel("Descrição:"));
        panel.add(descricaoField);
        panel.add(new JLabel("Prioridade:"));
        panel.add(prioridadeComboBox);
        panel.add(new JLabel("Data de Conclusão (AAAA-MM-DD HH:MM:SS):"));
        panel.add(dataConclusaoField);
        panel.add(new JLabel("Status:"));
        panel.add(statusComboBox);
        panel.add(new JLabel("Nota:"));
        panel.add(notaScrollPane);

        int result = JOptionPane.showConfirmDialog(this, panel, "Editar Tarefa",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Atualizar os dados da tarefa no banco de dados
            String descricao = descricaoField.getText();
            int prioridadeId = prioridadeComboBox.getSelectedIndex() + 1;
            int statusId = statusComboBox.getSelectedIndex() + 1;
            String dataConclusao = dataConclusaoField.getText().trim();
            String notaAtualizada = notaTextArea.getText();

            // Formatar a data de conclusão
            LocalDateTime dataConclusaoFormatada;
            try {
                dataConclusaoFormatada = LocalDateTime.parse(dataConclusao, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido. Use o formato: AAAA-MM-DD HH:MM:SS");
                return;
            }

            // Chamar o método para atualizar a tarefa
            boolean sucesso = listaDeAfazeres.alterarTarefas(descricao, prioridadeId, statusId, Timestamp.valueOf(dataConclusaoFormatada), notaAtualizada, tarefa.getId());
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Tarefa atualizada com sucesso.");
                // Recarregar a interface para refletir as mudanças
                removeAll();
                initComponents();
                revalidate();
                repaint();
            }
        }
    }

    private void removerTarefa(int tarefaId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover esta tarefa?", "Remover Tarefa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean sucesso = listaDeAfazeres.removerTarefa(tarefaId);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Tarefa removida com sucesso.");
                // Recarregar a interface para refletir as mudanças
                removeAll();
                initComponents();
                revalidate();
                repaint();
            }
        }
    }
}
