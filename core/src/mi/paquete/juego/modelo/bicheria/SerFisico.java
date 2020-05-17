package mi.paquete.juego.modelo.bicheria;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import mi.paquete.juego.modelo.CoordCasilla;
import mi.paquete.juego.modelo.Mundo;

/** Clase genérica que representa a un enemigo.
 *
 *  Definimos enemigo como un elemento hostil al jugador que le puede atacar y quitarle vida
 *  Así mismo, todos los enemigos pueden morir
 *  Se les considera 'entes físicos', con lo cual también tienen una posicion en el juego
 */
public abstract class SerFisico implements ElementoJuego
{
    
    /** Referencia al Mundo, para evitar re-referenciarlo
     *
     */
    protected final Mundo mundo=Mundo.getInstance();
    /** Caracteristicas de vida de una criatura.*/
    
    protected final int vidaMaxima;
    protected int vidaActual;
    protected final boolean recuperable;
    protected int ratioRecuperacion=0;
    protected int velocidad;
    
    protected float cronometroRegeneracion=0.0f;
    
    /** Propiedades de posicionamiento en el mundo*/
    //Posicion real del centro de gravedad del fulano
    protected float posX;
    protected float posY;
    //LA casilla del tile en la que se encuentra la criatura
    protected CoordCasilla casillaActual=new CoordCasilla(0,0);
    
    
    protected final Animation<TextureRegion>[] animaciones;
    
    protected final int ANCHO_SPRITE;
    protected final int ALTO_SPRITE;
    
    
    public SerFisico(Animation<TextureRegion>[] animaciones, CoordCasilla posicionInicial , int vidaMaxima, int velocidad )
    {
        //Establecemos los 'constraints' de tamaño
        this.animaciones=animaciones;
        this.ANCHO_SPRITE= animaciones[Mundo.ACCION_ABAJO].getKeyFrames()[0].getRegionWidth();
        this.ALTO_SPRITE= animaciones[Mundo.ACCION_ABAJO].getKeyFrames()[0].getRegionHeight();
        this.vidaMaxima=vidaMaxima;
        this.vidaActual=vidaMaxima;
        this.casillaActual.x=posicionInicial.x;
        this.casillaActual.y=posicionInicial.y;
        recuperable=false;
        this.velocidad=velocidad;
        this.posX = (posicionInicial.x)*this.ANCHO_SPRITE + this.ANCHO_SPRITE/2;
        this.posY = (posicionInicial.y)*this.ANCHO_SPRITE + this.ANCHO_SPRITE/2;
    }
    
    public SerFisico(Animation<TextureRegion>[] animaciones, CoordCasilla posicionInicial ,int vidaMaxima, int ratioRecuperacion, int velocidad  )
    {
        this.animaciones=animaciones;
        //Establecemos los 'constraints' de tamaño
        /**Todo: cambiar esto cuando tengamos todos los sprites*/
        this.ANCHO_SPRITE= animaciones[Mundo.ACCION_ABAJO].getKeyFrames()[0].getRegionWidth();
        this.ALTO_SPRITE= animaciones[Mundo.ACCION_ABAJO].getKeyFrames()[0].getRegionHeight();
        this.vidaMaxima=vidaMaxima;
        this.vidaActual=vidaMaxima;
        this.casillaActual.x=posicionInicial.x;
        this.casillaActual.y=posicionInicial.y;
        this.ratioRecuperacion=ratioRecuperacion;
        this.recuperable=true;
        this.velocidad=velocidad;
        this.posX = (posicionInicial.x)*this.ANCHO_SPRITE + this.ANCHO_SPRITE/2;
        this.posY = (posicionInicial.y)*this.ANCHO_SPRITE + this.ANCHO_SPRITE/2;
    }
    
     public boolean muriendo()
     {
         return vidaActual<=0;
     }
    

     
    /** Caracteristicas de representacion, vinculadas a la naturaleza de las mazmorras. */

    public void setPosX(float nuevaX)
    {
        this.posX=nuevaX;
        
    }
    
    public void setPosY(float nuevaY)
    {
        this.posY=nuevaY;
    }
     
     public float getXSprite()
     {
        return this.posX - mundo.getAnchoCasilla()/2;
     }
    
     public float getYSprite()
     {
        return this.posY ;
     }
     
     public float getPosX()
     {
         return this.posX;
     }
    
    public float getPosY()
    {
        return this.posY;
    }
    
    public int getVelocidad()
    {
        return this.velocidad;
    }
    
    public CoordCasilla getCasillaActual()
    {
        return casillaActual;
    }
    
    public int getvidaActual()
    {
        return this.vidaActual;
    }
    
    public int getvidaMaxima()
    {
        return this.vidaMaxima;
    }
    
    public boolean estaDanado()
    {
        return this.vidaActual<this.vidaMaxima;
    }
    
    
    public boolean estaMuerto()
    {
        return this.vidaActual<=0;
    
    }
    
    protected void regenera(float delta)
    {
        //Si esta muerto, no se regenera
        if(this.vidaActual<=0)
            return;
        
        
        if(this.ratioRecuperacion!=0 && vidaActual<vidaMaxima )
        {
            cronometroRegeneracion+=delta;
            if(cronometroRegeneracion>=ratioRecuperacion)
            {
                cronometroRegeneracion-=ratioRecuperacion;
                vidaActual++;
            }
        }
    }
    
    public abstract TextureRegion getTexturaCadaver();

    
}
