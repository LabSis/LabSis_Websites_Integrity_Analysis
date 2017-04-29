package analizador;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author fdrcbrtl
 */
public abstract class ObtenedorTags {

    public ArrayList<Tag> obtenerTags(String html) {
        ArrayList<Tag> listaTags = new ArrayList<>();
        try {
            Document document = Jsoup.parse(html);
            Elements tags = document.getElementsByTag(getNombreTag());
            for (Element t : tags) {
                Tag tag = new Tag(getNombreTag());
                for (Attribute a : t.attributes()) {
                    tag.addAtributo(a.getKey(), a.getValue());
                }
                tag.setTagHtml(t.toString());
                listaTags.add(tag);
            }
        } catch (IllegalArgumentException ex) {
            
        }
        return listaTags;
    }

    public abstract String getNombreTag();
}
