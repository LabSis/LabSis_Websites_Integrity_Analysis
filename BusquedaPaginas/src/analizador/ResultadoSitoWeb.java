package analizador;

import java.net.URL;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    
    public ClasificacionSitioWeb getClasificacionSitioWeb(){
        ClasificacionSitioWeb clasificacion = null;
        
        String protocolo = url.getProtocol();
        
        if(protocolo.equalsIgnoreCase("http")){
            clasificacion = ClasificacionSitioWeb.HTTP;
        }else if (protocolo.equalsIgnoreCase("https")){
            
            boolean hayCdnHttp = false;
            
            for(ResultadoTag rt : this.resultadosTags){
                String cdn = rt.getCdn();
                if(cdn != null && !cdn.equals("")){
                    if(cdn.trim().startsWith("http://")){
                        hayCdnHttp = true;
                        break;
                    }
                }
            }
            if(hayCdnHttp){
                clasificacion = ClasificacionSitioWeb.MIXED_CONTENT;
            }else{
                clasificacion = ClasificacionSitioWeb.HTTPS;
            }
        }
        
        return clasificacion;
    }
    
    public JSONObject getJson(){
        JSONObject resultadoJson = new JSONObject();
        
        JSONArray resultadosTagJson = new JSONArray();
        for(ResultadoTag rt : this.resultadosTags){
            resultadosTagJson.add(rt.getJson());
        }
        resultadoJson.put("resultadosTags", resultadosTagJson);
        resultadoJson.put("url", this.url.toString());
        ClasificacionSitioWeb clasificacionSitioWeb = this.getClasificacionSitioWeb();
        resultadoJson.put("clasificacion", (clasificacionSitioWeb != null)?clasificacionSitioWeb.toString():null);
        
        return resultadoJson;
    }
    
    public enum ClasificacionSitioWeb{
        HTTP,MIXED_CONTENT,HTTPS
    }
}
