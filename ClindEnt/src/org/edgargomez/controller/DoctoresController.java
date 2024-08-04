package org.edgargomez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.edgargomez.bean.Doctor;
import org.edgargomez.bean.Especialidad;
import org.edgargomez.db.Conexion;
import org.edgargomez.system.Principal;


public class DoctoresController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;   
    private ObservableList<Doctor> listaDoctor; 
    private ObservableList<Especialidad> listaEspecialidad;
    
    @FXML private TextField txtNumeroColegiado;
    
    @FXML private TextField txtNombreDoctor;
    
    @FXML private TextField txtApellidoDoctor;
    
    @FXML private TextField txtTelefonoContacto;
    
    @FXML private ComboBox cmbCodigoEspecialidad;
    
    @FXML private GridPane gprDoctor;
    
    @FXML private TableView tblDoctor;
    
    @FXML private TableColumn colNumeroColegiado;
    
    @FXML private TableColumn colNombreDoctor;
    
    @FXML private TableColumn colApellidoDoctor;
    
    @FXML private TableColumn colTelefono;
    
    @FXML private TableColumn colCodigoEspecialidad;
    
    @FXML private Button btnNuevo;
    
    @FXML private Button btnEliminar;
    
    @FXML private Button btnEditar;
    
    @FXML private Button btnReporte;
    
    @FXML private ImageView imgNuevo;
    
    @FXML private ImageView imgEliminar;
    
    @FXML private ImageView imgEditar;
    
    @FXML private ImageView imgReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     cargarDatos();   
     cmbCodigoEspecialidad.setItems(getEspecialidad());
    }  
    
    public void cargarDatos(){
        tblDoctor.setItems(getDoctor());
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("numeroColegiado"));
        colNombreDoctor.setCellValueFactory(new PropertyValueFactory<Doctor, String>("nombresDoctores"));
        colApellidoDoctor.setCellValueFactory(new PropertyValueFactory<Doctor, String>("apellidosDoctor"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Doctor, String>("telefonoContacto"));
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("codigoEspecialidad"));
    }
    
    public ObservableList<Doctor>getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        try{
            PreparedStatement  procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarDoctores}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Doctor(resultado.getInt("numeroColegiado"),
                                     resultado.getString("nombresDoctores"),
                                     resultado.getString("apellidosDoctor"),
                                     resultado.getString("telefonoContacto"),
                                     resultado.getInt("codigoEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctor = FXCollections.observableArrayList(lista);
    }
    
        public ObservableList<Especialidad> getEspecialidad(){
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEspecialidad}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Especialidad(resultado.getInt("codigoEspecialidas"),
                                           resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEspecialidad = FXCollections.observableArrayList(lista);
    }
      
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setText("Actualizar");
                btnReporte.setText("Detalles");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/edgargomez/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/edgargomez/image/Cancelar.png"));
                txtNumeroColegiado.setEditable(false);
                tipoDeOperacion = operaciones.GUARDAR;
            break;
            
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/edgargomez/image/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/edgargomez/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break; 
        }
    }
    
    public void guardar(){
        Doctor listado = new Doctor();
        listado.setNumeroColegiado(Integer.parseInt(txtNumeroColegiado.getText()));
        listado.setNombresDoctores(txtNombreDoctor.getText());
        listado.setApellidosDoctor(txtApellidoDoctor.getText());
        listado.setTelefonoContacto(txtTelefonoContacto.getText());
        listado.setCodigoEspecialidad(((Especialidad)cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarDoctor(?,?,?,?,?)}");
            procedimiento.setInt(1,listado.getNumeroColegiado());
            procedimiento.setString(2, listado.getNombresDoctores());
            procedimiento.setString(3, listado.getApellidosDoctor());
            procedimiento.setString(4, listado.getTelefonoContacto());
            procedimiento.setInt(5, listado.getCodigoEspecialidad());
            procedimiento.execute();
            listaDoctor.add(listado);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
        public void eliminar(){
        switch (tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/edgargomez/image/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/edgargomez/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
            default:
                if(tblDoctor.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Estas seguro de Eliminar el registro?", "Eliminar Doctor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){  
                   try{
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall(("Call sp_EliminarDoctor(?)"));                           
                    procedimiento.setInt(1, ((Doctor)tblDoctor.getSelectionModel().getSelectedItem()).getNumeroColegiado());
                    procedimiento.execute();
                    listaDoctor.remove(tblDoctor.getSelectionModel().getFocusedIndex());
                    limpiarControles();
                }catch(Exception e){
                       e.printStackTrace();
                       }
                   }
            }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un Elemento");
            }
        }
    }
        
     public void editar(){
        switch (tipoDeOperacion){
            case NINGUNO:
                if(tblDoctor.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/edgargomez/image/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/edgargomez/image/Cancelar.png"));
                    activarControles();
                    txtNumeroColegiado.setEditable(false);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un Elemento");
                break;
                
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/edgargomez/image/Actualizar.png"));
                imgReporte.setImage(new Image("/org/edgargomez/image/reporte.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;   
        }
    }
     
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarDoctor(?, ?, ?, ?, ?)}");
            Doctor registro = (((Doctor)tblDoctor.getSelectionModel().getSelectedItem()));
            registro.setNombresDoctores(txtNombreDoctor.getText());
            registro.setApellidosDoctor(txtApellidoDoctor.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            procedimiento.setInt(1, registro.getNumeroColegiado());
            procedimiento.setString(2, registro.getNombresDoctores());
            procedimiento.setString(3, registro.getApellidosDoctor());
            procedimiento.setString(4, registro.getTelefonoContacto());
            procedimiento.setInt(5, registro.getCodigoEspecialidad()); 
        }catch (Exception e){
            e.printStackTrace();
        }
    }
 
    public void seleccionarElemento(){
        if(tblDoctor.getSelectionModel().getSelectedItem() != null){
            try{
               txtNumeroColegiado.setText(String.valueOf(((Doctor)tblDoctor.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
               txtNombreDoctor.setText((((Doctor)tblDoctor.getSelectionModel().getSelectedItem()).getNombresDoctores()));
               txtApellidoDoctor.setText((((Doctor)tblDoctor.getSelectionModel().getSelectedItem()).getApellidosDoctor()));
               txtTelefonoContacto.setText((((Doctor)tblDoctor.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
               cmbCodigoEspecialidad.setValue((((Doctor)tblDoctor.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
            }catch(Exception e){
                e.printStackTrace();
            }   
        }else {
            JOptionPane.showMessageDialog(null, "NO HAY DATOS");
        }
    } 
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/edgargomez/image/Editar.png"));
                imgReporte.setImage(new Image("/org/edgargomez/image/Reporte.png"));
                tblDoctor.getSelectionModel().clearSelection();
                limpiarControles();
                cargarDatos();
            break;
        }
    }
    
    public void desactivarControles(){
        txtNumeroColegiado.setEditable(false);
        txtNombreDoctor.setEditable(false);
        txtApellidoDoctor.setEditable(false);
        txtTelefonoContacto.setEditable(false);
    }
    
    public void activarControles(){
        txtNumeroColegiado.setEditable(true);
        txtNombreDoctor.setEditable(true);
        txtApellidoDoctor.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNumeroColegiado.clear();
        txtNombreDoctor.clear();
        txtApellidoDoctor.clear();
        txtTelefonoContacto.clear();
        tblDoctor.getSelectionModel().clearSelection();
        cmbCodigoEspecialidad.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void ventanaDoctor (){
        escenarioPrincipal.menuPrincipal();
    }  
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}

