package mi.paquete.juego.modelo.bicheria.combate;

public class AtaqueDistancia extends Ataque
{
    private final String refProyectil;
    
    public AtaqueDistancia(String nombre,float tiempoEjecucion, int danoBase, int coste, String refSonido, String refProyectil)
    {
        super(nombre, tiempoEjecucion, danoBase, coste, refSonido);
        this.refProyectil = refProyectil;
    }
    
    public String getRefProyectil()
    {
        return refProyectil;
    }
}
