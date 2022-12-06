package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroClienteDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private DatePicker dpDataCadastro;

    @FXML
    private DatePicker dpDataNascimento;

    @FXML
    private TextField tfCPFCNPJ;

    @FXML
    private TextField tfCelular;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfInscEstadual;

    @FXML
    private TextField tfNome;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Cliente cliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.tfNome.setText(this.cliente.getNome());
        this.tfCelular.setText(this.cliente.getCelular());
        this.tfEmail.setText(this.cliente.getEmail());
        this.dpDataCadastro.setValue(this.cliente.getDataCadastro());

        if (cliente instanceof PessoaFisica) {
            tfCPFCNPJ.setText(((PessoaFisica) this.cliente).getCpf());
            dpDataNascimento.setValue(((PessoaFisica) this.cliente).getDataNascimento());
            tfInscEstadual.setDisable(true);
            dpDataNascimento.setDisable(false);
        } else {
            tfCPFCNPJ.setText(((PessoaJuridica) this.cliente).getCnpj());
            tfInscEstadual.setText(((PessoaJuridica) this.cliente).getInscricaoEstadual());
            dpDataNascimento.setDisable(true);
            tfInscEstadual.setDisable(false);
        }
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            cliente.setNome(tfNome.getText());
            cliente.setCelular(tfCelular.getText());
            cliente.setEmail(tfEmail.getText());
            cliente.setDataCadastro(dpDataCadastro.getValue());

            if (cliente instanceof PessoaFisica) {
                ((PessoaFisica) cliente).setCpf(tfCPFCNPJ.getText());
                ((PessoaFisica) cliente).setDataNascimento(dpDataNascimento.getValue());
            } else {
                ((PessoaJuridica) cliente).setCnpj(tfCPFCNPJ.getText());
                ((PessoaJuridica) cliente).setInscricaoEstadual(tfInscEstadual.getText());
            }
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }

    @FXML
    public void handlePessoaFisica() {
        this.tfInscEstadual.setDisable(true);
        this.dpDataNascimento.setDisable(false);
    }

    @FXML
    public void handlePessoaJuridica() {
        this.tfInscEstadual.setDisable(false);
        this.dpDataNascimento.setDisable(true);
    }

    //método para validar a entrada de dados
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfNome.getText() == null || this.tfNome.getText().length() == 0) {
            errorMessage += "Nome inválido.\n";
        }

        if (this.tfCelular.getText() == null || this.tfCelular.getText().length() == 0) {
            errorMessage += "Celular inválido.\n";
        }

        if (this.tfEmail.getText() == null || this.tfEmail.getText().length() == 0) {
            errorMessage += "E-mail inválido.\n";
        }

        if (this.dpDataCadastro.getValue() == null) {
            errorMessage += "Data inválida.\n";
        }

        if (cliente instanceof PessoaFisica) {
            if (this.tfCPFCNPJ.getText() == null || this.tfCPFCNPJ.getText().length() == 0) {
                errorMessage += "CPF inválido. \n";
            }

            if (this.dpDataNascimento.getValue() == null) {
                errorMessage += "Data de Nascimento inválida. \n";
            }
        } else {
            if (this.tfCPFCNPJ.getText() == null || this.tfCPFCNPJ.getText().length() == 0) {
                errorMessage += "CNPJ inválido. \n";
            }

            if (this.tfInscEstadual.getText() == null || this.tfInscEstadual.getText().length() == 0) {
                errorMessage += "Inscrição Estadual inválida. \n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            //exibindo uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}
