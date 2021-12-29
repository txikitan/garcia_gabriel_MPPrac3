/* Metodologias de la programacion - Practica 3
 *   Clase que nos proporcionara las dos implementaciones para resolver laberintos
 *          - Algoritmo avido (greedy)
 *          - Busqueda exhaustiva con poda y ramificacion (backtracking)
 *   Gabriel Garcia (gabriel.garcia@estudiants.urv.cat)
 */
public class Solver {
    Laberint laberinto;
    boolean[][] solucion;

    public Solver(Laberint laberinto) {
        this.laberinto=laberinto;
        this.solucion=new boolean[laberinto.getnFilas()][laberinto.getnCol()];
    }

    public static void avid() {

    }
}
