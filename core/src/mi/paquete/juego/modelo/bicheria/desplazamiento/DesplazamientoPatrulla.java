package mi.paquete.juego.modelo.bicheria.desplazamiento;

import com.badlogic.gdx.Gdx;

import java.util.List;

import mi.paquete.juego.modelo.CoordCasilla;
import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.auxiliar.Analizador;
import mi.paquete.juego.modelo.bicheria.SerFisico;

/** desplazamiento genérico de patrulla de las criaturas que la realizan.
 *
 * - La mayor parte de las criaturas que se meuven realizan patrulla.
 * - El objetivo de esta clase es mantener las posiciones de patrulla.
 * - IMPORTANTE: Cuando algo interrumpe el movimiento de patrulla, ese objeto deberá almacenar la posicion a la
 * que ir en caso de retomar la actividad.
 * La patrulla se realiza entre los puntos indicados. En general, los NPC indican como punto inicial de patrulla el punto de 'spawn'
 *
 * */
public class DesplazamientoPatrulla implements Desplazable
{
    //Necesitamos almacenar los 'waypoints'; esto lo haremos en un array indexado con los puntos de patrulla.
    //Tambien necesitamos :
    //Un array de las casillas a las que vamos.
    //UN puntero que nos indique en que posicion de esa casilla nos encontramos.
    
    //El proceso es:
    //Al iniciar el desplazamiento, se evalua el recorrido desde la posicion actual a la siguiente
    
    //Todo: refinar para que haga la parada en el punto de patrulla un tiempo determinado
    private int accionDesplazamiento;
    private int encaramiento;
    
    //Todo: 'granuralizar' en función de los resultados obtenidos.
    private float radioCorte=1.5f;
    
    private SerFisico bicho;
    /** ALmacena cual es la casilla objetivo a la que queremos llegar.
     * Al llegar al final del recorrido, invertimos el indice, para que recorra en sentido inverso
     */
    private int indicePatrulla=1;
    private int direccionPatrulla=1;
    /** Al crear el objeto, se genera el ArrayList de destinos de patrulla, que se recorreran en el orden corrrspondiente.
     *  - Dicho arraylist se recorrerá hacia abajo y viceversa segun se vayan obteniendo los puntos de patrulla
     */
    private List<CoordCasilla>caminoPatrulla;
    
    private float destinoX;
    private float destinoY;
    /**
     *
     * @param bicho
     * @param destinos CoordCasilla[] array ordenado de los destinos de patrulla del enemigo
     */
    public DesplazamientoPatrulla(SerFisico bicho, List<CoordCasilla> destinos)
    {
        this.bicho=bicho;
        this.caminoPatrulla=destinos;
        
        //Evaluacion del Encaramiento inicial
        int xActual=bicho.getCasillaActual().x;
        int yActual=bicho.getCasillaActual().y;

        
        
        Mundo mundo=Mundo.getInstance();
        
        //Almacenamos por conveniencia las coordenadas destino
        this.destinoX=caminoPatrulla.get(indicePatrulla).x*mundo.getAnchoCasilla()+ mundo.getAnchoCasilla()/2;
        this.destinoY=caminoPatrulla.get(indicePatrulla).y*mundo.getAltoCasilla() + mundo.getAltoCasilla()/2;
    
        //Analizamos el encaramiento en funcion de la casilla destino.
        try
        {
            this.encaramiento = Analizador.calculaEncaramiento(bicho.getCasillaActual(), caminoPatrulla.get(indicePatrulla));
        }
        catch(Exception ex)
        {
            Gdx.app.error("ERROR en ENCARAMINTO",ex.getMessage());
            ex.printStackTrace();
        }
        this.accionDesplazamiento=this.encaramiento;
    }
    
    
    @Override
    public void actualizaPosicion(float delta)
    {
        Mundo mundo=Mundo.getInstance();
        
        //El desplazamiento es igual que el ya evaluado.
        float posNuevaX=bicho.getPosX() + bicho.getVelocidad()*delta* Mundo.VECTOR_DESP[accionDesplazamiento].x;
        float posNuevaY=bicho.getPosY() + bicho.getVelocidad()*delta*Mundo.VECTOR_DESP[accionDesplazamiento].y;
        
        //Evaluar si hemos llegado a la posicion de corte
        if(Analizador.distancia(this.destinoX, this.destinoY, posNuevaX, posNuevaY)<=radioCorte)
        {
            //Reajustar a la posicion destino, recalcular el encaramiento (o la pausa de patrulla)
            bicho.getCasillaActual().x=caminoPatrulla.get(indicePatrulla).x;
            bicho.getCasillaActual().y=caminoPatrulla.get(indicePatrulla).y;
    
            posNuevaX=bicho.getCasillaActual().x*mundo.getAnchoCasilla()+mundo.getAnchoCasilla()/2;
            posNuevaY=bicho.getCasillaActual().y*mundo.getAltoCasilla()+mundo.getAltoCasilla()/2;
            
            //Analisis de la posicion de patrulla
            if(indicePatrulla>=this.caminoPatrulla.size()-1 || indicePatrulla==0)
                direccionPatrulla=direccionPatrulla*-1;
            
            indicePatrulla+=direccionPatrulla;
            
            //Cambiar el destino
            this.destinoX=caminoPatrulla.get(indicePatrulla).x*mundo.getAnchoCasilla()+mundo.getAnchoCasilla()/2;
            this.destinoY=caminoPatrulla.get(indicePatrulla).y*mundo.getAltoCasilla()+mundo.getAltoCasilla()/2;
            
            //Cambiar el encaramiento
    
            try
            {
                this.encaramiento = Analizador.calculaEncaramiento(bicho.getCasillaActual(), caminoPatrulla.get(indicePatrulla));
            }
            catch(Exception ex)
            {
                Gdx.app.error("ERROR en ENCARAMINTO",ex.getMessage());
                ex.printStackTrace();
            }
            this.accionDesplazamiento=this.encaramiento;
        }
        
        //Reajustamos la variable de posicion
        bicho.setPosX(posNuevaX);
        bicho.setPosY(posNuevaY);
   }
    
    @Override
    public void setEncaramiento(int nuevoEncaramiento)
    {
        this.encaramiento=nuevoEncaramiento;
    }
    
    @Override
    public int getEncaramiento()
    {
        return this.encaramiento;
    }
    
    @Override
    public int getAccionDesplazamiento()
    {
        return this.accionDesplazamiento;
    }
    
    @Override
    public void setAccionDesplazamiento(int accionDesplazamiento)
    {
        this.accionDesplazamiento=accionDesplazamiento;
    }
    
    @Override
    public boolean destinoAlcalzado()
    {
        return false;
    }
}
