import java.sql.*;

public class ListaDeAfazeres {
    private Conexao conexao;

    public ListaDeAfazeres() {
        conexao = new Conexao();
    }

    public boolean adicionarTarefa(Tarefa tarefa) {
        String sql = "INSERT INTO Tarefa (descricao, concluido) VALUES (?, ?)";

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql)) {
            stmt.setString(1, tarefa.getDescricao());
            stmt.setBoolean(2, tarefa.estaConcluido());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar tarefa: " + e.getMessage());
            return false;
        }
    }

    public boolean alterarTarefa(int numeroDaTarefa, Tarefa novaTarefa) {
        String sql = "UPDATE Tarefa SET descricao = ?, concluido = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql)) {
            stmt.setString(1, novaTarefa.getDescricao());
            stmt.setBoolean(2, novaTarefa.estaConcluido());
            stmt.setInt(3, numeroDaTarefa);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar tarefa: " + e.getMessage());
            return false;
        }
    }

    public boolean removerTarefa(int numeroDaTarefa) {
        String sql = "DELETE FROM Tarefa WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql)) {
            stmt.setInt(1, numeroDaTarefa);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao remover tarefa: " + e.getMessage());
            return false;
        }
    }

    public boolean marcarTarefaComoConcluida(int numeroDaTarefa) {
        return atualizarStatusTarefa(numeroDaTarefa, true);
    }

    public boolean marcarTarefaComoNaoConcluida(int numeroDaTarefa) {
        return atualizarStatusTarefa(numeroDaTarefa, false);
    }

    private boolean atualizarStatusTarefa(int numeroDaTarefa, boolean concluido) {
        String sql = "UPDATE Tarefa SET concluido = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql)) {
            stmt.setBoolean(1, concluido);
            stmt.setInt(2, numeroDaTarefa);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar status da tarefa: " + e.getMessage());
            return false;
        }
    }

    public void imprimirTarefas() {
        String sql = "SELECT * FROM Tarefa";

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                System.out.println("\nNão há tarefas na lista.");
            } else {
                System.out.println("\n===== Tarefas =====");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String descricao = rs.getString("descricao");
                    boolean concluido = rs.getBoolean("concluido");
                    String status = concluido ? "[Concluída]" : "[Pendente]";
                    System.out.println(id + ". " + status + " " + descricao);
                }
                System.out.println("===================");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao imprimir tarefas: " + e.getMessage());
        }
    }
}
