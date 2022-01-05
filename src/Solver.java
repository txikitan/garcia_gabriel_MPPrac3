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
        for(int i = 0; i<laberinto.getnFilas();i++){
            for(int j = 0; i<laberinto.getnCol();i++){
                solucion[i][j] = false;
            }
        }
        this.solucion[laberinto.getPos_fila()][laberinto.getPos_columna()]=true;
    }

    /* ******************** */
    /* ** METODE AVID ***** */
    /* ******************* */

    public int avid() {
        int blockedOrEnded = 1;
        while(blockedOrEnded != 0 && blockedOrEnded!=-1) {
            blockedOrEnded = this.escogerCandidato();
            this.solucion[laberinto.getPos_fila()][laberinto.getPos_columna()] = true;
        }
        return blockedOrEnded;
    }

    private int escogerCandidato() {
        int res_up, res_down, res_left, res_right;
        Laberint l_final;
        Laberint l_up = new Laberint(this.laberinto);
        Laberint l_down = new Laberint(this.laberinto);
        Laberint l_left= new Laberint(this.laberinto);
        Laberint l_right = new Laberint(this.laberinto);
        res_up = l_up.move('u');
        res_down = l_down.move('d');
        res_left = l_left.move('l');
        res_right = l_right.move('r');
        if(res_up == 1 || res_down == 1 || res_left == 1 || res_right == 1) return 1;
        else if(res_up == -1 && res_down == -1 && res_left == -1 && res_right == -1) return -1;
        else {
            if(res_up!=-1 && !(l_up.bloqueado())) l_final = l_up;
            else if(res_down!=-1 && !(l_down.bloqueado())) l_final = l_down;
            else if(res_left!=-1 && !(l_left.bloqueado())) l_final = l_left;
            else l_final = l_right;

            if(res_up!=-1 && !(l_up.bloqueado()) && l_up.getPuntuacion()>l_final.getPuntuacion()) l_final = l_up;
            if(res_down!=-1 && !(l_down.bloqueado()) && l_down.getPuntuacion()>l_final.getPuntuacion()) l_final = l_down;
            if(res_left!=-1 && !(l_left.bloqueado()) && l_left.getPuntuacion()>l_final.getPuntuacion()) l_final = l_left;
            if(res_right!=-1 && !(l_right.bloqueado()) && l_right.getPuntuacion()>l_final.getPuntuacion()) l_final = l_right;

            this.laberinto = l_final;
            return 0;
        }
    }

    /* *************************************************************** */
    /* ** CERCA EXHAUSTIVA (BACKTRACKING) AMB PODA I RAMIFICACIO ***** */
    /* *************************************************************** */


}
