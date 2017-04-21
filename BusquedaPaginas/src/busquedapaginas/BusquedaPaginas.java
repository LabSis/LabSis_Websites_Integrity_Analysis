/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busquedapaginas;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author 
 */
public class BusquedaPaginas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       ArrayList<SitioWeb> sitios= new ArrayList();
        try {
            System.out.println("Buscando paginas esto puede tardar un momento...");
            //VELOCIDAD VARIA SEGUN INTERNET
            sitios.addAll(BuscadorSitiosWeb.buscar("gob.ar",10));
            Iterator<SitioWeb> it = sitios.iterator();
            while(it.hasNext())
            {
                SitioWeb a = (it.next());
                System.out.println(a.toString()+ " || Es segura: "+((a.esSeguro())?"SI":"NO"));
               
            }
            System.out.println("Cantidad encontradas:"+sitios.size());
            
        } catch (Exception ex) {
            System.out.println("No se pudo realizar:"+ex.getMessage());
        }
    
       
    }
    
}
