package analizador;

import java.net.URL;
import org.json.simple.JSONObject;

/**
 *
 * @author fdrcbrtl
 */
public class ResultadoTag {
    
    /**
     * Un String con todo el html del tag.
     */
    private String tagHtml;
    /**
     * Este flag indica si el archivo está almacenado en un subdominio del
     * dominio principal.
     */
    private boolean subdominio;
    /**
     * Este flag indica si el archivo es cargado a través de CDN.
     */
    private boolean utilizaCdn;
    /**
     * Este flag indica si se hace la verificación de integradidad del archivo.
     */
    private boolean verificacionIntegridad;
    /**
     * Esta variable indica el tipo de integridad en caso que se realice.
     */
    private TipoVerificacion tipoVerificacion;
    /**
     * String con la url del cdn si es que utiliza.
     */
    private String cdn = "";
    
    public String getTagHtml() {
        return tagHtml;
    }

    public void setTagHtml(String tagHtml) {
        this.tagHtml = tagHtml;
    }

    public boolean estaEnSubdominio() {
        return subdominio;
    }

    public void setSubdominio(boolean subdominio) {
        this.subdominio = subdominio;
    }
    
    public boolean getUtilizaCdn() {
        return utilizaCdn;
    }

    public void setUtilizaCdn(boolean cdn) {
        this.utilizaCdn = cdn;
    }

    public boolean getVerificaIntegridad() {
        return verificacionIntegridad;
    }

    public void setVerificaIntegridad(boolean verificacionIntegridad) {
        this.verificacionIntegridad = verificacionIntegridad;
    }

    public TipoVerificacion getTipoVerificacion() {
        return tipoVerificacion;
    }

    public void setTipoVerificacion(TipoVerificacion tipoVerificacion) {
        this.tipoVerificacion = tipoVerificacion;
    }

    public String getCdn() {
        return cdn;
    }

    public void setCdn(String cdn) {
        this.cdn = cdn;
    }
    
    public boolean esSegura(URL urlPadre){               
        boolean https = (urlPadre.getProtocol().equalsIgnoreCase("https"));
        boolean cdnHttps = (this.cdn.startsWith("https"));        
        
        return https && cdnHttps && this.verificacionIntegridad;
    }
    
    public enum TipoVerificacion {
        SHA_256, SHA_386, SHA_512
    }
    
    public JSONObject getJson(){
        JSONObject resultadoJson = new JSONObject();
        resultadoJson.put("tag", this.tagHtml);        
        resultadoJson.put("usaCdn", this.utilizaCdn);
        resultadoJson.put("cdn", this.cdn);
        resultadoJson.put("verificaIntegridad", this.verificacionIntegridad);
        resultadoJson.put("tipoVerificacion", (this.tipoVerificacion != null)? this.tipoVerificacion.toString(): null);        
        return resultadoJson;
    }
}
