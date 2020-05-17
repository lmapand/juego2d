package mi.paquete.juego.modelo.bicheria.efectos;

import com.badlogic.gdx.graphics.Color;

import mi.paquete.juego.pantallas.PantallaAccion;

public class EfectoTextoXP extends EfectoTextoInformativoFlotante
{
    
    private static final float TIEMPO_DIBUJADO_ACTIVO=2.0f;
    private static final float DESFASE_POSICION_TEXTO_DANO_EJE_Y=20.0f;
    private static final int VELOCIDAD_DESP_TEXTO_Y=95;
    private static final Color COLOR_TEXTO=new Color(0.8f,0.0f,0.8f,1.0f);
    
    
    public EfectoTextoXP(float posX, float posY, PantallaAccion.RenderizadorAccion entorno, int cantidad)
    {
        super(TIEMPO_DIBUJADO_ACTIVO, posX, posY, entorno, "+"+cantidad+" XP", COLOR_TEXTO, VELOCIDAD_DESP_TEXTO_Y);
        
        //AJustamos la posicion inicial
        this.posY+=DESFASE_POSICION_TEXTO_DANO_EJE_Y;
    }
}
