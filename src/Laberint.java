/* Metodologias de la programacion - Practica 3
 *   Clase que implementa el TAD Laberinto que representara la estructura para poder operar y cargar un laberinto cualquiera
 *   Gabriel Garcia (gabriel.garcia@estudiants.urv.cat)
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Laberint {


    /*Clase entacada que nos definira las casillas del laberinto*/
    public static class Casilla {
        private final char operacion;
        private final int valor;
        private boolean visited;

        /*Constructor*/
        public Casilla(char operacion, int valor) {
            this.operacion = operacion;
            this.valor = valor;
            this.visited = false;
        }

        /*Copy constructor*/
        public Casilla(Casilla c) {
            this.operacion = c.operacion;
            this.valor = c.valor;
            this.visited = c.visited;
        }

        public boolean isVisited() {
            return visited;
        }
    }


    private int pos_fila;
    private int pos_columna;
    private int puntuacion; // Posicion actual en el laberinto
    private final int nFilas, nCol; // Numero de filas y de columnas del laberinto (dim)
    private final Casilla[][] laberinto; // Laberinto en si
    private final int fila_salida;   // Casilla de salida del laberinto
    private final int columna_salida;

    /*Constructor, la idea es que solo sea llamado desde el metodo load */
    private Laberint(int fila, int columna, int puntuacion, Casilla[][] laberinto, int fila_salida, int columna_salida, int nFilas, int nCol) {
        this.pos_fila = fila;
        this.pos_columna = columna;
        this.laberinto = laberinto;
        this.puntuacion = puntuacion;
        this.fila_salida = fila_salida;
        this.columna_salida = columna_salida;
        this.nFilas = nFilas;
        this.nCol = nCol;
    }

    /*Copy constructor*/
    public Laberint(Laberint l) {
        this.pos_fila = l.pos_fila;
        this.pos_columna = l.pos_columna;
        this.laberinto = new Casilla[l.nFilas][l.nCol];
        for (int i = 0; i < l.nFilas; i++) {
            for (int j = 0; j < l.nCol; j++) {
                if (l.laberinto[i][j] == null) this.laberinto[i][j] = null;
                else this.laberinto[i][j] = new Casilla(l.laberinto[i][j]);
            }
        }
        this.puntuacion = l.puntuacion;
        this.columna_salida = l.columna_salida;
        this.fila_salida = l.fila_salida;
        this.nFilas = l.nFilas;
        this.nCol = l.nCol;
    }

    /*Metodo que cargara el laberinto de un fichero de texto cuyo nombre vendra dado por parametro*/
    public static Laberint load(String filename) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String str = in.readLine();
        String[] tokens = str.split(",");
        int nFilas = Integer.parseInt(tokens[0]);
        int nCol = Integer.parseInt(tokens[1]);
        int pos_fila = Integer.parseInt(tokens[2]);
        int pos_col = Integer.parseInt(tokens[3]);
        int fila_salida = Integer.parseInt(tokens[4]);
        int col_salida = Integer.parseInt(tokens[5]);
        Casilla[][] laberinto = new Casilla[nFilas][nCol];
        int j = 0;
        while ((str = in.readLine()) != null && j < nFilas) {
            tokens = str.split(",");
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].equalsIgnoreCase("NA")) laberinto[j][i] = null; // Casilla inaccesible (wall)
                else
                    /*Creamos casilla */
                    laberinto[j][i] = new Casilla(tokens[i].charAt(0), Integer.parseInt(String.valueOf(tokens[i].charAt(1))));
            }
            j++;
        }
        Laberint full_laberint = new Laberint(pos_fila, pos_col, 0, laberinto, fila_salida, col_salida, nFilas, nCol);
        full_laberint.operar(pos_fila, pos_col);
        full_laberint.laberinto[pos_fila][pos_col].visited = true;
        return full_laberint;
    }


    /*Metodo que efectua un movimiento dentro del laberinto, devuelve:
     *   0: movimiento efectuado con normalidad
     *   1: se ha llegado al final del laberinto
     *   -1: No se ha podido realizar el movimiento por que se ha sobrepasado al limite del laberinto, puntuacion inferior a 1,casilla es inaccesible o visitada
     *   Param direction = 'l' izquierda, 'r' derecha, 'u' up, 'd' down*/
    public int move(char direction) {
        /*Iremos por cada direccion chequeando si estamos en el tablero y operando sobre la casilla indicada*/
        if (direction == 'l') {
            if (pos_columna - 1 >= 0 && operar(this.pos_fila, this.pos_columna - 1)) {
                this.pos_columna--;
            } else return -1;
        }
        else if (direction == 'r') {
            if (pos_columna + 1 <= nCol && operar(this.pos_fila, this.pos_columna + 1)) {
                this.pos_columna++;
            } else return -1;
        }
        else if (direction == 'u') {
            if (pos_fila - 1 >= 0 && operar(this.pos_fila - 1, this.pos_columna)) {
                this.pos_fila--;
            } else return -1;
        }
        else if (direction == 'd') {
            if (pos_fila + 1 <= nFilas && operar(this.pos_fila + 1, this.pos_columna)) {
                this.pos_fila++;
            } else return -1;
        }
        if (this.laberinto[pos_fila][pos_columna].visited) return -1;
        this.laberinto[pos_fila][pos_columna].visited = true;
        if (this.columna_salida == pos_columna && this.fila_salida == pos_fila) {
            return 1;
        }
        return 0;
    }

    /*Metodo que devolvera true en caso de encontrarse bloqueada la casilla correspondiente a las coordenadas pasadas por parametro
    *   estara bloqueada en caso de: estar rodeada de casillas visitadas, nulas o fuera de limite, o una combinacion de las tres */
    public boolean bloqueado(int x, int y) {
        if ((y==0) || (y- 1 >= 0 && (this.laberinto[x][y - 1] == null || this.laberinto[x][y - 1].visited))) {
            if ((y==nCol) || (y + 1 < this.nCol && (this.laberinto[x][y + 1] == null || this.laberinto[x][y + 1].visited))) {
                if ((x==0) || (x - 1 >= 0 && (this.laberinto[x - 1][y] == null || this.laberinto[x - 1][y].visited))) {
                    return (x==nFilas) || (x + 1 < this.nFilas && (this.laberinto[x + 1][y] == null || this.laberinto[x + 1][y].visited));
                } else return false;
            } else return false;
        } else return false;
    }

    /*Metodo que ejecuta la operacion de la casilla del laberinto correspondiente a las coordenadas indicadas por parametro, devolvera:
     *   True: Operacion realizada con exito
     *   False: No se ha podido llevar a cabo la operacion, puntuacion inferior a 1, o casilla inaccesible*/
    public boolean operar(int fila, int col) {
        int puntuacionCopy = this.puntuacion;
        if (fila == this.nFilas || col == this.nCol || fila < 0 || col < 0) return false;  // Si estamos en los limites
        if (laberinto[fila][col] == null) return false;
        char op = laberinto[fila][col].operacion;
        int valor = laberinto[fila][col].valor;
        if (op == '+') {
            puntuacionCopy = puntuacionCopy + valor;
        } else if (op == '-') {
            puntuacionCopy = puntuacionCopy - valor;
        } else if (op == '*') {
            puntuacionCopy = puntuacionCopy * valor;
        } else if (op == '/') {
            puntuacionCopy = puntuacionCopy / valor;
        }
        if (puntuacionCopy < 1) return false;
        else {
            this.puntuacion = puntuacionCopy;
            return true;
        }
    }

    /*Metodos que nos indican si la casilla diagonal inferior o superior derecha es accesible */
    public boolean diagonalDownNull() {
        if(pos_fila+1>=nFilas || pos_columna+1>=nCol) return true;
        if (laberinto[pos_fila + 1][pos_columna + 1] == null) return true;
        else {
            return laberinto[pos_fila + 1][pos_columna + 1].visited;
        }
    }

    public boolean diagonalUpNull() {
        if(pos_fila-1<0 || pos_columna+1<0) return true;
        if (laberinto[pos_fila - 1][pos_columna + 1] == null) return true;
        else {
            return laberinto[pos_fila - 1][pos_columna + 1].visited;
        }
    }

    /*Getters y setters basicos*/
    public int getnFilas() {
        return nFilas;
    }

    public int getnCol() {
        return nCol;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public int getPos_fila() {
        return pos_fila;
    }

    public int getPos_columna() {
        return pos_columna;
    }

    public int getFila_salida() {
        return fila_salida;
    }

    public int getColumna_salida() {
        return columna_salida;
    }

    public Casilla[][] getLaberinto() {
        return laberinto;
    }

    public void setVisited(int x, int y) {
        this.laberinto[x][y].visited=true;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

}