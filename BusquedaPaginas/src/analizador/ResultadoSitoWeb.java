package analizador;

import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author fdrcbrtl
 */
public class ResultadoSitoWeb {

    private URL url;
    private ArrayList<ResultadoTag> resultadosTags;

    public ResultadoSitoWeb(URL url) {
        this.url = url;
        this.resultadosTags = new ArrayList<>();
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void addResultadoPorTag(ResultadoTag rpt) {
        this.resultadosTags.add(rpt);
    }
    
    public ArrayList<ResultadoTag> getResultados(){
        return this.resultadosTags;
    }
}
