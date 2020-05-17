package mi.paquete.juego.modelo.bicheria.efectos;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mi.paquete.juego.modelo.bicheria.Dibujable;
import mi.paquete.juego.modelo.bicheria.ElementoJuego;
import mi.paquete.juego.pantallas.PantallaAccion;


/** Aquellos elementos que se dibujan y duran un determnado tiempo en pantalla.
 * - P.E: Los efectos de explosion, indicadores de putos de daño, explosin, etc
 */
public abstract class EfectoTemporalDibujable implements Dibujable, ElementoJuego
{
    protected final float tiempoDibujadoActivo;
    
    protected float cronometroDIbujado=0.0f;
    protected final PantallaAccion.RenderizadorAccion entorno;
    protected float posX;
    protected float posY;
    /**
     *
     * @param tiempoActivo float tiempo que estará en pantalla el efecto
     * @param entorno PantallaAccion.RenderizadorAccion referencia al renderizador(para referenciar la eliminacion)
     * @param posX float coordenada X inicial del efecto
     * @param posY float coordenada Y inicial del efecto
     */
    EfectoTemporalDibujable(float tiempoActivo, PantallaAccion.RenderizadorAccion entorno, float posX, float posY)
    {
        this.tiempoDibujadoActivo=tiempoActivo;
        this.entorno=entorno;
        this.posX=posX;
        this.posY=posY;
    }
    
    
    
    
    protected void eliminaEfecto()
    {
        //Gdx.app.log("ELIMINANDO EFECTO", "Se elimina " + this.getClass().getName());
        this.entorno.getElementosEliminar().add(this);
    }
    
    
    @Override
    public void dibuja(SpriteBatch batch, float delta)
    {
        this.cronometroDIbujado+=delta;
        if(cronometroDIbujado>=this.tiempoDibujadoActivo)
            this.eliminaEfecto();
    }
    
    public float getXSprite()
    {
        return this.posX -16.0f;
    }
    
    public float getYSprite()
    {
        return this.posY;
    }
    
    
    
    
}
