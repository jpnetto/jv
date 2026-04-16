import java.util.ArrayList;
import java.util.List;

public class Servicos {
    
    public static Pokemon compararPokemons(Pokemon p1, Pokemon p2) {
        
        ArrayList<Tipo> tipo1 = p1.getTipos();
        ArrayList<Tipo> tipo2 = p2.getTipos();
        
        int cont1 = 0;
        int cont2 = 0;

        for (Tipo t1 : tipo1) {
            for (Tipo t2 : tipo2) {
                if (t1.getFraquezas().contains(t2)) {
                    cont2++;
                }
            }
        }

        // Verifica se algum tipo de p2 é fraco contra algum tipo de p1
        for (Tipo t2 : tipo2) {
            for (Tipo t1 : tipo1) {
                if (t2.getFraquezas().contains(t1)) {
                    cont1++;
                }
            }
        }
        if(cont1==cont2){
            return (p1.getStats() > p2.getStats()) ? p1 : p2;

        } else{
            return (cont1 > cont2) ? p1 : p2;
        }
    }
}



