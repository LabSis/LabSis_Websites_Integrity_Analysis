package analizador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fdrcbrtl
 */
public class AnalizadorSitio {

    public static ResultadoSitoWeb ejecutar(URL url) {
        ResultadoSitoWeb result = null;
        InputStream is = null;
        BufferedReader br;
        StringBuilder html = new StringBuilder();
        String linea;
        try {
            is = url.openStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((linea = br.readLine()) != null) {
                html.append(linea);
            }

            result = analizar(url, html.toString());

        } catch (IOException ex) {
            System.out.printf("URL mal formada: " + url.toString());
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
                rt.getUtilizaCdn(usaCdn);
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

    private static boolean usaCdn(URL url, String src) {
        boolean usaCdn = false;
        try {
            if (src.startsWith("//")) {
                src = url.getProtocol() + ":" + src;
            }
            URI uri = new URI(src);
            if (uri.isAbsolute()) {
                String hostUri = uri.getHost();
                String hostUrl = url.getHost();
                if (hostUri.equals(hostUrl)) {
                    usaCdn = false;
                } else {
                    usaCdn = true;
                }
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(AnalizadorSitio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usaCdn;
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
}
