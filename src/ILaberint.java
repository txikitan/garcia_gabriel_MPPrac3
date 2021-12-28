/* Metodologias de la programacion - Practica 3
 *   Interfaz que define el TAD Laberinto que representara la estructura para poder operar y cargar un laberinto cualquiera
 *   Gabriel Garcia (gabriel.garcia@estudiants.urv.cat)
 */
public interface ILaberint {
    /*Metodo que cargara el laberinto de un fichero de texto cuyo nombre vendra dado por parametro*/
    void load(String filename);
    /*Metodo que efectua un movimiento dentro del laberinto, devuelve:
     *   0: movimiento efectuado con normalidad
     *   1: se ha llegado al final del laberinto
     *   -1: No se ha podido realizar el movimiento por que se ha llegado al limite del laberinto, puntuacion inferior a 1 o la casilla es inaccesible*/
    int move(char direction);
}
