
package org.edgargomez.report;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.edgargomez.db.Conexion;


public class GenerarReporte {
    public static void mostrarReporte(String nombreReporte,String titulo, Map Parametros ){
        InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport reporteMaestro = (JasperReport)JRLoader.loadObject(reporte);
            JasperPrint reporteImpreso = JasperFillManager.fillReport(reporteMaestro, Parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteImpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
            
        }catch (Exception e){
            e.printStackTrace();
        }
    } 
}
