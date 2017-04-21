/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busquedapaginas;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author 
 */
public class BuscadorSitiosWeb {
    
    
    public static HashSet buscar(String dominio,int cantidadBusquedas) throws Exception
    {
        HashSet<String> s = new HashSet();
        
        boolean bandera = true;
        int pagina=1;
        Pattern p = Pattern.compile("(http(s?)://www\\.\\w+\\."+dominio+")");
        Matcher m;
        String respuesta;
            while(bandera)
               {
                   respuesta = BuscadorSitiosWeb.getResponse("https://www.bing.com/search?q=site%3A"+dominio+"&first="+pagina);
                    m = p.matcher(respuesta);
                    while(m.find())
                    {
                        s.add(m.group().trim());
                         if(s.size()==cantidadBusquedas)
                        {
                            bandera = false;
                            break;
                        }
                    }
                    
                    pagina += 10;
               }
        HashSet<SitioWeb> sitios = new HashSet();
        Iterator it = s.iterator();
        while(it.hasNext())
        {
            sitios.add(new SitioWeb((String)it.next()));
        }
        return sitios;
    }
    
    private static String getResponse(String paginaBusqueda) throws Exception{
        String respuesta = "";
        URL url = new URL(paginaBusqueda);
        URLConnection conn = url.openConnection();
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Length", "0");
        conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
        conn.setRequestProperty("Accept-Lenguage", "es-AR");
        conn.setRequestProperty("Connection","close" );
        InputStream res = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(res));
        while(br.ready())
        {
            respuesta +=br.readLine();
        }
        br.close();
        res.close();
        
    
    
    return respuesta;
    }
}
