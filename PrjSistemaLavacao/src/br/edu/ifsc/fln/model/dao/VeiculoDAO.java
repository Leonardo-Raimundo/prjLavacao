package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VeiculoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo(veiculoPlaca,veiculoObservacoes, idModelo, idCor, idCliente) "
                + "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getModelo().getId());
            stmt.setInt(4, veiculo.getCor().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET veiculoPlaca=?, veiculoObservacoes=?, idModelo=?, "
                + "idCor=?, idCliente=? WHERE veiculoId=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getModelo().getId());
            stmt.setInt(4, veiculo.getCor().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.setInt(6, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Veiculo veiculo) {
        String sql = "DELETE FROM veiculo WHERE veiculoId=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Veiculo> listar() {
        String sql = "SELECT veiculo.veiculoId, veiculo.veiculoPlaca, veiculo.veiculoObservacoes, "
                + "marca.marcaId, marca.marcaNome, modelo.modeloId, modelo.modeloDescricao, cor.corId, cor.corNome, "
                + "motor.motorPotencia, tipoCombustivel, "
                + "cliente.clienteNome, idCliente "
                + "FROM veiculo INNER JOIN modelo ON modelo.modeloId = veiculo.idModelo "
                + "INNER JOIN marca ON marca.marcaId = modelo.idMarca "
                + "INNER JOIN cor ON cor.corId = veiculo.idCor "
                + "INNER JOIN motor ON motor.idModelo = modelo.modeloId "
                + "INNER JOIN cliente ON cliente.clienteId = veiculo.idCliente";
        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = populateVO(resultado);
                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

//    public List<Veiculo> listarPorModelo(Modelo modelo) {;
//        String sql = "SELECT veiculo.veiculoId, veiculo.veiculoPlaca, veiculoObservacoes "
//                + " marca.marcaId, marca.marcaNome "
//                + "FROM modelo INNER JOIN marca ON marca.marcaId = modelo.idMarca WHERE marcaId = ?;";
//        List<Modelo> retorno = new ArrayList<>();
//        try {
//            PreparedStatement stmt = connection.prepareStatement(sql);
//            stmt.setInt(1, modelo.getId());
//            ResultSet resultado = stmt.executeQuery();
//            while (resultado.next()) {
//                Modelo modelo = populateVO(resultado);
//                retorno.add(modelo);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return retorno;
//    }
    public Veiculo buscar(Veiculo veiculo) {
        String sql = "SELECT veiculo.veiculoId, veiculo.veiculoPlaca, modelo.modeloId,modelo.modeloDescricao "
                + "FROM veiculo INNER JOIN modelo ON modelo.modeloId = veiculo.idModelo WHERE veiculo.veiculoId = ?;";
        Veiculo retorno = new Veiculo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private Veiculo populateVO(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        Marca marca = new Marca();
        Modelo modelo = new Modelo();
        Cor cor = new Cor();
        PessoaFisica clientePF = new PessoaFisica();
        PessoaJuridica clientePJ = new PessoaJuridica();

        modelo.setMarca(marca);
        veiculo.setModelo(modelo);
        veiculo.setCor(cor);

        veiculo.setObservacoes(rs.getString("veiculoObservacoes"));
        veiculo.setId(rs.getInt("veiculoId"));
        veiculo.setPlaca(rs.getString("veiculoPlaca"));
        marca.setId(rs.getInt("marcaId"));
        marca.setNome(rs.getString("marcaNome"));
        modelo.setId(rs.getInt("modeloId"));
        modelo.setDescricao(rs.getString("modeloDescricao"));
        cor.setId(rs.getInt("corId"));
        cor.setNome(rs.getString("corNome"));
        modelo.getMotor().setPotencia(rs.getInt("motorPotencia"));
        modelo.getMotor().setTipoCombustivel(Enum.valueOf(ETipoCombustivel.class, rs.getString("tipoCombustivel")));
        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.setConnection(connection);
        Cliente cliente = clienteDAO.buscar(rs.getInt("idCliente"));
        return veiculo;
    }

}
