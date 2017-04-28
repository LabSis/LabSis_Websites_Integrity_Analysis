package busquedapaginas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Deprecated
public class BuscadorSitiosWeb {

    /**
     *
     * @param dominio es el dominio a buscar el motor de búsqueda. Por ejemplo
     * .gob.ar.
     * @param cantidadBusquedas es la cantidad de dominios que se desean
     * obtener.
     * @return un conjunto de objetos SitioWeb. Notar que al ser un conjunto
     * Hash el orden no está establecido.
     * @throws Exception
     */
    public static HashSet<SitioWeb> buscar(String dominio, int cantidadBusquedas) throws Exception {
        
        HashSet<SitioWeb> sitios = new HashSet();

        boolean bandera = true;
        int pagina = 1;
        Pattern p = Pattern.compile("(http(s?)://www\\.\\w+\\." + dominio + ")");
        Matcher m;
        String respuesta;
        while (bandera) {
            respuesta = BuscadorSitiosWeb.getHtml("https://www.bing.com/search?q=site%3A" + dominio + "&first=" + pagina);
            m = p.matcher(respuesta);
            while (m.find()) {
                String urlStr = m.group().trim();
                sitios.add(new SitioWeb(urlStr));
                if (sitios.size() == cantidadBusquedas) {
                    bandera = false;
                    break;
                }
            }

            pagina += 10;
        }

        return sitios;
    }

    private static String getHtml(String paginaBusqueda) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(paginaBusqueda);
            URLConnection urlCon = url.openConnection();
            urlCon.setUseCaches(false);
            urlCon.setDoOutput(true);
            urlCon.setRequestProperty("Content-Length", "0");
            urlCon.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
            urlCon.setRequestProperty("Accept-Lenguage", "es-AR");
            urlCon.setRequestProperty("Connection", "close");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            br.close();
        } catch (MalformedURLException mue) {
            System.out.println("URL mal formada: " + paginaBusqueda);
        } catch (IOException ex) {
            System.out.println("Error al leer " + paginaBusqueda);
        }
        
        return sb.toString();
    }
}
