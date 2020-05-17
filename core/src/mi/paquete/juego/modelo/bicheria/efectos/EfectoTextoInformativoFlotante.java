package mi.paquete.juego.modelo.bicheria.efectos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.bicheria.Dibujable;
import mi.paquete.juego.pantallas.PantallaAccion;


/** Los textos que aparecen en la pantalla y se van desvaneciendo hacia arriba.
 *
  */
public class EfectoTextoInformativoFlotante extends EfectoTemporalDibujable
{
    
    private static final BitmapFont FUENTE= Mundo.FUENTE;
    
    protected final String texto;
    protected final Color color;
    protected final int velocidadDespTexto;
    
    public EfectoTextoInformativoFlotante(float tiempoActivo, float posX, float posY, PantallaAccion.RenderizadorAccion entorno, String texto, Color color, int velocidadDesplazamientoTexto)
    {
        super(tiempoActivo,  entorno, posX, posY);
        this.texto=texto;
        this.color=color;
        this.velocidadDespTexto=velocidadDesplazamientoTexto;
    }
    
    @Override
    public void dibuja(SpriteBatch batch, float delta)
    {
        //MAntenemos el proceso de dibujado
        super.dibuja(batch, delta);
        Mundo.FUENTE.setColor(this.color);
      
            this.posY+=velocidadDespTexto*delta;
            Mundo.FUENTE.draw(batch, this.texto, this.posX,this.posY);
        }
    
    @Override
    public void eliminaRecursosGraficos()
    {
        //No hacer nada
    }
    
    @Override
    public boolean esVisible()
    {
        return true;
    }
    
    @Override
    public int getOrdenDibujado()
    {
        return Dibujable.ORDEN_SUPERPUESTO;
    }
    
}

