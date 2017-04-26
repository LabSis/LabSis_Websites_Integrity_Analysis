package analizador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 *
 * @author fdrcbrtl
 */
public class AnalizadorSitio {

    /**
     * Esta variable indica en qué posición desde atrás se encuentra el dominio
     * principal. Por ejemplo en: www.pabex.com.ar
     *
     * El número es 3 porque la palabara pabex está en el tercer lugar de atrás
     * hacia adelante.
     *
     * Es importante cambiar esta variable cuando se quiera analizar dominios
     * .com.
     *
     * Esta es la única forma que se me ocurrió para distinguir entre
     * subdominios y uso de CDN.
     *
     */
    private static final int UBICACION_DOMINIO_PRINCIPAL = 3;

    public static ResultadoSitoWeb ejecutar(URL url) {
        System.out.println("Por descargar: " + url.toString());
        ResultadoSitoWeb result = null;
        InputStream is = null;
        BufferedReader br;
        StringBuilder html = new StringBuilder();
        String linea;

        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Length", "0");
            urlConnection.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
            urlConnection.setRequestProperty("Accept-Lenguage", "es-AR");
            urlConnection.setRequestProperty("Connection", "close");
            urlConnection.setRequestProperty("User-Agent", "Mozila/5.0");
            is = urlConnection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((linea = br.readLine()) != null) {
                html.append(linea);
            }

            result = analizar(url, html.toString());

        } catch (Exception ex) {
            System.out.println("Error al descargar la pagina: " + url.toString());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {

            }
        }
        return result;
    }

    private static ResultadoSitoWeb analizar(URL url, String html) {
        ResultadoSitoWeb resultado = new ResultadoSitoWeb(url);

        ArrayList<Tag> scriptTags = TagParser.parser(html, new ScriptTag());
        for (Tag t : scriptTags) {
            /* Sino tiene definido el src no nos interesa */
            if (!t.contieneAtributo("src")) {
                continue;
            }

            ResultadoTag rt = new ResultadoTag();
            rt.setTagHtml(t.getTagHtml());

            /* CDN */
            Tag.Atributo atrSrc = t.getAtributo("src");
            if (atrSrc != null) {
                boolean usaCdn = usaCdn(url, atrSrc.getValor());
                rt.setUtilizaCdn(usaCdn);
                rt.setSubdominio(estaEnUnSubdominio(url, atrSrc.getValor()));
            }

            /* VERIFICACION DE INTEGRIDAD */
            Tag.Atributo atrIntegridad = t.getAtributo("integrity");
            boolean verificaIntegridad = false;
            if (atrIntegridad != null) {
                verificaIntegridad = true;
                String integrityValue = atrIntegridad.getValor();
                rt.setTipoVerificacion(tipoVerificacion(integrityValue));
            }
            rt.setVerificaIntegridad(verificaIntegridad);

            resultado.addResultadoPorTag(rt);
        }

        return resultado;
    }

    /**
     *
     * @param url es la URL del sitio que carga los recursos.
     * @param src es el src del tag script del sitio cargado.
     * @return true si usa CDN, false en caso contrario.
     */
    private static boolean usaCdn(URL url, String src) {
        boolean usaCdn = false;
        try {
            if (src.startsWith("//")) {
                src = url.getProtocol() + ":" + src;
            }
            URI uri = new URI(src);
            if (uri.isAbsolute()) {
                String hostRecurso = uri.getHost();
                String hostSitio = url.getHost();
                if (hostSitio.equals(hostRecurso)) {
                    usaCdn = false;
                } else {
                    String[] partesSitio = hostSitio.split("\\.");
                    String[] partesRecurso = hostRecurso.split("\\.");

                    if (partesSitio.length < UBICACION_DOMINIO_PRINCIPAL) {
                        throw new RuntimeException("Error con la URL del sitio a analizar. Tener en cuenta la constante UBICACION_DOMINIO_PRINCIPAL.");
                    }
                    if (partesRecurso.length >= UBICACION_DOMINIO_PRINCIPAL) {
                        String dominioPrincipalSitioPrincipal = partesSitio[partesSitio.length - UBICACION_DOMINIO_PRINCIPAL];
                        String dominioPrincipalRecurso = partesRecurso[partesRecurso.length - UBICACION_DOMINIO_PRINCIPAL];
                        if (dominioPrincipalSitioPrincipal.equals(dominioPrincipalRecurso)) {
                            usaCdn = false;
                        } else {
                            usaCdn = true;
                        }
                    } else {
                        usaCdn = true;
                    }
                }
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(AnalizadorSitio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return usaCdn;
    }

    private static boolean estaEnUnSubdominio(URL url, String src) {
        boolean subdominio = false;
        try {
            if (src.startsWith("//")) {
                src = url.getProtocol() + ":" + src;
            }
            URI uri = new URI(src);
            if (uri.isAbsolute()) {
                String hostRecurso = uri.getHost();
                String hostSitio = url.getHost();
                if (hostSitio.equals(hostRecurso)) {
                    subdominio = false;
                } else {
                    String[] partesSitio = hostSitio.split("\\.");
                    String[] partesRecurso = hostRecurso.split("\\.");
                    if (partesSitio.length < UBICACION_DOMINIO_PRINCIPAL) {
                        throw new RuntimeException("Error con la URL del sitio a analizar. Tener en cuenta la constante UBICACION_DOMINIO_PRINCIPAL.");
                    }
                    if (partesRecurso.length >= UBICACION_DOMINIO_PRINCIPAL) {
                        String dominioPrincipalSitioPrincipal = partesSitio[partesSitio.length - UBICACION_DOMINIO_PRINCIPAL];
                        String dominioPrincipalRecurso = partesRecurso[partesRecurso.length - UBICACION_DOMINIO_PRINCIPAL];
                        if (dominioPrincipalSitioPrincipal.equals(dominioPrincipalRecurso)) {
                            subdominio = true;
                        } else {
                            subdominio = false;
                        }
                    } else {
                        subdominio = false;
                    }
                }
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(AnalizadorSitio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return subdominio;
    }

    private static ResultadoTag.TipoVerificacion tipoVerificacion(String verificacion) {
        if (verificacion.contains("sha384")) {
            return ResultadoTag.TipoVerificacion.SHA_386;
        } else if (verificacion.contains("sha512")) {
            return ResultadoTag.TipoVerificacion.SHA_512;
        } else if (verificacion.contains("sha256")) {
            return ResultadoTag.TipoVerificacion.SHA_256;
        }
        return null;
    }

    /**
     * TESTS
     */
    public static void main(String args[]) throws MalformedURLException {
        // Subdominios
        if (estaEnUnSubdominio(new URL("https://www.pabex.com.ar"), "http://pabex.com.ar")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (estaEnUnSubdominio(new URL("https://www.pabex.com.ar"), "http://aq.pabex.com.ar")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (!estaEnUnSubdominio(new URL("https://www.pabex.com.ar"), "http://www.pabex.com.ar")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (estaEnUnSubdominio(new URL("https://www.pabex.com.ar"), "http://www1.pabex.com.ar")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (estaEnUnSubdominio(new URL("https://www.pabex.com.ar"), "http://qwe.www1.pabex.com.ar/a.js")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (!estaEnUnSubdominio(new URL("https://www.pabex.com.ar"), "archivo.js")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (!estaEnUnSubdominio(new URL("https://www.pabex.com.ar"), "http://www.juan.com.ar/a.js")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (!estaEnUnSubdominio(new URL("https://www.pabex.com.ar"), "http://google.com/a.js")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        // CDN
        if (!usaCdn(new URL("https://www.pabex.com.ar"), "http://pabex.com.ar")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (!usaCdn(new URL("https://www.pabex.com.ar"), "http://aq.pabex.com.ar")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (!usaCdn(new URL("https://www.pabex.com.ar"), "http://www.pabex.com.ar")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (!usaCdn(new URL("https://www.pabex.com.ar"), "http://www1.pabex.com.ar")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (!usaCdn(new URL("https://www.pabex.com.ar"), "http://qwe.www1.pabex.com.ar/a.js")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (!usaCdn(new URL("https://www.pabex.com.ar"), "archivo.js")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (usaCdn(new URL("https://www.pabex.com.ar"), "http://www.juan.com.ar/a.js")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }

        if (usaCdn(new URL("https://www.pabex.com.ar"), "http://google.com/a.js")) {
            System.out.println("Correcto");
        } else {
            System.out.println("Error");
        }
    }
}
