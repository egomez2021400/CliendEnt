package org.edgargomez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.edgargomez.bean.Paciente;
import org.edgargomez.db.Conexion;
import org.edgargomez.report.GenerarReporte;
import org.edgargomez.system.Principal;


public class PacientesController implements Initializable{
    private Principal escenarioPrincipal; 
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}; 
    private operaciones tipoDeOperacion = operaciones.NINGUNO; 
    private ObservableList<Paciente> listaPaciente; 
    private DatePicker fNacimiento, fPV;
    
    @FXML private TextField txtCodigoPaciente; 
    
    @FXML private TextField txtNomPaciente; 
    
    @FXML private TextField txtApellido;  
    
    @FXML private TextField txtSexo; 
    
    @FXML private TextField txtDireccion; 
    
    @FXML private TextField txtTelefonoPersonal;
    
    @FXML private GridPane grpFecha; 
    
    @FXML private TableView tblPaciente; 
    
    @FXML private TableColumn colPaciente; 
    
    @FXML private TableColumn colNomPaciente; 
    
    @FXML private TableColumn colApellidoPaciente;  
    
    @FXML private TableColumn colSexo;  
    
    @FXML private TableColumn colFechaNacimiento;  
    
    @FXML private TableColumn colDireccion; 
    
    @FXML private TableColumn colTelefono;  
    
    @FXML private TableColumn colPrimerVisita; 
    
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
        fNacimiento = new DatePicker(Locale.ENGLISH);
        fNacimiento.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fNacimiento.getCalendarView().todayButtonTextProperty().set("Today");
        fNacimiento.getCalendarView().setShowWeeks(false);
        grpFecha.add(fNacimiento, 3, 1);
        fPV = new DatePicker(Locale.ENGLISH);
        fPV.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fPV.getCalendarView().todayButtonTextProperty().set("today");
        fPV.getCalendarView().setShowWeeks(true);
        grpFecha.add(fPV, 4, 2);
        fNacimiento.getStylesheets().add("/org/edgargomez/resource/DataPicker.css");
        fPV.getStylesheets().add("/org/edgargomez/resource/DataPicker.css");
    }
    
    public void cargarDatos(){
        tblPaciente.setItems(getPaciente());
        colPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("codigoPaciente"));
        colNomPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, String>("nombresPaciente"));
        colApellidoPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, String>("apellidosPacientes"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente, String>("sexo"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente, Date>("fechaNacimiento"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("direccionPaciente"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Paciente, String>("telefonoPersonal"));
        colPrimerVisita.setCellValueFactory(new PropertyValueFactory<Paciente, Date>("fechaPrimeraVisita"));
        
    }
    
    public ObservableList<Paciente>getPaciente(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"), 
                                       resultado.getString("nombresPaciente"), 
                                       resultado.getString("apellidosPacientes"), 
                                       resultado.getString("sexo"), 
                                       resultado.getDate("fechaNacimiento"), 
                                       resultado.getString("direccionPaciente"), 
                                       resultado.getString("telefonoPersonal"),
                                       resultado.getDate("fechaPrimeraVisita"))); 
             }         
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableArrayList(lista);
    }
    
     public void seleccionarElemento (){
        if(tblPaciente.getSelectionModel().getSelectedItem() != null){
        try{
            txtCodigoPaciente.setText(String.valueOf(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
            txtNomPaciente.setText((((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getNombresPaciente()));
            txtApellido.setText((((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getApellidosPacientes()));
            txtSexo.setText((((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getSexo()));
            fNacimiento.selectedDateProperty().set(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getFechaNacimiento());
            txtDireccion.setText(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getDireccionPaciente());
            txtTelefonoPersonal.setText(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
            fPV.selectedDateProperty().set(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getFechaPrimeraVisita());
        }catch(Exception e){
            e.printStackTrace();
        }
    }else {
            JOptionPane.showMessageDialog(null, "No hay Datos");
            
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
                if(tblPaciente.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar el registro?", "Eliminar Paciente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall(("Call sp_EliminarPaciente(?)"));                           
                            procedimiento.setInt(1, ((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                            procedimiento.execute();
                            listaPaciente.remove(tblPaciente.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un Elemento");
                }
        }
    }
           public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblPaciente.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/edgargomez/image/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/edgargomez/image/Cancelar.png"));
                    activarControles();
                    txtCodigoPaciente.setEditable(false);
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
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarPaciente(?, ?, ?, ?, ?, ?, ?, ?)}");
         Paciente registro = (((Paciente)tblPaciente.getSelectionModel().getSelectedItem()));
         registro.setNombresPaciente(txtNomPaciente.getText());
         registro.setApellidosPacientes(txtApellido.getText());
         registro.setSexo(txtSexo.getText());
         registro.setFechaNacimiento(fNacimiento.getSelectedDate());
         registro.setDireccionPaciente(txtDireccion.getText());
         registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
         registro.setFechaPrimeraVisita(fPV.getSelectedDate());
        
         procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getNombresPaciente());
            procedimiento.setString(3, registro.getApellidosPacientes());
            procedimiento.setString(4, registro.getSexo());
            procedimiento.setDate(5, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(6, registro.getDireccionPaciente());
            procedimiento.setString(7, registro.getTelefonoPersonal());
            procedimiento.setDate(8, new java.sql.Date(registro.getFechaPrimeraVisita().getTime()));
            procedimiento.execute();
         
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/edgargomez/image/Editar.png"));
                imgReporte.setImage(new Image("/org/edgargomez/image/reporte.png"));
                tblPaciente.getSelectionModel().clearSelection();
                limpiarControles();
                cargarDatos();
            break;
        }
    }
      
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoPaciente", null);
        GenerarReporte.mostrarReporte("report1.jasper", "Reporte de Pacientes", parametros);
    }
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/edgargomez/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/edgargomez/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
            break;
            
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
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
        Paciente registro = new Paciente();
        registro.setCodigoPaciente(Integer.parseInt(txtCodigoPaciente.getText()));
        registro.setNombresPaciente(txtNomPaciente.getText());
        registro.setApellidosPacientes(txtApellido.getText());
        registro.setSexo(txtSexo.getText());
        registro.setFechaNacimiento(fNacimiento.getSelectedDate());
        registro.setDireccionPaciente(txtDireccion.getText());
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setFechaPrimeraVisita(fPV.getSelectedDate());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarPaciente(?,?,?,?,?,?,?,?)}");
            procedimiento.setInt(1,registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getNombresPaciente());
            procedimiento.setString(3, registro.getApellidosPacientes());
            procedimiento.setString(4, registro.getSexo());
            procedimiento.setDate(5,new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(6, registro.getDireccionPaciente());
            procedimiento.setString(7, registro.getTelefonoPersonal());
            procedimiento.setDate(8, new java.sql.Date(registro.getFechaPrimeraVisita().getTime()));      
            procedimiento.execute();
            listaPaciente.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        
        txtCodigoPaciente.setEditable(false);
        txtNomPaciente.setEditable(false); 
        txtApellido.setEditable(false);
        txtSexo.setEditable(false); 
        txtDireccion.setEditable(false);
        txtTelefonoPersonal.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoPaciente.setEditable(true); 
        txtNomPaciente.setEditable(true); 
        txtApellido.setEditable(true); 
        txtSexo.setEditable(true); 
        txtDireccion.setEditable(true);        
        txtTelefonoPersonal.setEditable(true);
    }
    
    public void limpiarControles(){
        
        txtCodigoPaciente.clear();
        txtNomPaciente.clear();
        txtApellido.clear();
        txtSexo.clear();
        txtDireccion.clear();        
        txtTelefonoPersonal.clear();
        tblPaciente.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
