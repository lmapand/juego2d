package mi.paquete.juego.modelo.bicheria.desplazamiento;

public interface Desplazable
{
    public void actualizaPosicion(float delta);
    
    public void setEncaramiento(int nuevoEncaramiento);
    
    public int getEncaramiento();

    public int getAccionDesplazamiento();

    public void setAccionDesplazamiento(int accionDesplazamiento);
    
    public boolean destinoAlcalzado();
}
