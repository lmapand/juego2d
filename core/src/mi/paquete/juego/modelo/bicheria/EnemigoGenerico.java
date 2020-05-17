package mi.paquete.juego.modelo.bicheria;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

import mi.paquete.juego.modelo.CoordCasilla;
import mi.paquete.juego.modelo.DefBicho;
import mi.paquete.juego.modelo.DefProyectil;
import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.auxiliar.Analizador;
import mi.paquete.juego.modelo.bicheria.combate.Atacable;
import mi.paquete.juego.modelo.bicheria.combate.Atacante;
import mi.paquete.juego.modelo.bicheria.combate.Ataque;
import mi.paquete.juego.modelo.bicheria.combate.AtaqueDistancia;
import mi.paquete.juego.modelo.bicheria.combate.AtaqueFisico;
import mi.paquete.juego.modelo.bicheria.combate.AtaqueSuicida;
import mi.paquete.juego.modelo.bicheria.desplazamiento.Desplazable;
import mi.paquete.juego.modelo.bicheria.desplazamiento.DesplazamientoAleatorio;
import mi.paquete.juego.modelo.bicheria.desplazamiento.DesplazamientoPatrulla;
import mi.paquete.juego.modelo.bicheria.desplazamiento.DesplazamientoPersecucion;
import mi.paquete.juego.modelo.bicheria.desplazamiento.DesplazamientoRecuperarPosicion;
import mi.paquete.juego.modelo.bicheria.desplazamiento.MementoMovimiento;
import mi.paquete.juego.modelo.bicheria.efectos.EfectoAtaqueSuicida;
import mi.paquete.juego.modelo.bicheria.percepcion.Percibidor;
import mi.paquete.juego.pantallas.PantallaAccion;

/** un enemigo Dummy para hacer las pruebas de busyqeda de caminos y maquina de estados de comportamiento
 *
 */
public class EnemigoGenerico extends SerFisico implements Desplazable, Dibujable, Percibidor, Atacante, Atacable
{

    
    private final DefBicho refBicho;
    
    private Desplazable desplazamiento=null;
    
    private float cronometroDibujado=0.0f;
    
    private final float distanciaPercepcion;
    
    private boolean enemigoPercibido=false;
    
    private MementoMovimiento movimientoAnterior=null;
    
    private float cronometroRegeneracion=0.0f;
    
    /** Ataques e indices de los mismos Indices de los ataques que puede realizar.*/
    private final Ataque[] ataques;
    private int indiceAtaqueDistancia=Atacante.ATAQUE_NO_DISPONIBLE;
    private int indiceAtaqueSuicida=Atacante.ATAQUE_NO_DISPONIBLE;
    private int  indiceAtaqueFisico=Atacante.ATAQUE_NO_DISPONIBLE;

    /** Variables para la gestion de los ataques*/
    private boolean estaAtacando;
    private Ataque ataqueActual=null;
    private float cronometroAtaque=0.0f;

    /** Constructor para un Enemigo con estrategia de Patrulla.
     *
     * @param anim
     * @param vidaMaxima
     * @param ratioRecuperacion
     * @param patrulla
     */
    public EnemigoGenerico(DefBicho bixoBase, Animation<TextureRegion>[] anim, int vidaMaxima, int ratioRecuperacion, int velocidad, List<CoordCasilla> patrulla, float distanciaPercepcion, Ataque[] ataques)
    {
        super(anim, patrulla.get(0), vidaMaxima, ratioRecuperacion, velocidad);
    
        this.refBicho=bixoBase;
        this.distanciaPercepcion=distanciaPercepcion;
        
        
        if(patrulla.size()>1)
            this.desplazamiento = new DesplazamientoPatrulla(this, patrulla);
        
        /* Establecer los ataques y los indices de los mismos*/
        this.ataques=ataques;
        for(int x=0;x<ataques.length;x++)
            if(ataques[x] instanceof AtaqueFisico)
            {
                Gdx.app.log("ATAQUE FISICO:", ataques[x].getNombre() );
                this.indiceAtaqueFisico = x;
            }
            else if(ataques[x] instanceof AtaqueDistancia)
            {
                Gdx.app.log("ATAQUE DISTANCIA:", ataques[x].getNombre() );
                this.indiceAtaqueDistancia = x;
            }
            else
            {
                Gdx.app.log("ATAQUE SUICIDA:", ataques[x].getNombre() );
                indiceAtaqueSuicida = x;
            }
    }
    
    public DefBicho getRefBicho()
    {
        return refBicho;
    }
    
    public void setDesplazable(Desplazable desplazamiento)
    {
        this.desplazamiento=desplazamiento;
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
    
    @Override
    public void setAccionDesplazamiento(int accionDesplazamiento)
    {
        this.desplazamiento.setAccionDesplazamiento(accionDesplazamiento);
    }
    
    @Override
    public boolean destinoAlcalzado()
    {
        return this.desplazamiento.destinoAlcalzado();
    }
    
    
    /** implementacion de dibujable.
     *
     * @param batch
     * @param delta
     */
    @Override
    public void dibuja(SpriteBatch batch, float delta)
    {
        //Si está muerto, no representar, porque lo sustiuye el cadaver
        if(this.vidaActual<=0)
            return;
        
        TextureRegion tx;
        //Si está atacando, mostrar la imagen de ataque asociada al ataque (EL sprite de la primera fila asociado al encaramiento)
        if(estaAtacando)
            tx=this.animaciones[0].getKeyFrames()[Mundo.INDICE_ANIM_ATAQUE[desplazamiento.getEncaramiento()]];
        else
        {
            this.cronometroDibujado += delta;
    
            //Evaluacion de la imagen en funcion de desplazamiento y encaramiento
            if (desplazamiento.getAccionDesplazamiento() == Mundo.ACCION_PARADO)
                tx = this.animaciones[desplazamiento.getEncaramiento()].getKeyFrames()[0];
            else
                tx = this.animaciones[desplazamiento.getEncaramiento()].getKeyFrame(this.cronometroDibujado, true);
        }
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
        return mundo.esCasillaVisible(this.casillaActual);
    }
    
    @Override
    public int getOrdenDibujado()
    {
        return Dibujable.ORDEN_APOYADO_SUELO;
    }
    
    /** Implementacin de Percibidor */
    
    
    
    
    @Override
    public void procesaPercepcion(PantallaAccion.RenderizadorAccion entorno, float delta)
    {
        //Realizar el proceso de regeneracion
        this.regenera(delta);
        
        //Si esta atacando, se encargará el render de gestionar el estado de ataque
        if(this.estaAtacando)  //Procesa el estado de ataque
        {
            return;
        }
        //Si no está atacando, se desplazará, a no ser que detecte al enemigo
        // Casos:

        float posicionProtaX=entorno.getProta().getPosX();
        float posicionProtaY=entorno.getProta().getPosY();
        
        //SE REALIZA LA PERCEPCION DEL ENEMIGO
        if(!entorno.getProta().estaMuerto() &&  this.mundo.seVenCasillas(this.distanciaPercepcion, this.posX, this.posY, posicionProtaX, posicionProtaY) )
        {
            if(!enemigoPercibido) //Se acaba de percibir al enemigo
            {
                this.enemigoPercibido = !this.enemigoPercibido;
                //Guardamos el estado actual de movimiento e iniciamos el movimiento 'persecucion'
                if(this.desplazamiento instanceof DesplazamientoPatrulla || this.desplazamiento instanceof DesplazamientoAleatorio)
                    this.movimientoAnterior=new MementoMovimiento(this.desplazamiento, this.casillaActual);
                
                this.desplazamiento=new DesplazamientoPersecucion(this, ((PantallaAccion.RenderizadorAccion) entorno).getProta().getCasillaActual());
            }
            else //El enemigo ya habia sido percibido, y se estará en un proceso de 'Persecucion' de 'Evasion'
            {
                //Todo: Evaluar si se puede realizar un ataque; sino lo que ya está
                if(this.desplazamiento.destinoAlcalzado())
                {
                    //Todo: Evaluar si no será necesario realizar un desplazamiento evasivo! ->Buscar al aliado mas cercano, p.e., o a una 'casilla segura'
                    //Gdx.app.log("PERCEPCION", "DESTINO ALCANZADO!!!!!");
                    this.desplazamiento=new DesplazamientoPersecucion(this, ((PantallaAccion.RenderizadorAccion) entorno).getProta().getCasillaActual());
                }
                else
                    this.desplazamiento.actualizaPosicion(delta);
                
            }
            
            //En cualquiera de los casos, si ha lugar a poder atacar, realizamos el ataque
            //Primero se evalua el posible ataque fisico o 'suicida' todo: evaluar que si estamos dañados
            Ataque ataqueSeleccionado=evaluaPosibilidadAtaque( entorno.getProta());
            if( ataqueSeleccionado!=null)
            {
                //Establecer las variables de ataque
                this.ataqueActual=ataqueSeleccionado;
                this.estaAtacando=true;
                this.cronometroAtaque=0.0f;
                
                //Reproducir el efecto de sonido del ataque
                this.mundo.getMapaEfectosSonido().get(this.ataqueActual.getRefSonido()).play();
            }
        }
        else //NO SE PERCIBE AL ENEMIGO
        {
            //  Si su rituina era patrullar o 'movimiento aleatorio', continua la rutina
            // Si su rutina era perseguir, continua la persecucionmientras no haya llegado al destino
            
            if(enemigoPercibido) //Se ha perdido la pista del enemigo
            {
    
                //Gdx.app.log("PERCEPCION", "El BIXO NO VE al PROTA!!!!!");
                //Si estabamos en modo persecuecion seguimos persiguiendo, hasta que hayamos llegado al punto procesado, momento en el cual el bixo recupera su estado anterior
                if (this.desplazamiento instanceof DesplazamientoPersecucion)
                {
                    if (this.desplazamiento.destinoAlcalzado())
                    {
                        //Gdx.app.log("PERCEPCION", "Destino alcanzado, retomando accion por defecto !!!!!");
                        enemigoPercibido = !enemigoPercibido;
    
                        //Trazamos una ruta hacia la casilla desde donde dejamos la patrulla.
                        this.desplazamiento = new DesplazamientoRecuperarPosicion(this, this.movimientoAnterior.getCasillaRetorno());
                        return;
                    }
                    else //Continuamos con el desplazamiento de persecucion
                    {
                        this.desplazamiento.actualizaPosicion(delta);
                        return;
                    }
                }
                else //No era un desplazamiento de persecucion, deberia de ser un desplazamiento de Evasion
                {
                    //Todo: desplazamiento de evasion
                    this.enemigoPercibido = !this.enemigoPercibido;
                    return;
                }
            }
            
            //Movimiento estandard, sin persecuciones...
            if(!this.desplazamiento.destinoAlcalzado())
                this.desplazamiento.actualizaPosicion(delta);
            else //Este seria el caso de DesplazamientoAleatorio o DesplazamientoRecuperarPosicion al finalizar el ciclo
            {
                //Si es un movimiento aleatorio, generar una nueva ruta
        
                //Si es un movimiento de recuperacion de posicion, cargar la informacion de desplazamiento anterior,
                if(this.desplazamiento instanceof DesplazamientoRecuperarPosicion)
                {
                    //Actualizamos la posicion 'a mano'
                    ((DesplazamientoRecuperarPosicion)this.desplazamiento).centra();
                    
                    //En teoria, deberia de retomar la informacion tal cual l atenia y seguir
                    this.desplazamiento = this.movimientoAnterior.getDesplazamientoAnterior();
                }
            }
        }
    }
    
    
    /** Interfaz de atacante*/
    
    @Override
    public boolean estaAtacando()
    {
        return this.estaAtacando;
    }
    
    @Override
    public Object procesaEstadoAtaque(float delta)
    {
        this.cronometroAtaque+=delta;
        //El ataque se está ejecutando
        if(this.cronometroAtaque<this.ataqueActual.getTiempoEjecucion())
            return null;
        else //Finalizacion de ejecucion del ataque, ahora hay que analizar el resultado y generar los objetos correspondientes
        {
            //reseteamos las variables necesarias
            this.cronometroAtaque=0.0f;
            this.estaAtacando = false;
            
            if(ataqueActual instanceof AtaqueDistancia)  //Crear y devolver el proyectil
            {
                DefProyectil dp=mundo.getMapaProyectiles().get( ((AtaqueDistancia)ataqueActual).getRefProyectil());
               
                return new Proyectil(mundo.getAtlasAnimaciones().get(dp.refAnimacion),
                                        this.casillaActual,
                                        this.getEncaramiento(),
                                        dp.velocidad,
                                        this,
                                        ataqueActual.getDanoBase(),
                                        dp.radioDeteccion,
                                        dp.sonido);
            }
            else if(ataqueActual instanceof AtaqueSuicida)  //Generar los elementos necesarios para el proceso
                //Generar el efecto de daño
                return new EfectoAtaqueSuicida(this,  ((AtaqueSuicida)ataqueActual).getRadio(), ataqueActual.getDanoBase() );

            else
                return null;
        }
    }
    
    @Override
    public Ataque iniciaAtaque(int idAtaque)
    {
        return null;
    }
    
    @Override
    public int getIndiceAtaqueDistancia()
    {
        return indiceAtaqueDistancia;
    }
    
    @Override
    public int getIndiceAtaqueSuicida()
    {
        return indiceAtaqueDistancia;
    }
    
    @Override
    public int getIndiceAtaqueFisico()
    {
        return indiceAtaqueFisico;
    }
    
    /** Evalua la posibilidad de ataque de la criatura.
     *
     * En caso de que sea viable, establece cual es el ataque a realizar.
     * El orden de ataca generico es: fisico, suicida, distancia
     * @return Ataque a realizar, o null si no es posible realizar ninguno
     */
    protected Ataque evaluaPosibilidadAtaque( Fulano objetivo)
    {
        //Evaluacion de ataque fisico
        
         if(indiceAtaqueFisico!= Atacante.ATAQUE_NO_DISPONIBLE && evaluaAtaqueFisico(objetivo))
         {
             return this.ataques[indiceAtaqueFisico];
         }
         
         if(indiceAtaqueSuicida!= Atacante.ATAQUE_NO_DISPONIBLE  && evaluaAtaqueSuicida(objetivo))
         {
             return this.ataques[indiceAtaqueSuicida];
         }
    
        if(indiceAtaqueDistancia!= Atacante.ATAQUE_NO_DISPONIBLE && evaluaAtaqueDistancia(objetivo) )
        {
            return this.ataques[indiceAtaqueDistancia];
        }
    
        return null;
    
    }
    
    /** Ataque fisico: el objetivo debe estar en la casilla adyacente al encaramiento de nuestro bicho (y a una distancia menor de ANCHO_CASILLA)
     *
     * @param objetivo el elemento susceptible de ataque
     * @return true si es posible realizar el ataque
     */
    protected boolean evaluaAtaqueFisico(Fulano objetivo)
    {
        if( Analizador.calculaEncaramientoParaIr(this.casillaActual, objetivo.casillaActual)==this.getEncaramiento()
                &&
                Analizador.distancia(this.posX,this.posY, objetivo.getPosX(),objetivo.getPosY()) < mundo.getAnchoCasilla() )
           return true;
           
        return false;
    }
    
    
    /** Ataque suicida:
     *   En caso de que esté a la distancia de impacto
     *
     * @param objetivo Fulano, el protegonista
     * @return true si es posible realizar el ataque
     */
    protected boolean evaluaAtaqueSuicida(Fulano objetivo)
    {
        if(Analizador.distancia(this.posX,this.posY, objetivo.getPosX(),objetivo.getPosY()) <((AtaqueSuicida)this.ataques[indiceAtaqueSuicida]).getRadio() )
            return true;
        
        return false;
        
    }
    
    
    /** Ataque distancia: el objetivo debe estar en linea, (el hecho de que debe ser visible hace que un disparo pueda tomarse como destino)
     *
     * @param objetivo el elemento susceptible de ataque
     * @return true si es posible realizar el ataque
     */
    protected boolean evaluaAtaqueDistancia(Fulano objetivo)
    {
        if( Analizador.calculaEncaramientoParaIr(this.casillaActual, objetivo.casillaActual)==this.getEncaramiento() )
            return true;
        
        return false;
    }
    
    /** Si hubiera algun tipo de reduccin de daño, habría que especificarlo aqui
     *
     * @param dano int cantidad de daño recibido (que no tiene por que ser equivalente a puntos de vida perdidos)
     */
    @Override
    public void setDano(int dano)
    {
        this.vidaActual-=dano;
    }
    
    @Override
    public TextureRegion getTexturaCadaver()
    {
        return this.animaciones[0].getKeyFrames()[0];
    }
    
}
