package mi.paquete.juego.modelo.bicheria.efectos;

import java.util.List;

import mi.paquete.juego.pantallas.PantallaAccion;

/** Un efecto representa algún tipo de cambio en el estado de los elementos del juego.
 *
 *  Puede llevar asociada también informacion de dibujado (representacion de Puntos o mensajes en pantalla, etc.)
 */
public abstract class Efecto
{
    /** Estas variables se deben de manejar desde el entorno de dibujo.
     *
    */
  
    protected float cronometroEjecucion=0.0f;
    protected final float retardoEjecucion;
    protected boolean ejecutado=false;
    protected PantallaAccion.RenderizadorAccion entorno;
    
    public Efecto( float retardoEjecucion)
    {
        this.retardoEjecucion = retardoEjecucion;
    }
    
    /** Para establecer el entorno si no se pudo durante la creacion del objeto
     *
     * @param entorno PantallaAccion.RenderizadorAccion que gestiona la ejecucion del juego
     */
    public void setEntorno(PantallaAccion.RenderizadorAccion entorno)
    {
        this.entorno=entorno;
    }

    public void marcaParaElimiacion()
    {
        this.entorno.getEfectosEliminar().add(this);
    }
    
    /** Evalua el tiempo transcurrido desde el lanzamiento del efecto y lo ejecuta si ha transcurrido
     * el tiempo marcado.
     *
     * UN efecto puede a su vez desencadenar nuevos efectos, que se recogeran como respuesta de la ejecucion
     *
     * @param delta float timepo transcurrido desde el lanzamiento
     * @return Object[], donde Object sera iuna instancia de efecto, EfectoTemporalDIbujable o ElementoJuego
     */
    public abstract List<Object> ejecuta(float delta);
    
    public  boolean estaEjecutado()
    {
        return this.ejecutado;
    }
}
