package mi.paquete.juego.modelo;

public class DefMazmorra
{
    public final String nombre;
    public final String archivo;
    public final Monstruo[] monstruos;
    
    public DefMazmorra(String nombre, String archivo, Monstruo[] monstruos)
    {
        this.nombre = nombre;
        this.archivo = archivo;
        this.monstruos = monstruos;
    }
    
    public String toString()
    {
        StringBuilder sb=new StringBuilder();
        sb.append(nombre).append("-").append(archivo);
         for(Monstruo monstruo: monstruos)
         {
             sb.append("\n").append(monstruo.defBicho.nombre);
             for(CoordCasilla cc:monstruo.puntosRuta)
                 sb.append("\t").append(""+cc.x).append(",").append(""+cc.y);
         }
         return sb.toString();
    }
    
    
    public static class Monstruo
    {
        private final DefBicho defBicho;
        private final CoordCasilla[] puntosRuta;
    
        public Monstruo(DefBicho defBicho, CoordCasilla[] puntosRuta)
        {
            this.defBicho = defBicho;
            this.puntosRuta = puntosRuta;
        }
    
        public DefBicho getDefBicho()
        {
            return defBicho;
        }
    
        public CoordCasilla[] getPuntosRuta()
        {
            return puntosRuta;
        }
    }
}
