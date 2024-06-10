import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class Main {
    private static ListaDeAfazeres listaDeAfazeres = new ListaDeAfazeres();

    public static void main(String[] args) {
        // Criar a janela principal
        JFrame frame = new JFrame("Lista de Tarefas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Criar o painel principal com GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margens entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Botão para adicionar tarefa
        JButton btnAdd = new JButton("Adicionar Tarefa");
        btnAdd.addActionListener(e -> adicionarTarefa());

        // Botão para alterar tarefa
        JButton btnAlter = new JButton("Alterar Tarefa");
        btnAlter.addActionListener(e -> alterarTarefa());

        // Botão para remover tarefa
        JButton btnRemove = new JButton("Remover Tarefa");
        btnRemove.addActionListener(e -> removerTarefa());

        // Botão para consultar tarefas
        JButton btnPrint = new JButton("Consultar Tarefas");
        /* btnPrint.addActionListener(e -> imprimirTarefas()); */

        // Botão para sair
        JButton btnExit = new JButton("Sair");
        btnExit.addActionListener(e -> System.exit(0));

        // Adicionar os botões ao painel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnAdd, gbc);
        gbc.gridy++;
        panel.add(btnAlter, gbc);
        gbc.gridy++;
        panel.add(btnRemove, gbc);
        gbc.gridy++;
        panel.add(btnPrint, gbc);
        gbc.gridy++;
        panel.add(btnExit, gbc);

        // Adicionar o painel à janela
        frame.getContentPane().add(panel);

        // Tornar a janela visível
        frame.setVisible(true);
    }

    private static void adicionarTarefa() {
        // Criação do JDialog personalizado para entrada de tarefas
        JDialog dialog = new JDialog((Frame) null, "Adicionar Tarefa", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new BorderLayout());

        // Campo de texto para entrada da descrição da tarefa
        JTextField descricaoField = new JTextField(20);
        
        // Botões de confirmação e cancelamento
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancelar");
        
        // Painel de botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Painel de entrada
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Descrição da Tarefa:"));
        inputPanel.add(descricaoField);

        // Adicionar painéis ao dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Ação do botão OK
        okButton.addActionListener(e -> {
            String descricao = descricaoField.getText();
            if (descricao != null && !descricao.trim().isEmpty()) {
                Tarefa tarefa = new Tarefa(descricao);
                if (listaDeAfazeres.adicionarTarefa(tarefa)) {
                    JOptionPane.showMessageDialog(dialog, "Tarefa adicionada com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao adicionar a tarefa.");
                }
                dialog.dispose();
            }
        });

        // Ação do botão Cancelar
        cancelButton.addActionListener(e -> dialog.dispose());

        // Tornar o dialog visível
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void alterarTarefa() {
        JDialog dialog = new JDialog((Frame) null, "Alterar Tarefa", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new BorderLayout());

        // Campos de texto para entrada dos novos dados da tarefa
        JTextField descricaoField = new JTextField(20);
        JTextField prioridadeField = new JTextField(5);
        JTextField dataConclusaoField = new JTextField(10);

        // Botões de confirmação e cancelamento
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancelar");

        // Painel de botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Painel de entrada
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.add(new JLabel("Número da Tarefa:"));
        JTextField numeroTarefaField = new JTextField(5); // Campo para inserir o número da tarefa
        inputPanel.add(numeroTarefaField);
        inputPanel.add(new JLabel("Nova Descrição da Tarefa:"));
        inputPanel.add(descricaoField);
        inputPanel.add(new JLabel("Nova Prioridade:"));
        inputPanel.add(prioridadeField);
        inputPanel.add(new JLabel("Nova Data de Conclusão (AAAA-MM-DD HH:mm:ss):"));
        inputPanel.add(dataConclusaoField);

        // Adicionar painéis ao dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Ação do botão OK
        okButton.addActionListener(e -> {
            try {
                int numeroDaTarefa = Integer.parseInt(numeroTarefaField.getText());
                String novaDescricao = descricaoField.getText();
                String novaDataConclusao = dataConclusaoField.getText();
                Timestamp tsNovaDataConclusao = Timestamp.valueOf(novaDataConclusao);

                Tarefa novaTarefa = new Tarefa(novaDescricao);
                novaTarefa.setDataConclusao(tsNovaDataConclusao);

                if (listaDeAfazeres.alterarTarefa(numeroDaTarefa, novaTarefa)) {
                    JOptionPane.showMessageDialog(dialog, "Tarefa alterada com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao alterar a tarefa.");
                }
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Por favor, insira dados válidos.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "Data de conclusão inválida.");
            }
        });

        // Ação do botão Cancelar
        cancelButton.addActionListener(e -> dialog.dispose());

        // Tornar o dialog visível
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void removerTarefa() {
        // Implementação omitida para brevidade
    }

    // Outros métodos omitidos para brevidade
}
