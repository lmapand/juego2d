package mi.paquete.juego.modelo.bicheria.combate;

public abstract class Ataque
{
    /* los ataques a distancio generan PROYECTILES*/
    public static final int TIPO_ATAQUE_DISTANCIA=0;
    
    /* los ataques físicos tienen en cuenta el encaramiento y se reallzian a un cuadro de distancia (solo las cuatro direciones básicas)*/
    public static final int TIPO_ATAQUE_FISICO=1;
    
    /* Los ataques suicidas conllevan la muerte del atacante cuando se finaliza el tiempo de activacion*/
    public static final int ATAQUE_SUICIDA=2;
    
    private final String nombre;
    private final float tiempoEjecucion;
    private final int danoBase;
    private final int coste;
    private final String refSonido;
    
    public Ataque(String nombre, float tiempoEjecucion, int danoBase, int coste, String refSonido)
    {
        this.nombre=nombre;
        this.tiempoEjecucion = tiempoEjecucion;
        this.danoBase = danoBase;
        this.coste = coste;
        this.refSonido=refSonido;
    }
    
    public String getNombre(){return nombre;}
    
    public float getTiempoEjecucion()
    {
        return tiempoEjecucion;
    }
    
    public int getDanoBase()
    {
        return danoBase;
    }
    
    public int getCoste()
    {
        return coste;
    }
    
    public String getRefSonido()
    {
        return refSonido;
    }
    
    public String toString()
    {
        return String.format("TE:%5.2f\tDAÑO BASE:%3d\tCOSTE:%3d\t%20s",tiempoEjecucion,danoBase,coste, nombre);
        
    }
}
