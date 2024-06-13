import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ListaDeAfazeres {
    private static Conexao conexao;

    public ListaDeAfazeres() {
        conexao = new Conexao();
        conexao.ConexaoDB();
    }

    // Adicionar Tarefa
    public boolean adicionarTarefa(Tarefa tarefa) {
        String sql = "INSERT INTO tarefa (descricao, prioridade_id, status_id, data_criacao, data_conclusao, notas) VALUES (?, ?, ?, ?, ?, ?)";
        int prioridade = definirPrioridade();

        LocalDateTime data_atual = LocalDateTime.now();
        LocalDateTime data_sla = LocalDateTime.now();

        switch (prioridade) {
            case 1:
                data_sla = data_atual.plusHours(72);
                break;
            case 2:
                data_sla = data_atual.plusHours(48);
                break;
            case 3:
                data_sla = data_atual.plusHours(24);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.");
                return false;
        }

        String nota = definirNota();

        Timestamp dataCriacao = Timestamp.valueOf(data_atual);
        Timestamp dataConclusao = Timestamp.valueOf(data_sla);

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql)) {
            stmt.setString(1, tarefa.getDescricao());
            stmt.setInt(2, prioridade);
            stmt.setInt(3, tarefa.getStatus());
            stmt.setTimestamp(4, dataCriacao);
            stmt.setTimestamp(5, dataConclusao);
            stmt.setString(6, nota);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar tarefa: " + e.getMessage());
            return false;
        }
    }

    private int definirPrioridade() {
        String sql = "SELECT * FROM prioridade";
        final int[] selectedIndex = {-1};
        List<Integer> listaDePrioridades = new ArrayList<>();
        List<String> options = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                options.add(nome);
                listaDePrioridades.add(id);
            }

            // Criar um JDialog personalizado para selecionar a prioridade
            JFrame frame = new JFrame();
            JDialog dialog = new JDialog(frame, "Escolha de Prioridade", true);
            dialog.setSize(500, 400);
            dialog.setLayout(new BorderLayout());

            // Criar e popular o JComboBox
            JComboBox<String> comboBox = new JComboBox<>(options.toArray(new String[0]));

            // Criar e adicionar um painel com o JComboBox
            JPanel inputPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);

            JLabel prioridadeLabel = new JLabel("Selecione uma prioridade:");
            prioridadeLabel.setFont(new Font("Arial", Font.BOLD, 16));

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            inputPanel.add(prioridadeLabel, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            inputPanel.add(comboBox, gbc);

            // Criar e adicionar botões
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancelar");
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            inputPanel.add(buttonPanel, gbc);

            // Action listener para o botão OK
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedIndex[0] = comboBox.getSelectedIndex();
                    dialog.dispose();
                }
            });

            // Action listener para o botão Cancelar
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedIndex[0] = -1;
                    dialog.dispose();
                }
            });

            dialog.add(inputPanel, BorderLayout.CENTER);
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar prioridades: " + e.getMessage());
        }

        if (selectedIndex[0] == -1) {
            // Caso o usuário tenha cancelado a seleção, retornamos um valor especial ou tratamos o caso
            return -1; // ou outra lógica para lidar com o cancelamento
        }

        return listaDePrioridades.get(selectedIndex[0]);
    }      

    private String definirNota() {
        final String[] nota = {"NULL"};

        // Criar o JDialog personalizado
        JFrame frame = new JFrame();
        JDialog dialog = new JDialog(frame, "Incluir Nota", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());

        // Campo de texto para entrada da nota
        JTextArea notaField = new JTextArea(10, 30);
        notaField.setLineWrap(true);
        notaField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(notaField);

        // Botões de confirmação e cancelamento
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancelar");

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Painel de entrada com GridBagLayout para melhor alinhamento
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel notaLabel = new JLabel("Digite a nota:");
        notaLabel.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputPanel.add(notaLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        inputPanel.add(scrollPane, gbc);

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action listener para o botão OK
        okButton.addActionListener(e -> {
            String inputNota = notaField.getText();
            if (nota != null && !inputNota.trim().isEmpty()) {
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "A nota não pode ser vazia.");
            }
        });

        // Action listener para o botão Cancelar
        cancelButton.addActionListener(e -> {
            nota[0] = "NULL";
            dialog.dispose();
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

        return nota[0];
    }

    public static boolean consultarTarefa(JPanel panel) {
        String sql = "SELECT TAR.descricao AS tarefa_descricao, PRI.descricao AS prioridade_descricao, TAR.data_criacao, TAR.data_conclusao, TAR.notas FROM TAREFA TAR INNER JOIN PRIORIDADE PRI ON TAR.prioridade_id = PRI.id";
        boolean consultado = false;

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                JPanel taskPanel = new JPanel();
                taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
                taskPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                taskPanel.setBackground(new Color(240, 240, 240)); // Cor de fundo
                taskPanel.setMaximumSize(new Dimension(350, 200)); // Tamanho máximo

                JLabel descricaoValue = new JLabel(rs.getString("tarefa_descricao"));
                descricaoValue.setFont(new Font("Arial", Font.BOLD, 18));
                taskPanel.add(descricaoValue);

                // Adiciona espaço entre descrição e prioridade
                taskPanel.add(Box.createVerticalStrut(10));

                JLabel prioridadeValue = new JLabel(rs.getString("prioridade_descricao"));
                prioridadeValue.setFont(new Font("Arial", Font.PLAIN, 14));
                taskPanel.add(prioridadeValue);

                JButton exibirButton = new JButton("Exibir");
                taskPanel.add(exibirButton);

                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                detailsPanel.setVisible(false);

                StringBuilder datas_inicio = new StringBuilder();

                datas_inicio.append("Início: ").append(rs.getString("data_criacao")).append("  ");                
                JLabel dataCriacao = new JLabel(datas_inicio.toString());
                dataCriacao.setLayout(new BoxLayout(dataCriacao, BoxLayout.Y_AXIS));
                dataCriacao.setVisible(false);

                StringBuilder datas_fim = new StringBuilder();

                datas_fim.append("Início: ").append(rs.getString("data_criacao")).append("  ");
                JLabel dataConclusao= new JLabel(datas_fim.toString());
                dataConclusao.setLayout(new BoxLayout(dataConclusao, BoxLayout.Y_AXIS));
                dataConclusao.setVisible(false);

                String nota = rs.getString("notas");
                if (nota != null) {
                    JLabel notaLabel = new JLabel("Nota:");
                    notaLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    JLabel notaValue = new JLabel(nota);
                    notaValue.setFont(new Font("Arial", Font.PLAIN, 14));
                    detailsPanel.add(notaLabel);
                    detailsPanel.add(notaValue);
                }

                exibirButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dataCriacao.setVisible(!dataCriacao.isVisible());
                        dataConclusao.setVisible(!dataConclusao.isVisible());
                        detailsPanel.setVisible(!detailsPanel.isVisible());
                        exibirButton.setText(detailsPanel.isVisible() ? "Ocultar" : "Exibir");
                    }
                });

                taskPanel.add(detailsPanel);
                panel.add(Box.createVerticalStrut(10)); // Espaçamento vertical entre os painéis de tarefa
                panel.add(taskPanel);

                consultado = true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar tarefas: " + e.getMessage());
        }

        return consultado;
    }

}
