package org.edgargomez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.edgargomez.bean.Especialidad;
import org.edgargomez.db.Conexion;
import org.edgargomez.report.GenerarReporte;
import org.edgargomez.system.Principal;

    
public class EspecialidadesController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidad> listaEspecialidad;
        @FXML 
        private TextField txtCodigoEspecialidad;
            
        @FXML 
        private TextField txtDescripcion;
            
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
        private ImageView imgReporte;
        
        @FXML 
        private GridPane grpFechas;
        
        @FXML 
        private TableView tblEspecialidad;
        
        @FXML 
        private TableColumn colCodigoEspecialidad;
        
        @FXML 
        private TableColumn colDescripcion;   

@Override
public void initialize(URL location, ResourceBundle resources) {
    cargarDatos();
}
    
public void cargarDatos() {
    tblEspecialidad.setItems(getEspecialidad());
    colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidad, Integer>("codigoEspecialidad"));
    colDescripcion.setCellValueFactory(new PropertyValueFactory<Especialidad, String>("descripcion"));    
}
    
public ObservableList<Especialidad> getEspecialidad() {
    ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEspecialidades()}");
        ResultSet resultado = procedimiento.executeQuery();
        while (resultado.next()) {
            lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                                        resultado.getString("descripcion")));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaEspecialidad = FXCollections.observableArrayList(lista);
}
    public void seleccionarElemento(){
            if(tblEspecialidad.getSelectionModel().getSelectedItem() !=null){
                try{
                    txtCodigoEspecialidad.setText(String.valueOf(((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
                    txtDescripcion.setText(((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getDescripcion());
                
                }catch (Exception e){
                    e.printStackTrace();
                }
                
            }else {
                JOptionPane.showMessageDialog(null, "NO HAY DATOS");
            }
        }
    
        public Especialidad buscarEspecialidad (int codigoEspecialidad){
            Especialidad resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEspecialidades(?)}");
                procedimiento.setInt(1, codigoEspecialidad);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado =  new Especialidad(registro.getInt("codigoEspecialidad"),
                            registro.getString("descripcion"));
                    
                    
                }
                
                
            }catch (Exception e){
                e.printStackTrace();
            }
        
            return resultado;
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
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
                    
                default:
                    if(tblEspecialidad.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registtro?", "Eliminar Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEspecialidade(?)}");
                                procedimiento.setInt(1, ((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                                procedimiento.execute();
                                listaEspecialidad.remove(tblEspecialidad.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }catch (Exception e){
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
                 if(tblEspecialidad.getSelectionModel().getSelectedItem() != null){
                     btnEditar.setText("Actualizar");
                     btnReporte.setText("Cancelar");
                     btnNuevo.setDisable(true);
                     btnEliminar.setDisable(true);
                     imgEditar.setImage(new Image("/org/edgargomez/image/Actualizar.png"));
                     imgReporte.setImage(new Image("/org/edgargomez/image/Cancelar.png"));
                     activarControles();
                     txtCodigoEspecialidad.setEditable(false);
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
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEspecialidade(?, ?)}");
             Especialidad registro = (Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem();
             registro.setDescripcion(txtDescripcion.getText());
            
             procedimiento.setInt(1, registro.getCodigoEspecialidad());
             procedimiento.setString(2, registro.getDescripcion());
             procedimiento.execute();
             
         }catch(Exception e){
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
                 tblEspecialidad.getSelectionModel().clearSelection();
        }
    
     }
      public void imprimirReporte(){
          Map parametros = new HashMap();
          parametros.put("codigoEspecialidad", null);
          GenerarReporte.mostrarReporte("ReporteEspecialidades.jasper", "Resportes de Especialidades", parametros);
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
            txtCodigoEspecialidad.setEditable(false);
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
        Especialidad registro = new Especialidad();
        registro.setDescripcion(txtDescripcion.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEspecialidade(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaEspecialidad.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }   
    
public void desactivarControles(){
    txtCodigoEspecialidad.setEditable(false);
    txtDescripcion.setEditable(false);
}
    
public void activarControles(){
    txtCodigoEspecialidad.setEditable(true);
    txtDescripcion.setEditable(true);
}
    
public void limpiarControles(){
    txtCodigoEspecialidad.clear();
    txtDescripcion.clear();
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
