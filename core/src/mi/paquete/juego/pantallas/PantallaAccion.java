package mi.paquete.juego.pantallas;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mi.paquete.juego.Juego2D;
import mi.paquete.juego.modelo.CoordCasilla;
import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.bicheria.Dibujable;
import mi.paquete.juego.modelo.bicheria.ElementoJuego;
import mi.paquete.juego.modelo.bicheria.EnemigoGenerico;
import mi.paquete.juego.modelo.bicheria.Fulano;
import mi.paquete.juego.modelo.bicheria.Proyectil;
import mi.paquete.juego.modelo.bicheria.combate.Atacante;
import mi.paquete.juego.modelo.bicheria.desplazamiento.Desplazable;
import mi.paquete.juego.modelo.bicheria.efectos.Efecto;
import mi.paquete.juego.modelo.bicheria.efectos.EfectoTemporalDibujable;
import mi.paquete.juego.modelo.bicheria.efectos.EfectoTextoInformativoFlotante;
import mi.paquete.juego.modelo.bicheria.percepcion.Percibidor;
import mi.paquete.juego.pantallas.escenarios.StageUIAccion;
import mi.paquete.juego.pantallas.escenarios.auxiliar.DialogoFinalNivel;
import mi.paquete.juego.pantallas.escenarios.auxiliar.DialogoGanador;
import mi.paquete.juego.pantallas.escenarios.auxiliar.DialogoMuerte;
import mi.paquete.juego.pantallas.escenarios.auxiliar.DialogoPausa;

public class PantallaAccion extends PantallaJuego
{
    
    
    public PantallaAccion(Juego2D juego)
    {
        super(juego);
    }
    
    @Override
    protected Renderizador creaRenderizador()
    {
        return new RenderizadorAccion();
    }
    
    @Override
    public void pause()
    {
        ((RenderizadorAccion)this.renderizador).setPausa(true);
        Gdx.app.log("PAUSA", "Pausando");
    }
    
    @Override
    public void resume()
    {
       //super.resume();
       ((RenderizadorAccion)this.renderizador).resume();
    }
    
    public class RenderizadorAccion extends PantallaJuego.Renderizador implements InputProcessor
    {

        private static final float TIEMPO_ACTIVO_TITULO_MAZMORR=1.5f;
        
        private boolean juegoPausado=false;
        
        private TiledMap mapa;
        private TiledMapRenderer renderMapa;

        private SpriteBatch sb;

        private List<ElementoJuego>elementos;
        private List<ElementoJuego>elementosEliminar=new ArrayList<ElementoJuego>();
        private List<Efecto>efectosActivos=new ArrayList<Efecto>();
        private List<Efecto>efectosEliminar=new ArrayList<Efecto>();
        private Fulano unFulano;
        
        private Viewport terminalJuego;
        private mi.paquete.juego.pantallas.escenarios.StageUIAccion ui;
    
        private Music musicaFondo;
        
        Mundo mundo;
        
        public RenderizadorAccion()
        {
            this.mundo = Mundo.getInstance();
            
            //inicializacin de la Lista de elementos
            elementos=new ArrayList<ElementoJuego>();
            
           
            /* Carga del mapa y evaluzacion de la posicion de inicio del jugador:*/
            mundo.cargaMapa(juego.getPartidaActual().getRefMazmorra());
    
            
            this.mapa = mundo.getMapaActual();
            this.renderMapa = new OrthogonalTiledMapRenderer(mapa);
            
            /* Creacion manual del fulano*/
            unFulano = new Fulano(mundo.getAtlasAnimaciones().get("anim_prota"),
                        100,
                    2,
                                    new CoordCasilla(mundo.getCasillaXIni(),mundo.getCasillaYini()),
                                    juego.getPartidaActual().getNombrePersonaje(),
                                    juego.getPartidaActual().getxPActual(),
                                    juego.getPartidaActual().getNivel());

            /* preparacion del Viewport y UI*/
            int anchoViewPort=Mundo.ANCHO_MUNDO_DESKTOP;
            int altoViewPort=Mundo.ALTO_MUNDO_DESKTOP;
            if(Gdx.app.getType()== Application.ApplicationType.Android)
            {
                anchoViewPort=Mundo.ANCHO_MUNDO_ANDROID;
                altoViewPort=Mundo.ALTO_MUNDO_ANDROID;
            }
            
            //Creacion de Pantallas: Viewport del juego
            terminalJuego = new FillViewport(anchoViewPort, altoViewPort,camara);
            terminalJuego.apply();
            //Creacion de Pantallas: UI
            ui=new StageUIAccion(anchoViewPort, altoViewPort, unFulano);
            
           
            //Añadir al fulano y los enemigos iniciales : Todo: obtener por carga de datos en Mundo
            this.elementos.add(unFulano);

            EnemigoGenerico[] enemigos=mundo.generaBichos(juego.getPartidaActual().getRefMazmorra());
            for(EnemigoGenerico enemigo:enemigos)
                this.elementos.add(enemigo);
            
            
            //Incluir el texto de inicio de mazmorra
            this.elementos.add(new EfectoTextoInformativoFlotante(TIEMPO_ACTIVO_TITULO_MAZMORR,
                            unFulano.getPosX(),
                            unFulano.getPosY(),
                            this,
                            "Entrando en  "+ mundo.getMapaMazmoras().get(juego.getPartidaActual().getRefMazmorra()).nombre,
                            Mundo.SKIN_PROPIO.getColor("Color texto Sistema"),
                            0));
            
            
            //Establecer el SpriteBatch
            this.sb = new SpriteBatch();

            //Establecer el InputProcessor, que gestionará tanto la pantalla principal (teclado) como el UI(touchpad)
            InputMultiplexer gestorEntrada = new InputMultiplexer();
            gestorEntrada.addProcessor(ui);
            gestorEntrada.addProcessor(this);
            Gdx.input.setInputProcessor(gestorEntrada);
            
            //Iniciar la musica
            //Una vez cargado el mapa, vamos a iniciar la reproduccion de la musica...
            this.musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musica" + File.separator+"mus_dungeon_cave_03.mp3"));
            this.musicaFondo.setLooping(true);
            this.musicaFondo.play();
        }
    
    
        //Vamos a cargar aqui los datos de momoento: luego los pasaremos al mundo...
    
        @Override
        void render(float delta)
        {
            if(this.juegoPausado)
                delta=0.0f;
                
             /* El protagonista se mueve en funcion de sus vectores.
             Los enemigos deben evaluar la IA y establecer los nuevos vectores.
             Los disparos se moveran en funcion de su vector.*/
    
            //Elementos uqe habrá que añadir
             ArrayList<ElementoJuego>nuevosTrebellos=new ArrayList<ElementoJuego>();

             //Reinicio de lalista de elementos y efectos a eliminnar
            this.elementosEliminar.clear();
            this.efectosEliminar.clear();
            
            for(ElementoJuego ej:this.elementos)
            {
                //La percepcion se procesa siempre al principio.
                //Si el elemento perceptor está en modo ataque, anulamos el proceso de percepcion DENTRO DE SU PROCESO DE PERCEPCION
                if (ej instanceof Percibidor)
                    ((Percibidor) ej).procesaPercepcion(this, delta);
                
                //ANalisis del estado de disparo, que anulará el desplazamiento
                if(ej instanceof Atacante && ((Atacante)ej).estaAtacando() )
                {
                    //Nos devolverá efectos, nuevos elementos, etc.
                    Object resultado=((Atacante)ej).procesaEstadoAtaque(delta);
                    if(resultado instanceof Proyectil) //Se ha generado un proyectil
                        nuevosTrebellos.add((Proyectil) resultado);
                    else if(resultado instanceof Efecto) //En el caso de que sea un efecto (por ataque o similar), hay que incorporarle el 'entorno'
                    {
                        ((Efecto) resultado).setEntorno(this);
                        this.efectosActivos.add(((Efecto) resultado));
                    }
                }
                else if (ej instanceof Desplazable)
                    ((Desplazable) ej).actualizaPosicion(delta);
            }

            /* Añadimos aquellos elementos resultado del proceso de desplazamiento/ataque.
            Obviamente hay que hacerlo en el exterior del bucle de analisis de elementos*/
            this.elementos.addAll(nuevosTrebellos);
            
            /* Aplicar efectos que sean necesarios*/
            List<Object>generados=new ArrayList<Object>();
            List<Object>resultadoEjecucion;
            for(Efecto ef:this.efectosActivos)
            {
                
                if (!ef.estaEjecutado())
                {
                    resultadoEjecucion = ef.ejecuta(delta);
                    if(resultadoEjecucion!=null)
                        generados.addAll(resultadoEjecucion);
                }
            }
            //Añadir los elementos resultantes
            for(Object o:generados)
            {
                if(o instanceof Efecto)
                    this.efectosActivos.add((Efecto)o);
                else if(o instanceof EfectoTemporalDibujable)
                    this.elementos.add((EfectoTemporalDibujable)o);
                else if(o instanceof ElementoJuego)
                    this.elementos.add((ElementoJuego)o);
            }
            
            /* Eliminar auqellos elementos que han sido destruidos*/
            //Todo: ordenar los elementos en el escenario
            
            
            Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            
            /* Renderizado de camara y mapa*/
            terminalJuego.apply();
            camara.position.set(unFulano.getPosX(),unFulano.getPosY(),0);
            camara.update();
            this.renderMapa.setView(camara);
            renderMapa.render();
            
            sb.setProjectionMatrix(camara.combined);
            sb.begin();
            /* RENDERIZADO DE LOS ELEMENTOS DEL JUEGO.
            *
            */
            for(ElementoJuego ej:this.elementos)
            {
                if (ej instanceof Dibujable && ((Dibujable)ej).esVisible())
                    ((Dibujable)ej).dibuja(sb, delta);
            }

            sb.end();
            
            /** Eliminar los efectos que han caducado y los dibujables que ya no deben de existir*/
            for(Efecto ef:this.efectosEliminar)
                this.efectosActivos.remove(ef);
    
            for(ElementoJuego ej:this.elementosEliminar)
            {
                this.elementos.remove(ej);
            }
            
            //RENDERIZADO DE LA UI (El método actualiza inclute la llamada a Stage::act()
            ui.actualiza();
            ui.getViewport().apply();
            ui.draw();
           
        }
    
        
        public void resume()
        {
            //Si venimos de una pausa por haber minimizado, mostramos el dialogo de pausa
            if(this.juegoPausado)
                muestraDialogoPasua();
        }
        
        
        @Override
        void resize(int width, int height)
        {
            
            Gdx.app.log("VALOR DE width y height:", String.format("%d , %d",width, height ));
            Gdx.app.log("VALOR DE AnchoMundo y AltoMundo:", String.format("%5.2f , %5.2f", terminalJuego.getWorldWidth(), terminalJuego.getWorldHeight()) );
            Gdx.app.log("VALOR DE AnchoPantala y AltoPantalla: ", String.format("%d , %d", terminalJuego.getScreenWidth(), terminalJuego.getScreenHeight()) );
            
            /* Calcular la proporcion necesaria para mantener el 'aspect/ratio'*/
            
            //viewport.update((int)(width/Juego2D.PROPORCION_PANTALLA),(int)((double)height/Juego2D.PROPORCION_PANTALLA));
            terminalJuego.update(width,height );
            
            //camara.position.set(camara.viewportWidth/2,camara.viewportHeight/2,0);
            //camara.setToOrtho(false, Mundo.ALTO_MUNDO, Mundo.ALTO_MUNDO);
            camara.update();
            
            //ui.getViewport().getCamera().lookAt(20,20,0);
            ui.getViewport().update(width, height, true);
            //ui.getViewport().setScreenX(20);
            //ui.getViewport().setScreenY(20);
        }
    
    
        @Override
        void dispose()
        {
            //Parar la musica y decargar el recurso
            this.musicaFondo.stop();
            this.musicaFondo.dispose();
            
            //Descargar los recursos graficos que son propios de este trebello
            this.ui.dispose();
            //Todo: el dispose del mundo debe hacerlo el Juego. Cuidado con esto...
            //Mundo.getInstance().dispose();
        }
    
        @Override
        public boolean keyDown(int keycode)
        {
            //Anula cualquier accion de movimiento.
            if(unFulano.estaAtacando())
                return true;
            //Evaluacion de los vectores de desplazamiento, para actualizarlos
            //COmprobar primero si estamos en modo juego o en otro tipo de estado
            unFulano.setAccionDesplazamiento(evaluaDesplazamiento());
            return true;
        }
    
        @Override
        public boolean keyUp(int keycode)
        {
            //Pasua tiene precedencia sobre cualquier tecla
            if (keycode==Input.Keys.P || keycode==Input.Keys.ESCAPE)
            {
                this.setPausa(true);
                muestraDialogoPasua();
                return true;
            }
            
            
            //Atatcar anula cualquier accion de movimiento.
            if(unFulano.estaAtacando())
                return true;
    
            if (keycode==Input.Keys.SPACE || keycode==Input.Keys.F)
            {
                unFulano.iniciaAtaque(keycode);
                return true;
            }
            
            unFulano.setAccionDesplazamiento(evaluaDesplazamiento());
            return true;
        }
    
    private int evaluaDesplazamiento()
    {
        
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A))
            return Mundo.ACCION_ARRIBA_IZQUIERDA;
        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D))
            return Mundo.ACCION_ARRIBA_DERECHA;
        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A))
            return Mundo.ACCION_ABAJO_IZQUIERDA;
        if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D))
            return Mundo.ACCION_ABAJO_DERECHA;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            return Mundo.ACCION_ABAJO;
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            return Mundo.ACCION_DERECHA;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            return Mundo.ACCION_IZQUIERDA;
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            return Mundo.ACCION_ARRIBA;
        return Mundo.ACCION_PARADO;
    }
    
    
    
        /** Sale de esta pantalla a la pantalla de inicio,
         *
         */
        public void vuelveAInicio()
        {
            PantallaIntro nuevaPantalla=new PantallaIntro(juego);
            juego.cambiaPantalla(nuevaPantalla);
        }
    
        public void reanudaPartida()
        {
            this.setPausa(false);
        }
    
        /** Crea una nueva sesion de juego con los datos de la partida actual.
         *
          */
        public void reiniciaPartida()
        {
            PantallaAccion nuevaPantalla=new PantallaAccion(juego);
            juego.cambiaPantalla(nuevaPantalla);
        }
    
    
        /** El jugador ha encontrado la empanada, y por consiguiente, ganado la partida.
         *
         */
        public void finalizaPartida()
         {
             this.setPausa(true);
             Dialog dialog = new DialogoGanador(this);
             dialog.show(ui);
         }
    
        
        private void muestraDialogoPasua()
        {
            this.setPausa(true);
            Dialog dialog = new DialogoPausa(this);
            dialog.show(ui);
        }
        
        
       public void muestraDialogoProtaMuerto()
       {
           this.setPausa(true);
           Dialog dialog = new DialogoMuerte(this);
           dialog.show(ui);
       }
    
        /** realiza el guardado de la partida y muestra el dialogo para iniciar el siguiente nivel,
         *
         */
        public void finalizaNivel()
        {
            //Pausar
            this.setPausa(true);
            
            //Almacenar la partida
            juego.guardaPartida(this.unFulano);
            
            
            //Mostrar el dialogo de Nivel terminado
            Dialog dialog = new DialogoFinalNivel(this);
            dialog.show(ui);
        }
       
       
    
        @Override
        public boolean keyTyped(char character)
        {
            return true;
        }
    
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button)
        {
                  return true;
        }
        
        public void setPausa(boolean pausar)
        {
            this.juegoPausado=pausar;
        }
        
    
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button)
        {
            return true;
        }
    
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer)
        {
            Gdx.app.log("Pantalla:","Se ha producido un toque:"  );
            return false;
        }
    
        @Override
        public boolean mouseMoved(int screenX, int screenY)
        {
            return false;
        }
    
        @Override
        public boolean scrolled(int amount)
        {
            return false;
        }
        
        public Fulano getProta()
        {
            return this.unFulano;
        }
        
        public List<ElementoJuego> getElementosJuego()
        {
            return this.elementos;
        }
        
        public List<ElementoJuego> getElementosEliminar()
        {
            return this.elementosEliminar;
        }
    
        public List<Efecto> getEfectosActivos()
        {
            return this.efectosActivos;
        }
    
        public List<Efecto> getEfectosEliminar()
        {
            return this.efectosEliminar;
        }
    }
}
