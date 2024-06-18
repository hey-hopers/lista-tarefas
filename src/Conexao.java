import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Conexao {

    private Connection conn = null;

    public Connection ConexaoDB() {
        try {

            // Estabelece a conexão com o banco de dados
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/db_lista_tarefa?useSSL=false&allowPublicKeyRetrieval=true",
                "root", "admin"
            );
            
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conn;
    }

    public PreparedStatement prepararDeclaracao(String sql) throws SQLException {
        if (conn != null) {
            return conn.prepareStatement(sql);
        } else {
            throw new SQLException("Conexão não estabelecida.");
        }
    }

    public void fecharConexao() {
        if (conn != null) {
            try {
                conn.close();

            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
            }
        }
    }
}