package mi.paquete.juego.modelo.bicheria.desplazamiento;

import com.badlogic.gdx.Gdx;

import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.bicheria.Fulano;

public class DesplazamientoPersonaje implements Desplazable
{
    private final Fulano prota;
    
    private int accionDesplazamiento=Mundo.ACCION_PARADO;
    private int encaramiento=Mundo.ACCION_ABAJO;
    
    private boolean hayCambioCasilla =false;
    
    public DesplazamientoPersonaje(Fulano prota)
    {
        this.prota=prota;
    }
    
    @Override
    public void actualizaPosicion(float delta)
    {
        //Si no hay que recalcular la posicion, no hacemos nada
        if(accionDesplazamiento == Mundo.ACCION_PARADO)
            return;
    
        /*Evaluar si podemos ir a la posicion:
        Calculamos si en la nueva posicion el tile es transitable
         */
    
        Mundo mundo=Mundo.getInstance();
    
        float posNuevaX=prota.getPosX() + prota.getVelocidad()*delta*Mundo.VECTOR_DESP[accionDesplazamiento].x;
        float posNuevaY=prota.getPosY() + prota.getVelocidad()*delta*Mundo.VECTOR_DESP[accionDesplazamiento].y;
    
        Integer[] coordCelda=mundo.getCasilla(posNuevaX,posNuevaY);
        if(mundo.esTransitable(coordCelda[0],coordCelda[1]))
        {
            /* Establecer nueva posicion*/
            prota.setPosX(posNuevaX);
            prota.setPosY(posNuevaY);
            
            /* Reajustar los tiles de posicion
            ToDo: analizar caracteristicas de la casilla en la que entramos
             */
            //TEST + //Evaluacion de la 'Niebla de Guerra'
            if(prota.getCasillaActual().x!=coordCelda[0] || prota.getCasillaActual().y!=coordCelda[1] )
            {
                this.hayCambioCasilla=true;
            }
            prota.getCasillaActual().x=coordCelda[0];
            prota.getCasillaActual().y=coordCelda[1];
        }
    }
    
    public void setEncaramiento(int nuevoEncaramiento)
    {
        this.encaramiento=nuevoEncaramiento;
        Gdx.app.log("Encaramineto:", ""+this.encaramiento);
    }
    
    public int getEncaramiento()
    {
        return this.encaramiento;
    }
    
    public int getAccionDesplazamiento()
    {
        return accionDesplazamiento;
    }
    
    public void setAccionDesplazamiento(int accionDesplazamiento)
    {
        this.accionDesplazamiento = accionDesplazamiento;
    }
    
    @Override
    public boolean destinoAlcalzado()
    {
        return true;
    }
    
    /** Indica si en el ultimo desplazamiento el personaje ha avanzado a una nueva casilla,
     *
     * @return boolean
     */
    public boolean hayCambioCasilla()
    {
        return this.hayCambioCasilla;
    }
    
    /** Establece si se ha Analizado el cambio de casilla del personaje,
     *
     */
    public void setCambioCasillaAnalizado()
    {
        this.hayCambioCasilla =false;
        
    }
}
