package mi.paquete.juego.modelo;

public class DefProyectil
{
    public final String nombre;
    public final int velocidad;
    public final float radioDeteccion;
    public final String refAnimacion;
    public final String sonido;
    
    public DefProyectil(String nombre, int velocidad, float radioDeteccion, String refAnimacion, String sonido)
    {
        this.nombre = nombre;
        this.velocidad = velocidad;
        this.radioDeteccion = radioDeteccion;
        this.refAnimacion = refAnimacion;
        this.sonido = sonido;
    }
    
    @Override
    public String toString()
    {
        return "DefProyectil{" +
                "nombre='" + nombre + '\'' +
                ", velocidad=" + velocidad +
                ", radioDeteccion=" + radioDeteccion +
                ", refAnimacion='" + refAnimacion + '\'' +
                ", sonido='" + sonido + '\'' +
                '}';
    }
}
