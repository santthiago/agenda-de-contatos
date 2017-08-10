package br.com.agenda.dal;

/**
 * MÓDULO DE CONEXÃO - DAL
 * @author thiago.lsasantos
 */

// a linha abaixo importa bibliotecas e recursos para trabalhar com SQL
import java.sql.*;
// SERÁ NECESSÁRIO O DOWNLOAD DA BIBLIOTECA SQL, O CAMINHO: https://dev.mysql.com/downloads/connector/j/
// INSTALE O ARQUIVO .BIN NA RAIZ DA PASTA SDK E IMPORTE A BIBLIOTECA COMO JAR DE DENTRO DO PROGRAMA

public class Dal {
    // criando metodo com retorno
    // CONNECTION -> RECURSO DA BIBLIOTECA JAVA.SQL.* USADO PARA CONEXÃO AO BANCO DE DADOS
    // CONECTOR -> NOME DO METODO
    public static Connection conector() {
        //" STRING DE CONEXÃO"
        //criando uma variável especial para armazenar e consultar no banco de dados
        // java.sql.Connection -> classe usada para conexão com o banco
        // conexão -> noe da variável
        // atribuimos o valor null(nulo) por questões de segurança
        java.sql.Connection conexao = null;
        // carregando o driver do banco de dados
        // String -> tipo de variável
        // driver -> nome de variavel que recebe o driver
        String driver = "com.mysql.jdbc.Driver";
        // caminho do banco de dados (local , servidor  local, cloud)
        // url -> nome da variavel que recebe a informação do caminho, porta padrão do mysql e nome do banco de dados
        String url = "jdbc:mysql://localhost:3306/dbagenda";
        // autenticação do banco de dados (usuário e senha) com permissão de acesso
        // Observação: auterar o usuario e senha de acordo com o servidor /banco
        String user = "root";
        String password = "senac@tat";
        // iremos utilizar uma estrutura do tipo try catch para tratamento de exceções
        // na tentativa de conectar com o banco de dados (try) 
        try {
            // se o banco de dados estiver disponível, estabeleça a conexão quando solicitado
            // usaremos mais recursos para estabelecer a conexão
            Class.forName(driver);
            // atribua a variavel conexão as informações necessarias para estabelecer sincronia com o banco de dados
            // DriverManager -> gerenciamento do driver (mysql)
            // getConnection ->  obter a conexão e fazer a passagem  dos parametros descritos -> ()
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            // se o banco de dados estiver indisponivel
            // a linha abaixo ajuda a identificar o problema de conexao
            //System.out.println(e);
            return null;
        }
        
    }
}
