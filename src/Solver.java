/* Metodologias de la programacion - Practica 3
 *   Clase que nos proporcionara las dos implementaciones para resolver laberintos
 *          - Algoritmo avido (greedy)
 *          - Busqueda exhaustiva con poda y ramificacion (backtracking)
 *   Gabriel Garcia (gabriel.garcia@estudiants.urv.cat)
 */
public class Solver {
    private Laberint laberinto;
    private final int[][] solucion;   // Matriz con las casillas del camino mediante se vaya resolviendo el laberinto
    private int index = 1;

    /*Metodo constructor*/
    public Solver(Laberint laberinto) {
        this.laberinto=laberinto;
        this.solucion=new int[laberinto.getnFilas()][laberinto.getnCol()];
        Laberint.Casilla[][] tablero = laberinto.getLaberinto();
        for(int i = 0; i<laberinto.getnFilas();i++){
            for(int j = 0; j<laberinto.getnCol();j++){
                if(tablero[i][j]==null) solucion[i][j] = -3;
                else solucion[i][j] = 0;
            }
        }
        this.solucion[laberinto.getFila_salida()][laberinto.getColumna_salida()]=-2; // Ponemos la salida a 2
        this.solucion[laberinto.getPos_fila()][laberinto.getPos_columna()]=-1; // Ponemos el inicio a -1
    }

    /*Metodo para escribir la matriz con el camino de la solucion*/
    private void printSol(){
        for(int i=0;i<laberinto.getnFilas();i++){
            for(int j=0;j<laberinto.getnCol();j++){
                if(solucion[i][j]==-1) System.out.print("I\t");
                else if(solucion[i][j]==0) System.out.print("X\t");
                else if(solucion[i][j]==-2) System.out.print("F\t");
                else if(solucion[i][j]==-3) System.out.print("N\t");
                else System.out.print(solucion[i][j]+"\t");
            }
            System.out.println();
        }
        System.out.println();
        //System.out.println("La puntaucion ha sido: "+this.laberinto.getPuntuacion());
    }

    /* ******************** */
    /* ** METODE AVID ***** */
    /* ******************* */

    /*Se encarga de ejecutar el algoritmo de calculo de laberintos voraz*/
    public int avid() {
        int blockedOrEnded = 0;
        //Mientras no se bloquee o se acabe el laberinto
        while(blockedOrEnded == 0) {
            /*Llamada a la funcion que escogera el candidato de movimiento mas optimo*/
            blockedOrEnded = this.escogerCandidato();
            /*En caso que se haya podido llevar a cabo el movimiento, ponemos la casilla de la matriz solucion con el numero del paso*/
            if(blockedOrEnded==0)this.solucion[laberinto.getPos_fila()][laberinto.getPos_columna()] = index;
            /*Incrementamos indice de pasos*/
            index++;
            printSol();
        }
        return blockedOrEnded;
    }

    /*Metodo que aplica la logica de eleccion de movimiento mas optimo basandose en unas precondiciones
    * Devolvera: 1 si se ha llegado al final del laberinto
    *            -1 si no se ha podido realizar ningun movimiento
    *           0 si se ha elegido y efectuado el movimiento correctamente
    * */
    private int escogerCandidato() {
        int res_up, res_down, res_left, res_right;
        Laberint l_final;
        /*Simulamos los 4 movimientos sobre copias del laberinto principal para evaluar la opcion mas optima*/
        Laberint l_up = new Laberint(this.laberinto);
        Laberint l_down = new Laberint(this.laberinto);
        Laberint l_left= new Laberint(this.laberinto);
        Laberint l_right = new Laberint(this.laberinto);
        res_up = l_up.move('u');
        res_down = l_down.move('d');
        res_left = l_left.move('l');
        res_right = l_right.move('r');
        /*Si hemos llegado al final*/
        if(res_up == 1 || res_down == 1 || res_left == 1 || res_right == 1) {
            laberinto.operar(laberinto.getFila_salida(), laberinto.getColumna_salida());
            return 1;
        }
        /*Si estamos bloqueados*/
        else if(res_up == -1 && res_down == -1 && res_left == -1 && res_right == -1) return -1;
        /*Si no, empezamos a comparar para decidir cual es el movimiento más optimo */
        else {
            int pos_Col = laberinto.getPos_columna();
            int pos_Fil = laberinto.getPos_fila();
            int nFilas = laberinto.getnFilas();
            int nCol = laberinto.getnCol();
            /* Escogemos el primer movimiento como referencia para comparar */
            // Up: Siempre y cuando la diagonal superior no sea nula o visitada y no nos encontremos en la primera fila y no estemos bloqueados
            if(!(laberinto.diagonalUpNull()) && pos_Fil!=0 && res_up!=-1 && !(l_up.bloqueado(pos_Fil,pos_Col))) l_final = l_up;
            // Down: Siempre y cuando la diagonal inferior no sea nula o visitada y no nos encontremos en la ultima fila y no estemos bloqueados
            else if(!(laberinto.diagonalDownNull()) && pos_Fil!=nFilas-1 && res_down!=-1 && !(l_down.bloqueado(pos_Fil,pos_Col))) l_final = l_down;
            // Left: Siempre que no nos encontremos por encima de la mitad del recorrido, que no estemos en la ultima ni primera fila ni columna y no estemos bloqueados
            else if(pos_Col<((nCol-1)/2) && pos_Fil!=nFilas-1 && pos_Fil != 0 && pos_Col!=nCol && pos_Col!=0 && res_left!=-1 && !(l_left.bloqueado(pos_Fil,pos_Col))) l_final = l_left;
            // Right: Siempre que no nos encontremos en la ultima columna y no estemos bloqueados
            else if(pos_Col!=nCol-1 && res_right!=-1 && !(l_right.bloqueado(pos_Fil,pos_Col))) l_final = l_right;
            else return -1;
            /*Empezamos a comparar*/
            if(!(laberinto.diagonalUpNull()) && pos_Fil!=0 && res_up!=-1 && !(l_up.bloqueado(pos_Fil,pos_Col)) && l_up.getPuntuacion()>l_final.getPuntuacion()) l_final = l_up;
            if(!(laberinto.diagonalDownNull()) && pos_Fil!=nFilas-1 && res_down!=-1 && !(l_down.bloqueado(pos_Fil,pos_Col)) && l_down.getPuntuacion()>l_final.getPuntuacion()) l_final = l_down;
            if(pos_Col<((nCol-1)/2) && pos_Fil!=nFilas-1 && pos_Fil != 0 && pos_Col!=nCol && pos_Col!=0 && res_left!=-1 && !(l_left.bloqueado(pos_Fil,pos_Col)) && l_left.getPuntuacion()>l_final.getPuntuacion()) l_final = l_left;
            if(pos_Col!=nCol-1 && res_right!=-1 && !(l_right.bloqueado(pos_Fil,pos_Col)) && l_right.getPuntuacion()>l_final.getPuntuacion()) l_final = l_right;
            /*Asignamos el laberinto con el movimiento optimo escogido al laberinto principal para hacerlo efectivo*/
            this.laberinto = l_final;
            return 0;
        }
    }


    /* *************************************************************** */
    /* ** CERCA EXHAUSTIVA (BACKTRACKING) AMB PODA I RAMIFICACIO ***** */
    /* *************************************************************** */

    /*Llamara al metodo recursivo de busqueda exhaustiva ramificada con poda y printeara la solucion*/
    public void exhaustivaPyR() {
        solveExhaustiva(laberinto.getPos_fila(), laberinto.getPos_columna(),laberinto.getPuntuacion());
        printSol();
    }

    /*Funcion recursiva que devolvera true si se ha llegado al final del laberinto o se ha efectuado un movimiento resolutivo con exito */
    public boolean solveExhaustiva(int x, int y,int puntuacion) {
        if (x == laberinto.getFila_salida() && y == laberinto.getColumna_salida()) return true; // Si estamos en el final
        /*Me copio el laberinto para poder hacer una simulacion de operacion y ver si estoy siguiendo un camino valido*/
        Laberint laberintoCopy = new Laberint(this.laberinto);
        laberintoCopy.setPuntuacion(puntuacion); // Restauro la puntuacion de la anterior llamada recursiva para no perderla
        if ((x!=laberinto.getPos_fila() || y!=laberinto.getPos_columna()) && ((!laberintoCopy.operar(x, y)) || laberintoCopy.getLaberinto()[x][y].isVisited())) return false;
        puntuacion=laberintoCopy.getPuntuacion(); // Capturo la puntuacion despues de operar sobre el laberinto de la copia (para mantener la puntuacion entre iteracion e iteracion))
        if (x!=laberinto.getPos_fila() || y!=laberinto.getPos_columna()) laberinto.setVisited(x,y); // Marco como visitado en caso de no estar en el inicio (ya estaria como visitado// )
        /*Si no estoy en el inicio de filas y no estoy bloqueado --> UP */
        if (x != 0 && !laberinto.bloqueado(x-1,y) && x-1!=laberinto.getPos_fila()) { // Las precondiciones estas serian la PODA
            /*Sigo recorriendo recursivamente*/
            if (solveExhaustiva(x - 1, y,puntuacion)) { // RAMIFICO llamando recursivamente
                /*En estos bloques solo se entrara cuando se hayan resuelto las llamadas recursivas de un camino todas en true, con lo que se marcara el camino correcto final */
                if(solucion[x][y]!=-1)solucion[x][y] = index; // Marco solucion con el indice
                laberinto.operar(x - 1, y);   // Opero sobre el laberinto definitivo
                index++; // Incremento indice de movimientos
                printSol(); // Printeo la solucion de cada paso
                return true;
            }
        }
        /*Si no estoy en el final de filas y no estoy bloqueado --> DOWN */
        if (x != laberinto.getnFilas() - 1 && !laberinto.bloqueado(x+1,y) && x+1!=laberinto.getPos_fila()) {
            if (solveExhaustiva(x + 1, y,puntuacion)) {
                if(solucion[x][y]!=-1)solucion[x][y] = index;
                laberinto.operar(x + 1, y);
                index++;
                printSol();
                return true;
            }
        }
        /*Si no estoy en el inicio de columnas y no estoy bloqueado --> LEFT */
        if (y != 0 && !laberinto.bloqueado(x,y-1) && y-1!=laberinto.getPos_columna()) {
            if (solveExhaustiva(x, y - 1,puntuacion)) {
                if(solucion[x][y]!=-1)solucion[x][y] = index;
                laberinto.operar(x, y - 1);
                index++;
                printSol();
                return true;
            }
        }
        /*Si no estoy en el final de columnas y no estoy bloqueado --> RIGHT */
        if (y != laberinto.getnCol() - 1 && !laberinto.bloqueado(x,y+1) && y+1!=laberinto.getPos_columna()) {
            if (solveExhaustiva(x, y + 1,puntuacion)) {
                if(solucion[x][y]!=-1)solucion[x][y] = index;
                laberinto.operar(x, y + 1);
                index++;
                printSol();
                return true;
            }
        }
        return false;
    }
}
