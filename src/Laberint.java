/* Metodologias de la programacion - Practica 3
 *   Clase que implementa el TAD Laberinto que representara la estructura para poder operar y cargar un laberinto cualquiera
 *   Gabriel Garcia (gabriel.garcia@estudiants.urv.cat)
 */
public class Laberint implements ILaberint{
    /*Clase entacada que nos definira las casillas del laberinto*/
    private static class Casilla {
        private final char operacion;
        private final int valor;
        private boolean visited;
        /*Constructor*/
        private Casilla(char operacion, int valor){
            this.operacion = operacion;
            this.valor = valor;
            this.visited = false;
        }
        /*Copy constructor*/
        private Casilla(Casilla c){
            this.operacion=c.operacion;
            this.valor = c.valor;
            this.visited = c.visited;
        }
        private boolean equals(Casilla c){
            return c.operacion == this.operacion && c.valor == this.valor && c.visited == this.visited;
        }
    }

    private int pos_fila, pos_columna, puntuacion; // Posicion actual en el laberinto
    private final int nFilas, nCol; // Numero de filas y de columnas del laberinto (dim)
    private final Casilla[][] laberinto; // Laberinto en si
    private final Casilla salida;   // Casilla de salida del laberinto

    /*Constructor, la idea es que solo sea llamado desde el metodo load */
    public Laberint(int fila, int columna, int puntuacion, Casilla[][] laberinto, Casilla salida, int nFilas, int nCol) {
        this.pos_fila=fila;
        this.pos_columna=columna;
        this.laberinto = laberinto;
        this.puntuacion = puntuacion;
        this.salida = salida;
        this.nFilas = nFilas;
        this.nCol = nCol;
    }
    /*Copy constructor*/
    public Laberint(Laberint l){
        this.pos_fila=l.pos_fila;
        this.pos_columna=l.pos_columna;
        this.laberinto = new Casilla[l.nFilas][l.nCol];
        for(int i=0;i<l.nFilas;i++){
            for(int j=0;j<l.nCol;j++){
                this.laberinto[i][j]=new Casilla(l.laberinto[i][j]);
            }
        }
        this.puntuacion = l.puntuacion;
        this.salida = new Casilla(l.salida);
        this.nFilas = l.nFilas;
        this.nCol = l.nCol;
    }

    /*Metodo que cargara el laberinto de un fichero de texto cuyo nombre vendra dado por parametro*/
    public void load(String filename) {

    }

    /*Metodo que efectua un movimiento dentro del laberinto, devuelve:
    *   0: movimiento efectuado con normalidad
    *   1: se ha llegado al final del laberinto
    *   -1: No se ha podido realizar el movimiento por que se ha llegado al limite del laberinto, puntuacion inferior a 1 o la casilla es inaccesible*/
    public int move(char direction) {
        if(direction=='l') {
            if(pos_columna-1>=0 && operar(this.pos_fila,this.pos_columna-1)) {
                    this.pos_columna--;
            }
            else return -1;
        }
        else if(direction=='r'){
            if(pos_columna+1<=nCol && operar(this.pos_fila,this.pos_columna+1)){
                this.pos_columna++;
            }
            else return -1;
        }
        else if (direction == 'u') {
            if(pos_fila-1>=0 && operar(this.pos_fila-1,this.pos_columna)){
                this.pos_fila--;
            }
            else return -1;
        }
        else if(direction == 'd') {
            if(pos_fila+1<=nFilas && operar(this.pos_fila+1,this.pos_columna)){
                this.pos_fila++;
            }
            else return -1;
        }
        if(this.laberinto[pos_fila][pos_columna].visited) return -1;
        else this.laberinto[pos_fila][pos_columna].visited=true;
        if(this.laberinto[pos_fila][pos_columna].equals(this.salida)) return 1;
        return 0;
    }

    /*Metodo que ejecuta la operacion de la casilla del laberinto correspondiente a las coordenadas indicadas por parametro, devolvera:
    *   True: Operacion realizada con exito
    *   False: No se ha podido llevar a cabo la operacion, puntuacion inferior a 1, o casilla inaccesible*/
    private boolean operar(int fila, int col){
        int puntuacionCopy = this.puntuacion;
        char op = laberinto[fila][col].operacion;
        int valor = laberinto[fila][col].valor;
        if (laberinto[fila][col]==null) return false;
        if(op=='+') {
            puntuacionCopy = puntuacionCopy+valor;
        }
        else if(op=='-'){
            puntuacionCopy = puntuacionCopy-valor;
        }
        else if(op=='*'){
            puntuacionCopy = puntuacionCopy * valor;
        }
        else if(op=='/'){
            puntuacionCopy = puntuacionCopy/valor;
        }
        if(puntuacionCopy<1) return false;
        else {
            this.puntuacion = puntuacionCopy;
            return true;
        }
    }
}
