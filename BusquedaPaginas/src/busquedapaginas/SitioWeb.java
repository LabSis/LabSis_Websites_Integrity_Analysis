package busquedapaginas;

public class SitioWeb {
    
    private String dominio;
    private boolean seguro;

    public SitioWeb(String dominio) {
        this.dominio = dominio;
        
        if(this.dominio.contains("https"))
        {
            this.seguro = true;
        }else{
            this.seguro = false;
        }
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public boolean esSeguro() {
        return seguro;
    }

    public void setSeguro(boolean seguro) {
        this.seguro = seguro;
    }
    
    
    
}
