package org.edgargomez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.edgargomez.bean.Cita;
import org.edgargomez.bean.Doctor;
import org.edgargomez.bean.Paciente;
import org.edgargomez.db.Conexion;
import org.edgargomez.system.Principal;

public class CitaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, ELIMINAR, GUARDAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Cita> listaCita;
    private ObservableList<Paciente> listaPaciente;
    private ObservableList<Doctor> listaDoctor;
    private DatePicker fCita;
    
        @FXML private TextField txtCodigoCita;
        @FXML private TextField txtFechaCita;
        @FXML private TextField txtHoraCita;
        @FXML private TextField txtTratamiento;
        @FXML private TextField txtDescripCondActual;
        @FXML private TextField txtCodigoPaciente;
        @FXML private GridPane grpFechas;
        @FXML private ComboBox cmbCodigoPaciente;
        @FXML private ComboBox cmbNumeroColegiado;
        @FXML private TableView tblCitas;
        @FXML private TableColumn colCodigoCita;
        @FXML private TableColumn colFechaCita;
        @FXML private TableColumn colHoraCita;  
        @FXML private TableColumn colTratamiento;
        @FXML private TableColumn colDescripCondActual;
        @FXML private TableColumn colCodigoPaciente;
        @FXML private TableColumn colNumeroColegiado;
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
        fCita = new DatePicker(Locale.ENGLISH);
        fCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fCita.getCalendarView().todayButtonTextProperty().set("Today");
        fCita.getCalendarView().setShowWeeks(false);
        grpFechas.add(fCita, 3, 0);
        fCita.getStylesheets().add("/org/edgargomez/resource/DatePicker.css");
        cmbCodigoPaciente.setItems(getPaciente());
        cmbNumeroColegiado.setItems(getDoctor());
        cmbCodigoPaciente.setDisable(true);
        cmbNumeroColegiado.setDisable(true);
    }
    
    public void cargarDatos(){
        tblCitas.setItems(getCita());
        colCodigoCita.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("codigoCita"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<Cita, Date>("fechaCita"));
        colHoraCita.setCellValueFactory(new PropertyValueFactory<Cita, Time>("horaCita"));
        colTratamiento.setCellValueFactory(new PropertyValueFactory<Cita, String>("tratamiento"));
        colDescripCondActual.setCellValueFactory(new PropertyValueFactory<Cita, String>("descripCondActual"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("codigoPaciente"));
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("numeroColegiado")); 
    }
    
    public ObservableList<Cita> getCita(){
        ArrayList<Cita> lista = new ArrayList<Cita>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarCitas()}");
           ResultSet resultado = procedimiento.executeQuery();
           while(resultado.next()){
               lista.add(new Cita(resultado.getInt("codigoCita"),
                                            resultado.getDate("fechaCita"),
                                            resultado.getTime("horaCita"),
                                            resultado.getString("tratamiento"),
                                            resultado.getString("descripcionCondActual"),
                                            resultado.getInt("codigoPaciente"),
                                            resultado.getInt("numeroColegiado")));
                            }
                         }catch(Exception e){
                             e.printStackTrace();
                         }
                            return listaCita = FXCollections.observableArrayList(lista);
        }
    
    
    public ObservableList<Paciente>getPaciente(){
        ArrayList<Paciente> lista = new ArrayList <Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPacientes()}");
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
    
        public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
        try{
          PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1,codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Paciente(registro.getInt("codigoPaciente"),
                                                        registro.getString("nombresPaciente"),
                                                        registro.getString("apellidosPaciente"),
                                                        registro.getString("sexo"),
                                                        registro.getDate("fechaNacimiento"),
                                                        registro.getString("direccionPaciente"),
                                                        registro.getString("telefonoPersonal"),
                                                        registro.getDate("fechaPrimeraVisita"));        
                            }
                        }catch(Exception e){
                          e.printStackTrace();
                        } 
                             return resultado; 
    }
        
    public ObservableList<Doctor>getDoctor(){
        ArrayList<Doctor>lista = new ArrayList<Doctor>();
        try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctores()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Doctor (resultado.getInt("numeroColegiado"),
                                             resultado.getString("nombresDoctores"),
                                             resultado.getString("apellidosDoctor"),
                                             resultado.getString("telefonoContacto"),
                                             resultado.getInt("codigoEspecialidad")) );
                            }
                        }catch(Exception e){
                        e.printStackTrace();
                    }
                            return listaDoctor = FXCollections.observableArrayList(lista);
}
    
    public Doctor buscarDoctor(int NumeroColegiado){
        Doctor resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDoctor(?)}");
            procedimiento.setInt(1, NumeroColegiado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado= new Doctor(registro.getInt("numeroColegiado"),
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
           
    public void seleccionarElemento(){        
        if(tblCitas.getSelectionModel().getSelectedItem() != null){
                txtCodigoCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita()));  
                fCita.selectedDateProperty().set(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getFechaCita());
                txtTratamiento.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getTratamiento());
                txtDescripCondActual.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getDescripCondActual()); 
                cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
                cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
        }
        else{
              JOptionPane.showMessageDialog(null, "No hay registro en el lugar seleccionado");
        }
    }
 
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/edgargomez/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/edgargomez/image/Cancelar.png"));
                txtCodigoCita.setDisable(false);
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
        Cita registro = new Cita();
         registro.setFechaCita(fCita.getSelectedDate());
         registro.setHoraCita(Time.valueOf(txtHoraCita.getText()));
         registro.setTratamiento(txtTratamiento.getText());
         registro.setDescripCondActual(txtDescripCondActual.getText());
         registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
         registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
         cargarDatos();
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarCita(?, ?, ?, ?, ?, ?)}");
             procedimiento.setTime(1, registro.getHoraCita());
             procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
             procedimiento.setString(3, registro.getTratamiento());
             procedimiento.setString(4, registro.getDescripCondActual());
             procedimiento.setInt(5, registro.getCodigoPaciente());
             procedimiento.setInt(6, registro.getNumeroColegiado());
             procedimiento.execute();
             listaCita.add(registro);
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
                if(tblCitas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de querer eliminar este dato?", "Eliminar Cita", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCita(?)}");
                                procedimiento.setInt(1, ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita());
                                procedimiento.execute();
                                listaCita.remove(tblCitas.getSelectionModel().getSelectedIndex());
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
                if(tblCitas.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/edgargomez/image/Editar.png"));
                    imgReporte.setImage(new Image("/org/edgargomez/image/Cancelar.png"));
                    activarControles();
                    cmbNumeroColegiado.setDisable(true);
                    txtCodigoCita.setEditable(false);
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
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarCita(?, ?, ?, ?, ?)}");
             Cita registro = (Cita)tblCitas.getSelectionModel().getSelectedItem();
             registro.setFechaCita(fCita.getSelectedDate());
             procedimiento.setDate(1, new java.sql.Date(registro.getFechaCita().getTime()));
             procedimiento.setString(2, registro.getTratamiento());
             procedimiento.setString(3, registro.getDescripCondActual());
             procedimiento.setInt(4, registro.getCodigoPaciente());
             procedimiento.setInt(5, registro.getNumeroColegiado());
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
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/edgargomez/image/Editar.png"));
                imgReporte.setImage(new Image("/org/edgargomez/image/Reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
      public void desactivarControles(){
          txtCodigoCita.setEditable(false);
          txtTratamiento.setEditable(false);
          txtDescripCondActual.setEditable(false);
          cmbCodigoPaciente.setDisable(true);
          cmbNumeroColegiado.setDisable(true);
      }
      
      public void activarControles(){
          txtCodigoCita.setEditable(false);
          txtTratamiento.setEditable(true);
          txtDescripCondActual.setEditable(true);
          cmbCodigoPaciente.setDisable(false);
          cmbNumeroColegiado.setDisable(false);
      }
      
      public void limpiarControles(){
          txtCodigoCita.clear();
          txtTratamiento.clear();
          fCita.setSelectedDate(null);
          txtDescripCondActual.clear();
          tblCitas.getSelectionModel().clearSelection();
         cmbCodigoPaciente.getSelectionModel().clearSelection();
         cmbNumeroColegiado.getSelectionModel().clearSelection();  
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
