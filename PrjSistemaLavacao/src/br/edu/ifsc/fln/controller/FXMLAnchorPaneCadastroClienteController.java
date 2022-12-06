package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroClienteController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbClienteId;

    @FXML
    private Label lbClienteCPF;

    @FXML
    private Label lbClienteCNPJ;

    @FXML
    private Label lbClienteDataNascimento;

    @FXML
    private Label lbClienteNome;

    @FXML
    private Label lbClienteCelular;

    @FXML
    private Label lbClienteEmail;

    @FXML
    private Label lbClienteDataCadastro;

    @FXML
    private Label lbClienteInscEstadual;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteCelular;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteNome;

    @FXML
    private TableView<Cliente> tableViewClientes;

    private List<Cliente> listaClientes;
    private ObservableList<Cliente> observableListClientes;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        carregarTableViewCliente();

        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));
    }

    public void carregarTableViewCliente() {
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnClienteCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));

        listaClientes = clienteDAO.listar();

        observableListClientes = FXCollections.observableArrayList(listaClientes);
        tableViewClientes.setItems(observableListClientes);
    }

    public void selecionarItemTableViewClientes(Cliente cliente) {
        if (cliente != null) {
            lbClienteId.setText(String.valueOf(cliente.getId()));
            lbClienteNome.setText(cliente.getNome());
            lbClienteCelular.setText(cliente.getCelular());
            lbClienteEmail.setText(cliente.getEmail());
            lbClienteDataCadastro.setText(String.valueOf(
                    cliente.getDataCadastro()));
//                            .format(DateTimeFormatter.ofPattern("dd/mm/yyyy"))));;

            if (cliente instanceof PessoaFisica) {
                lbClienteCPF.setText(((PessoaFisica) cliente).getCpf());
                lbClienteDataNascimento.setText(String.valueOf(
                        ((PessoaFisica) cliente).getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                lbClienteCNPJ.setText("");
                lbClienteInscEstadual.setText("");
            } else {
                lbClienteCNPJ.setText(((PessoaJuridica) cliente).getCnpj());
                lbClienteInscEstadual.setText(((PessoaJuridica) cliente).getInscricaoEstadual());
                lbClienteCPF.setText("");
                lbClienteDataNascimento.setText("");
            }

        } else {
            lbClienteId.setText("");
            lbClienteNome.setText("");
            lbClienteCPF.setText("");
            lbClienteCelular.setText("");
            lbClienteEmail.setText("");
            lbClienteDataCadastro.setText("");
            lbClienteCPF.setText("");
            lbClienteDataNascimento.setText("");
            lbClienteCNPJ.setText("");
            lbClienteInscEstadual.setText("");
        }

    }

    @FXML
    public void handleBtInserir() throws IOException {
        Cliente cliente = getTipoCliente();
        if (cliente != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                clienteDAO.inserir(cliente);
                carregarTableViewCliente();
            }
        }
    }
    
    private Cliente getTipoCliente() {
        List<String> opcoes = new ArrayList<>();
        opcoes.add("PessoaFisica");
        opcoes.add("PessoaJuridica");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("PessoaFisica", opcoes);
        dialog.setTitle("Dialogo de Opções");
        dialog.setHeaderText("Escolha o tipo de cliente");
        dialog.setContentText("Tipo de cliente: ");
        Optional<String> escolha = dialog.showAndWait();
        if (escolha.isPresent()) {
            if (escolha.get().equalsIgnoreCase("PessoaFisica")) 
                return new PessoaFisica();
            else 
                return new PessoaJuridica();
        } else {
            return null;
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                clienteDAO.alterar(cliente);
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Cliente na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            clienteDAO.remover(cliente);
            carregarTableViewCliente();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cliente na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroClienteDialog(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroClienteController.class.getResource("../view/FXMLAnchorPaneCadastroClienteDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cliente");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o obejto cliente para o controller
        FXMLAnchorPaneCadastroClienteDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }

}
