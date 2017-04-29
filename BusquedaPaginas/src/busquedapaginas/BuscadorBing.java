package busquedapaginas;

public class BuscadorBing extends Buscador {

    private static final int PRIMER_SALTO = 8;
    private static final int SALTO = 10;
    private static final String url = "https://www.bing.com/search";
    private static final String qParam = "q=";
    private static final String startParam = "first=";

    private String terminacionDominio;
    private int pagina = 1;

    public BuscadorBing() {
        this.pagina = -SALTO;
    }

    @Override
    public void setTerminacionDominio(String terminacionDominio) {
        this.terminacionDominio = terminacionDominio;
    }

    @Override
    public String proximaUrl() {
        if (this.pagina == 1) {
            this.pagina += PRIMER_SALTO;
        } else {
            this.pagina += SALTO;
        }
        String proxUrl = url + "?" + qParam + this.terminacionDominio + "&" + startParam + this.pagina;
        return proxUrl;
    }

    @Override
    public int getPagina() {
        return this.pagina;
    }
}
