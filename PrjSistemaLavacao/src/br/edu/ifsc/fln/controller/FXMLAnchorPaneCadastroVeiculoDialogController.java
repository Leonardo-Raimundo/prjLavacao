package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Motor;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroVeiculoDialogController implements Initializable {

    @FXML
    private TextField tfVeiculoPlaca;
    @FXML
    private ComboBox<Marca> cbVeiculoMarca;
    @FXML
    private ComboBox<Modelo> cbVeiculoModelo;
    @FXML
    private ComboBox<Cor> cbVeiculoCor;
    @FXML
    private TextField tfVeiculoObservacoes;
    @FXML
    private Button btConfirmar;
    @FXML
    private Button btCancelar;

    //    private List<Categoria> listaCategorias;
//    private ObservableList<Categoria> observableListCategorias;
    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final CorDAO corDAO = new CorDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Veiculo veiculo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        modeloDAO.setConnection(connection);
        corDAO.setConnection(connection);
        carregarComboBoxMarcas();
        carregarComboBoxModelos();
        carregarComboBoxCores();
        setFocusLostHandle();
    }

    private void setFocusLostHandle() {
        tfVeiculoPlaca.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { // focus lost
                if (tfVeiculoPlaca.getText() == null || tfVeiculoPlaca.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    tfVeiculoPlaca.requestFocus();
                }
            }
        });
    }

    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas;

    public void carregarComboBoxMarcas() {
        listaMarcas = marcaDAO.listar();
        observableListMarcas
                = FXCollections.observableArrayList(listaMarcas);
        cbVeiculoMarca.setItems(observableListMarcas);
    }

    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;

    public void carregarComboBoxModelos() {
        listaModelos = modeloDAO.listar();
        observableListModelos
                = FXCollections.observableArrayList(listaModelos);
        cbVeiculoModelo.setItems(observableListModelos);
    }

    private List<Cor> listaCores;
    private ObservableList<Cor> observableListCores;

    public void carregarComboBoxCores() {
        listaCores = corDAO.listar();
        observableListCores
                = FXCollections.observableArrayList(listaCores);
        cbVeiculoCor.setItems(observableListCores);
    }

    /**
     * @return the dialogStage
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmarClicked
     */
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    /**
     * @return the veiculo
     */
    public Veiculo getVeiculo() {
        return veiculo;
    }

    /**
     * @param veiculo the produto to set
     */
    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        if (veiculo.getId() != 0) {
            tfVeiculoPlaca.setText(veiculo.getPlaca());
            cbVeiculoMarca.getSelectionModel().select(veiculo.getModelo().getMarca());
            cbVeiculoModelo.getSelectionModel().select(veiculo.getModelo());
            cbVeiculoCor.getSelectionModel().select(veiculo.getCor());
            tfVeiculoObservacoes.setText(veiculo.getObservacoes());
        }
    }

    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            veiculo.setPlaca(tfVeiculoPlaca.getText());
            veiculo.setModelo(
                    cbVeiculoModelo.getSelectionModel().getSelectedItem());
            veiculo.getModelo().setMarca(
                    cbVeiculoMarca.getSelectionModel().getSelectedItem());
            veiculo.setCor(
                    cbVeiculoCor.getSelectionModel().getSelectedItem());
            veiculo.setObservacoes(tfVeiculoObservacoes.getText());

            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }

    //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (tfVeiculoPlaca.getText() == null || tfVeiculoPlaca.getText().isEmpty()) {
            errorMessage += "Placa inválida!\n";
        }

        if (cbVeiculoMarca.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma marca!\n";
        }

        if (cbVeiculoModelo.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um modelo!\n";
        }

        if (cbVeiculoCor.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma cor!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}
