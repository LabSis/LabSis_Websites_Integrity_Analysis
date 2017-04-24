package analizador;

/**
 *
 * @author fdrcbrtl
 */
public class ResultadoTag {
    
    private String tagHtml;
    private boolean cdn;
    private boolean verificacionIntegridad;
    private TipoVerificacion tipoVerificacion;

    public String getTagHtml() {
        return tagHtml;
    }

    public void setTagHtml(String tagHtml) {
        this.tagHtml = tagHtml;
    }
    
    public boolean isCdn() {
        return cdn;
    }

    public void setCdn(boolean cdn) {
        this.cdn = cdn;
    }

    public boolean isVerificacionIntegridad() {
        return verificacionIntegridad;
    }

    public void setVerificacionIntegridad(boolean verificacionIntegridad) {
        this.verificacionIntegridad = verificacionIntegridad;
    }

    public TipoVerificacion getTipoVerificacion() {
        return tipoVerificacion;
    }

    public void setTipoVerificacion(TipoVerificacion tipoVerificacion) {
        this.tipoVerificacion = tipoVerificacion;
    }

    enum TipoVerificacion {
        SHA_256, SHA_386, SHA_512
    }
}