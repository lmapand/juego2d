package mi.paquete.juego.modelo.bicheria.efectos;

import com.badlogic.gdx.graphics.Color;

import mi.paquete.juego.pantallas.PantallaAccion;

public class EfectoTextoDano extends EfectoTextoInformativoFlotante
{
    
    private static final float TIEMPO_DIBUJADO_ACTIVO=1.0f;
    private static final float DESFASE_POSICION_TEXTO_DANO_EJE_Y=20.0f;
    private static final int VELOCIDAD_DESP_TEXTO_Y=80;
    private static final Color COLOR_TEXTO=new Color(0.8f,0.0f,0.0f,1.0f);
    
    
    public EfectoTextoDano(float posX, float posY, PantallaAccion.RenderizadorAccion entorno, String texto)
    {
        super(TIEMPO_DIBUJADO_ACTIVO, posX, posY, entorno, texto, COLOR_TEXTO, VELOCIDAD_DESP_TEXTO_Y);
        
        //AJustamos la posicion inicial
        this.posY+=DESFASE_POSICION_TEXTO_DANO_EJE_Y;
    }
}
