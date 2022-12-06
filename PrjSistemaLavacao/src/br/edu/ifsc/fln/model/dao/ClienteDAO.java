package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente(ClienteNome, clienteCelular, clienteEmail, dataCadastro) VALUES(?, ?, ?, ?)";
        String sqlCPF = "INSERT INTO pessoaFisica "
                + "(idCliente, cpf, dataNascimento) VALUES((SELECT max(clienteId) FROM cliente),?,?)";
        String sqlCPJ = "INSERT INTO pessoaJuridica "
                + "(idCliente, cnpj, inscricaoEstadual) VALUES((SELECT max(clienteId) FROM cliente),?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCelular());
            stmt.setString(3, cliente.getEmail());
            stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataCadastro()));
            stmt.execute();

            if (cliente instanceof PessoaFisica) {
                stmt = connection.prepareStatement(sqlCPF);
                stmt.setString(1, ((PessoaFisica) cliente).getCpf());
                stmt.setDate(2, java.sql.Date.valueOf(((PessoaFisica) cliente).getDataNascimento()));
                stmt.execute();
            } else {
                stmt = connection.prepareStatement(sqlCPJ);
                stmt.setString(1, ((PessoaJuridica) cliente).getCnpj());
                stmt.setString(2, ((PessoaJuridica) cliente).getInscricaoEstadual());
                stmt.execute();
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Cliente cliente) {
        String sql = "UPDATE cliente SET clienteNome=?, clienteCelular=?, clienteEmail=?, dataCadastro=? WHERE clienteId=?";
        String sqlCPF = "UPDATE pessoafisica SET cpf=?, dataNascimento=? WHERE idCliente=?";
        String sqlCPJ = "UPADATE pessoajuridica SET cnpj=?, inscricaoEstadual=? WHERE idCliente=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCelular());
            stmt.setString(3, cliente.getEmail());
            stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataCadastro()));
            stmt.setInt(5, cliente.getId());
            stmt.execute();

            if (cliente instanceof PessoaFisica) {
                stmt = connection.prepareStatement(sqlCPF);
                stmt.setString(1, ((PessoaFisica) cliente).getCpf());
//                stmt.setDate(2, ((PessoaFisica) cliente).getDataNascimento());
                stmt.setDate(2, java.sql.Date.valueOf(((PessoaFisica) cliente).getDataNascimento()));
                stmt.setInt(3, cliente.getId());
                stmt.execute();
            } else {
                stmt = connection.prepareStatement(sqlCPJ);
                stmt.setString(1, ((PessoaJuridica) cliente).getCnpj());
                stmt.setString(2, ((PessoaJuridica) cliente).getInscricaoEstadual());
                stmt.setInt(3, cliente.getId());
                stmt.execute();
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Cliente cliente) {
        String sql = "DELETE FROM cliente WHERE clienteId=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Cliente> listar() {
        String sql = "SELECT * FROM cliente "
                + "LEFT JOIN pessoaFisica ON pessoaFisica.idCliente = cliente.clienteId "
                + "LEFT JOIN pessoaJuridica ON pessoaJuridica.idCliente = cliente.clienteId";
        List<Cliente> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cliente cliente = populateVO(resultado);
                retorno.add(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Cliente buscar(Cliente cliente) {
        String sql = "SELECT * FROM cliente "
                + "LEFT JOIN pessoaFisica ON pessoaFisica.idCliente = cliente.clienteId "
                + "LEFT JOIN pessoaJuridica ON pessoaJuridica.idCliente = cliente.clienteId WHERE clienteId = ?";
        Cliente retorno = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private Cliente populateVO(ResultSet rs) throws SQLException {
        Cliente cliente;

        if (rs.getString("cnpj") == null || rs.getString("cnpj").length() <= 0) {
            //é cliente pessoa física
            cliente = new PessoaFisica();
            ((PessoaFisica) cliente).setCpf(rs.getString("cpf"));
            ((PessoaFisica) cliente).setDataNascimento(rs.getDate("dataNascimento").toLocalDate());
        } else {
            cliente = new PessoaJuridica();
            ((PessoaJuridica) cliente).setCnpj(rs.getString("cnpj"));
            ((PessoaJuridica) cliente).setInscricaoEstadual(rs.getString("inscricaoEstadual"));
        }

        cliente.setId(rs.getInt("clienteId"));
        cliente.setNome(rs.getString("ClienteNome"));
        cliente.setCelular(rs.getString("clienteCelular"));
        cliente.setEmail(rs.getString("clienteEmail"));
        cliente.setDataCadastro(rs.getDate("dataCadastro").toLocalDate());
        return cliente;
    }
}
