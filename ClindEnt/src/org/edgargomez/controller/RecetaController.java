package org.edgargomez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.edgargomez.bean.Doctor;
import org.edgargomez.bean.Receta;
import org.edgargomez.db.Conexion;
import org.edgargomez.system.Principal;

public class RecetaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO, ELIMINAR, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Receta> listaReceta;
    private ObservableList<Doctor> listaDoctor;
    private DatePicker fReceta;
    
    @FXML private TextField txtCodigoReceta;
    @FXML private GridPane grpReceta;
    @FXML private TableView tblReceta;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colFechaReceta;
    @FXML private TableColumn colNumeroColegiado;
    @FXML private ComboBox cmbNumeroColegiado;
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
        fReceta = new DatePicker (Locale.ENGLISH);
        fReceta.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fReceta.getCalendarView().todayButtonTextProperty().set("Today");
        fReceta.getCalendarView().setShowWeeks(false);
        grpReceta.add(fReceta, 4, 0);
        fReceta.getStylesheets().add("/org/edgargomez/resource/DatePicker.css");
        cmbNumeroColegiado.setItems(getDoctor());
        cmbNumeroColegiado.setDisable(true);
        fReceta.setDisable(true);
    }
    
    public void cargarDatos(){
        tblReceta.setItems(getReceta());
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<Receta, Integer>("codigoReceta"));
        colFechaReceta.setCellValueFactory(new PropertyValueFactory<Receta, Date>("fechaReceta"));
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Receta, Integer>("numeroColegiado"));
    }
    
     public ObservableList<Receta> getReceta(){
        ArrayList<Receta> lista = new ArrayList<Receta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new Receta(resultado.getInt("codigoReceta"),
                    resultado.getDate("fechaReceta"),
                    resultado.getInt("numeroColegiado")));
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaReceta = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Doctor> getDoctor(){
        ArrayList<Doctor> lista= new ArrayList<Doctor>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarDoctores}");
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
                return listaDoctor =FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){        
        if(tblReceta.getSelectionModel().getSelectedItem() != null){
            txtCodigoReceta.setText(String.valueOf(((Receta)tblReceta.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            fReceta.selectedDateProperty().set(((Receta)tblReceta.getSelectionModel().getSelectedItem()).getFechaReceta());
            cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Receta)tblReceta.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
        }
        else{
              JOptionPane.showMessageDialog(null, "No hay registro en el lugar seleccionado");
        }
    }
    
    public Doctor buscarDoctor(int numeroColegiado){
        Doctor resultado = null;
            try{
              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarReceta(?)}");
                procedimiento.setInt(1,numeroColegiado);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new Doctor(registro.getInt("numeroColegiado"),
                                                        registro.getString("nombresDoctor"),
                                                        registro.getString("apellidosDoctor"),
                                                        registro.getString("telefonoContacto"),
                                                        registro.getInt("codigoEspecialidad"));
                                }
                            }catch(Exception e){
                              e.printStackTrace();
                    } 
                    return resultado; 
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
                txtCodigoReceta.setDisable(false);
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
                
            case CANCELAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/edgargomez/image/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/edgargomez/image/Eliminar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                cargarDatos();
                break;
        }              
    }
    
    public void guardar(){
        Receta registro = new Receta();
        registro.setFechaReceta(fReceta.getSelectedDate());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
            try{
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarReceta(?,?)}");
                    procedimiento.setDate(1, new java.sql.Date(registro.getFechaReceta().getTime()));
                    procedimiento.setInt(2, registro.getNumeroColegiado());
                    procedimiento.execute();
                    listaReceta.add(registro);
                }catch(Exception e){
                    e.printStackTrace();
            }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
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
                if(tblReceta.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de querer eliminar este dato?", "Eliminar Receta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                         if (respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarReceta(?)}");
                                procedimiento.setInt(1, ((Receta)tblReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
                                procedimiento.execute();
                                listaReceta.remove(tblReceta.getSelectionModel().getSelectedIndex());
                                limpiarControles();   
                        }catch(Exception e){
                            e.printStackTrace();
                        }     
                    }else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        tipoDeOperacion = operaciones.NINGUNO;
                    }     
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
     public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblReceta.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/edgargomez/image/Editar.png"));
                    imgReporte.setImage(new Image("/org/edgargomez/image/Cancelar.png"));
                    activarControles();
                    cmbNumeroColegiado.setDisable(true);
                    txtCodigoReceta.setEditable(false);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    }else
                       JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                break;
                
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/edgargomez/image/Editar.png"));
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarReceta(?,?)}");
            Receta registro = (Receta)tblReceta.getSelectionModel().getSelectedItem();
            registro.setFechaReceta(fReceta.getSelectedDate());
            procedimiento.setInt(1, registro.getCodigoReceta());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/edgargomez/image/Editar.png"));
                imgReporte.setImage(new Image("/org/edgargomez/image/Cancelar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
        }
    }
    
    public void desactivarControles(){
        txtCodigoReceta.setDisable(false);
        fReceta.setDisable(true);
        cmbNumeroColegiado.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoReceta.setDisable(false);
        fReceta.setDisable(false);
        cmbNumeroColegiado.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoReceta.clear();
        fReceta.setSelectedDate(null);
        tblReceta.getSelectionModel().clearSelection();
        cmbNumeroColegiado.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal; 
    }
     
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }  
}
