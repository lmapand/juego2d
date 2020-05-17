package mi.paquete.juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.bicheria.Fulano;
import mi.paquete.juego.pantallas.PantallaAccion;
import mi.paquete.juego.pantallas.PantallaIntro;
import mi.paquete.juego.pantallas.PantallaJuego;

public class Juego2D extends Game
{

    /** Numero maximo de partidas que se pueden almacenar*/
     public static final int MAXIMO_SLOTS_PARTIDAS=6;
     
    /** INdicador de que no hay partidas seleccionadas, ni para crear ni para guardar.
     *
     */
    public static final int PARTIDA_NO_SELECCINADA=-1;
    
    /** Indica si en la pantalla de inicio ya se mostró la conversacion de inicio    */
    private boolean introEjecutada=false;
    
    /** puntero a la pantalla que se estó mostrando en este momento */
    private PantallaJuego pantallaActual;
    
    private List<Partida> partidas;
    private int indicePartidaActual =Juego2D.PARTIDA_NO_SELECCINADA;
    
    private final GestorPartidas gp=new GestorPartidas();
    
    private Cursor punteroRaton;
    
    @Override
    public void create()
    {
        
        Mundo mundo=Mundo.getInstance();
    
        //Carga de las partidas disponibles y klos huecos
        this.partidas=this.cargaAlmacenamiento();
        
       
        //pantallaActual=new PantallaAccion(this);
        this.pantallaActual=new PantallaIntro(this);
        this.setScreen(this.pantallaActual);
    
        //Creacion del cursor del ratón.
        Pixmap pixmapCursorRaton = new Pixmap(Gdx.files.internal("imagenes"+File.separator+"puntero_raton.png"));
        int xHotspot = pixmapCursorRaton.getWidth() / 2;
        int yHotspot = pixmapCursorRaton.getHeight() / 2;
        this.punteroRaton = Gdx.graphics.newCursor(pixmapCursorRaton, xHotspot, yHotspot);
        Gdx.graphics.setCursor(punteroRaton);
        pixmapCursorRaton.dispose();
        
        
        
        
    }
    
    
    @Override
    public void dispose()
    {
        super.dispose();
        Gdx.app.log("Mundo::dispose():","Saliendo de la Aplicación");
        //Llamamos al método de limpieza de la pantalla actual
        if(null!=this.pantallaActual)
            this.pantallaActual.dispose();
        
        //Limpieza de los recursos generales del juego
        Mundo mundo=Mundo.getInstance();
        mundo.dispose();
        
        //Limpieza del cursor
        this.punteroRaton.dispose();
    }
    
    
    /** Comprueba el estado de almacenamiento y obtiene la lista de partidas existentes.
     *
     * El juego presenta 6 'slots' para partidas. Las carpetas se guardan el  LOCAL, en a carpeta
     * 'sdle.pertidas'. Cada partida se almacena en un archivo xml, llamado partida_X, donde X es
     * el indice del Slot. Ademas de comprobar que el almacenamiento existe, recupera la informacion
     * de las partidas para poder hacer uso del 'Slot' en caso de partida nueva o para recuperar
     * partidas antiguas con la opcion 'cargar partida' del menu principal
     *
     * @return Partida[] lista de las partidas almacenadas.
     */
    private List<Partida>cargaAlmacenamiento()
    {
        List<Partida>lista=new ArrayList<Partida>();
        //Comprobacion de que el directorio de almacenamiento existe, SI no existe, intentará crearlo.
        FileHandle fhnd = Gdx.files.local(GestorPartidas.DIR_ALMACENAMINETO);
        File carpetaGuardar=fhnd.file();
        
        //Intentar crear la carpeta
        if(!carpetaGuardar.exists() )
        {
            try
            {
                Gdx.app.log("CREACION DIRECTORIO ALMACENAMIENTO:", "No existe carpeta de almacenamiento: intentando crearla");
                carpetaGuardar.mkdir();
            }
            catch (Exception ex)
            {
                Gdx.app.log("CREACION DIRECTORIO ALMACENAMIENTO:", "No se pudo crear la carpeta");
                return null;
            }
        }
    
        //Carga de la informacion de las partidas:
        File partida;
        for(int x=0;x<Juego2D.MAXIMO_SLOTS_PARTIDAS;x++)
        {
            partida=Gdx.files.local(GestorPartidas.DIR_ALMACENAMINETO+File.separator+"partida_"+x+".xml").file();
            if(!partida.exists())
            {
                lista.add(null);
            }
            else
                lista.add(gp.cargaPartida(x));
        }
        return lista;
    }
    
    /** Referencia a las partidas almacenadas.
     *
     * Necesaria para algunos elementos de interfaz, a la hora de guardar/recuperar/reiniciar pantallas
     *
     * @return Partida[] lista de las partidas almacenadas
     */
    public List<Partida>getPartidas()
    {
        return this.partidas;
    }
    
    /** Sustituye un slot de partida con los datos indicados, e inicia la partida.
     *
     * @param nombreJugador String El nombre del personaje para esta partida
     * @param numeroSlot int EL id de slot de esta partida
     */
    public void iniciaNuevaPartida(String nombreJugador, int  numeroSlot )
    {
       //Actualizacion de los datos de la partida
       Partida nuevaPartida=gp.registraPartida(nombreJugador, numeroSlot);
       this.partidas.set(numeroSlot, nuevaPartida);
       indicePartidaActual =numeroSlot;
    
        /** Inicio (o reinicio de la partida)
         *  Aqui se carga la ventana de juego
         */
        Mundo mundo= Mundo.getInstance();
        PantallaAccion nuevaPantalla=new PantallaAccion(this);
       this.cambiaPantalla(nuevaPantalla);
    }
    
    public Partida getPartidaActual()
    {
        return this.partidas.get(this.indicePartidaActual);
    }
    
    public void cambiaPantalla(PantallaJuego nuevaPantalla)
    {
        pantallaActual.dispose();
        this.setScreen(nuevaPantalla);
        this.pantallaActual=nuevaPantalla;
    }
    
    /** Guarda en el slot de la partida actual los datos del Protagonista y establece la mazmorra siguiente
     *
     *
     * @param unFulano
     */
    public void guardaPartida(Fulano unFulano)
    {
        //Actualizar la partida
        Partida partidaActualizada=this.gp.guardaPartida(unFulano, this.indicePartidaActual, this.partidas.get(this.indicePartidaActual));
        
        //Sustituir la partida anterior
        this.partidas.set(this.indicePartidaActual,partidaActualizada);
    }
    
    /** Establece como actual la partida en el slot indicado e inicia el juego.
     *
     * @param slotCargar
     */
    public void cargaPartida(int slotCargar)
    {
        //Almacenar la informacion necesaria
        this.indicePartidaActual =slotCargar;
        
        //Iniciar la partida
        PantallaAccion nuevaPantalla=new PantallaAccion(this);
        this.cambiaPantalla(nuevaPantalla);
    }
    
    
    public boolean isIntroEjecutada()
    {
        return introEjecutada;
    }
    
    /** establece que la Introduccion ya ha sido ejecutada
     *
     */
    public void setIntroEjecutada()
    {
        this.introEjecutada = true;
    }
}
