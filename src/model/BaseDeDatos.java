/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Rodolfo
 */
public class BaseDeDatos {

    private static BaseDeDatos instancia;
    private Connection coneccion;
    private String baseDeDatos = "";
    private String host = "";
    private short puerto = 3306;
    private String usuario = "";
    private String password = "";

    private BaseDeDatos() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        
    }
    
    public void setBaseDeDatos(String base) {
        this.baseDeDatos = base;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public void setPuerto(short puerto) {
        this.puerto = puerto;
    }
    public void setUsaurio(String usuario) {
        this.usuario = usuario;
    }
    public void SetPassword(String password) {
        this.password = password;
    }

    private void iniciarBaseDeDatos() throws SQLException {
        String sqlCreatBaseDeDatos = "CREATE DATABASE IF NOT EXISTS `" + baseDeDatos+"`;";
        executeUpdate(sqlCreatBaseDeDatos);
        executeQuery("USE "+baseDeDatos+";");
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS `alumnos` (\n"
                + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `nombre` varchar(100) NOT NULL,\n"
                + "  `telefono` varchar(15) NOT NULL,\n"
                + "  `semestre` tinyint(4) NOT NULL,\n"
                + "  `direccion` varchar(250) NOT NULL,\n"
                + "  PRIMARY KEY (`id`)\n"
                + ") ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;");
        executeUpdate(sb.toString());
    }

    public static synchronized BaseDeDatos getInstancia() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        if (instancia == null) {
            instancia = new BaseDeDatos();
        }

        return instancia;
    }

    public int insertar(String sql) throws SQLException {
        return executeUpdate(sql);
    }

    public void cerrarConexion() throws SQLException {
        coneccion.close();
    }

    public ArrayList<HashMap<String, Object>> getResultados(String consulta) throws SQLException {

        Statement st = coneccion.createStatement();
        ResultSet resultSet = null;
        resultSet = st.executeQuery(consulta);
        ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String, Object>>();

        while (resultSet != null && resultSet.next()) {
            HashMap<String, Object> row = new HashMap<String, Object>();
            row.put("ID", resultSet.getString("id"));
            row.put("NOMBRE", resultSet.getString("nombre"));
            row.put("TELEFONO", resultSet.getString("telefono"));
            row.put("SEMESTRE", resultSet.getString("semestre"));
            row.put("DIRECCION", resultSet.getString("direccion"));
            resultados.add(row);
        }
        return resultados;

    }

    public int borrar(String sql) throws SQLException {
        return executeUpdate(sql);
    }

    private int executeUpdate(String sql) throws SQLException {
        try (Statement st = coneccion.createStatement()) {
            return st.executeUpdate(sql);
        }
    }
    private void executeQuery(String sql) throws SQLException {
        try (Statement st = coneccion.createStatement()) {
            st.executeQuery(sql);
        }
    }
    public void conectar() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        String url = "jdbc:mysql://" + host + ":" + puerto + "/";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        coneccion = (Connection) DriverManager.getConnection(url, usuario, password);
        
        iniciarBaseDeDatos();
    }
}
