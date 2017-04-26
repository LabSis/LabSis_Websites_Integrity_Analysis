package principal;

import analizador.AnalizadorSitio;
import analizador.ResultadoSitoWeb;
import analizador.ResultadoTag;
import busquedapaginas.GoogleBuscadorSitios;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fdrcbrtl
 */
public class AnalizadorDeSitios {

    private final String terminacionDominio;
    private final int cantidadMaximaSitios;

    private final int SHA_256 = 0;
    private final int SHA_386 = 1;
    private final int SHA_512 = 2;

    public AnalizadorDeSitios(String terminacionDominio, int cantidadMaximaSitios) {
        this.terminacionDominio = terminacionDominio;
        this.cantidadMaximaSitios = cantidadMaximaSitios;
    }

    private ArrayList<ResultadoSitoWeb> analizarSitios() {
        ArrayList<ResultadoSitoWeb> resultado = new ArrayList<>();
        GoogleBuscadorSitios buscador = new GoogleBuscadorSitios(this.terminacionDominio, this.cantidadMaximaSitios);
        try {
            TreeSet<GoogleBuscadorSitios.Enlace> enlaces = buscador.buscar();

            for (GoogleBuscadorSitios.Enlace enlace : enlaces) {

                ResultadoSitoWeb resultadoSitio = AnalizadorSitio.ejecutar(enlace.getUrl());

                if (resultadoSitio != null) {
                    resultado.add(resultadoSitio);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(AnalizadorDeSitios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }

    private void analizarResultados(ArrayList<ResultadoSitoWeb> resultado) {
        int cantidadDePaginas = 0;
        int cantidadTotalTags = 0;
        int cantidadUsanCdn = 0;
        int cantidadRecursoEnSubdominio = 0;
        int cantidadVerificanIntegridad = 0;
        int[] contadorTipoVerificacion = new int[3]; //0:sha256 1:sha386 2:sha512

        for (ResultadoSitoWeb rsw : resultado) {
            System.out.println("----------------------------");
            System.out.println("Sitio web: " + rsw.getUrl());
            ArrayList<ResultadoTag> resultadosTagSitio = rsw.getResultados();
            if (resultadosTagSitio.size() > 0) {
                cantidadDePaginas++;
            }
            for (ResultadoTag tr : resultadosTagSitio) {
                cantidadTotalTags++;
                System.out.println("\t" + tr.getTagHtml());

                /* CDN */
                if (tr.getUtilizaCdn()) {
                    System.out.println("\t¡¡¡USA CDN!!!");
                    cantidadUsanCdn++;
                } else {
                    System.out.println("\tNo usa CDN");
                }

                /* Subdominio */
                if (tr.estaEnSubdominio()) {
                    cantidadRecursoEnSubdominio++;
                }

                /* Verificacion de integridad */
                if (tr.getVerificaIntegridad()) {
                    cantidadVerificanIntegridad++;
                    System.out.println("\tVerifica la integridad");

                    ResultadoTag.TipoVerificacion tipoVerificacion = tr.getTipoVerificacion();
                    switch (tipoVerificacion) {
                        case SHA_256:
                            contadorTipoVerificacion[SHA_256]++;
                            break;
                        case SHA_386:
                            contadorTipoVerificacion[SHA_386]++;
                            break;
                        case SHA_512:
                            contadorTipoVerificacion[SHA_512]++;
                            break;
                    }
                }

            }
        }
        System.out.println("###############RESUMEN################");
        System.out.println("Cantidad de páginas: " + cantidadDePaginas);
        System.out.println("Cantidad de tags: " + cantidadTotalTags);
        System.out.println("Cantidad de tags con CDN: " + cantidadUsanCdn);
        System.out.println("Cantidad de tags en subdominios: " + cantidadRecursoEnSubdominio);
        System.out.println("Cantidad de tags que verifican integridad: " + cantidadVerificanIntegridad);
        System.out.println("\tSHA256: " + contadorTipoVerificacion[SHA_256]);
        System.out.println("\tSHA386: " + contadorTipoVerificacion[SHA_386]);
        System.out.println("\tSHA512: " + contadorTipoVerificacion[SHA_512]);

    }

    public static void main(String[] args) {
        AnalizadorDeSitios analizador = new AnalizadorDeSitios("gob.ar", 1500);
        analizador.analizarResultados(analizador.analizarSitios());        
    }

}
