package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteDao {

    Connection con;
    PreparedStatement st;
    ResultSet rs;

    public boolean conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/controle_clientes", "root", "admin");
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            return false;
        }
    }

    public int salvar(Cliente cliente) {
        try {
            st = con.prepareStatement("INSERT INTO cliente values (?,?,?,?)");
            st.setString(1, cliente.getCodigo());
            st.setString(2, cliente.getNome());
            st.setString(3, cliente.getEndereco());
            st.setDouble(4, cliente.getRenda());
            //Execução do comando SQL
            st.executeUpdate();
            return 1;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                return 1062;
            } else {
                return 0;
            }
        }
    }

    public int excluir(String codigo) {
        try {
            st = con.prepareStatement("DELETE FROM cliente WHERE codigo = ?");
            st.setString(1, codigo);
            int r = st.executeUpdate();
            if (r == 1) {
                return 1; // Excluiu
            } else {
                return 0; // Não excluiu
            }
        } catch (SQLException ex) {
            return 2; // Deu erro
        }
    }

    // Método consultar por código
    public Cliente consultarCodigo(String codigo){
        try {
            st = con.prepareStatement("SELECT * FROM cliente WHERE codigo = ?");
            st.setString(1, codigo);
            rs = st.executeQuery();
            if(rs.next()){ // se encontrou o código informado
                Cliente cliente;
                cliente = new Cliente();
                cliente.setCodigo(rs.getString("codigo"));
                cliente.setNome(rs.getString("nome"));
                cliente.setEndereco(rs.getString("endereco"));
                cliente.setRenda(rs.getDouble("renda"));
                return cliente;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            return null;
        }
    }
}
