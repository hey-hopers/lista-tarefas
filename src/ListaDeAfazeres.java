import javax.swing.*;

import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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

    public boolean consultarTarefa(JPanel naoIniciadoPanel, JPanel emProgressoPanel, JPanel concluidoPanel) {
        String sql = "SELECT TAR.ID, TAR.DESCRICAO AS tarefa_descricao, PRI.DESCRICAO AS prioridade_descricao, STA.NOME AS status_descricao, TAR.DATA_CRIACAO, TAR.DATA_CONCLUSAO, TAR.NOTAS "
                   + "FROM TAREFA TAR "
                   + "INNER JOIN PRIORIDADE PRI ON TAR.PRIORIDADE_ID = PRI.ID "
                   + "INNER JOIN STATUS STA ON TAR.STATUS_ID = STA.ID";

        boolean consultado = false;

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int tarefaId = rs.getInt("TAR.ID");
                String statusDescricao = rs.getString("status_descricao").trim().toLowerCase();

                // Log do status para depuração
                System.out.println("Status da tarefa: " + statusDescricao);

                JPanel taskPanel = new JPanel(new BorderLayout());
                taskPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                taskPanel.setBackground(new Color(240, 240, 240)); // Cor de fundo
                taskPanel.setMaximumSize(new Dimension(350, 200)); // Tamanho máximo

                JPanel textPanel = new JPanel();
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
                textPanel.setOpaque(false);

                JLabel descricaoValue = new JLabel(rs.getString("tarefa_descricao"));
                descricaoValue.setFont(new Font("Arial", Font.BOLD, 18));
                textPanel.add(descricaoValue);

                // Adiciona espaço entre descrição e prioridade
                textPanel.add(Box.createVerticalStrut(10));

                JLabel prioridadeValue = new JLabel(rs.getString("prioridade_descricao"));
                prioridadeValue.setFont(new Font("Arial", Font.PLAIN, 14));
                textPanel.add(prioridadeValue);

                JButton exibirButton = new JButton("Exibir");
                JButton alterarButton = new JButton("Alterar");
                JButton removerButton = new JButton("Remover");

                exibirButton.setPreferredSize(new Dimension(80, 25)); // Define o tamanho dos botões
                alterarButton.setPreferredSize(new Dimension(80, 25)); // Define o tamanho dos botões
                removerButton.setPreferredSize(new Dimension(80, 25)); // Define o tamanho dos botões

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
                buttonPanel.setOpaque(false);

                buttonPanel.add(exibirButton);
                buttonPanel.add(Box.createVerticalStrut(5)); // Espaçamento vertical entre botões
                buttonPanel.add(alterarButton);
                buttonPanel.add(Box.createVerticalStrut(5)); // Espaçamento vertical entre botões
                buttonPanel.add(removerButton);

                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                detailsPanel.setVisible(false);

                JLabel data = new JLabel("Datas");
                data.setFont(new Font("Arial", Font.BOLD, 14));
                detailsPanel.add(data);

                StringBuilder datasInicio = new StringBuilder();
                datasInicio.append("Início: ").append(rs.getString("DATA_CRIACAO")).append("  ");

                JLabel dataCriacao = new JLabel(datasInicio.toString());
                dataCriacao.setFont(new Font("Arial", Font.PLAIN, 14));
                detailsPanel.add(dataCriacao);

                StringBuilder datasFim = new StringBuilder();
                datasFim.append("Fim: ").append(rs.getString("DATA_CONCLUSAO")).append("  ");

                JLabel dataConclusao = new JLabel(datasFim.toString());
                dataConclusao.setFont(new Font("Arial", Font.PLAIN, 14));
                detailsPanel.add(dataConclusao);

                // Adiciona espaço entre descrição e prioridade
                detailsPanel.add(Box.createVerticalStrut(10));

                String nota = rs.getString("NOTAS");
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
                        detailsPanel.setVisible(!detailsPanel.isVisible());
                        exibirButton.setText(detailsPanel.isVisible() ? "Ocultar" : "Exibir");
                    }
                });

                alterarButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        editarTarefa(tarefaId);
                    }
                });

                removerButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int opcao = JOptionPane.showConfirmDialog(
                            null,
                            "Tem certeza que deseja remover esta tarefa?",
                            "Confirmação",
                            JOptionPane.YES_NO_OPTION
                        );

                        if (opcao == JOptionPane.YES_OPTION) {
                            removerTarefa(tarefaId);
                            Container parent = taskPanel.getParent();
                            parent.remove(taskPanel);
                            parent.revalidate();
                            parent.repaint();
                        }
                    }
                });

                taskPanel.add(textPanel, BorderLayout.CENTER);
                taskPanel.add(buttonPanel, BorderLayout.EAST);
                taskPanel.add(detailsPanel, BorderLayout.SOUTH);

                switch (statusDescricao) {
                    case "não iniciado":
                    case "não_iniciado":
                    case "nao iniciado":
                    case "nao_iniciado":
                        naoIniciadoPanel.add(taskPanel);
                        break;
                    case "em progresso":
                    case "em_progresso":
                        emProgressoPanel.add(taskPanel);
                        break;
                    case "concluído":
                    case "concluido":
                        concluidoPanel.add(taskPanel);
                        break;
                    default:
                        throw new IllegalStateException("Status desconhecido: " + statusDescricao);
                }
            }
            consultado = true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar tarefas: " + e.getMessage());
        }

        return consultado;
    }

    private void removerTarefa(int tarefaId) {
        String sql = "DELETE FROM TAREFA WHERE ID = ?";

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql)) {
            stmt.setInt(1, tarefaId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Tarefa removida com sucesso.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover tarefa: " + e.getMessage());
        }
    }

    private void editarTarefa(int tarefaId) {
        // Diálogo para editar tarefa
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField descricaoField = new JTextField(20);
        JComboBox<String> prioridadeComboBox = new JComboBox<>(new String[]{"Baixa", "Média", "Alta"});
        JTextField dataConclusaoField = new JTextField(20);
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Não iniciado", "Em progresso", "Concluído"});
        JTextArea notaTextArea = new JTextArea(5, 20);
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

        // Obter informações da tarefa do banco de dados
        String selectSql = "SELECT DESCRICAO, PRIORIDADE_ID, DATA_CONCLUSAO, STATUS_ID, NOTAS FROM TAREFA WHERE ID=?";
        try (PreparedStatement stmt = conexao.prepararDeclaracao(selectSql)) {
            stmt.setInt(1, tarefaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                descricaoField.setText(rs.getString("DESCRICAO"));
                prioridadeComboBox.setSelectedIndex(rs.getInt("PRIORIDADE_ID") - 1);
                dataConclusaoField.setText(rs.getTimestamp("DATA_CONCLUSAO").toLocalDateTime().toString());
                statusComboBox.setSelectedIndex(rs.getInt("STATUS_ID") - 1);
                notaTextArea.setText(rs.getString("NOTAS"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao obter informações da tarefa: " + ex.getMessage());
            return;
        }

        int result = JOptionPane.showConfirmDialog(null, panel, "Editar Tarefa",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Atualizar os dados da tarefa no banco de dados
            String descricao = descricaoField.getText();
            String prioridade = (String) prioridadeComboBox.getSelectedItem();
            String dataConclusao = dataConclusaoField.getText().trim(); // Remover espaços extras
            String status = (String) statusComboBox.getSelectedItem();
            String notaAtualizada = notaTextArea.getText();

            // Conversão da prioridade para ID
            int prioridadeId = prioridadeComboBox.getSelectedIndex() + 1;

            // Conversão do status para ID
            int statusId = statusComboBox.getSelectedIndex() + 1;

            // Formatar a data de conclusão
            LocalDateTime dataConclusaoFormatada;
            try {
                dataConclusaoFormatada = LocalDateTime.parse(dataConclusao);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(null, "Formato de data inválido. Use o formato: AAAA-MM-DD HH:MM:SS");
                return;
            }

            // Atualizar os dados no banco de dados
            String updateSql = "UPDATE TAREFA SET DESCRICAO=?, PRIORIDADE_ID=?, STATUS_ID=?, DATA_CONCLUSAO=?, NOTAS=? WHERE ID=?";
            try (PreparedStatement stmt = conexao.prepararDeclaracao(updateSql)) {
                stmt.setString(1, descricao);
                stmt.setInt(2, prioridadeId);
                stmt.setInt(3, statusId);
                stmt.setTimestamp(4, Timestamp.valueOf(dataConclusaoFormatada));
                stmt.setString(5, notaAtualizada);
                stmt.setInt(6, tarefaId);

                int linhasAfetadas = stmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    JOptionPane.showMessageDialog(null, "Tarefa atualizada com sucesso.");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao atualizar tarefa.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao atualizar tarefa: " + ex.getMessage());
            }
        }
    }

}
