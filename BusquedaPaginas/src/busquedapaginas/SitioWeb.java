package busquedapaginas;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
@Deprecated
public class SitioWeb {

    private final String dominioMasProtocolo;
    private String dominio;
    private String protocolo;
    private boolean seguro;

    public SitioWeb(String dominioMasProtocolo) {
        this.dominioMasProtocolo = dominioMasProtocolo;
        String partes[] = this.dominioMasProtocolo.split("://");
        if (partes.length == 2) {
            this.protocolo = partes[0];
            this.dominio = partes[1];
            if (this.dominioMasProtocolo.contains("https://")) {
                this.seguro = true;
            } else {
                this.seguro = false;
            }
        } else {
            throw new RuntimeException("Dominio y protocolo incorrecto: " + dominioMasProtocolo);
        }
    }

    public String getDominio() {
        return dominio;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public boolean esSeguro() {
        return seguro;
    }

    public void setSeguro(boolean seguro) {
        this.seguro = seguro;
    }

    public URL getUrl(){
        URL url = null;
        String urlStr = "";
        try {
            urlStr = this.protocolo + "://" + this.dominio;            
            url = new URL(urlStr.trim());
        } catch (MalformedURLException ex) {
            System.out.println("Error al formar url: "+urlStr);
        }
        return url;
    }
    
    @Override
    public String toString() {
        return this.dominioMasProtocolo;
    }
}
