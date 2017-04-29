package busquedapaginas;

public class BuscadorBing extends Buscador {

    private static final int SALTO = 10;
    private static final String url = "https://www.bing.com/search";
    private static final String qParam = "q=";
    private static final String startParam = "first=";

    private String terminacionDominio;
    private int pagina;

    public BuscadorBing() {
        this.pagina = -SALTO;
    }

    @Override
    public void setTerminacionDominio(String terminacionDominio) {
        this.terminacionDominio = terminacionDominio;
    }

    @Override
    public String proximaUrl() {
        this.pagina += SALTO;
        String proxUrl = url + "?" + qParam + this.terminacionDominio + "&" + startParam + this.pagina;
        return proxUrl;
    }

    @Override
    public int getPagina() {
        return this.pagina;
    }
}
