package controle;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Cliente;
import modelo.ClienteDao;

@WebServlet(name = "ControleClientes", urlPatterns = {"/ControleClientes"})
public class ControleClientes extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Declaração das variáveis
        ClienteDao dao;
        boolean retorno;
        int r;
        String codigo;
        String flag;

        flag = request.getParameter("flag");
        if (flag.equals("cad_cli")) {
            String nome, endereco;
            double renda;
            Cliente cliente;

            //Recebe os dados vidos do formulário cadcli.html
            codigo = request.getParameter("codigo");
            nome = request.getParameter("nome");
            endereco = request.getParameter("endereco");
            try {
                renda = Double.parseDouble(request.getParameter("renda"));

                // Pegar os dados recebidos e encapsular em 
                // um objeto da classe Cliente
                cliente = new Cliente();
                cliente.setCodigo(codigo);
                cliente.setNome(nome);
                cliente.setEndereco(endereco);
                cliente.setRenda(renda);

                //Conectar com o banco de dados
                dao = new ClienteDao();
                retorno = dao.conectar();
                if (retorno == true) { // conectou com BD
                    // Salvar os dados
                    r = dao.salvar(cliente);
                    if (r == 1) {
                        out.print("<p style='color:red'>Cliente salvo com sucesso</p><br>");
                        out.print("<a href='cad_cli.html' style='text-decoration:none'>Voltar</a>");
                    } else if (r == 1062) {
                        out.print("<p>Este código já está cadatrado</p>");
                    } else {
                        out.print("<p>Erro ao tentar salvar os dados</p>");
                    }
                } else {
                    out.print("<p>Erro na conexão com o banco de dados</p>");
                }
            } catch (NumberFormatException ex) {
                out.print("<p>A renda precisa ser composta apenas de números no formato xxxx.xx</p>");
            }
        } else if (flag.equals("exc_cli")) {
            //Receber o código vindo do formulário exc_cli
            codigo = request.getParameter("codigo");
            //Conectar com o Banco de dados
            dao = new ClienteDao();
            retorno = dao.conectar();
            if (retorno == true) {
                // Excluir os dados do cliente
                r = dao.excluir(codigo);
                if (r == 1) {
                    out.print("<p>Cliente excluído com sucesso</p>");
                } else if (r == 0) {
                    out.print("<p>Este código não está cadastrado</p>");
                } else {
                    out.print("<p>Erro na exclusão dos dados</p>");
                }
            } else {
                out.print("<p>Erro ao tentar conectar com o banco de dados</p>");
            }
        } else if (flag.equals("con_cli")) {
            codigo = request.getParameter("codigo");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
