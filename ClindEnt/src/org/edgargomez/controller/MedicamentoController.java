package org.edgargomez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.edgargomez.bean.Medicamento;
import org.edgargomez.db.Conexion;
import org.edgargomez.system.Principal;


public class MedicamentoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medicamento> listaMedicamento; 
    
    
    @FXML
    private TextField txtCodigoMedicamento;
    
    @FXML
    private TextField txtNombreMedicamento;
    
    @FXML
    private GridPane grpFecha;
    
    @FXML
    private TableView tblMedicamento;
    
    @FXML
    private TableColumn colCodMedicamento;
    
    @FXML
    private TableColumn colNombreMedicamento;
    
    @FXML
    private Button btnNuevo;
    
    @FXML
    private Button btnEliminar;
    
    @FXML
    private Button btnEditar;
    
    @FXML
    private Button btnReporte;
    
    @FXML
    private ImageView imgNuevo;
    
    @FXML
    private ImageView imgEliminar;
    
    @FXML
    private ImageView imgEditar;
    
    @FXML
    private ImageView imgReportes;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) { 
    cargarDatos(); 
    }
    
    public void cargarDatos(){
        tblMedicamento.setItems(getMedicamento());
        colCodMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, Integer>("codigoMedicamento"));
        colNombreMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, String>("nombreMedicamento"));
    }
//    
    public ObservableList<Medicamento>getMedicamento(){
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarMedicamentos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medicamento(resultado.getInt("codigoMedicamento"),
                                     resultado.getString("nombreMedicamento")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaMedicamento = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
      if(tblMedicamento.getSelectionModel().getSelectedItem() != null){
                try{
                    txtCodigoMedicamento.setText(String.valueOf((((Medicamento)tblMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento())));
                    txtNombreMedicamento.setText((((Medicamento)tblMedicamento.getSelectionModel().getSelectedItem()).getNombreMedicamento()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                
            }else{
                JOptionPane.showMessageDialog(null, "No hay Datos");
            }
        }
    
    
//    public Medicamento buscarMedicamento (int codigoMedicamento){
//            Medicamento resultado = null;
//                try{
//                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarMedicamento(?)}");
//                    procedimiento.setInt(1, codigoMedicamento);
//                    ResultSet registro = procedimiento.executeQuery();
//                    while(registro.next()){
//                        resultado = new Medicamento(registro.getInt("codigoMedicamento"),
//                            registro.getString("nombreMedicamento"));
//                    }
//                    
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//                return resultado;
//        }
    
    
     public void eliminar (){
        
            switch (tipoDeOperacion){
                case GUARDAR:
                        desactivarControles();
                        limpiarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setDisable (false);
                        btnReporte.setDisable(false);
                        tipoDeOperacion = operaciones.NINGUNO;
                          break;
                          
                default:
                    if(tblMedicamento.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registtro?", "Eliminar Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarMedicamento(?)}");
                                procedimiento.setInt(1, ((Medicamento)tblMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
                                procedimiento.execute();
                                listaMedicamento.remove(tblMedicamento.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                                }catch(Exception e){
                                e.printStackTrace();
                            }
                        
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento");
                    }
                    
            }
        }
     public void editar(){
         
         switch(tipoDeOperacion){
             case NINGUNO:
                 if(tblMedicamento.getSelectionModel().getSelectedItem() != null){
                     btnEditar.setText("Actualizar");
                     btnReporte.setText("Cancelar");
                     btnNuevo.setDisable(true);
                     btnEliminar.setDisable(true);
                     imgEditar.setImage(new Image("/org/edgargomez/image/Actualizar.png"));
                     imgReportes.setImage(new Image("/org/edgargomez/image/Cancelar.png"));
                     activarControles();
                     txtCodigoMedicamento.setEditable(false);
                     tipoDeOperacion = operaciones.ACTUALIZAR;
                     
                     
                 }else 
                     JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento");
                 break;
                 
             case ACTUALIZAR:
                 actualizar();
                 btnEditar.setText("Editar");
                 btnReporte.setText("Reporte");
                 btnNuevo.setDisable(false);
                 btnEliminar.setDisable(false);
                 imgEditar.setImage(new Image("/org/edgargomez/image/Editar.png"));
                 imgReportes.setImage(new Image("/org/edgargomez/image/reporte.png"));
                 desactivarControles();
                 limpiarControles();
                 tipoDeOperacion = operaciones.NINGUNO;//
                 cargarDatos();
                 break;
             
         }
         
    }
     

     public void actualizar(){
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarMedicamento(?, ?)}");
             Medicamento registro = (Medicamento)tblMedicamento.getSelectionModel().getSelectedItem();
             registro.setNombreMedicamento(txtNombreMedicamento.getText());
            
             procedimiento.setInt(1, registro.getCodigoMedicamento());
             procedimiento.setString(2, registro.getNombreMedicamento());
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
                 imgReportes.setImage(new Image("/org/edgargomez/image/reporte.png"));
                 tipoDeOperacion = operaciones.NINGUNO;
                 break; 
                  
        }
     
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
                txtCodigoMedicamento.setEditable(false);
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
        Medicamento listado = new Medicamento();
        listado.setNombreMedicamento(txtNombreMedicamento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarMedicamento(?)}");
            procedimiento.setString(1, listado.getNombreMedicamento()); 
            procedimiento.execute(); 
            listaMedicamento.add(listado); 
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtCodigoMedicamento.setEditable(false);
        txtNombreMedicamento.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoMedicamento.setEditable(true);
        txtNombreMedicamento.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoMedicamento.clear();
        txtNombreMedicamento.clear();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaMedicamento(){
        escenarioPrincipal.menuPrincipal();
}
    public void menuPrincipal (){
        escenarioPrincipal.menuPrincipal();
    }
}
