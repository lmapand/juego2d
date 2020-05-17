package mi.paquete.juego.modelo;


import mi.paquete.juego.modelo.bicheria.combate.Ataque;

/** Definicion de los datos mas importantes de las criaturas.
 *
 */
public class DefBicho
{
    public final String nombre;
    public final int pv;
    public final int tasaRegen;
    public final String archivoAnim;
    public final float vRepro; //Velocidad de reproduccion de la animacion
    public final int velocidad;
    public final float distanciaPercepcion;
    public final Ataque[] ataques;
    public final int xp;
    
    public DefBicho(String nombre, int pv, int tasaRegen, String archivosAnim, int velocidad, float distPercepcion, float velReproAnimacion, Ataque[] ataques, int xp)
    {
        this.nombre = nombre;
        this.pv = pv;
        this.tasaRegen = tasaRegen;
        this.archivoAnim = archivosAnim;
        this.velocidad = velocidad;
        this.distanciaPercepcion=distPercepcion;
        this.vRepro=velReproAnimacion;
        this.xp=xp;
        this.ataques=ataques;
    }
    
    public boolean esMovil()
    {
        return velocidad<=0;
    }

    @Override
    public String toString()
    {
        return String.format("%20s\t%3d\t%d\t%d",nombre, pv, tasaRegen,velocidad);
    }
   
}
