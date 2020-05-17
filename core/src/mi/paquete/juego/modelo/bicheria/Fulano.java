package mi.paquete.juego.modelo.bicheria;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

import mi.paquete.juego.modelo.CoordCasilla;
import mi.paquete.juego.modelo.DefProyectil;
import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.bicheria.combate.Atacable;
import mi.paquete.juego.modelo.bicheria.combate.Atacante;
import mi.paquete.juego.modelo.bicheria.combate.Ataque;
import mi.paquete.juego.modelo.bicheria.combate.AtaqueDistancia;
import mi.paquete.juego.modelo.bicheria.combate.AtaqueFisico;
import mi.paquete.juego.modelo.bicheria.desplazamiento.Desplazable;
import mi.paquete.juego.modelo.bicheria.desplazamiento.DesplazamientoPersonaje;
import mi.paquete.juego.modelo.bicheria.efectos.EfectoGeneraAviso;
import mi.paquete.juego.modelo.bicheria.percepcion.Percibidor;
import mi.paquete.juego.pantallas.PantallaAccion;

public class Fulano extends SerFisico implements Dibujable, Desplazable, Percibidor, Atacante, Atacable
{
    public static final int MANA_INICIAL=100;
    public static final int TASA_RECUPERACION_MANA=1;
    
    //Todo buscar una forma menos absurda de contabilizar todos estos tiempos.
    private float contadorEstadoAnimacion =0.0f;  //Cuenta la delta de estado de animacion
    private float contadorAtaque=0.0f; //Cuenta la delta de realizacion de  ataque
    
    
    private Desplazable desplazamiento;
    
    /** Variables de control de mana*/
    private int manaMaximo=Fulano.MANA_INICIAL;
    private int manaActual=Fulano.MANA_INICIAL;
    private float cronometroRecupMana=0.0f; //Cuenta la delta del contador de percepcion (recuperacion de mana y vida)
    private int indiceRecuperacionMana=TASA_RECUPERACION_MANA; //Numero de puntos de mana recuperados por segundo
    
    /** Variables de control de ataque*/
    private boolean estaAtacando=false;
    
    private ArrayList<Ataque>ataquesDisponibles=new ArrayList<Ataque>();
    private Ataque ataqueActual=null;
    
    /**Nombre,  niveles y experiencia*/
    private final String nombre;
    private int nivel=0;
    private int XP=0;
    
    
    /** Sistema provisional de control: si tiene la lave, puede finalizar la mazmorra*/
    private boolean tieneLlave=false;
    
    /* La accion es la direccion de movimiento,
     el encaramiento es hacia donde mira (aunque no haya movimiento).
      Sirve para la orientación del disparo y el conjjunto de Sprites de dibujado*/
    //Todo: establecer estos datos al cargar el MAPA (deberian de venir codificadas en el mismo)
  
    
    public Fulano(Animation<TextureRegion>[] anim, int vidaMaxima, int ratioRecuperacion, CoordCasilla posicionInicial, String nombre, int xpActual, int nivel )
    {
        super(anim, posicionInicial,vidaMaxima, ratioRecuperacion, 60 );
    
        
        /** Establecemos la interfaz desplazable de nuestro fulano.*/
        this.desplazamiento=new DesplazamientoPersonaje(this);
        
        //Establecemos el ataque básico inicial
        Mundo mundo=Mundo.getInstance();
        this.ataquesDisponibles.add(mundo.getMapaAtaques().get("Proyectil igneo"));
        
        //Caracteristicas de inicio del personaje
        this.XP=xpActual;
        this.nivel=nivel;
        this.nombre=nombre;
    }
    
    @Override
    public void actualizaPosicion(float delta)
    {
        this.desplazamiento.actualizaPosicion(delta);
    }
    
    @Override
    public void setEncaramiento(int nuevoEncaramiento)
    {
        this.desplazamiento.setEncaramiento(nuevoEncaramiento);
    }
    
    @Override
    public int getEncaramiento()
    {
        return this.desplazamiento.getEncaramiento();
    }
    
    @Override
    public int getAccionDesplazamiento()
    {
        return this.desplazamiento.getAccionDesplazamiento();
    }
    
    /** Establece la accion de Desplazaiento adecuada.
     *
     * Todos los cambios de accion se deben hacer desde este método
     *
     * @param accionDesplazamiento una de las constantes MUNDO_ACCION_*
     */
    @Override
    public void setAccionDesplazamiento(int accionDesplazamiento)
    {
        //Debemos mantener el encaramiento
        if(accionDesplazamiento!=Mundo.ACCION_PARADO)
        {
            this.desplazamiento.setEncaramiento(accionDesplazamiento);
            
        }
    
        this.desplazamiento.setAccionDesplazamiento(accionDesplazamiento);
    }
    
    @Override
    public boolean destinoAlcalzado()
    {
        return this.desplazamiento.destinoAlcalzado();
    }
    
    
    @Override
    public void dibuja(SpriteBatch batch, float delta)
    {
        this.contadorEstadoAnimacion +=delta;
        TextureRegion tx;
        
        //Evaluacion de la imagen en funcion de desplazamiento y encaramiento
        if(desplazamiento.getAccionDesplazamiento()==Mundo.ACCION_PARADO)
            tx = this.animaciones[desplazamiento.getEncaramiento()].getKeyFrames()[0];
        else
            tx = this.animaciones[desplazamiento.getEncaramiento()].getKeyFrame(contadorEstadoAnimacion, true);
            
        
        //batch.draw(this.textura,this.getXSprite(),this.getYSprite() );
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
        return ORDEN_APOYADO_SUELO;
    }
    
    
    /** En el caso de nuestro fulano, la percepcion consiste en desvelar las casillas del mundo que son visibles para el.
     *
     * Tambien se evaluan los objetos que hay en la casilla tras el desplazamiento
     *
     * */
    @Override
    public void procesaPercepcion(PantallaAccion.RenderizadorAccion entorno, float delta)
    {
        //Actualizacion de mana y de Vida.
        this.regenera(delta);
        
        //Evalucacion de visibilidad
        Mundo mundo=Mundo.getInstance();
        mundo.evaluaVisibilidad(this.casillaActual.x,this.casillaActual.y);
       
        /*Evaluacion de la existencia de llave, salida o empanada (fon de partida) si se ha cambiado de casilla*/
        if( ((DesplazamientoPersonaje)this.desplazamiento).hayCambioCasilla())
        {
            if(mundo.hayLLaveEnCasilla(this.casillaActual))
            {
                //añadimos el efecto de mostrar el texto de aviso
                entorno.getEfectosActivos().add(new EfectoGeneraAviso(Mundo.TEXTO_AVISO_LLAVE_ENCONTRADA,
                                                                        Mundo.COLOR_AVISO_LLAVE_ENCONTRADA,
                                                                        this.getPosX(),
                                                                        this.getPosY(),
                                                                        entorno));
                //Marcamos la llave como conseguida
                this.tieneLlave = true;
            }
            
            //Hemos llegado a la salida del nivel
            if(mundo.estaEnSalida(this.casillaActual))
            {
                if(this.tieneLlave) //Llamar al proceso  de fin de nivel
                    entorno.finalizaNivel();
                else //Mostrar mensaje flotante
                    entorno.getEfectosActivos().add(new EfectoGeneraAviso(Mundo.TEXTO_AVISO_FALTA_LLAVE,
                            Mundo.COLOR_AVISO_FALTA_LLAVE,
                            this.getPosX(),
                            this.getPosY(),
                            entorno));
            }
            
            /* SE HA ENCONTRADO LA EMPANADA (FIN DE PARTIDA: EL JUGADOR HA GANADO.
            * Mostrar diálogo de ganador y fin de partida
             */
            if(mundo.hayEmpanadaEnCasilla(this.casillaActual))
                entorno.finalizaPartida();
           
            //MArcar la casilla como evaluada
            ((DesplazamientoPersonaje)this.desplazamiento).setCambioCasillaAnalizado();
        }
    }
    
    
    @Override
    public boolean estaAtacando()
    {
        return estaAtacando;
    }
    
    @Override
    public Object procesaEstadoAtaque(float delta)
    {
        Mundo mundo=Mundo.getInstance();
        this.contadorAtaque+=delta;
        if(contadorAtaque>=ataqueActual.getTiempoEjecucion())
        {
            //Reinicio e las variables de ataque
            this.contadorAtaque=0.0f;
            estaAtacando=false;
            
            //Recalculo del mana
            //Deberiamos de mostrar un 'mensajito' (Efecto mensaje)
            if(this.manaActual < ataqueActual.getCoste())
            {
                //Todo: mostrar mensaje 'mana insuficiente'
                return null;
            }
            manaActual-=ataqueActual.getCoste();
            
            //se procesa el ataque, y se devuelven los efectos correspondientes
            //ToDo:PROBANDO Caso generico, ataque de bola de fuego, generar los demas
            if(ataqueActual instanceof AtaqueDistancia)
            {
                DefProyectil dp=mundo.getMapaProyectiles().get( ((AtaqueDistancia)ataqueActual).getRefProyectil());
                /** Tomar los datos de este ataque a distancia y crear el proyectil.
                 * como es el caso del fulano, este tiene modificadores que se aplican a l daño inflingido, a la velocidad, etc.
                 * ToDo: aplicar los modificadores apropiados. JUSTO AQUI DEBAJO...
                  */
                int velocidad=dp.velocidad;
                int dano=ataqueActual.getDanoBase();
                
                
                //Cosntruir el proyectil, aplicando los modificadores necesarios.
                return new Proyectil(mundo.getAtlasAnimaciones().get(dp.refAnimacion),
                                                    this.casillaActual,
                                                    this.desplazamiento.getEncaramiento(),
                                                    velocidad,
                                                    this,
                                                    dano,
                                                    dp.radioDeteccion,
                                                    dp.sonido);
            }
            else if(ataqueActual instanceof AtaqueFisico)
            {
                //Proceso del ataque fisico...
                
                return null;
            }
            else
                return null;
        }
        else
            return null;
    }
    
    
    
    @Override
    public Ataque iniciaAtaque(int idAtaque)
    {
        switch(idAtaque)
        {
            case Input.Keys.F:
                if(this.getIndiceAtaqueFisico()!=-1)
                {
                    this.contadorAtaque=0.0f;
                    this.estaAtacando=true;
                    ataqueActual=this.ataquesDisponibles.get(getIndiceAtaqueFisico());
                    return ataqueActual;
                }
                else
                {
                    //Mostrar un mensaje de 'MANA INSUFICIENTE
                    Gdx.app.log("INICIO ATAQUE", "No dispones de ese ataque!!!!");
                }
                break;
    
            case Input.Keys.SPACE: //El ataque a distancia es el que se guarda como primero del fulano.
                
                if(this.manaActual >= this.ataquesDisponibles.get(0).getCoste())
                {
                    this.contadorAtaque=0.0f;
                    this.estaAtacando=true;
                    ataqueActual=this.ataquesDisponibles.get(0);
                    mundo.getMapaEfectosSonido().get(ataqueActual.getRefSonido()).play();
                    return ataqueActual;
                    
                }
                else
                {
                    //Mostrar un mensaje de 'MANA INSUFICIENTE
                    mundo.getMapaEfectosSonido().get("mana insuficiente").play();
                    Gdx.app.log("INICIO ATAQUE", "MANA INSUFICIENTE!!!!");
                }
                break;
        }
        //Nuestro fulano ataca con bola de fuego o con Ataque físico; de momento, implementaremos el atque Bola de fuego nada más
        return null;
    }
    
    
    @Override
    public int getIndiceAtaqueDistancia()
    {
        return 0;
    }
    
    @Override
    public int getIndiceAtaqueSuicida()
    {
        return -1;
    }
    
    @Override
    public int getIndiceAtaqueFisico()
    {
        return (ataquesDisponibles.size()>1) ? 1: -1;
    }
    
    @Override
    protected void regenera(float delta)
    {
        //Si esta muerto, mo regenera
        if(vidaActual<=0)
            return;
            
        super.regenera(delta);
        
        //Regeneracion del mana
        if(this.manaActual<this.manaMaximo)
        {
            this.cronometroRecupMana += delta;
            if (this.cronometroRecupMana >= 1.0)
            {
                int uds = (int) this.cronometroRecupMana;
                this.manaActual += uds * this.indiceRecuperacionMana;
                this.cronometroRecupMana-=uds;
    
                if (this.manaActual >= this.manaMaximo)
                {
                    this.manaActual = this.manaMaximo;
                    this.cronometroRecupMana = 0.0f;
                }
            }
        }
    }
    
    @Override
    public TextureRegion getTexturaCadaver()
    {
        return this.animaciones[0].getKeyFrames()[0];
    }
    
    @Override
    public void setDano(int dano)
    {
        //Todo: hacer una prueba de reduccion de daño (con un tipo de habilidad de nivel, p.e.)
        this.vidaActual-=dano;
        if(this.vidaActual<0)
            this.vidaActual=0;
    }
    
    
    public void anhadeXP(int xpAnadir)
    {
        this.XP+=xpAnadir;
        
    }
    
    public int getXP()
    {
        return this.XP;
    }
    
    
    public int getManaActual()
    {
        return manaActual;
    }
    
    public int getManaMaximo()
    {
        return manaMaximo;
    }
    
    public String getNombre()
    {
        return nombre;
    }
    
    public int getNivel()
    {
        return nivel;
    }
}
