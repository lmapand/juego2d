package mi.paquete.juego.modelo.bicheria.combate;


/** un objeto susceptible de  recibir daño.
 *
 *  El protagonista y la bicheria son atacables; algunos objetos también se pueden 'atacar'
 *
 */
public interface Atacable
{
    
    /** Establece el daño recibido por un Atacable.
     *
     * @param dano int puntos de vida perdidos
     */
    public void setDano(int dano);

}
