package analizador;

import java.util.ArrayList;

/**
 *
 * @author fdrcbrtl
 */
public class Tag {
    private String nombre;
    private ArrayList<Atributo> atributos;
    private String tagHtml;
    
    public Tag(String nombre){
        this.nombre = nombre;
        this.atributos = new ArrayList<>();
    }
    
    public void addAtributo(String nombre, String valor){
        this.atributos.add(new Atributo(nombre, valor));
    }
    
    public void setTagHtml(String tagHtml){
        this.tagHtml = tagHtml;
    }
    
    public String getTagHtml(){
        return this.tagHtml;
    }
    
    public boolean contieneAtributo(String nombreAtributo){       
        return (getAtributo(nombreAtributo) != null);
    }
    
    public Atributo getAtributo(String nombreAtributo){
        if(this.atributos == null || this.atributos.isEmpty()) return null;
        return this.atributos.stream().filter(a -> a.atributo.equals(nombreAtributo)).findFirst().orElse(null);
    }
    
    public class Atributo{
        private String atributo;
        private String valor;
        
        public Atributo(String atributo,String valor){
            this.atributo = atributo;
            this.valor = valor;
        }

        public String getAtributo() {
            return atributo;
        }

        public void setAtributo(String atributo) {
            this.atributo = atributo;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }
    }
}
