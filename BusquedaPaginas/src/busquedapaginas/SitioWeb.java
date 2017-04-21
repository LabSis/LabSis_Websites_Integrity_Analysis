/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busquedapaginas;

/**
 *
 * @author 
 */
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
