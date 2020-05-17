package mi.paquete.juego.modelo.bicheria;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

import mi.paquete.juego.modelo.CoordCasilla;
import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.auxiliar.Analizador;
import mi.paquete.juego.modelo.bicheria.combate.Atacable;
import mi.paquete.juego.modelo.bicheria.combate.Atacante;
import mi.paquete.juego.modelo.bicheria.desplazamiento.Desplazable;
import mi.paquete.juego.modelo.bicheria.efectos.Efecto;
import mi.paquete.juego.modelo.bicheria.efectos.EfectoDano;
import mi.paquete.juego.modelo.bicheria.percepcion.Percibidor;
import mi.paquete.juego.pantallas.PantallaAccion;


public class Proyectil extends SerFisico implements Desplazable, Dibujable, Percibidor, ElementoJuego
{
    
    private final Atacante lanzador;
    private final int encaramiento;
    
    private float contadorEstadoAnimacion =0.0f;
    private final int dano;
    private final String refSonido;
    private final float radioDeteccion;
    
    
    private final Class objetivo;
    
    public Proyectil(Animation<TextureRegion>[] animaciones, CoordCasilla casillaGeneracion, int encaramiento, int velocidad, Atacante lanzador, int dano, float radioDeteccion, String sonido )
    {
        super(animaciones, casillaGeneracion , 0, velocidad);
        this.lanzador=lanzador;
        this.encaramiento=encaramiento;
        this.refSonido=sonido;
        this.radioDeteccion=radioDeteccion;
        this.dano=dano;
        
        if(this.lanzador instanceof Fulano)
            this.objetivo = EnemigoGenerico.class;
        else
            this.objetivo=Fulano.class;
    
        //Gdx.app.log("CREACION PROYECTIL", "El enemigo es un " + this.objetivo.getName());
        
        Mundo mundo=Mundo.getInstance();
        mundo.getMapaEfectosSonido().get(this.refSonido).play();
        
        //Sobreescribimos los datos de osicionamiento de SerFisico...
        //this.posX = (casillaGeneracion.x)*this.ANCHO_SPRITE + this.ANCHO_SPRITE/2 + mundo.getAnchoCasilla()*Mundo.VECTOR_DESP[this.encaramiento].x;;
        //this.posY = (casillaGeneracion.y)*this.ANCHO_SPRITE + this.ANCHO_SPRITE/2 + mundo.getAltoCasilla()*Mundo.VECTOR_DESP[this.encaramiento].y;
    
        this.posX = ((SerFisico)lanzador).getPosX() + mundo.getAnchoCasilla()*Mundo.VECTOR_DESP[this.encaramiento].x;
        this.posY = ((SerFisico)lanzador).getPosY() + mundo.getAltoCasilla()*Mundo.VECTOR_DESP[this.encaramiento].y;
    }
    
    
    
    
    @Override
    public void dibuja(SpriteBatch batch, float delta)
    {
        this.contadorEstadoAnimacion+=delta;
        TextureRegion tx;
    
        tx = this.animaciones[this.encaramiento].getKeyFrame(this.contadorEstadoAnimacion, true);
        batch.draw(tx,this.getXSprite(),this.getYSprite() );
    }
    
    @Override
    public void eliminaRecursosGraficos()
    {
    
    }
    
    @Override
    public boolean esVisible()
    {
        return true;
    }
    
    @Override
    public int getOrdenDibujado()
    {
        return Dibujable.ORDEN_FLOTANDO_SOBRE_SUELO;
    }
    
    @Override
    public void actualizaPosicion(float delta)
    {
        this.posX+=this.velocidad*delta* Mundo.VECTOR_DESP[this.encaramiento].x;
        this.posY+=this.velocidad*delta* Mundo.VECTOR_DESP[this.encaramiento].y;
    
        Integer[] coordCelda=mundo.getCasilla(this.posX,this.posY);
        
        this.casillaActual.x=coordCelda[0];
        this.casillaActual.y=coordCelda[1];
        
    }
    
    @Override
    public void setEncaramiento(int nuevoEncaramiento)
    {
        //No hacer nada
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
    
    @Override
    public void setAccionDesplazamiento(int accionDesplazamiento)
    {
        //No hacer nada
    }
    
    @Override
    public boolean destinoAlcalzado()
    {
        return false;
    }
    
    @Override
    public void procesaPercepcion(PantallaAccion.RenderizadorAccion entorno, float delta)
    {
        
        boolean hayImpacto=false;
        //El proceso de entorno de este trebello es detectar un enemigo y achicharrarlo.
        List<Atacable> heridos=new ArrayList<Atacable>();
        
        for(ElementoJuego ej:entorno.getElementosJuego())
        {
            if(ej.getClass().equals(objetivo) && ej instanceof Atacable)
            {
                if (Analizador.distancia(this.posX, this.posY, ((SerFisico) ej).getPosX(), ((SerFisico) ej).getPosY()) < this.radioDeteccion)
                    heridos.add((Atacable) ej);
            }
        }
        
        if(!heridos.isEmpty())
        {
            //Gdx.app.log("IMPACTO PROYECTIL", "Hemos topado con una jicho(" + this.casillaActual.x+","+this.casillaActual.y);
            Efecto efectoImpacto=new EfectoDano( this.lanzador, heridos, this.dano, entorno);
            entorno.getEfectosActivos().add(efectoImpacto);
            hayImpacto = true;
        }
        
        if(!hayImpacto && mundo.esPared(this.casillaActual))  //Comprobar si hay impacto con la pared
        {
            //Gdx.app.log("IMPACTO PROYECTIL", "Hemos topado con una pared(" + this.casillaActual.x+","+this.casillaActual.y);
            hayImpacto = true;
        }
    
        //En el caso de impacto, hay que añadir esta casilla a las casilla a eliminar
        if(hayImpacto)
        {
            //ToDo: añadir efecto explosivo
            
            entorno.getElementosEliminar().add(this);
        }
    }
    
    
    @Override
    public TextureRegion getTexturaCadaver()
    {
        return null;
    }
}
