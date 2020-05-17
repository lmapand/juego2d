package mi.paquete.juego.modelo.bicheria.desplazamiento;

import com.badlogic.gdx.Gdx;

import mi.paquete.juego.modelo.CoordCasilla;

public class MementoMovimiento
{
    private final Desplazable desplazaminetoAnterior;
    private final CoordCasilla casillaRetorno;
    
    public MementoMovimiento(Desplazable desplazamiento, CoordCasilla destino)
    {
        this.desplazaminetoAnterior=desplazamiento;
        this.casillaRetorno=new CoordCasilla(destino.x, destino.y);
        Gdx.app.log("MEMENTO", "Se ha creado un memento con destino a  "+destino.x+","+destino.y);
    }
    
    public CoordCasilla getCasillaRetorno()
    {
        return this.casillaRetorno;
    }
    
    public Desplazable getDesplazamientoAnterior()
    {
        return this.desplazaminetoAnterior;
    }
}
