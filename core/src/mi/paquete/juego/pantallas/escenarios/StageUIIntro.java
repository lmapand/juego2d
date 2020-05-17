package mi.paquete.juego.pantallas.escenarios;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.List;

import mi.paquete.juego.Juego2D;
import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.auxiliar.CargadorRecursos;
import mi.paquete.juego.pantallas.escenarios.auxiliar.DialogoCargarPartida;
import mi.paquete.juego.pantallas.escenarios.auxiliar.DialogoNuevaPartida;

public class StageUIIntro extends Stage
{
 
    //Ancho botones para una configuracion gráfica minima
    private final int ANCHO_BOTOMES_800x600=250;
   
    private final Image imagenHablante;
    private final TextArea txtHablante;
    
    private final TextButton btSiguienteDialogo;
    private final TextButton btCancelarDialogo;
    private final TextButton btNuevaPartida;
    private final TextButton btCargarPartida;
    private final TextButton btSalirJuego;
    
    private final Juego2D juego;
    private final Oyente oyente;
    private final List<Conversacion> conversacionIntro;
    
    /** Controla en que punto de la conversacion de Intro nos encontramos */
    private int secuenciaConversacion=0;
    
    private String nombreJugadorNuevaPartida=null;
    private int numeroSLotNuevaPartida= Juego2D.PARTIDA_NO_SELECCINADA;
    
    public StageUIIntro(int ancho, int alto, Juego2D juego, SpriteBatch sp)
    {
        super(new FitViewport(ancho, alto));
        
        this.juego=juego;
        
        //establecer el oyente
        this.oyente=new Oyente(juego);
        this.conversacionIntro= CargadorRecursos.cargaConversacionInicio();
        //Carga de la conversacion
        
        
        //Creacion de la interfaz
        Table tabla=new Table();
        
        //Titulo de cabecera
        Image tituloIntro=new Image(Mundo.SKIN_PROPIO.get("Imagen Cabecera Juego", TextureRegion.class));
        tabla.add(tituloIntro).colspan(6);
        
        //Fila de ajuste vertical
        tabla.row().expandY();
        //Zona de diálogo inicial
        tabla.row();
        this.imagenHablante=new Image(Mundo.SKIN_PROPIO.get("Actor "+this.conversacionIntro.get(secuenciaConversacion).getActor() , TextureRegion.class));
        tabla.add(imagenHablante).width(32).height(32);
        
        
        this.txtHablante=new TextArea(this.conversacionIntro.get(secuenciaConversacion).getTexto(), Mundo.SKIN_PROPIO.get("Estilo cuadro texto", TextArea.TextFieldStyle.class));
        this.txtHablante.getStyle().background.setLeftWidth(10.0f);
        this.txtHablante.getStyle().background.setTopHeight(10.0f);
        tabla.add(this.txtHablante).colspan(5).fill();
        tabla.row();
        
        //Botones de 'Siguiente' y de Cancelar animacion
        btSiguienteDialogo=new TextButton("Continuar", Mundo.SKIN_PROPIO.get("Estilo boton grande", TextButton.TextButtonStyle.class));
        btSiguienteDialogo.setName(Oyente.NOMBRE_BOTON_SIGUIENTE_DIALOGO);
        btSiguienteDialogo.addListener(this.oyente);
        
        tabla.add(btSiguienteDialogo).colspan(2).fillX().width(ANCHO_BOTOMES_800x600);
        tabla.add().colspan(2);
        btCancelarDialogo=new TextButton(Mundo.SKIN_PROPIO.get("Texto boton saltar intro", String.class), Mundo.SKIN_PROPIO.get("Estilo boton grande", TextButton.TextButtonStyle.class));
        btCancelarDialogo.setName(Oyente.NOMBRE_BOTON_CANCELAR_DIALOGO);
        btCancelarDialogo.addListener(this.oyente);
        tabla.add(btCancelarDialogo).colspan(2).fillX().width(ANCHO_BOTOMES_800x600);
        //Fila de Separacion
        tabla.row().expandY();

        
        //Botones de Cargar partida, Salir y Nueva Partida
        btNuevaPartida=new TextButton("Nueva partida", Mundo.SKIN_PROPIO.get("Estilo boton grande", TextButton.TextButtonStyle.class));
        btNuevaPartida.setName(Oyente.NOMBRE_BOTON_NUEVA_PARTIDA);
        btNuevaPartida.setVisible(false);
        btNuevaPartida.addListener(this.oyente);
        tabla.add(btNuevaPartida).colspan(2).fillX().width(ANCHO_BOTOMES_800x600);
        btCargarPartida=new TextButton("Cargar partida", Mundo.SKIN_PROPIO.get("Estilo boton grande", TextButton.TextButtonStyle.class));
        btCargarPartida.setName(Oyente.NOMBRE_BOTON_CARGAR_PARTIDA);
        btCargarPartida.setVisible(false);
        btCargarPartida.addListener(this.oyente);
        tabla.add(btCargarPartida).colspan(2).fillX().width(ANCHO_BOTOMES_800x600);
        btSalirJuego=new TextButton("Salir", Mundo.SKIN_PROPIO.get("Estilo boton grande", TextButton.TextButtonStyle.class));
        btSalirJuego.setName(Oyente.NOMBRE_BOTON_SALIR_JUEGO);
        btSalirJuego.addListener(this.oyente);
        tabla.add(btSalirJuego).colspan(2).fillX().width(ANCHO_BOTOMES_800x600);
    
        //proiedades y establecimiento de la tabla
        //tabla.setDebug(true);
        tabla.setFillParent(true);
        this.addActor(tabla);
        
        //Analizamos si hay que mostrar la secuencia de conversacion o no
        if(this.juego.isIntroEjecutada())
            muestraInterfazInicioPartida();
        
    }
    
    public void cargaPartida(int slot)
    {
        juego.cargaPartida(slot);
    }
    
    public void estableceDatosNuevaPartida(String nombreJugador, int numeroSlot)
    {
        
        this.nombreJugadorNuevaPartida=nombreJugador;
        this.numeroSLotNuevaPartida=numeroSlot;
        
        
        if(null!=nombreJugador)
        {
            //Registrar la nueva partida
            
            
            this.juego.iniciaNuevaPartida(nombreJugador, numeroSlot );
            
            //Indicar inicio de nueva partida
        }
    }
    
    private void muestraDialogo(Dialog dialogo)
    {
        dialogo.show(this);
    }
    
    private void muestraConversacion()
    {
        imagenHablante.setDrawable(Mundo.SKIN_PROPIO, "Actor "+conversacionIntro.get(secuenciaConversacion).getActor());
        txtHablante.setText(conversacionIntro.get(secuenciaConversacion).getTexto());
    }
    
    /** Oculta los botones de la convesacion y muestra los botones que habilitan el inico de partida (nueva partida)/(cargar partida)
     *
     */
    private void muestraInterfazInicioPartida()
    {
        this.secuenciaConversacion=this.conversacionIntro.size()-1;
        muestraConversacion();
        this.btCancelarDialogo.setVisible(false);
        this.btSiguienteDialogo.setVisible(false);
        this.btNuevaPartida.setVisible(true);
        this.btCargarPartida.setVisible(true);
        
        //establecer en el uego que ya se ha visualizado (o anulado) la presentacion inicial
        this.juego.setIntroEjecutada();
    }
    
    
    
    public int getSecuenciaConversacion()
    {
        return this.secuenciaConversacion;
        
    }
    
    public static class Conversacion
    {
        private final String actor;
        private final String texto;
        
        public Conversacion(String actor, String texto)
        {
            this.actor=actor;
            this.texto=texto;
        }
    
        public String getActor()
        {
            return this.actor;
        }
        
        public String getTexto()
        {
            return this.texto;
        }
    }
    
    public class Oyente extends ChangeListener
    {
        /**
         * Referencia al juego, ya que los eventos de los botones implican acciones sobre las pantallas.
         */
        private final Juego2D juego;
    
        public Oyente(Juego2D juego)
        {
            this.juego = juego;
        }
    
        /**
         * Contantes para el referenciado de Actores; se declaran a nivel de paquete
         */
        static final String NOMBRE_BOTON_CANCELAR_DIALOGO = "Boton Cancelar Dialogo";
        static final String NOMBRE_BOTON_SIGUIENTE_DIALOGO = "Boton Siguiente Dialogo";
        static final String NOMBRE_BOTON_NUEVA_PARTIDA = "Boton Nueva Partida";
        static final String NOMBRE_BOTON_CARGAR_PARTIDA = "Boton Cargar Partida";
        static final String NOMBRE_BOTON_SALIR_JUEGO = "Boton Salir Juego";
    
        @Override
        public void changed(ChangeListener.ChangeEvent event, Actor actor)
        {
            switch (actor.getName())
            {
                case Oyente.NOMBRE_BOTON_SALIR_JUEGO:
                    Gdx.app.exit();
                    break;
                    
                case Oyente.NOMBRE_BOTON_CANCELAR_DIALOGO:
                    secuenciaConversacion=conversacionIntro.size()-1;
                    muestraInterfazInicioPartida();
                    muestraConversacion();
                    break;
                    
                case Oyente.NOMBRE_BOTON_SIGUIENTE_DIALOGO:
                    secuenciaConversacion++;
                 
                    //Si es la ultima secuencia del dialogo, ocultamos los botones que ya no se necesitan, y mostramos los otros
                    if( secuenciaConversacion>=conversacionIntro.size()-1)
                        muestraInterfazInicioPartida();
                    
                    //Mostramos la conversacion en curso:
                    muestraConversacion();
                    break;
                    
                case NOMBRE_BOTON_NUEVA_PARTIDA:  //Cargamos un dialogo
                    DialogoNuevaPartida dlg=new DialogoNuevaPartida(juego.getPartidas());
                    //Realizamos la sobreescritura de las acciones del dialogo
                    muestraDialogo(dlg);
                    
                    break;
                    
                case NOMBRE_BOTON_CARGAR_PARTIDA:
                    DialogoCargarPartida dlg2=new DialogoCargarPartida(juego.getPartidas());
                    //Realizamos la sobreescritura de las acciones del dialogo
                    muestraDialogo(dlg2);
                    
                    break;
            }
        }
    }
    
}




