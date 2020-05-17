package mi.paquete.juego.modelo.bicheria.desplazamiento;


import com.badlogic.gdx.Gdx;

import java.util.List;

import mi.paquete.juego.modelo.CoordCasilla;
import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.auxiliar.Analizador;
import mi.paquete.juego.modelo.bicheria.SerFisico;

/** El desplazamiento que realiza un bicho para intentar alcanzar al Protagonista.
 *
 * Basicamente es un movimiento de posicionamiento, pero el uso de una clase diferenciada nos permite
 * analizar el movimiento por clase
 */
 
public class DesplazamientoPersecucion implements Desplazable
{
    private int accionDesplazamiento;
    private int encaramiento;
    
    private float radioCorte=1.5f;
    //Aqui guardamos el cambio de encaramiento debido a la realizacion de un ataque
    private int encaramientoPrevio;
    
    
    private final List<CoordCasilla> camino;
    private final SerFisico bicho;
    
    private int indiceCasillaObjetivo=0;
    
    private float destinoX;
    private float destinoY;
    
    private boolean destinoAlcanzado=false;
    
    private boolean reposicionando =true;
    //Angulo para la rutina de reposicionamiento
    private double anguloAcercamiento;
    
    
    public DesplazamientoPersecucion(SerFisico bicho, CoordCasilla destino)
    {
        Mundo mundo=Mundo.getInstance();
        this.bicho=bicho;
        List<CoordCasilla> trayectoProyectado=mundo.getCamino(bicho.getCasillaActual(), destino);
        //trayectoProyectado.remove(0);
        this.camino=trayectoProyectado;
    
    
        Gdx.app.log("PERCEPCION", "Iniciando proceso de persecucion");
        
        //DESPLAZAMIENTO PREVIO:
        //En el caso de que se haya detectado al enemigo, tenemos que posicionar a nuestro bicho en el centro de la casilla, para a continuacion seguir el movimiento.
        //El encaramiento lo vamos a analizar por distancia a dicho centro.
        float posCentroCasillaX = bicho.getCasillaActual().x * mundo.getAnchoCasilla() + mundo.getAnchoCasilla() / 2;
        float posCentroCasillaY = bicho.getCasillaActual().y * mundo.getAltoCasilla() + mundo.getAltoCasilla() /2;
        
        //Si estamos a distancia de corte, reposicionamos directamente:
        if(Analizador.distancia(bicho.getPosX(), bicho.getPosY(), posCentroCasillaX,posCentroCasillaY)<=radioCorte)
        {
            //Reajustar a la posicion destino, recalcular el encaramiento (o la pausa de patrulla)
            bicho.getCasillaActual().x = camino.get(indiceCasillaObjetivo).x;
            bicho.getCasillaActual().y = camino.get(indiceCasillaObjetivo).y;
            bicho.setPosX(posCentroCasillaX);
            bicho.setPosY(posCentroCasillaY);
            this.indiceCasillaObjetivo++;
            
            //Situacion en la que Perseguir es una sola casilla
            if (this.indiceCasillaObjetivo >= this.camino.size() )
            {
                this.destinoAlcanzado = true;
            }
            else
            {
                this.reposicionando = false;
                this.destinoX = camino.get(indiceCasillaObjetivo).x * mundo.getAnchoCasilla() + mundo.getAnchoCasilla() / 2;
                this.destinoY = camino.get(indiceCasillaObjetivo).y * mundo.getAltoCasilla() + mundo.getAltoCasilla() / 2;
                try
                {
                    this.encaramiento = Analizador.calculaEncaramiento(bicho.getCasillaActual(), camino.get(indiceCasillaObjetivo));
                } catch (Exception ex)
                {
                    Gdx.app.error("ERROR en ENCARAMINTO", ex.getMessage());
                    ex.printStackTrace();
                }
                this.accionDesplazamiento = this.encaramiento;
            }
        }
        else
        {
            this.destinoX=posCentroCasillaX;
            this.destinoY=posCentroCasillaY;
            this.anguloAcercamiento=Analizador.anguloResultante(bicho.getPosX(), bicho.getPosY(), this.destinoX, this.destinoY);
            Gdx.app.log("DESP PERSECUCION:", String.format("Yendo de %5.0f,%5.0f a %5.0f,%5.0f   %3.2f",bicho.getPosX(), bicho.getPosY(),destinoX, destinoY, Math.toDegrees(anguloAcercamiento)));
            
            //Analisis del encaramiento
            this.encaramiento=Analizador.calculaEncaramiento(anguloAcercamiento);
            this.accionDesplazamiento=this.encaramiento;
        }
    }
    
    @Override
    public void setEncaramiento(int nuevoEncaramiento)
    {
        this.encaramientoPrevio=this.encaramiento;
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
        return this.encaramiento;
    }
    
    /** Esta funcion puede ser llamada para realizar un disparo. */
    @Override
    public void setAccionDesplazamiento(int accionDesplazamiento)
    {
        //No hacer nada. La accionde desplazamiento es la misma que el encaramiento.
    }
    
    
    @Override
    public void actualizaPosicion(float delta)
    {
        //Si estamos en el destino, ya no nos desplazamos
        if(destinoAlcanzado)
            return;
        
        //Si estamos reposicionandonos en la casilla ejecutamos esta rutino
        if (this.reposicionando)
        {
            reposiciona(delta);
            return;
        }
        
        //Movimiento general, una vez alcanzado el destino
        Mundo mundo=Mundo.getInstance();
        //El desplazamiento es igual que el ya evaluado.
        float posNuevaX=bicho.getPosX() + bicho.getVelocidad()*delta* Mundo.VECTOR_DESP[accionDesplazamiento].x;
        float posNuevaY=bicho.getPosY() + bicho.getVelocidad()*delta*Mundo.VECTOR_DESP[accionDesplazamiento].y;
    
        if(Analizador.distancia(this.destinoX, this.destinoY, posNuevaX, posNuevaY)<=radioCorte)
        {
            //Reajustar a la posicion destino, recalcular el encaramiento (o la pausa de patrulla)
            bicho.getCasillaActual().x = camino.get(indiceCasillaObjetivo).x;
            bicho.getCasillaActual().y = camino.get(indiceCasillaObjetivo).y;
    
            posNuevaX = bicho.getCasillaActual().x * mundo.getAnchoCasilla() + mundo.getAnchoCasilla() / 2;
            posNuevaY = bicho.getCasillaActual().y * mundo.getAltoCasilla() + mundo.getAltoCasilla() / 2;
    
            //Aumentamos el contador de casillas Objetivo
            if (indiceCasillaObjetivo == this.camino.size() - 1)
            {
                this.destinoAlcanzado = true; //Se ha alcanzado el objetivo
            }
            else
            {
                //Actualizamos el contador
                indiceCasillaObjetivo++;
                //Cambiar el destino
                this.destinoX = camino.get(indiceCasillaObjetivo).x * mundo.getAnchoCasilla() + mundo.getAnchoCasilla() / 2;
                this.destinoY = camino.get(indiceCasillaObjetivo).y * mundo.getAltoCasilla() + mundo.getAltoCasilla() / 2;
    
                //Cambiar el encaramiento
    
                try
                {
                    this.encaramiento = Analizador.calculaEncaramiento(bicho.getCasillaActual(), camino.get(indiceCasillaObjetivo));
                } catch (Exception ex)
                {
                    Gdx.app.error("ERROR en ENCARAMINTO", ex.getMessage());
                    ex.printStackTrace();
                }
                this.accionDesplazamiento = this.encaramiento;
            }
        }
    
        //Reajustamos la variable de posicion
        bicho.setPosX(posNuevaX);
        bicho.setPosY(posNuevaY);
    }
    
    /** Funcion de acercamiento al centro de la casilla, para iniciar la persecucion de una manera controlda.
     *
     */
    private void reposiciona(float delta)
    {
        float nuevaPosX=bicho.getPosX() + bicho.getVelocidad()*delta*(float)Math.cos(anguloAcercamiento);
        float nuevaPosY=bicho.getPosY() + bicho.getVelocidad()*delta*(float)Math.sin(anguloAcercamiento);
        
        //Comprobar acercamiento
        double distancia=Analizador.distancia(nuevaPosX, nuevaPosY, this.destinoX, this.destinoY);
       
        if(distancia<this.radioCorte)
        {
            Gdx.app.log("DISTANCIA ACERCAMIENTO:", " Posicion alacanzada ("+distancia+")");
            bicho.setPosX(this.destinoX);
            bicho.setPosY(this.destinoY);
            this.reposicionando=false;
        }
        else
        {
            bicho.setPosX(nuevaPosX);
            bicho.setPosY(nuevaPosY);
        }
    }
    
    @Override
    public boolean destinoAlcalzado()
    {
        return destinoAlcanzado;
    }
}
