package mi.paquete.juego.modelo.bicheria.combate;




public interface Atacante
{
    
    public static final int ATAQUE_NO_DISPONIBLE=-1;
    
    
    public boolean estaAtacando();
    
    /** Indica si el atacante está en proceso de ataque.
     *
     *  El proceso de ataque tiene un tiempo de activación.
     *  Durante ese tiempo, no se puede llevar a cabo ninguna otra acción
     * @param delta tiempo transcurrido desde la anterior llamada.
     * @return true si se está atacando, false si no.
     */
    public Object procesaEstadoAtaque(float delta );
    
    /** Realiza el proceso de ataque.
     *
     * El proceso de ataque conlleva la generación de objetos(p.e. disparos) y también
     * efectos visuales.
     * Cuando se produce el ataque, se produce un daño.
      * @return
     * @param idAtaque
     */
    public Ataque iniciaAtaque(int idAtaque);
    
    
    /** Funciones auxiliares para acelerar la seleccion de tipos de ataque.
     *  - Se deben establecer en el constructor los boolean adecuados.
     *  - Devuelven o el indice del ataque corresondiente, o -1 si no dispone de ese tipo de ataque
     *  - Para que la lógica del juego funcione correctamente, no se debe disponer en la misma
     *  criatura de ataque físico y suicida a la vez.
     */
     

    public int getIndiceAtaqueDistancia();
    public int getIndiceAtaqueSuicida();
    public int getIndiceAtaqueFisico();
    
}
