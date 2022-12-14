package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.EstatusServico;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(OrdemServico ordemServico) {
        String sql = "INSERT INTO OrdemServico(total, agenda, desconto, situacao, idVeiculo) "
                + "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            stmt.setBigDecimal(1, ordemServico.getTotal());
            stmt.setDate(2, java.sql.Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(3, ordemServico.getTaxaDesconto());

            if (ordemServico.geteStatusServico() != null) {
                stmt.setString(4, ordemServico.geteStatusServico().name());
            } else {
                stmt.setString(5, EstatusServico.ABERTA.name());
            }

            stmt.setInt(6, ordemServico.getVeiculo().getId());
            stmt.execute();

            ItemOSDAO itemOSDAO = new ItemOSDAO();
            itemOSDAO.setConnection(connection);

            ServicoDAO servicoDAO = new ServicoDAO();
            servicoDAO.setConnection(connection);

            for (ItemOS itemOS : ordemServico.getItensOS()) {
                Servico servico = itemOS.getServico();
                itemOS.setOrdemServico(this.buscarUltimaOrdemServico());
                itemOSDAO.inserir(itemOS);
            }
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {

        }
    }

    public boolean alterar(OrdemServico ordemServico) {
        String sql = "UPDATE ordemServico SET total=?, agenda=?, desconto=?, situacao=?, idVeiculo=? WHERE numeroOrdem=?";
        try {
            connection.setAutoCommit(false);
            ItemOSDAO itemOsDAO = new ItemOSDAO();
            itemOsDAO.setConnection(connection);
            ServicoDAO servicoDAO = new ServicoDAO();
            servicoDAO.setConnection(connection);

            //Venda vendaAnterior = buscar(venda.getCdVenda());
//            Venda vendaAnterior = buscar(venda);
//            List<ItemDeVenda> itensDeVenda = itemDeVendaDAO.listarPorVenda(vendaAnterior);
//            for (ItemDeVenda iv : itensDeVenda) {
//                //Produto p = iv.getProduto(); //isto não da certo ...
//                Produto p = estoqueDAO.getEstoque(iv.getProduto());
//                p.getEstoque().repor(iv.getQuantidade());
//                estoqueDAO.atualizar(p.getEstoque());
//                itemDeVendaDAO.remover(iv);
//            }
            //atualiza os dados da ordemServico
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1, ordemServico.getTotal());
            stmt.setDate(2, Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(3, ordemServico.getTaxaDesconto());

            if (ordemServico.geteStatusServico() != null) {
                stmt.setString(4, ordemServico.geteStatusServico().name());
            } else {
                stmt.setString(5, EstatusServico.ABERTA.name());
            }
            stmt.setInt(6, ordemServico.getVeiculo().getId());
            stmt.setLong(7, ordemServico.getNumero());
            stmt.execute();
            for (ItemOS iv : ordemServico.getItensOS()) {
                //Produto p = iv.getProduto(); //isto não da certo ...
//                Servico s = estoqueDAO.getEstoque(iv.getProduto());
//                p.getEstoque().retirar(iv.getQuantidade());
//                estoqueDAO.atualizar(p.getEstoque());
                itemOsDAO.inserir(iv);
            }
            connection.commit();
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException exc1) {
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc1);
            }
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(OrdemServico ordemServico) {
        String sql = "DELETE FROM ordemServico WHERE numeroOrdem=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            try {
                connection.setAutoCommit(false);
                ItemOSDAO itemOsDAO = new ItemOSDAO();
                itemOsDAO.setConnection(connection);

                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);

                for (ItemOS itemOS : ordemServico.getItensOS()) {
                    Servico servico = itemOS.getServico();
                    itemOsDAO.remover(itemOS);
                }
                stmt.setLong(1, ordemServico.getNumero());
                stmt.execute();
                connection.commit();
            } catch (SQLException exc) {
                try {
                    connection.rollback();
                } catch (SQLException exc1) {
                    Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc1);
                }
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc);
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<OrdemServico> listar() {
        String sql = "SELECT * FROM ordemServico";
        List<OrdemServico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
                Veiculo veiculo = new Veiculo();
                List<ItemOS> itensOS = new ArrayList();

                ordemServico.setNumero(resultado.getLong("numeroServico"));
                ordemServico.setTotal(resultado.getBigDecimal("total"));
                ordemServico.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServico.setTaxaDesconto(resultado.getDouble("desconto"));
                ordemServico.seteStatusServico(Enum.valueOf(EstatusServico.class, resultado.getString("situacao")));
                veiculo.setId(resultado.getInt("idVeiculo"));

                //Obtendo os dados completos do Cliente associado à Venda
                VeiculoDAO veiculoDAO = new VeiculoDAO();
                veiculoDAO.setConnection(connection);
                veiculo = veiculoDAO.buscar(veiculo);

                //Obtendo os dados completos dos Itens de Venda associados à Venda
                ItemOSDAO itemOSDAO = new ItemOSDAO();
                itemOSDAO.setConnection(connection);
                itensOS = itemOSDAO.listarPorOrdemServico(ordemServico);

                ordemServico.setVeiculo(veiculo);
                ordemServico.setItensOS(itensOS);
                retorno.add(ordemServico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public OrdemServico buscar(OrdemServico ordemServico) {
        String sql = "SELECT * FROM ordemServico WHERE numeroOrdem=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                ordemServico.setNumero(resultado.getLong("numeroServico"));
                ordemServico.setTotal(resultado.getBigDecimal("total"));
                ordemServico.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServico.setTaxaDesconto(resultado.getDouble("desconto"));
                ordemServico.seteStatusServico(Enum.valueOf(EstatusServico.class, resultado.getString("situacao")));
                veiculo.setId(resultado.getInt("idVeiculo"));
                ordemServicoRetorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordemServicoRetorno;
    }

    public OrdemServico buscar(long numero) {
        /*
            Método necessário para evitar que a instância de retorno seja 
            igual a instância a ser atualizada.
         */
        String sql = "SELECT * FROM ordemServico WHERE numeroOrdem=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, numero);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
                Veiculo veiculo = new Veiculo();
                ordemServico.setNumero(resultado.getLong("numeroServico"));
                ordemServico.setTotal(resultado.getBigDecimal("total"));
                ordemServico.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServico.setTaxaDesconto(resultado.getDouble("desconto"));
                ordemServico.seteStatusServico(Enum.valueOf(EstatusServico.class, resultado.getString("situacao")));
                veiculo.setId(resultado.getInt("idVeiculo"));
                ordemServicoRetorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordemServicoRetorno;
    }

    public OrdemServico buscarUltimaOrdemServico() {
        String sql = "SELECT max(numeroOrdem) as max FROM ordemServico";

        OrdemServico retorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                retorno.setNumero(resultado.getInt("max"));
                return retorno;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

}
