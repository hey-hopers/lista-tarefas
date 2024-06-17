import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ListaDeAfazeres {
    private static Conexao conexao;

    public ListaDeAfazeres() {
        conexao = new Conexao();
        conexao.ConexaoDB();
    }

    // Adicionar Tarefa
    public boolean adicionarTarefa(Tarefa tarefa) {
        String sql = "INSERT INTO tarefa (descricao, prioridade_id, status_id, data_criacao, data_conclusao, notas) VALUES (?, ?, ?, ?, ?, ?)";
    
        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql)) {
            stmt.setString(1, tarefa.getDescricao());
            stmt.setInt(2, tarefa.getPrioridade());
            stmt.setInt(3, tarefa.getStatus());
            stmt.setTimestamp(4, Timestamp.valueOf(tarefa.getDataCriacao()));
            stmt.setTimestamp(5, Timestamp.valueOf(tarefa.getDataConclusao()));
            stmt.setString(6, tarefa.getNota());
    
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar tarefa: " + e.getMessage());
            return false;
        }
    }

    // Consultar tarefa
    public List<Tarefa> consultarTarefas() {
        String sql = "SELECT TAR.ID, TAR.DESCRICAO AS tarefa_descricao, PRI.ID AS prioridade_id, STA.ID AS status_id, TAR.DATA_CRIACAO, TAR.DATA_CONCLUSAO, TAR.NOTAS "
                   + "FROM TAREFA TAR "
                   + "INNER JOIN PRIORIDADE PRI ON TAR.PRIORIDADE_ID = PRI.ID "
                   + "INNER JOIN STATUS STA ON TAR.STATUS_ID = STA.ID";

        List<Tarefa> tarefas = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String descricao = rs.getString("tarefa_descricao");
                int prioridade = rs.getInt("prioridade_id");
                int status = rs.getInt("status_id");
                LocalDateTime dataCriacao = rs.getTimestamp("DATA_CRIACAO").toLocalDateTime();
                LocalDateTime dataConclusao = rs.getTimestamp("DATA_CONCLUSAO") != null ? rs.getTimestamp("DATA_CONCLUSAO").toLocalDateTime() : null;
                String notas = rs.getString("NOTAS");

                Tarefa tarefa = new Tarefa(id, descricao, prioridade, status, dataCriacao, dataConclusao, notas);
                tarefas.add(tarefa);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar tarefas: " + e.getMessage());
        }

        return tarefas;
    }

    // Atualizar os dados no banco de dados
    public boolean alterarTarefas(String descricao, int prioridadeId, int statusId, Timestamp dataConclusaoFormatada, String notaAtualizada, int tarefaId) {
        String updateSql = "UPDATE TAREFA SET DESCRICAO=?, PRIORIDADE_ID=?, STATUS_ID=?, DATA_CONCLUSAO=?, NOTAS=? WHERE ID=?";
        try (PreparedStatement stmt = conexao.prepararDeclaracao(updateSql)) {
            stmt.setString(1, descricao);
            stmt.setInt(2, prioridadeId);
            stmt.setInt(3, statusId);
            stmt.setTimestamp(4, dataConclusaoFormatada);
            stmt.setString(5, notaAtualizada);
            stmt.setInt(6, tarefaId);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(null, "Tarefa atualizada com sucesso.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao atualizar tarefa.");
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar tarefa: " + ex.getMessage());
            return false;
        }
    }

    // Remover tarefa
    public boolean removerTarefa(int tarefaId) {
        String sql = "DELETE FROM TAREFA WHERE ID = ?";
        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql)) {
            stmt.setInt(1, tarefaId);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(null, "Tarefa removida com sucesso.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao remover tarefa.");
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover tarefa: " + e.getMessage());
            return false;
        }
    }
}
