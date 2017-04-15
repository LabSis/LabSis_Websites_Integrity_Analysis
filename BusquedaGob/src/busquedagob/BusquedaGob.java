/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busquedagob;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.*;




/**
 *
 * @author Nico del Valle
 */
public class BusquedaGob {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      
        //INICIALIZAMOS LISTA QUE NO PERMITE DUPLICADOS
        HashSet<String> ultimo = new HashSet();
        //INICIALIZAMOS RESPUESTA -> PAGINAS QUE BUSCAMOS
        String respuesta;
        //INICIALIZAMOS CANTIDAD DE PAGINAS QUE ENCONTRAMOS
        int cantidad=0;
        // BUSCAMOS POR VARIAS PESTAÑAS DEL MOTOR QUE BUSQUEDA
        // 1 -> PRIMERA PESTAÑA
        //11-> SEGUNDA PESTAÑA
        //21 -> TERCER PESTAÑA Y ASI
        //EN ESTA CASO SE ANALIZARON 400 PESTAÑAS
        //LA CANTIDAD DE PAGINAS OBTENIDAS VARIA SEGUN LAS BUSQUEDAS DEL MOTOR
        // LA CANTIDAD DE BUSQUEDAS VARIA ENTRE 193 - 230 APROX
        for (int i = 1; i <= 401; i+=10) {
            //OBTENGO HTML DE RESPUESTA
            respuesta = getResponse(i);
            //CREO MI PATRON
            Pattern p = Pattern.compile("(http(s?)://www\\.\\w+\\.go(b|v)\\.ar)");
            //CREO MI MATCHER PARA TRABAJAR CON MI PATRON
            Matcher m = p.matcher(respuesta);
            //ESTE CICLO BUSCA TODAS LAS COINCIDENCIAS
            while(m.find())
            {
               //AGREGO LA COINCIDENCIA EN LA LISTA
               ultimo.add(m.group().trim());
            }
        }
       
        //MUESTRO EL RESULTADO
        for(String e : ultimo) {
            System.out.println(e);
            cantidad++;
        }
        System.out.println(cantidad+" Paginas encontradas");
    
       
    
    }
    
    public static String getResponse(int pagina){
    
        String respuesta = null;
        
        try {
            //SE ESTABLACE LA PAGINA A COMUNICARSE
            URL url = new URL("https://www.bing.com/search?q=.gob.ar+OR+.gov.ar&qs=n&sp=-1&pq=.gob.ar+or+.gov.ar&sc=1-11&sk=&cvid=4EB50E1CDAC54EB28966F0924364D2E6&first="+pagina+"&FORM=PERE4"); 
           //SE CONECTA A DICHA PAGINA
            URLConnection conn = url.openConnection();
            //NO USAR CACHE 
            conn.setUseCaches(false);
            //SOLO SALIDA DE DATOS 
            conn.setDoOutput(true);
            //ESTABLECEMOS PROPIEDADES DE LA CONEXION 
            conn.setRequestProperty("Content-Length", "0");
            conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
            conn.setRequestProperty("Accept-Lenguage", "es-AR");
             
            //OBTENEMOS INPUTSTREAM DE LA CONEXION
             InputStream res = conn.getInputStream();
             //TRANSFORMAMOS FLUJO DE BYTES EN FLUJO DE CARACTERES
             BufferedReader br = new BufferedReader(new InputStreamReader(res));
             //LEEMOS LINEA A LINEA
             while(br.ready())
             {
                 respuesta +=br.readLine();
             }
             //CERRAMOS CONEXION
             br.close();
             res.close();
             //EN CASO DE ERROR
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
       
    //RETORNAMOS RESULTADO
    return respuesta;
    }
    
    
}
