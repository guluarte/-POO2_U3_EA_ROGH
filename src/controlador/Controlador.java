package controlador;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.BaseDeDatos;
import vista.VentanaPrincipal;

/**
 *
 * @author Rodolfo
 */
public class Controlador {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Controlador controlador = new Controlador();
                new VentanaPrincipal(controlador).setVisible(true);

            }
        });
    }
    BaseDeDatos db;
    VentanaPrincipal ventana;

    public Controlador() {
        try {
            
            db = BaseDeDatos.getInstancia();
            preguntarDatosConeccionDB();
            db.conectar();
        } catch (ClassNotFoundException ex) {
            mostrarMensajeError("Error", "No se pudo conectar con la base de datos");
            //Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            mostrarMensajeError("Error", "No se pudo conectar con la base de datos");
            ////Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            mostrarMensajeError("Error", "No se pudo conectar con la base de datos");
            //Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            mostrarMensajeError("Error", "No se pudo conectar con la base de datos");
            //Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            mostrarMensajeError("Error", "Hubo un problema al conectarse a la base de datos");

        }

    }

    public void registrarVentana(VentanaPrincipal ventana) {
        this.ventana = ventana;
    }

    private void mostrarMensaje(String msj, String titulo) {
        if (ventana instanceof VentanaPrincipal) {
            ventana.mostrarMensaje(titulo, msj);
        }
    }

    private void mostrarMensajeError(String msj, String titulo) {
        if (ventana instanceof VentanaPrincipal) {
            ventana.mostrarMensajeError(titulo, msj);
        }
    }

    public void insertar(String nombre, String telefono, short semestre, String direccion) {
        int registrosInsertados = 0;
        try {
            String sql = String.format("INSERT INTO alumnos (nombre, telefono, semestre, direccion) VALUES(\"%s\", \"%s\", \"%d\", \"%s\")", nombre, telefono, semestre, direccion);
            registrosInsertados = db.insertar(sql);
            mostrarMensaje("Ok", "Se insertaron " + registrosInsertados + " registro");
        } catch (Exception ex) {
            mostrarMensajeError("Error", "Hubo un problema al guardar los datos");
            //Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void salir() {
        try {
            db.cerrarConexion();
        } catch (SQLException ex) {
        } catch (Exception ex) {
        } finally {
            System.exit(0);
        }
    }

    public void preguntarDatosConeccionDB() {
        String host = JOptionPane.showInputDialog("Por favor ingrese el servidor de base de datos : ");

        String puerto = JOptionPane.showInputDialog("Por favor ingrese el puerto (1-65000): ");

        String baseDeDatos = JOptionPane.showInputDialog("Por favor ingrese el nombre de la base de datos: ");
        
        String usuario = JOptionPane.showInputDialog("Por favor ingrese el nombre usuario: ");
        
        String password = JOptionPane.showInputDialog("Por favor ingrese la contraseña: ");
        
        db.setHost(host);
        db.setPuerto(Short.parseShort(puerto));
        db.setBaseDeDatos(baseDeDatos);
        db.setUsaurio(usuario);
        db.SetPassword(password);
    }

    public void actualizar() {
        if (ventana instanceof VentanaPrincipal) {
            try {
                //ArrayList<HashMap<String, Object>> lista
                String consulta = "SELECT * FROM alumnos;";
                ArrayList<HashMap<String, Object>> resultados = db.getResultados(consulta);
                ventana.actualizarTabla(resultados);
            } catch (SQLException ex) {
                mostrarMensajeError("Error al realiazar la consulta!", "Error");
                
            } catch (Exception ex) {
                mostrarMensajeError("Error", "Hubo un problema al recibir los datos");

            }
        }
    }

    public void borrar(int idSeleccionado) {
        int registrosBorrados = 0;
        try {
            String sql = String.format("DELETE FROM alumnos WHERE id='%d';", idSeleccionado);
            registrosBorrados = db.insertar(sql);
            mostrarMensaje("Ok", "Se borraron " + registrosBorrados + " registro");
        } catch (Exception ex) {
            mostrarMensajeError("Error", "Hubo un problema al borrar los datos");
            
        }

    }

    public void actualizar(int idActual, String nombre, String telefono, short semestre, String direccion) {
        int registrosActualizados = 0;
        try {
            String sql = String.format("UPDATE alumnos SET nombre='%s',telefono='%s',semestre='%d',direccion='%s' WHERE id='%d';", nombre, telefono, semestre, direccion, idActual);
            registrosActualizados = db.insertar(sql);
            mostrarMensaje("Ok", "Se actualizo un " + registrosActualizados + " registro");
        } catch (Exception ex) {
            mostrarMensajeError("Error", "Hubo un problema al editar los datos");
           
        }
    }
}
