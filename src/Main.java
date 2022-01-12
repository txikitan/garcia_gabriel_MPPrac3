/* Metodologias de la programacion - Practica 3
 *   Clase que expone el programa principal encargado de cargar y resolver laberintos
 *   Gabriel Garcia (gabriel.garcia@estudiants.urv.cat)
 */
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String [] args) {
        String filename;
        Scanner scanner = new Scanner(System.in);
        int modo;
        try {
            System.out.println("Modo voraz o exhaustivo?(1/2)");
            modo = scanner.nextInt();
            while(modo!=1 && modo!=2){
                System.out.println("Error! Repite");
                modo = scanner.nextInt();
            }
            System.out.println("Nombre del fichero?");
            filename = scanner.next();
            Laberint lab = Laberint.load(filename);
            Solver solver = new Solver(lab);
            if(modo==1) solver.avid();
            else solver.exhaustivaPyR();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
