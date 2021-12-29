import java.io.IOException;

/* Metodologias de la programacion - Practica 3
*   Clase que expone el programa principal encargado de cargar y resolver laberintos
*   Gabriel Garcia (gabriel.garcia@estudiants.urv.cat)
 */
public class Main {
    public static void main(String [] args) {
        String filename = "test.txt";
        try {
            Laberint lab = Laberint.load(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
