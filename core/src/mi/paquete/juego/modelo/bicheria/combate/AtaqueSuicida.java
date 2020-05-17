package mi.paquete.juego.modelo.bicheria.combate;

public class AtaqueSuicida extends Ataque
{
    private final int radio;
    
    public AtaqueSuicida(String nombre, float tiempoEjecucion, int danoBase, int coste, String refSonido, int radio)
    {
        super(nombre, tiempoEjecucion, danoBase, coste, refSonido);
        this.radio = radio;
    }
    
    public int getRadio()
    {
        return radio;
    }
}
