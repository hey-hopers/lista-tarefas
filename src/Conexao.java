import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Conexao {

    Connection conn = null;

    public Connection ConexaoDB() {
        
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_lista_tarefa?user=root&password=admin");
            System.out.println("Conexão estabelecida com sucesso.");
            
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }

        return conn;

    }

    public PreparedStatement prepararDeclaracao(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    public void fecharConexao() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexão fechada com sucesso.");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
            }
        }
    }
}
