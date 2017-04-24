package analizador;

/**
 *
 * @author fdrcbrtl
 */
public class ResultadoTag {
    
    private String tagHtml;
    private boolean utilizaCdn;
    private boolean verificacionIntegridad;
    private TipoVerificacion tipoVerificacion;

    public String getTagHtml() {
        return tagHtml;
    }

    public void setTagHtml(String tagHtml) {
        this.tagHtml = tagHtml;
    }
    
    public boolean setUtilizaCdn() {
        return utilizaCdn;
    }

    public void getUtilizaCdn(boolean cdn) {
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

    public enum TipoVerificacion {
        SHA_256, SHA_386, SHA_512
    }
}
