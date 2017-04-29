package busquedapaginas;

public abstract class Buscador {

    public abstract void setTerminacionDominio(String terminacionDominio);

    public abstract String proximaUrl();

    public abstract int getPagina();
}
