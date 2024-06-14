import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;

public class Main {
    private static ListaDeAfazeres listaDeAfazeres = new ListaDeAfazeres();

    public static void main(String[] args) {
        // Criar a janela principal
        JFrame frame = new JFrame("Lista de Tarefas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 

        // Criar o painel principal com GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margens entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Botão para adicionar tarefa
        JButton btnAdd = new JButton("Adicionar Tarefa");
        btnAdd.addActionListener(e -> adicionarTarefa());

        // Botão para consultar tarefas
        JButton btnFind = new JButton("Consultar Tarefas");
        btnFind.addActionListener(e -> consultarTarefa());

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
        panel.add(btnFind, gbc);
        gbc.gridy++;
        panel.add(btnExit, gbc);

        // Adicionar o painel à janela
        frame.getContentPane().add(panel);

        // Tornar a janela visível
        frame.setVisible(true);
    }

    private static void adicionarTarefa() {
        // Criação do JDialog personalizado para entrada de tarefas
        JFrame frame = new JFrame();
        JDialog dialog = new JDialog(frame, "Adicionar Tarefa", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(frame);
    
        // Campo de texto para entrada da descrição da tarefa
        JTextField descricaoField = new JTextField(30);
        descricaoField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Botões de confirmação e cancelamento
        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(70, 130, 180));
        okButton.setForeground(Color.WHITE);
        
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.WHITE);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(); 
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
    
        // Painel de entrada com GridBagLayout para melhor alinhamento
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel descricaoLabel = new JLabel("Descrição da Tarefa:");
        descricaoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputPanel.add(descricaoLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        inputPanel.add(descricaoField, gbc);

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
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }   

    private static void consultarTarefa() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Consulta de Tarefas");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1000, 600);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adiciona margem ao redor do painel principal

            JPanel consultaPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // Grid layout com 3 colunas

            JPanel naoIniciadoPanel = new JPanel();
            naoIniciadoPanel.setLayout(new BoxLayout(naoIniciadoPanel, BoxLayout.Y_AXIS));
            naoIniciadoPanel.setBorder(new TitledBorder("Não iniciado"));

            JPanel emProgressoPanel = new JPanel();
            emProgressoPanel.setLayout(new BoxLayout(emProgressoPanel, BoxLayout.Y_AXIS));
            emProgressoPanel.setBorder(new TitledBorder("Em progresso"));

            JPanel concluidoPanel = new JPanel();
            concluidoPanel.setLayout(new BoxLayout(concluidoPanel, BoxLayout.Y_AXIS));
            concluidoPanel.setBorder(new TitledBorder("Concluído"));

            consultaPanel.add(naoIniciadoPanel);
            consultaPanel.add(emProgressoPanel);
            consultaPanel.add(concluidoPanel);

            JScrollPane scrollPane = new JScrollPane(consultaPanel);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            if (!listaDeAfazeres.consultarTarefa(naoIniciadoPanel, emProgressoPanel, concluidoPanel)) {
                JOptionPane.showMessageDialog(frame, "Erro ao consultar tarefas.");
            } else {
                frame.add(mainPanel);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

}
