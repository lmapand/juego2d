package mi.paquete.juego.modelo.auxiliar;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import mi.paquete.juego.modelo.CoordCasilla;
import mi.paquete.juego.modelo.DefBicho;
import mi.paquete.juego.modelo.DefMazmorra;
import mi.paquete.juego.modelo.DefProyectil;
import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.bicheria.combate.Ataque;
import mi.paquete.juego.modelo.bicheria.combate.AtaqueDistancia;
import mi.paquete.juego.modelo.bicheria.combate.AtaqueFisico;
import mi.paquete.juego.modelo.bicheria.combate.AtaqueSuicida;
import mi.paquete.juego.pantallas.escenarios.StageUIIntro;

/** Clase con métodos estáticos para la carga de los recursos necesarios para el juego
 *
 */
public class CargadorRecursos
{
    
    public static final String RUTA_RECURSOS_SKIN="skin"+File.separator+"raw"+File.separator;
    
    
    
    /** Skin para el juego generado programáticamente.
     *
     * Cuando el SKin importado cumpla las especificaciones, utilizaremos ese por comodidad.
     *
     * @return Skin a utilizar en la mayor parte de nuestros diseños
     */
    public static Skin cargaSkinPersonalizado()
    {
       
        Skin skin=new Skin();
    
        //Carga de recursos graficos necesarios:

       
        skin.add("Actor Maestro", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN + "intro_maestro.png"))));
        skin.add("Actor Tu", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN + "intro_personaje.png"))));
        skin.add("Actor Canduterio", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN + "intro_canduterio.png"))));
        skin.add("Actor Sistema", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN + "intro_sistema.png"))));
        skin.add("Imagen Cabecera Juego", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN + "titulo_intro.png"))));
        skin.add("Fondo cuadro texto una linea", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN + "fondo_cuadro_texto_simple.png"))));
        skin.add("Fondo ventana",new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"fondo_ventana.png"))));
        skin.add("Fondo cuadro texto", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN +"fondo_cuadro_texto.png"))));
        skin.add("Boton grande sin pulsar", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN +"boton_menu_normal.png"))));
        skin.add("Boton grande pulsado", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN +"boton_menu_pulsado.png"))));
        skin.add("Boton grande sobre", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN +"boton_menu_sobre.png"))));
        skin.add("marcador barra vida mal", new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"barra_vida_mal.png")))));
        skin.add("marcador barra vida regular", new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"barra_vida_regular.png")))));
        skin.add("marcador barra vida bien", new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"barra_vida_bien.png")))));
        skin.add("marcador barra mana", new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"barra_mana.png")))));
        skin.add("marcador punto XP", new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"barra_xp_punto.png")))));
        skin.add("marcador barra XP", new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"barra_xp_completa.png")))));
        skin.add("imagen vida", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"etq_vida.png"))));
        skin.add("imagen mana", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"etq_mana.png"))));
        skin.add("boton bola fuego", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"boton_bola_fuego.png"))));
        skin.add("boton pausa", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"boton_pausa.png"))));
        skin.add("boton vareada activado", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"boton_vareada_act.png"))));
        skin.add("boton vareada desactivado", new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"boton_vareada_des.png"))));
        skin.add("fondo touchpad",new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"touchpad.png")))));
        skin.add("marcador touchpad",new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"mando_touchpad.png")))));
        skin.add("boton imagen pequeno",new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"boton_pequeno_cuad.png"))));
        skin.add("boton imagen pequeno pulsado",new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"boton_pequeno_cuad_pulsado.png"))));
        skin.add("boton imagen pequeno selec",new TextureRegion(new Texture(Gdx.files.internal(CargadorRecursos.RUTA_RECURSOS_SKIN+"boton_pequeno_cuad_selec.png"))));
        
        
        
        //Juego de colores para la aplicacion:
        skin.add("Color texto celeste", new Color(0.8f,0.8f,1.0f,1.0f));
        skin.add("Color texto Maestro", new Color(0.8f,0.7f,0.0f,1.0f));
        skin.add("Color texto Tu", new Color(0.5f,0.5f,1.0f,1.0f));
        skin.add("Color texto Canduterio", new Color(1.0f,0.5f,0.5f,1.0f));
        skin.add("Color texto Sistema", new Color(1.0f,1.0f,1.0f,1.0f));
        skin.add("Color texto boton grande", new Color(0.9f,0.8f,0.0f,1.0f));
        skin.add("Color texto boton grande sobre", new Color(1.0f,1.0f,0.0f,1.0f));
        skin.add("Color texto boton grande pulsado", new Color(0.5f,0.4f,0.0f,1.0f));
        skin.add("Color texto ventana titulo", new Color(0.1f,0.5f,0.2f,1.0f));
        skin.add("Color texto ventana dentro", new Color(0.2f,0.8f,0.2f,1.0f));
        skin.add("Color texto ventana error", new Color(0.6f,0.1f,0.1f,1.0f));
        
        //Carga e inclusión de las fuentes
        BitmapFont fuenteGrande= new BitmapFont(Gdx.files.internal("fuentes"+File.separator+"font-export.fnt"),
                Gdx.files.internal("fuentes"+File.separator+"font-export.png"), false);
    
        BitmapFont fuenteMedia= new BitmapFont(Gdx.files.internal("fuentes"+File.separator+"font-export-glass.fnt"),
                Gdx.files.internal("fuentes"+File.separator+"font-export-glass.png"), false);
        
        BitmapFont fuenteGenerica=new BitmapFont();
        
        skin.add("Fuente grande", fuenteGrande);
        skin.add("Fuente media", fuenteMedia);
        skin.add("Fuente generica", fuenteGenerica);
        
        skin.add("Texto boton saltar intro", "Saltar Intro");
        
        /*Creacion del fondo para las barras de progreso: Diseño básico para el 'canalillo' de las barras de progreso*/
        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        skin.add("barra", new TextureRegionDrawable(new TextureRegion(new Texture(pixmap))));
        
        //Estilo del cuadro de texto
        TextArea.TextFieldStyle estiloCuadroTexto=new TextArea.TextFieldStyle();
        estiloCuadroTexto.font=skin.getFont("Fuente media");
        estiloCuadroTexto.background=skin.getDrawable("Fondo cuadro texto");
        estiloCuadroTexto.fontColor=skin.getColor("Color texto celeste");
        skin.add("Estilo cuadro texto", estiloCuadroTexto, TextArea.TextFieldStyle.class);
    
        //Estilo del cuadro de texto simple
        TextField.TextFieldStyle estiloCuadroTextoSimple=new TextField.TextFieldStyle();
        estiloCuadroTextoSimple.font=skin.getFont("Fuente media");
        estiloCuadroTextoSimple.background=skin.getDrawable("Fondo cuadro texto una linea");
        estiloCuadroTextoSimple.fontColor=skin.getColor("Color texto Canduterio");
        skin.add("Estilo cuadro texto simple", estiloCuadroTextoSimple, TextField.TextFieldStyle.class);
        
        //estilo de Boton grande de menu
        TextButton.TextButtonStyle estiloBotonMenu=new TextButton.TextButtonStyle();
        estiloBotonMenu.font=skin.getFont("Fuente media");
        estiloBotonMenu.fontColor=skin.getColor("Color texto boton grande");
        estiloBotonMenu.overFontColor =skin.getColor("Color texto boton grande sobre");
        estiloBotonMenu.downFontColor =skin.getColor("Color texto boton grande pulsado");
        estiloBotonMenu.up=skin.getDrawable("Boton grande sin pulsar");
        estiloBotonMenu.down=skin.getDrawable("Boton grande pulsado");
        estiloBotonMenu.over=skin.getDrawable("Boton grande sobre");
        skin.add("Estilo boton grande", estiloBotonMenu);
        
        //Estilo boton selector con imagen
        ImageButton.ImageButtonStyle estiloBotonSeleccion=new ImageButton.ImageButtonStyle();
        estiloBotonSeleccion.up=skin.getDrawable("boton imagen pequeno");
        estiloBotonSeleccion.down=skin.getDrawable("boton imagen pequeno pulsado");
        estiloBotonSeleccion.checked=skin.getDrawable("boton imagen pequeno selec");
        skin.add("Estilo boton pequeno seleccion", estiloBotonSeleccion);
        
        //Estilos Barras de Progreso
        ProgressBar.ProgressBarStyle estiloVidaBien = new ProgressBar.ProgressBarStyle(skin.get("barra",TextureRegionDrawable.class), skin.get("marcador barra vida bien" , TextureRegionDrawable.class));
        estiloVidaBien.knobBefore = estiloVidaBien.knob;
        skin.add("Barra progreso vida bien",estiloVidaBien);
    
        ProgressBar.ProgressBarStyle estiloVidaRegular = new ProgressBar.ProgressBarStyle(skin.get("barra",  TextureRegionDrawable.class), skin.get("marcador barra vida regular", TextureRegionDrawable.class));
        estiloVidaRegular.knobBefore = estiloVidaRegular.knob;
        skin.add("Barra progreso vida regular",estiloVidaRegular);
    
    
        ProgressBar.ProgressBarStyle estiloVidaMal = new ProgressBar.ProgressBarStyle(skin.get("barra", TextureRegionDrawable.class), skin.get("marcador barra vida mal", TextureRegionDrawable.class));
        estiloVidaMal.knobBefore = estiloVidaMal.knob;
        skin.add("Barra progreso vida mal",estiloVidaMal);
    
        ProgressBar.ProgressBarStyle estiloMana = new ProgressBar.ProgressBarStyle(skin.get("barra", TextureRegionDrawable.class), skin.get("marcador barra mana", TextureRegionDrawable.class));
        estiloMana.knobBefore = estiloMana.knob;
        skin.add("Barra progreso mana",estiloMana);
    
        ProgressBar.ProgressBarStyle estiloXP = new ProgressBar.ProgressBarStyle(skin.get("barra", TextureRegionDrawable.class), skin.get("marcador punto XP", TextureRegionDrawable.class));
        estiloXP.knobBefore = skin.get("marcador barra XP", TextureRegionDrawable.class);
        skin.add("Barra progreso XP",estiloXP);
    
        //Estilo del touchPad
        Touchpad.TouchpadStyle estiloTouchpad=new Touchpad.TouchpadStyle();
        estiloTouchpad.background=skin.get("fondo touchpad", TextureRegionDrawable.class);
        estiloTouchpad.knob=skin.get("marcador touchpad", TextureRegionDrawable.class);
        skin.add("touchpad",estiloTouchpad);
       
        //Estilos Etiquetas
        //Todo: cambiar valores de colores por constantes skin
        Label.LabelStyle estiloEtiquetaSimple =new Label.LabelStyle(skin.getFont("Fuente media"), new Color(1.0f,1.0f,1.0f, 1.0f));
        skin.add("Estilo etiqueta simple",estiloEtiquetaSimple);
    
        Label.LabelStyle estiloEtiquetaPtNombreJugador =new Label.LabelStyle(skin.getFont("Fuente generica"), new Color(0.5f,0.5f,1.0f, 1.0f));
        skin.add("Estilo etiqueta partida nombre jugador",estiloEtiquetaPtNombreJugador);
    
        Label.LabelStyle estiloEtiquetaPtNivelJugador =new Label.LabelStyle(skin.getFont("Fuente generica"), new Color(0.5f,1.0f,0.5f, 1.0f));
        skin.add("Estilo etiqueta partida nivel jugador",estiloEtiquetaPtNivelJugador);
    
        Label.LabelStyle estiloEtiquetaVentana =new Label.LabelStyle(skin.getFont("Fuente generica"), skin.getColor("Color texto ventana dentro"));
        skin.add("Estilo etiqueta ventana",estiloEtiquetaVentana);
    
        Label.LabelStyle estiloEtiquetaVentanaError =new Label.LabelStyle(skin.getFont("Fuente generica"), skin.getColor("Color texto ventana error"));
        skin.add("Estilo etiqueta ventana error",estiloEtiquetaVentanaError);
        
        
        
        //Estilo de ventana para lso dialogos
        Window.WindowStyle estiloDialogo=new Window.WindowStyle();
        estiloDialogo.background=skin.getDrawable("Fondo ventana" );
        //estiloDialogo.stageBackground=skin.getDrawable("Boton grande sin pulsar" );
        estiloDialogo.titleFont=skin.getFont("Fuente media");
        estiloDialogo.titleFontColor=skin.getColor("Color texto boton grande sobre");
        skin.add("Estilo dialogo",estiloDialogo);
        
        
        return skin;
    }
    
    /** Lee el listado de mazmorras, vinculando el titulo de la mazmorra al archivo asociado.
     *
      * @return Map[String, String], donde el indice es el nombre de la mazmorra y el valor es el nombre del archivo
     */
    public static final Map<Integer, DefMazmorra>cargaMapaMazmorras()
    {
        
        Mundo mundo=Mundo.getInstance();
        
        Map<Integer, DefMazmorra>mapa=new TreeMap<Integer, DefMazmorra>();
        XmlReader parser=new XmlReader();
        FileHandle punteroArchivo=Gdx.files.internal("data"+File.separator+"mazmorras.xml");
        XmlReader.Element raiz;
        try
        {
            raiz = parser.parse(punteroArchivo);
        }
        catch(Exception ex)
        {
            Gdx.app.error("Error en mazmorras.xml", ex.getMessage());
            return null;
        }
        
        Iterator it=raiz.getChildrenByName("mazmorra").iterator();
        int cont=0;
        while(it.hasNext())
        {
            XmlReader.Element maz=(XmlReader.Element)it.next();
            String nombreMaz=maz.getAttribute("nombre");
            String archMaz=maz.getAttribute("archivo");
            XmlReader.Element monstruos=maz.getChildByName("enemigos");
            
            //Lectura de los enemigos del nivel
            DefMazmorra.Monstruo bichos[]=new DefMazmorra.Monstruo[monstruos.getChildCount()];
            for(int x=0;x<monstruos.getChildCount();x++)
            {
                //Lectura de la definicion del monstruo
                String defMonstruo=monstruos.getChild(x).getAttribute("ref");
                
                //Lectura de los puntos de paso
                CoordCasilla puntosRecorrido[]=new CoordCasilla[monstruos.getChild(x).getChildCount()];
                for(int y=0;y<monstruos.getChild(x).getChildCount();y++)
                {
                    puntosRecorrido[y]=new CoordCasilla(Integer.parseInt(monstruos.getChild(x).getChild(y).getAttribute("x")),
                                                        Integer.parseInt(monstruos.getChild(x).getChild(y).getAttribute("y")));
                    
                }
                bichos[x]=new DefMazmorra.Monstruo( mundo.getAtlasBicheria().get(defMonstruo), puntosRecorrido);
            }

            mapa.put(cont, new DefMazmorra(nombreMaz,archMaz,bichos));
            cont++;
        }
        
        return mapa;
        
        
        
    }

    public static List<CoordCasilla> cargaMatrizInterpolacion(String archivoDatos)
    {
        FileHandle punteroArchivo=Gdx.files.internal(archivoDatos);
        if(!punteroArchivo.exists())
        {
            Gdx.app.error("RECURSOS:", "El ARCHIVO NO EXISTE");
            return null;
        }
        
        try
        {
            String strDatos=punteroArchivo.readString();
            StringTokenizer st = new StringTokenizer(strDatos,",");
            List<CoordCasilla> lista=new ArrayList<>();
            int x;
            int y;
            while (st.hasMoreTokens())
            {
                x=Integer.valueOf(st.nextToken().trim());
                y=Integer.valueOf(st.nextToken().trim());
                lista.add(new CoordCasilla(x,y));
            }
            return lista;
        }
        catch(Exception ex)
        {
            Gdx.app.error("RECURSOS: ", "Fallo en la carga del Archivo de interpolacion:"+ex.getMessage());
            ex.printStackTrace();
        }
        
        return null;
        
    }
    
    public static List<StageUIIntro.Conversacion>cargaConversacionInicio()
    {
        List<StageUIIntro.Conversacion>lista=new ArrayList<>();
        XmlReader parser=new XmlReader();
        FileHandle punteroArchivo=Gdx.files.internal("data"+File.separator+"conversacion_intro.xml");
        XmlReader.Element raiz;
        try
        {
            raiz = parser.parse(punteroArchivo);
        }
        catch(Exception ex)
        {
            Gdx.app.error("Error en conversacion_intro.xml", ex.getMessage());
            return null;
        }
        Iterator it=raiz.getChildrenByName("conversacion").iterator();
        while(it.hasNext())
        {
            XmlReader.Element cv=(XmlReader.Element)it.next();
            lista.add(new StageUIIntro.Conversacion(cv.getAttribute("actor"), cv.getText()));
        }
      
        return lista;
    }
    
    
    public static Map<String, Ataque> cargaDefAtaques()
    {
        Map<String, Ataque>definicionAtaques=new TreeMap<String, Ataque>();
        XmlReader parser=new XmlReader();
        FileHandle punteroArchivo=Gdx.files.internal("data"+File.separator+"ataques.xml");
        XmlReader.Element raiz;
        try
        {
            raiz = parser.parse(punteroArchivo);
        }
        catch(Exception ex)
        {
            Gdx.app.error("Error en ataques.xml", ex.getMessage());
            return null;
        }
        Iterator it=raiz.getChildrenByName("ataque").iterator();
    
        Ataque unAtaque;
        while(it.hasNext())
        {
            XmlReader.Element ataque=(XmlReader.Element)it.next();
        
            String nombre=ataque.getAttribute("nombre");  //Nombre
            int tipo= Integer.parseInt(ataque.getAttribute("tipo")); //Puntos de vida
            int danoBase=Integer.parseInt(ataque.getAttribute("dano_base"));
            float tiempoEjecucionBase=Float.parseFloat(ataque.getAttribute("tiempo_ejecucion"));
            String refSonido=ataque.getAttribute("ref_sonido");  //Referencia al sonido
            
            int coste=0;
            if(ataque.hasAttribute("coste"))
                coste=Integer.parseInt(ataque.getAttribute("coste"));
        
            String nombreProyectil=null;
            if(ataque.hasAttribute("proyectil"))
                nombreProyectil = ataque.getAttribute("proyectil");
        
            int radio=0;
            if(ataque.hasAttribute("radio"))
                radio= Integer.parseInt(ataque.getAttribute("radio"));
            
            switch(tipo)
            {
                case Ataque.TIPO_ATAQUE_FISICO:
                    unAtaque = new AtaqueFisico(nombre, tiempoEjecucionBase, danoBase, coste, refSonido);
                    break;
                case Ataque.TIPO_ATAQUE_DISTANCIA:
                    unAtaque=new AtaqueDistancia(nombre, tiempoEjecucionBase, danoBase, coste, refSonido, nombreProyectil );
                    break;
                case Ataque.ATAQUE_SUICIDA:
                    unAtaque=new AtaqueSuicida(nombre,tiempoEjecucionBase, danoBase, coste, refSonido, radio);
                    break;
                default:
                    continue;
            }
            
            definicionAtaques.put(nombre, unAtaque);
        }
        
        return definicionAtaques;
    }
    
    
    public static Map<String, DefProyectil>cargaDefProyectiles()
    {
        Map<String, DefProyectil> mapa=new TreeMap<String, DefProyectil>();
    
        XmlReader parser=new XmlReader();
        FileHandle punteroArchivo=Gdx.files.internal("data"+File.separator+"proyectiles.xml");
        XmlReader.Element raiz;
        try
        {
            raiz = parser.parse(punteroArchivo);
        }
        catch(Exception ex)
        {
            Gdx.app.error("Error en ataques.xml", ex.getMessage());
            return null;
        }
        Iterator it=raiz.getChildrenByName("proyectil").iterator();
    
        while(it.hasNext())
        {
            XmlReader.Element proyectil=(XmlReader.Element)it.next();
            String nombre=proyectil.getAttribute("nombre");
            int velocidad= Integer.parseInt(proyectil.getAttribute("velocidad"));
            String refAnim=proyectil.getAttribute("animacion");
            float radioDeteccion= Float.parseFloat(proyectil.getAttribute("radio_deteccion"));
            String refSonido=proyectil.getAttribute("sonido");
        
            mapa.put(nombre, new DefProyectil(nombre, velocidad,radioDeteccion, refAnim,refSonido));
        }
        
        return mapa;
    }
    
    
    public static Animation<TextureRegion>[] cargaAnimaciones(String archivoAnimacion, float vReproduccion)
    {
        //Todo: pasar a cosntantes del mundo (para incluir los ataques en las animaciones parciales)
        int framePorAnimacion=12;
        int totalAnimaciones=9;
        //Trincamos el archivo de sprites
        Texture hojaSprites = new Texture(Gdx.files.internal("imagenes" + File.separator+archivoAnimacion+".png"));
    
        
        TextureRegion[][] tmp = TextureRegion.split(hojaSprites,
                hojaSprites.getWidth() / framePorAnimacion,
                hojaSprites.getHeight() / totalAnimaciones);
        Animation<TextureRegion>[] animaciones=new Animation[totalAnimaciones];
        
        for(int x=0;x<totalAnimaciones;x++)
            animaciones[x]=new Animation<TextureRegion>(vReproduccion,tmp[x] );
        
        //Todo: preparar el sistema para que cargue las animaciones de ataque/disparo
        return animaciones;
        
    }
    
    
    public static  Map<String, Animation<TextureRegion>[]> cargaAnimacionesBichos(Map<String,DefBicho>mapaBichos )
    {
        Map<String, Animation<TextureRegion>[]> animacionesBichos=new TreeMap<String, Animation<TextureRegion>[]>();
        
        for(Map.Entry<String,DefBicho>bicho:mapaBichos.entrySet())
            animacionesBichos.put(bicho.getValue().archivoAnim, cargaAnimaciones(bicho.getValue().archivoAnim, bicho.getValue().vRepro));
            
        return animacionesBichos;
    }
    
    
    public static Map<String, DefBicho> cargaDefBichos(Map<String,Ataque>mapaReferenciaAtaques)
    {
        Map<String, DefBicho>definicionBichos=new TreeMap<String, DefBicho>();
        XmlReader parser=new XmlReader();
        FileHandle punteroArchivo=Gdx.files.internal("data"+File.separator+"bichos.xml");
        XmlReader.Element raiz;
        try
        {
            raiz = parser.parse(punteroArchivo);
        }
        catch(Exception ex)
        {
            Gdx.app.error("Error en defBichos", ex.getMessage());
            return null;
        }
      
        Iterator it=raiz.getChildrenByName("enemigo").iterator();
        //Leer todos los enemigos
        while(it.hasNext())
        {
            XmlReader.Element bicho=(XmlReader.Element)it.next();

            String nombre=bicho.getAttribute("nombre");  //Nombre
            int pv= Integer.parseInt(bicho.getAttribute("pv")); //Puntos de vida

            float distanciaPercepcion=Float.parseFloat(bicho.getAttribute("dist_percepcion"));
    
            float velocidadAnimacion=Float.parseFloat(bicho.getAttribute("v_reproduccion_animacion"));
            
            String nombreArchivoAnimacion=bicho.getAttribute("animacion");
    
            int xp=Integer.parseInt(bicho.getAttribute("xp")); //Puntos de vida
            
            //Tasa de regeneracion
            int regeneracion=0;
            if(bicho.hasAttribute("regenera"))
                regeneracion = Integer.parseInt(bicho.getAttribute("regenera"));
    
            int velocidad=0;
            if(bicho.hasAttribute("velocidad"))
                velocidad = Integer.parseInt(bicho.getAttribute("velocidad"));
            
            //Carga de los ataques y las animaciones asociadas Todo: CARGAR LA REFERENCIA DELAS ANIMACIONES DE ATAQUE!!
            
            int cont=0;
            Ataque[] ataques=new Ataque[bicho.getChildrenByName("ataque").size];
            Iterator it2=bicho.getChildrenByName("ataque").iterator();
            
            while(it2.hasNext())
            {
                XmlReader.Element ataque=(XmlReader.Element)it2.next();
                ataques[cont]=mapaReferenciaAtaques.get(ataque.getAttribute("ref"));
                cont++;
            }
            
            //Creacion del bichaco
            definicionBichos.put(nombre, new DefBicho(nombre, pv, regeneracion, nombreArchivoAnimacion, velocidad, distanciaPercepcion, velocidadAnimacion,ataques,xp) );
        }
    
        return definicionBichos;
    }
    
}
