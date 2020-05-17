package mi.paquete.juego.modelo;

public class CoordCasilla implements Comparable<CoordCasilla>
{
    public int x;
    public int y;
    
    public CoordCasilla(int x, int y)
    {
        this.x=x;
        this.y=y;
    }
    
    @Override
    public int compareTo(CoordCasilla coordCasilla)
    {
       if(this.x!=coordCasilla.x)
           return coordCasilla.x - this.x;
       
       return coordCasilla.y - this.y;
    }
}
