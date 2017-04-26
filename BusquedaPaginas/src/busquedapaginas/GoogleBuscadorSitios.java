package busquedapaginas;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.TreeSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author fdrcbrtl
 */
public class GoogleBuscadorSitios {
    private final String terminacionDominio;
    private final int cantidadMaxima;
    
    public GoogleBuscadorSitios(String terminacionDominio, int cantidadMaxima){
        this.terminacionDominio = terminacionDominio;
        this.cantidadMaxima = cantidadMaxima;
    }
    
    public TreeSet<Enlace> buscar() {
        TreeSet<Enlace> enlaces = new TreeSet<>();

        UrlGoogle google = new UrlGoogle(this.terminacionDominio);
        boolean haySiguientePagina = true;
        int numeroPagina = 0;
        
        while (haySiguientePagina && enlaces.size() <= this.cantidadMaxima) {
            String urlStr = google.proximaUrl();
            numeroPagina++;
            System.out.println("En página de google N°: " + numeroPagina);
            
            String html = Util.getHtml(urlStr);
            
            TreeSet<Enlace> links = extraerEnlaces(html);
            
            if(!links.isEmpty()){
                enlaces.addAll(links);
            }
            
            haySiguientePagina = haySiguientePagina(html);            
        }
        System.out.println("");
        return enlaces;
    }

    private TreeSet<Enlace> extraerEnlaces(String html) {
        TreeSet<Enlace> enlaces = new TreeSet<>();

        Document document = Jsoup.parse(html);
        Elements aTags = document.getElementsByTag("a");

        for (Element a : aTags) {                           
            if (a.hasAttr("href")) {               
                String dataHref = a.attr("href");                               
                try {
                    URL url = new URL(dataHref);
                    if (!url.getHost().endsWith(this.terminacionDominio)) {
                        continue;
                    }                    
                    enlaces.add(new Enlace(url));                    
                } catch (MalformedURLException ex) {

                }
            }
        }
        return enlaces;
    }
    
    private boolean haySiguientePagina(String html){
        
        /* reutilizo extraerEnlaces pero habria que buscar 
        una forma mejor. Por ejemplo si el enlace siguiente no esta */
        return !extraerEnlaces(html).isEmpty();
    }
    
    class UrlGoogle {

        private static final int SALTO = 10;
        private static final String url = "https://www.google.com.ar/search";
        private static final String qParam = "q=";
        private static final String startParam = "start=";

        private final String terminacionDominio;
        private int pagina;

        public UrlGoogle(String terminacionDominio) {
            this.terminacionDominio = terminacionDominio;
            this.pagina = -SALTO;
        }

        public String proximaUrl() {
            this.pagina += SALTO;
            String proxUrl = url + "?" + qParam + this.terminacionDominio + "&" + startParam + this.pagina;
            return proxUrl;
        }
        
        public int getPagina(){
            return this.pagina;
        }
    }
    
    public class Enlace implements Comparable<Enlace>{
        private URL url;
        
        public Enlace(URL url){
            this.url = url;
        }

        public URL getUrl() {
            return url;
        }

        @Override
        public int compareTo(Enlace o) {            
            return this.url.getHost().compareTo(o.url.getHost());                
        }
        
        /**
         * SOLO COMPARA POR EL HOST ENTERO. NO TIENE EN CUENTA SI HAY UN 
         * SUB DOMINIO DIFERENTE.
         * @param obj
         * @return 
         */
        @Override
        public boolean equals(Object obj) {          
            return (this.url.getHost().equals(((Enlace)obj).url.getHost()));
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.url);
            return hash;
        }           
    }           
}
