import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ListaDeAfazeres {
    private Conexao conexao;

    public ListaDeAfazeres() {
        conexao = new Conexao();
        conexao.ConexaoDB();
    }

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
        int selectedIndex = 0;
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
    
            String input = (String) JOptionPane.showInputDialog(null, "Selecione uma prioridade:",
                    "Escolha de Prioridade", JOptionPane.QUESTION_MESSAGE, null,
                    options.toArray(), options.get(0));
    
            selectedIndex = options.indexOf(input);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao imprimir prioridade: " + e.getMessage());
        }
    
        return listaDePrioridades.get(selectedIndex);
    }        

    private String definirNota() {
        String nota = "NULL";

        int option = JOptionPane.showConfirmDialog(null, "Deseja incluir uma nota na tarefa?", "Incluir Nota",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            nota = JOptionPane.showInputDialog(null, "Digite a nota:");
        }

        return nota;
    }

    // Corrigido para receber os parâmetros necessários
    public boolean alterarTarefa(int numeroDaTarefa, Tarefa novaTarefa) {
        // Implementação da alteração da tarefa
        return false; // Alterado para não gerar erro de compilação
    }
    
    // Outros métodos como imprimirTarefas, removerTarefa podem ser adicionados aqui
}
