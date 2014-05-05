
package servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ROBERTOVA
 */

public class Servidor {    
    
    public static void main() throws Exception {

        Connection con;
        con = DriverManager.getConnection("jdbc:mysql://localhost:3307","root","root");
        ServerSocket socket = new ServerSocket(6785);
        
        
        while (true) {
           
            
            Socket conexion = socket.accept();
            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
             
            String comando = entrada.readLine();
            String[] orden = comando.split("::");
            String resultado = "";
            if (orden.length == 0) {
                Principal.p.setJtextArea1("Comando incorrecto");
                resultado = "Comando incorrecto";
            }
            if (orden.length == 1) {
                if (orden[0].equals("readdb")) { 
                    try {
                        PreparedStatement statement = con.prepareStatement("SHOW DATABASES");
                        ResultSet rs = statement.executeQuery();
                        while (rs.next()) {
                            resultado += rs.getString("Database") + " ";
                        }
                    } catch (SQLException sQLException) {
                        Principal.p.setJtextArea1("Houbo un erro o ler as bases de datos");
                        resultado = "Houbo un erro o ler as bases de datos";
                    }
                }
            } 
            if (orden.length == 2) {
                if (orden[0].equals("list")) {
                    //SELECT table_name FROM information_schema.table WHERE tables_schema=?
                    String consulta = "SHOW TABLES FROM " + orden[1];
                    Principal.p.setJtextArea1(orden[0] + " " + orden[1] );
                    try {
                        PreparedStatement statement = con.prepareStatement(consulta);
                        ResultSet rs = statement.executeQuery();
                        while (rs.next()) {
                            resultado += rs.getString("Tables_in_" + orden[1]) + " ";
                        }
                    } catch (SQLException sQLException) {
                        Principal.p.setJtextArea1("Houbo un erro o executar: " + orden[0] + " " + orden[1] );
                        resultado = "Houbo un erro o executar: " + orden[0] + " " + orden[1];
                    }
                }
            }
            if (orden.length == 3) {
                if (orden[0].equals("num")) {
                    String consulta = "SELECT COUNT(*) AS CONTA FROM " + orden[1] + "." + orden[2];
                    Principal.p.setJtextArea1(orden[0] + " " + orden[1] + " " + orden[2]);
                    try {
                        PreparedStatement statement = con.prepareStatement(consulta);
                        ResultSet rs = statement.executeQuery();
                        while (rs.next()) {
                            resultado += String.valueOf(rs.getInt("CONTA"));
                        }
                    } catch (SQLException sQLException) {
                        Principal.p.setJtextArea1("Houbo un erro o executar: " + orden[0] + " " + orden[1] + " " + orden[2]);
                        resultado = "Houbo un erro o executar: " + orden[0] + " " + orden[1] + " " + orden[2];
                    }
                } else if (orden[0].equals("del")) {
                    String consulta = "DELETE FROM " + orden[1] + "." + orden[2];
                    Principal.p.setJtextArea1(orden[0] + " " + orden[1] + " " + orden[2]);
                    try {
                        PreparedStatement statement = con.prepareStatement(consulta);
                        statement.execute();    
                        resultado = "OK";
                    } catch (SQLException sQLException) {
                        Principal.p.setJtextArea1("Houbo un erro o executar: " + orden[0] + " " + orden[1] + " " + orden[2]);
                        resultado = "Houbo un erro o executar: " + orden[0] + " " + orden[1] + " " + orden[2];
                    }
                }
            }
            if (orden.length == 4) {
                if (orden[0].equals("ren")) {
                    String consulta = "RENAME TABLE " + orden[1] + "." + orden[2] + " TO " + orden[1] + "." + orden[3];
                    Principal.p.setJtextArea1(orden[0] + " " + orden[1] + " " + orden[2] + " " + orden[3] );
                    try {
                        PreparedStatement statement = con.prepareStatement(consulta);
                        statement.execute();
                        resultado = "OK";
                    } catch (SQLException sQLException) {
                        Principal.p.setJtextArea1("Houbo un erro o executar: " + orden[0] + " " + orden[1] + " " + orden[2]+ " " + orden[3]);
                        resultado = "Houbo un erro o executar: " + orden[0] + " " + orden[1] + " " + orden[2] + " " + orden[3];
                    }
                }
            }
            if (orden.length > 4) {
                Principal.p.setJtextArea1("Comando incorrecto");
                resultado = "Comando incorrecto";
            }
            salida.writeBytes(resultado + "\n");
        }
    }
}
