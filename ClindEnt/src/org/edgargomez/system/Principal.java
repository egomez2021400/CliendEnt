/*
Nombre: Edgar Edwin Alexander Gómez García
Código Técnico: IN5CV
Materia: Taller, Tecnologia, Dibujo, Calculo, Tic's.
Carnet: 2021400
Fecha de Creación: 23/04/2022
Fecha de Modificación: 14/07/2022
 */
package org.edgargomez.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;
import org.edgargomez.controller.DetalleRecetaController;
import org.edgargomez.controller.DoctoresController;
import org.edgargomez.controller.EspecialidadesController;
import org.edgargomez.controller.MedicamentoController;
import org.edgargomez.controller.MenuPrincipalController;
import org.edgargomez.controller.PacientesController;
import org.edgargomez.controller.ProgramadorController;
import org.edgargomez.controller.RecetaController;
import org.edgargomez.controller.CitaController;
import org.edgargomez.controller.LoginController;
import org.edgargomez.controller.UsuarioController;

public class Principal extends Application {
    
    private Stage escenarioPrincipal;
            private Scene escena;
            private final String PAQUETE_VISTA = "/org/edgargomez/view/";
            
    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("ClindEnt");
        escenarioPrincipal.getIcons().add(new Image("/org/edgargomez/image/ClindEnt.png"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/edgargomez/view/MenuPrincipalView.fxml")); 
//        Scene escena = new Scene(root);
//        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.show();
         menuPrincipal();
//         ventanaLogin();
//         escenarioPrincipal.show();
    }
    
    public void ventanaLogin(){
        try{
            LoginController login = (LoginController) cambiarEscena("LoginView.fxml",  600, 350);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try{
            UsuarioController usuario = (UsuarioController) cambiarEscena ("UsuarioView.fxml", 600, 300);
            usuario.setEscenariosPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuPrincipal(){
        try {
            MenuPrincipalController ventanaMenu = (MenuPrincipalController) cambiarEscena ("MenuPrincipalView.fxml", 600, 400);
            ventanaMenu.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    } 
    
    public void ventanaProgramador(){
        try{
            ProgramadorController vistaprogramador = (ProgramadorController) cambiarEscena ("ProgramadorView.fxml", 570, 375);
            vistaprogramador.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    } 
    
    public void ventanaPacientes(){
        try{
            PacientesController vistaPacientes = (PacientesController) cambiarEscena ("PacientesView.fxml", 962, 450);
            vistaPacientes.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        } 
    }
    
    public void ventanaEspecialidades(){
        try{
            EspecialidadesController vistaEspecialidades = (EspecialidadesController) cambiarEscena ("Especialidad.fxml", 838, 412);  
            vistaEspecialidades.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaMedicamentos(){
        try{
            MedicamentoController vistaMedicamentos = (MedicamentoController) cambiarEscena ("Medicamentos.fxml", 787, 450);
            vistaMedicamentos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDoctores(){
        try{
            DoctoresController vistaDoctores = (DoctoresController) cambiarEscena ("Doctores.fxml", 962, 450);
            vistaDoctores.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaReceta(){
        try{
            RecetaController vistaReceta = (RecetaController) cambiarEscena ("Receta.fxml", 962, 450);
            vistaReceta.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDetalleReceta(){
        try{
            DetalleRecetaController vistaDetalleReceta = (DetalleRecetaController) cambiarEscena ("DetalleReceta.fxml", 962, 450);
            vistaDetalleReceta.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCitas(){
        try{
            CitaController vistaCitas = (CitaController) cambiarEscena ("Citas.fxml", 962, 450);
            vistaCitas.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String [] args){
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        
        return resultado;
    } 
}
