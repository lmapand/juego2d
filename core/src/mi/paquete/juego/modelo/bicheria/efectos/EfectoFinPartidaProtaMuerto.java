package mi.paquete.juego.modelo.bicheria.efectos;


import java.util.List;

import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.pantallas.PantallaAccion;

/** Efecto que se desencadena cuando el protagonista muere.
 *
 * Esencialmente, muestra el dialogo de FIN DE PARTIDA
 *
  */
public class EfectoFinPartidaProtaMuerto extends Efecto
{
    private static final float RETARDO_EJECUCION= Mundo.TIEMPO_DESCOMPOSICION_CADAVER;

    public EfectoFinPartidaProtaMuerto(PantallaAccion.RenderizadorAccion entorno)
    {
        super( EfectoFinPartidaProtaMuerto.RETARDO_EJECUCION);
        this.entorno=entorno;
    }
    
    
    @Override
    public List<Object> ejecuta(float delta)
    {
        entorno.muestraDialogoProtaMuerto();
        this.ejecutado = true;
        this.marcaParaElimiacion();
    
        return null;
        
    }
}