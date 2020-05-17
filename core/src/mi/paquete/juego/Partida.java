package mi.paquete.juego;


import java.io.File;

/** clase que represeta una partida, almacenada o en curso.
 *
 * La partida guarda la informacion necesaria para recrear un personaje, y almacena
 * la informacon del nivel en el que se encuentra.
 * Del personaje almacenamos su nombre, XP actual,  nivel y caracteristicas de nivel acumuladas.
 * De la mazmorra se almacena la referencia a que mazmorra tiene que 'limpiar'.
 *
 * Cuando en el transcurso del juego el jugador abandona o se cierra la aplicaión, se tendrá que
 * empezar el nivel desde el principio.
 */
public class Partida
{
    private final String nombrePersonaje;
    private int xPActual=0;
    private int nivel=0;
    private int refMazmorra=0;
    private int[][] repNivel=null;
    
    /** Constructor para una partida que se quiere recuperar o actualizar
     *
     * @param nombrePersonaje String
     * @param xPActual int
     * @param nivel int
     * @param refMazmorra int INdice de referencia de la mazmorra en el mapa Mundo::mapaMazmorras
     * @param repNivel int[][] matriz de las habilidades adquiridas por el personaje tra s subir de nivel
     */
    public Partida (String nombrePersonaje, int xPActual, int nivel, int refMazmorra, int[][] repNivel)
    {
        this.nombrePersonaje =nombrePersonaje;
        this.xPActual=xPActual;
        this.nivel=nivel;
        this.refMazmorra=refMazmorra;
        this.repNivel=repNivel;
    }
    
    /** Constructor para una partida nueva*/
    public Partida (String nombrePersonaje)
    {
        this.nombrePersonaje=nombrePersonaje;
    }
    
    /** Carga la partida guardada en el archivo indicado
     *
     * @param archivo File archivo a leer
     * @return Partida almacenada en el archivo indicado, o null si se produce un error de lectura
     */
    public static  Partida cargaPartida(File archivo)
    {
        
        Partida partida;
        
        return null;
    }
    
    public String getNombrePersonaje()
    {
        return nombrePersonaje;
    }
    
    public int getxPActual()
    {
        return xPActual;
    }
    
    public int getNivel()
    {
        return nivel;
    }
    
    public int getRefMazmorra()
    {
        return refMazmorra;
    }
    
    public int[][] getRepNivel()
    {
        return repNivel;
    }
}
