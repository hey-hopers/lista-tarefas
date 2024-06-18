import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXDatePicker;

import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ConsultarPanel extends JPanel {
    private ListaDeAfazeres listaDeAfazeres;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");

    public ConsultarPanel(ListaDeAfazeres listaDeAfazeres) {
        this.listaDeAfazeres = listaDeAfazeres;
        setBackground(new Color(200, 200, 200));
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Criar os painéis para as categorias
        JPanel naoIniciadoPanel = createCategoryPanel("Não iniciado");
        naoIniciadoPanel.setBackground(new Color(200, 200, 200));
        JPanel emProgressoPanel = createCategoryPanel("Em progresso");
        emProgressoPanel.setBackground(new Color(200, 200, 200));
        JPanel concluidoPanel = createCategoryPanel("Concluído");
        concluidoPanel.setBackground(new Color(200, 200, 200));

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
            JPanel taskPanel = new JPanel(new GridBagLayout());
            taskPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            taskPanel.setBackground(new Color(245, 245, 245));
            taskPanel.setMaximumSize(new Dimension(400, 180)); 
            taskPanel.setPreferredSize(new Dimension(380, 160));
            taskPanel.setMinimumSize(new Dimension(380, 140));
            taskPanel.setOpaque(true);
            taskPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    
            ));

            GridBagConstraints gbc_task = new GridBagConstraints();
            gbc_task.insets = new Insets(10, 10, 10, 10);
            gbc_task.fill = GridBagConstraints.BOTH;
            gbc_task.weightx = 1.0; 
            gbc_task.weighty = 0.0;

            // Painel Título da tarefa e prioridade
            JPanel visualPainel = new JPanel();
            visualPainel.setLayout(new BoxLayout(visualPainel, BoxLayout.Y_AXIS));
            visualPainel.setBackground(new Color(245, 245, 245));
            JLabel titleLabel = new JLabel(tarefa.getTitulo());
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel priorityLabel = new JLabel("Prioridade: " + tarefa.getnomePrioridade());
            priorityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            visualPainel.add(titleLabel);
            visualPainel.add(Box.createVerticalStrut(5));
            visualPainel.add(priorityLabel);

            gbc_task.gridx = 0;
            gbc_task.gridy = 0;
            taskPanel.add(visualPainel, gbc_task);

            // Painel para exibir informações da tarefa
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(new Color(245, 245, 245));
            infoPanel.setVisible(false);

            JLabel criacaoLabel = new JLabel("Data de Criação: " + tarefa.getDataCriacao().format(DATE_TIME_FORMATTER));
            JLabel conclusaoLabel = new JLabel("Data de Conclusão: " + (tarefa.getDataConclusao() != null ? tarefa.getDataConclusao().format(DATE_TIME_FORMATTER) : "N/A"));
            JTextArea notaArea = new JTextArea(tarefa.getNota());
            notaArea.setLineWrap(true);
            notaArea.setWrapStyleWord(true);
            notaArea.setEditable(false); 
            notaArea.setBackground(new Color(245, 245, 245));
            notaArea.setMinimumSize(new Dimension(getPreferredSize().width, 50));

            infoPanel.add(criacaoLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(conclusaoLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(notaArea);

            gbc_task.gridy = 1;
            taskPanel.add(infoPanel, gbc_task);

            // Botão para exibir/ocultar informações
            JButton exibirButton = new JButton("Exibir");
            JButton editarButton = new JButton("Editar");
            JButton removerButton = new JButton("Remover");

            Dimension buttonSize = new Dimension(100, 30);
            exibirButton.setPreferredSize(buttonSize);
            editarButton.setPreferredSize(buttonSize);
            removerButton.setPreferredSize(buttonSize);

            exibirButton.setMinimumSize(buttonSize);
            editarButton.setMinimumSize(buttonSize);
            removerButton.setMinimumSize(buttonSize);

            exibirButton.setMaximumSize(buttonSize);
            editarButton.setMaximumSize(buttonSize);
            removerButton.setMaximumSize(buttonSize);

            exibirButton.addActionListener(e -> {
                boolean isVisible = infoPanel.isVisible();
                infoPanel.setVisible(!isVisible);
                exibirButton.setText(isVisible ? "Exibir" : "Ocultar");
            });

            editarButton.addActionListener(e -> editarTarefa(tarefa));
            removerButton.addActionListener(e -> removerTarefa(tarefa.getId()));

            // Painel para os botões na parte direita
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.setBackground(new Color(245, 245, 245));
            buttonPanel.add(exibirButton);
            buttonPanel.add(Box.createVerticalStrut(5));
            buttonPanel.add(editarButton);
            buttonPanel.add(Box.createVerticalStrut(5));
            buttonPanel.add(removerButton);

            GridBagConstraints gbc_buttons = new GridBagConstraints();
            gbc_buttons.gridx = 1;
            gbc_buttons.gridy = 0;
            gbc_buttons.gridheight = 2;
            gbc_buttons.anchor = GridBagConstraints.NORTHEAST;
            gbc_buttons.insets = new Insets(0, 10, 0, 10);

            taskPanel.add(buttonPanel, gbc_buttons);

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

    private void editarTarefa(Tarefa tarefa) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    
        Dimension campoTamanho = new Dimension(100, 30);

        // Ajustes linha Titulo
        JLabel tituloLabel = new JLabel("Título:");
        tituloLabel.setFont(tituloLabel.getFont().deriveFont(Font.BOLD, 20f));
        JTextField tituloField = new JTextField(tarefa.getTitulo(), 20);
        tituloField.setFont(tituloField.getFont().deriveFont(Font.PLAIN, 16f));
    
        // Ajustes linha Prioridade
        JLabel prioridadeLabel = new JLabel("Prioridade:");
        prioridadeLabel.setFont(prioridadeLabel.getFont().deriveFont(Font.BOLD, 20f));
        JComboBox<String> prioridadeComboBox = new JComboBox<>(new String[]{"Baixa", "Média", "Alta"});
        prioridadeComboBox.setSelectedIndex(tarefa.getPrioridade() - 1);
        prioridadeComboBox.setFont(prioridadeComboBox.getFont().deriveFont(Font.PLAIN, 16f));
        prioridadeComboBox.setPreferredSize(campoTamanho);
        prioridadeComboBox.setMinimumSize(campoTamanho);
        prioridadeComboBox.setMaximumSize(campoTamanho);

        // Ajustes linha Data de Conclusão
        JLabel data_conclusao_texto = new JLabel("Data de Conclusão:");
        data_conclusao_texto.setFont(data_conclusao_texto.getFont().deriveFont(Font.BOLD, 20f));
        LocalDateTime dataConclusaoTarefa = tarefa.getDataConclusao();
        Date dataConclusaoDate = dataConclusaoTarefa != null ? Date.from(dataConclusaoTarefa.atZone(ZoneId.systemDefault()).toInstant()) : null;

        JXDatePicker data_conclusao = CustomizarData();
        if (dataConclusaoDate != null) {
            data_conclusao.setDate(dataConclusaoDate);
        }
        CustomizarBotaoData(data_conclusao);

        JSpinner hora_conclusao = CustomizarHora();
        if (dataConclusaoDate != null) {
            hora_conclusao.setValue(dataConclusaoDate);
        }

        JPanel data_conclusao_panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        data_conclusao_panel.add(data_conclusao);
        data_conclusao_panel.add(hora_conclusao);
    
        // Ajustes linha Status
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 20f));
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Não iniciado", "Em progresso", "Concluído"});
        statusComboBox.setSelectedIndex(tarefa.getStatus() - 1);
        statusComboBox.setFont(statusComboBox.getFont().deriveFont(Font.PLAIN, 16f));
        statusComboBox.setPreferredSize(campoTamanho);
        statusComboBox.setMinimumSize(campoTamanho);
        statusComboBox.setMaximumSize(campoTamanho);
    
        // Ajustes linha Nota
        JLabel notaLabel = new JLabel("Nota:");
        notaLabel.setFont(notaLabel.getFont().deriveFont(Font.BOLD, 20f));
        JTextArea notaTextArea = new JTextArea(tarefa.getNota(), 10, 45);
        notaTextArea.setLineWrap(true);
        notaTextArea.setWrapStyleWord(true);
        notaTextArea.setFont(notaTextArea.getFont().deriveFont(Font.PLAIN, 16f));
        notaTextArea.setPreferredSize(new Dimension(150, notaTextArea.getPreferredSize().height));
        JScrollPane notaScrollPane = new JScrollPane(notaTextArea);
    
        // Adicionando os componentes ao painel do formulário
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_START;
        
        panel.add(tituloLabel, gbc);

        gbc.gridy = 1;
        panel.add(tituloField, gbc);

        gbc.gridy = 2;
        panel.add(prioridadeLabel, gbc);

        gbc.gridy = 3;
        panel.add(prioridadeComboBox, gbc);

        gbc.gridy = 4;
        panel.add(data_conclusao_texto, gbc);

        gbc.gridy = 5;
        panel.add(data_conclusao_panel, gbc);

        gbc.gridy = 6;  
        panel.add(statusLabel, gbc);

        gbc.gridy = 7;
        panel.add(statusComboBox, gbc);

        gbc.gridy = 8; 
        panel.add(notaLabel, gbc);

        gbc.gridy = 9;
        panel.add(notaScrollPane, gbc);

        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);
    
        // Diálogo para editar tarefa
        int result = JOptionPane.showConfirmDialog(this, panel, "Editar Tarefa", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Obter valores do formulário
            String titulo = tituloField.getText();
            int prioridadeId = prioridadeComboBox.getSelectedIndex() + 1;
            Date dataConclusaoDat = data_conclusao.getDate();
            Date horaConclusaoDat = (Date) hora_conclusao.getValue();
            int statusId = statusComboBox.getSelectedIndex() + 1;
            String notaAtualizada = notaTextArea.getText();

            // Formatar a data de conclusão
            LocalDateTime dataConclusao = LocalDateTime.ofInstant(dataConclusaoDat.toInstant(), ZoneId.systemDefault());
            LocalDateTime horaConclusao = LocalDateTime.ofInstant(horaConclusaoDat.toInstant(), ZoneId.systemDefault());
    
            LocalDateTime dataHoraConclusao = dataConclusao.withHour(horaConclusao.getHour()).withMinute(horaConclusao.getMinute());
    
            // Atualizar os dados da tarefa no banco de dados
            boolean sucesso = listaDeAfazeres.alterarTarefas(titulo, prioridadeId, statusId, Timestamp.valueOf(dataHoraConclusao), notaAtualizada, tarefa.getId());
            if (sucesso) {
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
                // Recarregar a interface para refletir as mudanças
                removeAll();
                initComponents();
                revalidate();
                repaint();
            }
        }
    }

    // Metodos para auxiliar na interface de Data e Hora
    private JXDatePicker CustomizarData() {
        JXDatePicker data_campo = new JXDatePicker();
        data_campo.setFormats(new SimpleDateFormat("dd/MM/yyyy"));
        data_campo.getEditor().setColumns(10);
        data_campo.setFont(data_campo.getFont().deriveFont(Font.PLAIN, 16f));
        data_campo.setPreferredSize(new Dimension(150, data_campo.getPreferredSize().height));
        return data_campo;
    }
    
    private JSpinner CustomizarHora() {
        SpinnerDateModel modelo = new SpinnerDateModel();
        JSpinner rotacao = new JSpinner(modelo);
        rotacao.setEditor(new JSpinner.DateEditor(rotacao, "HH:mm"));
        rotacao.setValue(new Date());
        rotacao.setFont(rotacao.getFont().deriveFont(Font.PLAIN, 16f));
        rotacao.setPreferredSize(new Dimension(80, rotacao.getPreferredSize().height));
        return rotacao;
    }
    
    private void CustomizarBotaoData(JXDatePicker data_botao) {
        JButton botao = (JButton) data_botao.getComponent(1);
        ImageIcon icone = new ImageIcon("img/calendar-icon.png");
        Image imagem_ajustada = icone.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        botao.setIcon(new ImageIcon(imagem_ajustada));
        botao.setPreferredSize(new Dimension(30, botao.getPreferredSize().height));
    }
}
