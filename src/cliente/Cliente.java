
package cliente;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author ROBERTOVA
 */
public class Cliente {

    public static String main(String comando) throws Exception {
        Socket conexion = new Socket("localhost", 6785);
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        salida.writeBytes(comando + "\n");
        String resultado = entrada.readLine();
        conexion.close();
        return resultado;
    }
}
