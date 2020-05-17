package mi.paquete.juego.modelo.bicheria.efectos;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import mi.paquete.juego.pantallas.PantallaAccion;

/** este efecto se utiliza para la generacion de un EfectoTemporal visual dentro del ciclo de iteracion
 * de los Dibujables.
 *
 * AL ejecutarse, se crea un EfectoTemporalDibujable que se añade a la coleccin de Drawables, justo despues
 * de la iteracion de percepcion/movimiento de los mismos.
 *
 * Los avisos son estáticos, y se quedan fijados en la pantalla hasta que se disipan
 */

public class EfectoGeneraAviso extends Efecto
{
    private static final float RETARDO_EJECUCION= 0.0f;
    
    /** el tiempo que debe estar visible el aviso enviado.*/
    private static final float TIEMPO_TEXTO_ACTIVO=1.5f;
    
    /** La velocidad de desplazamineto del texto (en la version original, 0.0f, para que el texto quede fijado*/
    private static final int VELOCIDAD_DESPLAZAMINETO_TEXTO=0;
    
    
    private final String textoAviso;
    private final Color colorAviso;
    private float posX;
    private float posY;
    
    /** Crea un efecto de generacion de Aviso
     *
     * @param textoMostrar String texto a mostrar (usualmente una constante Mundo::TEXTO_AVISO_*
     * @param colorAviso Color color para mostrar el texto (una constante de tipo Mundo::COLOR_AVISO_*
     * @param posX posicion X donde debe aparecer el aviso
     * @param posY posicion Y donde debe aparecer el aviso
     */
    public EfectoGeneraAviso(String textoMostrar, Color colorAviso, float posX, float posY, PantallaAccion.RenderizadorAccion entorno)
    {
        super(RETARDO_EJECUCION);
        this.textoAviso=textoMostrar;
        this.colorAviso=colorAviso;
        this.entorno=entorno;
        this.posX=posX;
        this.posY=posY;
    }
    
    
    public List<Object> ejecuta(float delta)
    {
        List<Object>resultado=new ArrayList<>();
        //Generar el texto a mostrar
        resultado.add(new EfectoTextoInformativoFlotante(TIEMPO_TEXTO_ACTIVO,
                                                        posX,
                                                        posY,
                                                        this.entorno,
                                                        this.textoAviso,
                                                        this.colorAviso,
                                                        VELOCIDAD_DESPLAZAMINETO_TEXTO));
        
        this.ejecutado = true;
        this.marcaParaElimiacion();
    
        return resultado;
    }
    
}
