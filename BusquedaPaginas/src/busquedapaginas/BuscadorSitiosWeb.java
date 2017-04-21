package busquedapaginas;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        HashSet<String> s = new HashSet();

        boolean bandera = true;
        int pagina = 1;
        Pattern p = Pattern.compile("(http(s?)://www\\.\\w+\\." + dominio + ")");
        Matcher m;
        String respuesta;
        while (bandera) {
            respuesta = BuscadorSitiosWeb.getResponse("https://www.bing.com/search?q=site%3A" + dominio + "&first=" + pagina);
            m = p.matcher(respuesta);
            while (m.find()) {
                s.add(m.group().trim());
                if (s.size() == cantidadBusquedas) {
                    bandera = false;
                    break;
                }
            }

            pagina += 10;
        }
        HashSet<SitioWeb> sitios = new HashSet();
        Iterator<String> it = s.iterator();
        while (it.hasNext()) {
            sitios.add(new SitioWeb(it.next()));
        }
        return sitios;
    }

    private static String getResponse(String paginaBusqueda) throws Exception {
        String respuesta = "";
        URL url = new URL(paginaBusqueda);
        URLConnection conn = url.openConnection();
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Length", "0");
        conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
        conn.setRequestProperty("Accept-Lenguage", "es-AR");
        conn.setRequestProperty("Connection", "close");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            while (br.ready()) {
                respuesta += br.readLine();
            }
        }
        return respuesta;
    }
}
