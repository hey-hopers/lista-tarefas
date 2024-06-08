import java.sql.*;
import java.util.Scanner;

public class ListaDeAfazeres {
    private Conexao conexao;
    private int prioridade_id;
    private static Scanner scanner = new Scanner(System.in);

    public ListaDeAfazeres() {
        conexao = new Conexao();
        conexao.ConexaoDB();
    }

    public boolean adicionarTarefa(Tarefa tarefa) {
        String sql = "INSERT INTO tarefa (descricao, prioridade_id, status_id, data_criacao, data_conclusao, notas) VALUES ( ?, ?, ?, ?, ?, ?)";

        int prioridade = imprimirPrioridade();
        
        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql)) {
            stmt.setString(1, tarefa.getDescricao());  
            stmt.setInt(2, prioridade);
            stmt.setInt(3, tarefa.getStatus()); 

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar tarefa: " + e.getMessage());
            return false;
        }
    }

    public int imprimirPrioridade() {
        String sql = "SELECT * FROM prioridade";

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql);
            ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n===== Prioridades =====");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                System.out.println(id + ". " + nome + " - " + descricao);
            }
            System.out.println("===================");
            System.out.print("Selecione uma prioridade: ");
            scanner.nextInt();
            prioridade_id = scanner.nextInt();        

        } catch (SQLException e) {
            System.out.println("Erro ao imprimir prioridade: " + e.getMessage());
        } 

        return prioridade_id;   
    }

    public boolean alterarTarefa(int numeroDaTarefa, Tarefa novaTarefa) {
        String sql = "UPDATE Tarefa SET descricao = ?, concluido = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql)) {
            stmt.setString(1, novaTarefa.getDescricao());
            stmt.setInt(3, numeroDaTarefa);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar tarefa: " + e.getMessage());
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

}
