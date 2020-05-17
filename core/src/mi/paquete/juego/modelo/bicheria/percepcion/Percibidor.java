package mi.paquete.juego.modelo.bicheria.percepcion;

import mi.paquete.juego.pantallas.PantallaAccion;

public interface Percibidor
{
    public void procesaPercepcion(PantallaAccion.RenderizadorAccion entorno, float delta);
}
