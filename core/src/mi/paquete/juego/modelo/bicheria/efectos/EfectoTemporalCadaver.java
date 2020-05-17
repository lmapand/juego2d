package mi.paquete.juego.modelo.bicheria.efectos;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.bicheria.Dibujable;
import mi.paquete.juego.pantallas.PantallaAccion;

/** este efecto mostrará el Sprite 0 de la primera secuencia de animacion, que irá haciendo un 'FADE'.
 *
 * Establecemos que el tiempo para que desaparezca un cadaver son 2seg., así que el factor de 'FADE' será
 * la mitad del delta de la animacin
 */
public class EfectoTemporalCadaver extends EfectoTemporalDibujable
{
    private static final float TIEMPO_DESCOMPOSICION= Mundo.TIEMPO_DESCOMPOSICION_CADAVER;
    
    private float transparencia=1.0f;
    private final Sprite sp;
    
    public EfectoTemporalCadaver(float posX, float posY, PantallaAccion.RenderizadorAccion entorno,TextureRegion texturaCadaver)
    {
        super(TIEMPO_DESCOMPOSICION, entorno, posX, posY);
        this.sp=new Sprite(texturaCadaver);
    }
    
    
    @Override
    public void eliminaRecursosGraficos()
    {
    
    }
    
    @Override
    public void dibuja(SpriteBatch batch, float delta)
    {
        super.dibuja(batch, delta);
        
        this.transparencia-=delta/2.0f;
        
        //Proceso del dibujado
        if(this.transparencia<=0.0f)
                this.transparencia=0.0f;

        
        sp.setAlpha(transparencia);
        sp.setPosition(this.getXSprite(),this.getYSprite() );
        sp.draw(batch);
    }
    
    @Override
    public boolean esVisible()
    {
        return true;
    }
    
    @Override
    public int getOrdenDibujado()
    {
        return Dibujable.ORDEN_SIEMPRE_ATRAS;
    }
    
    @Override
    public float getXSprite()
    {
        return this.posX -16.0f;
    }
    
    @Override
    public float getYSprite()
    {
        return this.posY ;
    }
}
