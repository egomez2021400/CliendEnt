package org.edgargomez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.edgargomez.bean.Usuario;
import org.edgargomez.db.Conexion;
import org.edgargomez.system.Principal;


public class UsuarioController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {NINGUNO, GUARDAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    @FXML private TextField txtCodigoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtPassword;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/edgargomez/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/edgargomez/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("/org/edgargomez/image/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/edgargomez/image/Eliminar.png"));
                tipoDeOperacion  = operaciones.NINGUNO;
                break;
        }
    }

    public void cancelar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/edgargomez/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/edgargomez/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("/org/edgargomez/image/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/edgargomez/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setContrasena(txtPassword.getText());
        try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarUsuario(?, ?, ?, ?)}");
             procedimiento.setString(1, registro.getNombreUsuario());
             procedimiento.setString(2, registro.getApellidoUsuario());
             procedimiento.setString(3, registro.getUsuarioLogin());
             procedimiento.setString(4, registro.getContrasena());
             procedimiento.execute();
             JOptionPane.showConfirmDialog(null, "Datos ingresados exitosamente");
             ventanaLogin();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtPassword.setEditable(false);
        btnEliminar.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoUsuario.setEditable(true);
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtPassword.setEditable(true);
        btnEliminar.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoUsuario.clear();
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtPassword.clear();
    }
    public Principal getEscenariosPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenariosPrincipal(Principal escenariosPrincipal) {
        this.escenarioPrincipal = escenariosPrincipal;
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
}
