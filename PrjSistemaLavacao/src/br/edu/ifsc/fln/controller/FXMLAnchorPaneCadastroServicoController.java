/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Servico;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Leonardo Raimundo
 */
public class FXMLAnchorPaneCadastroServicoController implements Initializable {

    @FXML
    private TableView<Servico> tableView;
    @FXML
    private TableColumn<Servico, String> tableColumnDescricao;
    @FXML
    private Label lbServicoId;
    @FXML
    private Label lbServicoDescricao;
    @FXML
    private Label lbServicoValor;
    @FXML
    private Label lbServicoPontos;
    @FXML
    private Label lbServicoCategoria;
    @FXML
    private Button btInserir;
    @FXML
    private Button btAlterar;
    @FXML
    private Button btRemover;

    private List<Servico> listaServicos;
    private ObservableList<Servico> observableListServicos;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicoDAO.setConnection(connection);

        carregarTableView();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }

    public void carregarTableView() {
        tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        listaServicos = servicoDAO.listar();

        observableListServicos = FXCollections.observableArrayList(listaServicos);
        tableView.setItems(observableListServicos);
    }

    public void selecionarItemTableView(Servico servico) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (servico != null) {
            lbServicoId.setText(Integer.toString(servico.getId()));
            lbServicoDescricao.setText(servico.getDescricao());
            lbServicoValor.setText(Double.toString(servico.getValor()));
            lbServicoPontos.setText(Integer.toString(servico.getPontos()));
            lbServicoCategoria.setText(servico.geteCategoria().name());
        } else {
            lbServicoId.setText("");
            lbServicoDescricao.setText("");
            lbServicoValor.setText("");
            lbServicoPontos.setText("");
            lbServicoCategoria.setText("");
        }
    }

    @FXML
    private void handleBtInserir(ActionEvent event) throws IOException {
        Servico servico = new Servico();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosServicoDialog(servico);
        if (buttonConfirmarClicked) {
            servicoDAO.inserir(servico);
            carregarTableView();
        }
    }

    @FXML
    private void handleBtAlterar(ActionEvent event) throws IOException {
        Servico servico = tableView.getSelectionModel().getSelectedItem();
        if (servico != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosServicoDialog(servico);
            if (buttonConfirmarClicked) {
                servicoDAO.alterar(servico);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um serviço na Tabela.");
            alert.show();
        }
    }

    @FXML
    private void handleBtRemover(ActionEvent event) {
        Servico servico = tableView.getSelectionModel().getSelectedItem();
        if (servico != null) {
            servicoDAO.remover(servico);
            carregarTableView();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um serviço na Tabela.");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneCadastrosServicoDialog(Servico servico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroServicoController.class.getResource(
                "../view/FXMLAnchorPaneCadastroServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de serviços");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o modelo ao controller
        FXMLAnchorPaneCadastroServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setServico(servico);

        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }

}
