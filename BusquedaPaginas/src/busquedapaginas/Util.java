package busquedapaginas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author fdrcbrtl
 */
public class Util {

    public static String getHtml(String urlStr) {
        StringBuilder sb = new StringBuilder();        
        InputStreamReader inputStrem = null;
        
        try {
            URL url = new URL(urlStr);
            URLConnection urlCon = url.openConnection();
            
            urlCon.setUseCaches(false);
            urlCon.setDoOutput(true);
            urlCon.setRequestProperty("Content-Length", "0");
            urlCon.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
            urlCon.setRequestProperty("Accept-Lenguage", "es-AR");
            urlCon.setRequestProperty("Connection", "close");
            urlCon.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");

            inputStrem = new InputStreamReader(urlCon.getInputStream());
            BufferedReader br = new BufferedReader(inputStrem);

            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            br.close();
        } catch (MalformedURLException mue) {
            System.out.println("URL mal formada: " + urlStr);
        } catch (IOException ex) {
            System.out.println("Error al leer " + urlStr);
        } finally {
            try {
                if (inputStrem != null) {
                    inputStrem.close();
                }
            } catch (IOException ex) {

            }
        }

        return sb.toString();
    }
}
