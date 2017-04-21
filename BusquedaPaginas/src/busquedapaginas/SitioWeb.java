package busquedapaginas;

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

    @Override
    public String toString() {
        return this.dominioMasProtocolo;
    }
}
