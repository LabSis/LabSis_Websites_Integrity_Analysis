package analizador;

import java.util.ArrayList;

/**
 *
 * @author fdrcbrtl
 */
public class TagParser {
    
    public static ArrayList<Tag> parser(String html,ObtenedorTags obtenedorTags){
        return obtenedorTags.obtenerTags(html);
    }
        
}
