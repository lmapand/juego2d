package mi.paquete.juego.modelo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import mi.paquete.juego.modelo.auxiliar.Analizador;
import mi.paquete.juego.modelo.auxiliar.CargadorRecursos;
import mi.paquete.juego.modelo.auxiliar.grafo.GrafoConPeso;
import mi.paquete.juego.modelo.bicheria.EnemigoGenerico;
import mi.paquete.juego.modelo.bicheria.combate.Ataque;

public class Mundo
{
    
    /** INDICES DE TILES DE OBJETOS Y ELEMENTOS DE MAPA DETERMINANTES*/
    public static final int ID_TILE_OBJETO_LLAVE=295;
    public static final int ID_TILE_OBJETO_SALIDA=263;
    public static final int ID_TILE_OBJETO_EMPANADA=296;
    
    //A los siguientes tiles se le suma uno al indice de tile
    public static final int ID_TILE_MURO_TRANSITABLE_1=99;
    public static final int ID_TILE_MURO_TRANSITABLE_2=98;
    public static final int ID_TILE_MURO_TRANSITABLE_3=37;
    
    /** TAMAÑO DEL MUNDO, Equivalente a ver 20 casillas de ancho.*/
    public static final int ANCHO_MUNDO_ANDROID=480;
    public static final int ALTO_MUNDO_ANDROID=250;
    public static final int ANCHO_MUNDO_DESKTOP=960;
    public static final int ALTO_MUNDO_DESKTOP=500;
    
    
    /** Mensajes predefinidos del juego*/
    public static final String TEXTO_AVISO_LLAVE_ENCONTRADA="Has encontrado una llave!";
    public static final Color COLOR_AVISO_LLAVE_ENCONTRADA=new Color(1.0f, 1.0f, 0.0f,1.0f);
    public static final String TEXTO_AVISO_FALTA_LLAVE="Necesitas una llave";
    public static final Color COLOR_AVISO_FALTA_LLAVE=new Color(1.0f, 0.5f, 0.0f,1.0f);
    public static final String TEXTO_AVISO_MANA_INSUFICIENTE="No tienes suficiente mana";
    public static final Color COLOR_AVISO_MANA_INSUFICIENTE=new Color(1.0f, 0.2f, 0.0f,1.0f);
    
    
    public static final float TIEMPO_DESCOMPOSICION_CADAVER=2.0f;
    public static final BitmapFont FUENTE;
    
    public static final Skin SKIN_PROPIO;
    
    public static final float VELOCIDAD_REPRO_PROTA=0.10f;
    /** Para no realizar cálculos extra en la CPU, especificamos como constantes los valores de las acciones y los vectores de desplazamiento asociados
     * a dichas acciones.
     */
    public static int ACCION_PARADO=0;
    public static int ACCION_IZQUIERDA=1;
    public static int ACCION_ARRIBA_IZQUIERDA=2;
    public static int ACCION_ARRIBA=3;
    public static int ACCION_ARRIBA_DERECHA=4;
    public static int ACCION_DERECHA=5;
    public static int ACCION_ABAJO_DERECHA=6;
    public static int ACCION_ABAJO=7;
    public static int ACCION_ABAJO_IZQUIERDA=8;
    
    /* Indice del sprite de la fila 0 de la hoja de sprites que va a representar temporalmente al ser atacando
    *  Todo: Crear animaciones propias para cada ataque y bicho.
    * */
    public static int[] INDICE_ANIM_ATAQUE={0,1,0,3,0,5,0,7,0};
    
    
    public static Vector2[] VECTOR_DESP={new Vector2(0.0f,0.0f), //Parado
                                        new Vector2(-1.0f,0.0f), //180º
                                        new Vector2((float)Math.cos(Math.PI *3 / 4),(float) Math.sin(Math.PI *3 /4)), //135º
                                        new Vector2(0.0f,1.0f), //90º
                                        new Vector2((float)Math.cos(Math.PI  / 4),(float)Math.sin(Math.PI  /4)), //45º
                                        new Vector2(1.0f,0.0f), //90º
                                        new Vector2((float)Math.cos(Math.PI  * 7 / 4),(float)Math.sin(Math.PI * 7 /4)), //315º
                                        new Vector2(0.0f,-1.0f), //270º
                                        new Vector2((float)Math.cos(Math.PI  * 5 / 4),(float)Math.sin(Math.PI * 5 /4)) //225
                                        };
    
    /** Matriz para las operaciones de cálculo de visibilidad  lñlimpiado de la Niebla de Guerra.
     * Se evaluan las casillas a 5 uds. de distancia
      */
    public static List<CoordCasilla>MATRIZ_INTERPOLACION;
    
    private static Mundo EL_MUNDO=null;
    
    
    static
    {
        SKIN_PROPIO=CargadorRecursos.cargaSkinPersonalizado();
        Gdx.app.log("INICIO:", "tras cargar datos skin");
        //Carga y preparacion de los diferentes recursos estáticos necesarios para el juego.
        FUENTE = SKIN_PROPIO.getFont("Fuente media");
        
        
        //Carga y preparacion de la matriz de interpolacion para el algoritmo de 'FoW' y visibilidad
        MATRIZ_INTERPOLACION= mi.paquete.juego.modelo.auxiliar.CargadorRecursos.cargaMatrizInterpolacion("data"+File.separator+"interpol.dat");
    }
    
    
    
    
    /** Datos de la bicheria en general.
     * - Mapa de lectura de los prototipos de criaturas.
     */
    private Map<String, DefBicho>atlasBicheria;
    
    
    /** Mapa con todas las animaciones, referenciadas por nombre
     *
     */
    private Map<String, Animation<TextureRegion>[]> mapaAnimaciones =new TreeMap<String, Animation<TextureRegion>[]>();
    
    /** Mapa de los tipos de ataque posibles
     *
     */
    private final Map<String, Ataque> mapaAtaques;
    
    
    /** Mapa de efectos de sonido */
    private final Map<String, Sound>mapaEfectosSonido;
    
    
    /** Mapa de los tipos de proyectiles usados en los ataques.*/
    private final Map<String, DefProyectil> mapaProyectiles;
    
    /** Datos referentes al Mapa actual cargado.
     *
     */
    private TiledMap mapaActual;
    private  int casillaXIni;
    private  int casillaYini;
    
    private int anchoCasilla;
    private int altoCasilla;
    
    private int anchoMapa;
    private int altoMapa;
    
    private Music musicaFondo;
    
    /** Coordenadas auxiliares en memoria para evitar crear Objetos Coordenada sin necesidad
     *
     */
    private CoordCasilla aux1=new CoordCasilla(0,0);
    private CoordCasilla aux2=new CoordCasilla(0,0);
    
    /** Referencia a la capa que almacena las casillas transitables*/
    private TiledMapTileLayer capaTransitable;
    
    /** Refeencia a la capa que presenta la 'Niebla de Guerra'.
     *  Se referencia para poder borrar más comodamente los tiles que se van descubriendo.
     */
    private TiledMapTileLayer capaFoW;
    
    /** Referencia a la capa que almacena la información de obstáculos que impiden la visibilidad*/
    private TiledMapTileLayer capaViibilidad;
    
    /** Lista de las casillas visibles por el jugador en un momento dado.
     *
     * La evaluación se realiza cada vez que el jugador cambia su posicion a una casilla distinta.
     * Los elementos del mundo que no estén en estas casillas no se verán, aunque no haya niebla de guerra.
     */
    private TreeSet<CoordCasilla>casillasVisibles;
    
    /** Grafo de transitabilidad de la Mazmorra.
     * Definición del grafo de transitabilidad d ela mazmorra y de los mapas de referencia vertice-casilla.
     * Los mapas se implementan como TreeMaps, y permiten el mapeado vertice -> casila y casilla -> vertice
     *
     *
     */
    private GrafoConPeso<CoordCasilla> grafoMazmorra;
    private Map<CoordCasilla,Integer>mapaCasillaAVertice;
    private Map<Integer,CoordCasilla> mapaVerticeACasilla;
    
    private Map<Integer, DefMazmorra>mapaMazmoras;
    
    
    private Mundo()
    {
        //Cargar la definicon de los ataques
        this.mapaAtaques = CargadorRecursos.cargaDefAtaques();
    
        //Cargar la definicion de los Proyectiles
        this.mapaProyectiles=CargadorRecursos.cargaDefProyectiles();
        
        //Cargar la definicion de los sonidos
        
        //Cargar la definicion de los bichos
        this.atlasBicheria= CargadorRecursos.cargaDefBichos(this.mapaAtaques);
        
        //Cargar las animaciones de los bichos
        this.mapaAnimaciones.putAll(CargadorRecursos.cargaAnimacionesBichos(this.atlasBicheria));
        
        //Cargar la animacion del Prota
        this.mapaAnimaciones.put("anim_prota", CargadorRecursos.cargaAnimaciones("protagonista_spritesheet", Mundo.VELOCIDAD_REPRO_PROTA));
        
        //Carga de animacion de la bola de fuego y de la bola 8: //Todo: indicar las velocidades de reprodución en algún sitio....
        this.mapaAnimaciones.put("bola de fuego", CargadorRecursos.cargaAnimaciones("bola_fuego_spritesheet", 0.15f));
        this.mapaAnimaciones.put("bola negra", CargadorRecursos.cargaAnimaciones("bola8_spritesheet", 0.15f));
        
        //Carga de los efectos de sonido
        this.mapaEfectosSonido=new TreeMap<String, Sound>();
        
        //ToDo: Cargarlos desde el gestor de recursos
        this.mapaEfectosSonido.put("fuego01",Gdx.audio.newSound(Gdx.files.internal("musica/efectos"+ File.separator+"foom_0.wav") ));
        this.mapaEfectosSonido.put("invocar bola fuego",Gdx.audio.newSound(Gdx.files.internal("musica/efectos"+ File.separator+"fantasy-mage-spell-1-sound-effect-58181763.mp3") ));
        this.mapaEfectosSonido.put("generar bola",Gdx.audio.newSound(Gdx.files.internal("musica/efectos"+ File.separator+"droid-jet-charging-sound-effect-73800660.mp3") ));
        this.mapaEfectosSonido.put("bola escupida",Gdx.audio.newSound(Gdx.files.internal("musica/efectos"+ File.separator+"latch--metal--release-latch-sound-effect-44631959.mp3") ));
        this.mapaEfectosSonido.put("preparando explosion balon",Gdx.audio.newSound(Gdx.files.internal("musica/efectos"+ File.separator+"droid-data-motion-02-sound-effect-51675416.mp3") ));
        this.mapaEfectosSonido.put("explosion globo",Gdx.audio.newSound(Gdx.files.internal("musica/efectos"+ File.separator+"explosion_globo.mp3") ));
        this.mapaEfectosSonido.put("mana insuficiente",Gdx.audio.newSound(Gdx.files.internal("musica/efectos"+ File.separator+"papercutterarm-297-sound-effect-53759766.mp3") ));

        //Pruebas
        for(Map.Entry<String,DefBicho>unBicho:atlasBicheria.entrySet())
            Gdx.app.log(unBicho.getKey(), unBicho.getValue().toString());
    
        for(Map.Entry<String,Ataque>unAtaque:mapaAtaques.entrySet())
            Gdx.app.log(unAtaque.getKey(), unAtaque.getValue().toString());
    
        for(Map.Entry<String,DefProyectil>unProyectil:mapaProyectiles.entrySet())
            Gdx.app.log(unProyectil.getKey(), unProyectil.getValue().toString());
        
    }
    
    
    public static Mundo getInstance()
    {
        if(EL_MUNDO==null)
            EL_MUNDO=new Mundo();
        
        return EL_MUNDO;
        
    }
    
   
    /** Gestion de los recursos gráficos.
     *
     */
    
    public TiledMap cargaMapa(int indiceMapa)
    {
        //Cargar el mapa
        TiledMap mapaTiles = new TmxMapLoader().load("mazmorras"+File.separator+this.mapaMazmoras.get(indiceMapa).archivo);
    
        if(mapaTiles==null)
            Gdx.app.log("ANALISIS MAPA:", "ID MAZMORRA="+this.mapaMazmoras.get(indiceMapa));
        
        
        //Establecer las propiedades de referencia necesarias del mapa
        MapProperties prop=mapaTiles.getProperties();
    
        casillaXIni= (Integer) prop.get("XIni");
        casillaYini= (Integer) prop.get("YIni");
        anchoCasilla=(Integer) prop.get("tilewidth");
        altoCasilla=(Integer) prop.get("tileheight");
        anchoMapa=(Integer) prop.get("width");
        altoMapa=(Integer) prop.get("height");
    
        //Referenciar las capas y elementos del mapa necesarios
        this.capaTransitable= (TiledMapTileLayer)mapaTiles.getLayers().get("TRANSITABLE");
        this.capaFoW=(TiledMapTileLayer)mapaTiles.getLayers().get("FOW");
        this.capaViibilidad=(TiledMapTileLayer)mapaTiles.getLayers().get("VISIBILIDAD");

        
        //Crear el grafo de la mazmorra.
        this.mapaVerticeACasilla=new TreeMap<Integer,CoordCasilla>();
        this.mapaCasillaAVertice=new TreeMap<CoordCasilla,Integer>();
        this.grafoMazmorra=Analizador.generaGrafoTransitabilidad(this.capaTransitable, this.mapaVerticeACasilla, this.mapaCasillaAVertice);

        //Evaluar visibilidad inicial del personaje
        this.evaluaVisibilidad(casillaXIni, casillaYini);
        
        //Establecer el mapa para referencias internas...
        this.mapaActual=mapaTiles;
        return mapaActual;
    }

    public EnemigoGenerico[] generaBichos(int indiceMapa)
    {
        DefMazmorra defMaz=this.mapaMazmoras.get(indiceMapa);
        EnemigoGenerico[] monstruos=new EnemigoGenerico[defMaz.monstruos.length];
        int cont=0;
        for(DefMazmorra.Monstruo monstruo: defMaz.monstruos)
        {
            //Creacion del camino. Todo: hay que considerar la posibilidad de poner mas de dos puntos de patrulla
            List<CoordCasilla> camino = this.getCamino(monstruo.getPuntosRuta()[0],monstruo.getPuntosRuta()[1]);
            
            
            //Creacion del bicho
            monstruos[cont]=new EnemigoGenerico(monstruo.getDefBicho(),
                                            this.mapaAnimaciones.get(monstruo.getDefBicho().archivoAnim),
                                            monstruo.getDefBicho().pv,
                                            monstruo.getDefBicho().tasaRegen,
                                            monstruo.getDefBicho().velocidad,
                                            camino,
                                            monstruo.getDefBicho().distanciaPercepcion,
                                            monstruo.getDefBicho().ataques);
            
            cont++;
        }
        return monstruos;
    }
    
    //Todo: codigo de test, eliminar
    public EnemigoGenerico[] cargaBichos()
    {
    
        //Crear la bicheria (hardCoded)
        CoordCasilla[] origen={new CoordCasilla(1,1), new CoordCasilla(18,1), new CoordCasilla(14,8), new CoordCasilla(17,17), new CoordCasilla(13,5)};
        CoordCasilla[] destino={new CoordCasilla(6,18) , new CoordCasilla(1,1), new CoordCasilla(1,8), new CoordCasilla(18,1) , new CoordCasilla(13,18) };
        
    
        DefBicho pelota=this.atlasBicheria.get("Pelota Maligna");
        EnemigoGenerico bichos[]=new EnemigoGenerico[origen.length];
        
        for(int x=0;x<origen.length;x++)
        {
            List<CoordCasilla> camino = this.getCamino(origen[x], destino[x]);
            Gdx.app.log("CAMINO:", "DESDE ("+origen[x].x+","+origen[x].y+") HASTA ( "+destino[x].x+","+destino[x].y+")");
            for (CoordCasilla cc : camino)
                Gdx.app.log("CASILLA.", String.format("(%2d ,%2d )", cc.x, cc.y));
    
    
            bichos[x] = new EnemigoGenerico(pelota, this.mapaAnimaciones.get(pelota.archivoAnim), pelota.pv, pelota.tasaRegen, pelota.velocidad, camino, pelota.distanciaPercepcion, pelota.ataques);
        }
        
        return bichos;
    }
    
    
    public List<CoordCasilla> getCamino(CoordCasilla origen,CoordCasilla destino)
    {
        int posIni=this.mapaCasillaAVertice.get(destino);
        int posFin=this.mapaCasillaAVertice.get(origen);
        GrafoConPeso<CoordCasilla>.ArbolCaminoMasCorto arbolPrueba= this.grafoMazmorra.getCaminoMasCorto(posIni);
        return arbolPrueba.getCamino(posFin);
    }
    
    
    /** Realiza la evaluacion de las casillas que ve el personaje.
     *
     * Se activa cada vez que hay cambio de casilla. Quita la niebla de guerra si es necesario y
     * establece las casillas visibles por el personaje
     * Para la evaluacion se utiliza la matriz AreaVision
     * @param posX coordenada X de la casilla
     * @param posY coordenada y de la casilla
     */
    public void evaluaVisibilidad(int  posX, int posY)
    {
    
        /** Contiene todas las posibles casillas visibles.
         * Si en el proceso una casilla tapa, pasa a modo 'oclusion, y ocluye las demás hasta que llega al punto final (Las quita del Array)
         */
        this.casillasVisibles=new TreeSet<>();
        
        //Realizar la interpolacion
        boolean visible=true;
        CoordCasilla origen=new CoordCasilla(posX , posY);
        CoordCasilla destino;
        List<CoordCasilla>camino;
    
      /*  Cell cell = new Cell();
        cell.setTile(mapaActual.getTileSets().getTileSet(0).getTile(138));*/
        
        //Calcular las interpolaciones
        for(CoordCasilla cc:MATRIZ_INTERPOLACION)
        {
            visible=true;
            destino=new CoordCasilla(origen.x +cc.x,origen.y+cc.y);
            camino= Analizador.interpola(origen, destino);
            
            
            for(CoordCasilla casillaEstudio:camino)
            {
                //Si la celda está fuera de limites no tiene sentido realizar el analisis de las casillas siguientes
                if(casillaEstudio.x<0 || casillaEstudio.x>this.anchoMapa-1 || casillaEstudio.y<0 || casillaEstudio.y>this.altoMapa - 1)
                    break;
    
                
                if(!visible )  //No hay visibilidad, quitamos la casilla si la hay del TreeSet
                {
                   //posibleVisibilidad.remove(celdaEstudio);
                    continue;
                }
                else if(visible && this.capaViibilidad.getCell(casillaEstudio.x,casillaEstudio.y)!=null) //La casilla es limite de visibilidad
                    visible=false;
                
                //TEST: Marcamos esta casilla como visible
                //this.capaAux.setCell(celdaEstudio.x, celdaEstudio.y,cell);
                
                //Llegados a este punto, quitamos la niebla de guerra de la casilla
                quitaNieblaDeGuerra(casillaEstudio);
                
                this.casillasVisibles.add(casillaEstudio);
            }
        }
    }
    
    /** Evalua si la casilla en las coordenadas indicadas es una pared.
     *
     * Utilizada para impactos y limites de desplazamientos en algunos casos.
     *
     * @param cc CoordCasilla a evaluar
     * @return true si la casilla indicada es pared, false si no lo es
     */
    public boolean esPared(CoordCasilla cc)
    {
        //Gdx.app.log("ES PARED", "ANalizando ("+cc.x+","+cc.y+")");
    
        TiledMapTileLayer.Cell casilla=( (TiledMapTileLayer)mapaActual.getLayers().get("PAREDES")).getCell(cc.x,cc.y);
        if(casilla==null)
            return false;
        int idTile=casilla.getTile().getId();
        return idTile!=Mundo.ID_TILE_MURO_TRANSITABLE_1 && idTile!=Mundo.ID_TILE_MURO_TRANSITABLE_2 && idTile!=Mundo.ID_TILE_MURO_TRANSITABLE_3;
    }
    
    /** Evalua si en la casilla actual hay una llave y la retira del juego.
     *
     *  Este método lo debe invocar el personaje. Es un arreglo provisional mientras no se crea
     *  un sistema genérico de inventario.
     *
     */
    public boolean hayLLaveEnCasilla(CoordCasilla cc)
    {
        TiledMapTileLayer.Cell casilla=( (TiledMapTileLayer)mapaActual.getLayers().get("OBJETOS")).getCell(cc.x,cc.y);
        if(casilla!=null && casilla.getTile().getId()==Mundo.ID_TILE_OBJETO_LLAVE)
        {
            ((TiledMapTileLayer)this.mapaActual.getLayers().get("OBJETOS")).setCell(cc.x,cc.y,null);
            return true;
        }
        return false;
    }
    
    public boolean hayEmpanadaEnCasilla(CoordCasilla cc)
    {
        TiledMapTileLayer.Cell casilla=( (TiledMapTileLayer)mapaActual.getLayers().get("OBJETOS")).getCell(cc.x,cc.y);
        if(casilla!=null && casilla.getTile().getId()==Mundo.ID_TILE_OBJETO_EMPANADA)
        {
            ((TiledMapTileLayer)this.mapaActual.getLayers().get("OBJETOS")).setCell(cc.x,cc.y,null);
            return true;
        }
        return false;
    }
    
    public boolean estaEnSalida(CoordCasilla cc)
    {
        
        TiledMapTileLayer.Cell casilla=( (TiledMapTileLayer)mapaActual.getLayers().get("OBJETOS")).getCell(cc.x,cc.y);
        
        if(casilla!=null && casilla.getTile().getId()==Mundo.ID_TILE_OBJETO_SALIDA)
            return true;
        
        return false;
    }
    
    /** Evalua la visibilidad entre dos puntos del mapa actual.
     *
     * @param distanciaVision alcance de percepcion a analizar
     * @param xDesde coordenada X del punto Inicial de Análisis
     * @param yDesde coordenada Y del punto Inicial de Análisis
     * @param xHasta coordenada X del punto Final de Análisis
     * @param yHasta coordenada Y del punto Final de Análisis
     * @return
     */
    public boolean seVenCasillas(float distanciaVision, float xDesde , float yDesde, float xHasta, float  yHasta)
    {
        //Calculo directo del limite de percepcion
        
        if(Analizador.distancia(xDesde,yDesde,xHasta, yHasta) > distanciaVision)
        {
            //Gdx.app.log("SEVENCASILLAS:", String.format("Fuera de rango(%5.2f > %5.2f) ", distanciaEntreBichos, distanciaVision));
            return false;
        }
        
        
        //ANalisis del trayecto de vision
        Integer[] coordenadas;
        //Coordenadas del origen
        coordenadas=getCasilla(xDesde, yDesde);
        this.aux1.x=coordenadas[0];
        this.aux1.y=coordenadas[1];
        //Coordenadas de destino
    
        coordenadas=getCasilla(xHasta, yHasta);
        this.aux2.x=coordenadas[0];
        this.aux2.y=coordenadas[1];
        
        //Analizamos las casillas de Interpolacion:
        for(CoordCasilla cc:Analizador.interpola(aux1, aux2))
        {
            if (this.capaViibilidad.getCell(cc.x, cc.y) != null)
                return false;
        }
        
        return true;
    }
    
    /** Elimina la niebla de guerra en una casilla.
     *
     * @param coordenadaCasilla referncia a la casilla que queremos limpiar
     */
    private void quitaNieblaDeGuerra(CoordCasilla coordenadaCasilla)
    {
        this.capaFoW.setCell(coordenadaCasilla.x,coordenadaCasilla.y,null);
    }
    
   
    /** Indica si la casilla en la posicion indicada permite la posicion de un personaje.
     *
     * @param x coordenada X del tile
     * @param y coordenada Y del tile
     * @return boolean
     */
    public boolean esTransitable(int x, int y)
    {
        return (this.capaTransitable.getCell(x,y)!=null);
    }
    
    public boolean esTransitable(float x, float y)
    {
        return (this.capaTransitable.getCell(Math.round(x)/this.anchoCasilla, Math.round(y)/this.altoCasilla)!=null);
    }
    
    /** Devuelve las coordenadas de casilla de una posicion dada.
     *
     * @param x float coordenada x de la posicion a analizar
     * @param y float coordenada y de la posicion a analizar
     * @return Vector2 conteniendo las coordenadas de la casilla a analizar
     */
    public Integer[] getCasilla(float x, float y)
    {
        return new Integer[]  { (Math.round(x) / this.anchoCasilla), (Math.round(y) / this.altoCasilla)};
    }
    
    public int getCasillaXIni()
    {
        return casillaXIni;
    }
    
    public int getCasillaYini()
    {
        return casillaYini;
    }
    
    public int getAnchoCasilla()
    {
        return anchoCasilla;
    }
    
    public int getAltoCasilla()
    {
        return altoCasilla;
    }
    
    public int getAnchoMapa()
    {
        return anchoMapa;
    }
    
    public int getAltoMapa()
    {
        return altoMapa;
    }
    
    
    public void dispose()
    {
        //Descarga del mapa actual
        if(mapaActual!=null)
            this.mapaActual.dispose();
        
        //Descarga de los recursos gráficos en animaciones
        for( Animation<TextureRegion>[] animaciones: this.mapaAnimaciones.values())
        {
            for(Animation<TextureRegion> an :animaciones)
            {
                for (TextureRegion tx : an.getKeyFrames())
                    tx.getTexture().dispose();
            }
        }
    
        //Descarga de los efectos de sonido
        for(Map.Entry<String, Sound>efecto:this.mapaEfectosSonido.entrySet())
            efecto.getValue().dispose();
        
        //Descarga de la musica de fondo
        if(musicaFondo!=null)
            musicaFondo.dispose();
        
        //Descarga de los Skins
        SKIN_PROPIO.dispose();
    }
    
    public TiledMap getMapaActual()
    {
        return this.mapaActual;
    }
   
    public Map<String, Ataque>getMapaAtaques()
   {
       return this.mapaAtaques;
   }
    
    public Map<String, DefProyectil>getMapaProyectiles()
    {
        return this.mapaProyectiles;
    }
   
   public Map<String,Animation<TextureRegion>[]>getAtlasAnimaciones()
   {
       return this.mapaAnimaciones;
   }
    
    public Map<String,Sound>getMapaEfectosSonido()
    {
        return this.mapaEfectosSonido;
    }
    
    
    public boolean esCasillaVisible(CoordCasilla casillaEvaluar)
    {
        return this.casillasVisibles.contains(casillaEvaluar);
    }
    
    /** Realiza la carga de las mazmorras.
     *
     * La carga se realiza por 'lazy instantation', ya que es necesario tener otros recursos iniciados previamente
     * @return Map Mapa de las mazmorras, vinculadas por un indice
     */
    public Map<Integer, DefMazmorra>getMapaMazmoras()
    {
        if(null==this.mapaMazmoras)
            this.mapaMazmoras=CargadorRecursos.cargaMapaMazmorras();
        
        
        //Comprobacion:
        for(Map.Entry<Integer, DefMazmorra>maz: mapaMazmoras.entrySet())
            Gdx.app.log("DEF_MAZMORRA:", ""+maz.getKey()+":"+maz.getValue());
        
        
        return this.mapaMazmoras;
    }
    
    public Map<String, DefBicho>getAtlasBicheria()
    {
        return this.atlasBicheria;
    }
    
}
