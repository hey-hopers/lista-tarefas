import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
                System.out.println("Opção inválida. Tente novamente.");
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
            System.out.println("Erro ao adicionar tarefa: " + e.getMessage());
            return false;
        }
    }

    public int definirPrioridade() {
        String sql = "SELECT * FROM prioridade";
        int prioridade_id = 0;
        List<Integer> listaDePrioridades = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepararDeclaracao(sql);
            ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n===== Prioridades =====");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                System.out.println(id + ". " + nome + " - " + descricao);
                listaDePrioridades.add(id);
            }
            System.out.println("===================");

            while (true) {
                System.out.print("Selecione uma prioridade: ");
                if (scanner.hasNextInt()) {
                    prioridade_id = scanner.nextInt();
                    if (listaDePrioridades.contains(prioridade_id)) {
                        break;
                    } else {
                        System.out.println("Prioridade inválida. Tente novamente.");
                    }
                } else {
                    System.out.println("Entrada inválida. Por favor, insira um número.");
                    scanner.next();
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao imprimir prioridade: " + e.getMessage());
        }

        return prioridade_id;   
    }

    public String definirNota() {

        String nota = "";

        System.out.print("Deseja incluir uma nota na tarefa?");
        scanner.nextLine();
        nota = scanner.nextLine();

        return nota;
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
