package mi.paquete.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;

import java.io.File;
import java.io.StringWriter;

import mi.paquete.juego.modelo.bicheria.Fulano;

public class GestorPartidas
{
    /**
     * Nombre de la carpeta utilizada para el almacenamiento de las partidas.
     */
    public static final String DIR_ALMACENAMINETO = "data";
    private static final String TAG_PARTIDA = "partida";
    private static final String TAG_PERSONAJE = "personaje";
    private static final String TAG_NIVEL = "nivel";
    private static final String TAG_XP = "XP";
    private static final String TAG_MAZMORRA = "mazmorra";
    private static final String TAG_HABIL = "habilidades";
    
    
    /**
     * Registra un XML de partida nuevo en el Slot indicado, sustituyendo el que hubiera.
     * <p>
     * El slot que hubiera anteriormente será borrado
     * //Todo: crear un metodo análogo que 'actualice' los datos de una partida existente.
     *
     * @param nombrePersonaje    String nombre del personaje a registrar
     * @param slotAlmacenamiento Slot de almacenamiento de la partida
     * @return Partida: EL objeto Partida equivalente a los datos guardados
     */
    public Partida registraPartida(String nombrePersonaje, int slotAlmacenamiento)
    {
        
        Partida unaPartida = new Partida(nombrePersonaje);
        
        //Creacion del XML correspondiente
        StringWriter escritor = new StringWriter();
        StringBuilder sb = new StringBuilder();
        creaAperturaTagRaiz(sb);
        creaTag(sb, TAG_PERSONAJE, nombrePersonaje);
        creaTag(sb, TAG_NIVEL, "0");
        creaTag(sb, TAG_XP, "0");
        creaTag(sb, TAG_MAZMORRA, "0");
        creaCierreTagRaiz(sb);
        
        //Almacenamiento del XML
        FileHandle fhnd = Gdx.files.local(GestorPartidas.DIR_ALMACENAMINETO + File.separator + "partida_" + slotAlmacenamiento + ".xml");
        fhnd.writeString(sb.toString(), false);
        if(!fhnd.file().exists())
            Gdx.app.error("Error creando partida","Partida" + slotAlmacenamiento + ".xml");
        
        //Intento de lectura de la trapallada en pantalla
        /*FileHandle punteroArchivo = Gdx.files.local(GestorPartidas.DIR_ALMACENAMINETO + File.separator + "partida_" + slotAlmacenamiento + ".xml");
        String texto=punteroArchivo.readString();
        Gdx.app.log("CONTENIDO DEL ARCHIVO:",texto);*/
        //Devolucion de la nueva partida
        return unaPartida;
    }
    
    private final void creaAperturaTagRaiz(StringBuilder sb)
    {
        sb.append("<").append(TAG_PARTIDA).append(">").append("\n");
    }
    
    private final void creaCierreTagRaiz(StringBuilder sb)
    {
        sb.append("</").append(TAG_PARTIDA).append(">").append("\n");
    }
    
    private final void creaTag(StringBuilder sb, String tag, String valor)
    {
        sb.append("<").append(tag).append(">").append(valor).append("</").append(tag).append(">").append("\n");
    }
    
    /** Lee la partida almacenanda en el Slot indicado     *
     * @param slot
     * @return
     */
    public Partida cargaPartida(int slot)
    {
        XmlReader parser = new XmlReader();
        FileHandle punteroArchivo = Gdx.files.local(GestorPartidas.DIR_ALMACENAMINETO + File.separator + "partida_" + slot + ".xml");
        XmlReader.Element raiz;
        try
        {
            raiz = parser.parse(punteroArchivo);
        } catch (Exception ex)
        {
            Gdx.app.error("Error cargando partida_" + slot + ".xml", ex.getMessage());
            return null;
        }
        String nombrePersonaje = raiz.getChildByName("personaje").getText();
        int nivel = Integer.parseInt(raiz.getChildByName("nivel").getText());
        int xp = Integer.parseInt(raiz.getChildByName("XP").getText());
        int refMazmorra = Integer.parseInt(raiz.getChildByName("mazmorra").getText());
        int[][] habilidades = null;
        if (raiz.hasChild("habilidades"))
        {
            habilidades = procesaHabilidadesdesdeXML(raiz.getChildByName("personaje").getText());
        }
        
        return new Partida(nombrePersonaje, xp, nivel, refMazmorra, habilidades);
        
    }
    
    
    /** Almacena los datos de la partida actual en el slot indicado.
     *
     * El proceso de Subida de nivel lo procesamos aqui provisionalmente, y sin habilidades
     *  Todo: hacer que el proceso de subida de nivel se realice ANTES de llegar aqui
     * @param unFulano Fulano protagonista con los datos a actualiar
     * @param indiceSlot int Slot de la partida a actualizar
     * @param partidaActual Partida pasado por referencia para tomar los datos no accesibles (todo: PARAMETRO PROVISIONAL)
     * @return Partida
     */
    Partida guardaPartida(Fulano unFulano, int indiceSlot, Partida partidaActual)
    {
        
        //Proceso provisional de subida de nivel
        int nivel=unFulano.getXP()/100;
        int xpActual=unFulano.getXP()%100;
        String nombrePersonaje=partidaActual.getNombrePersonaje();
        int refMazmorra=partidaActual.getRefMazmorra() +1;
    
        //escritua del XML
        StringWriter escritor = new StringWriter();
        StringBuilder sb = new StringBuilder();
        creaAperturaTagRaiz(sb);
        creaTag(sb, TAG_PERSONAJE, nombrePersonaje);
        creaTag(sb, TAG_NIVEL, String.valueOf(nivel));
        creaTag(sb, TAG_XP, String.valueOf(xpActual));
        creaTag(sb, TAG_MAZMORRA, String.valueOf(refMazmorra));
        creaCierreTagRaiz(sb);
    
        //Almacenamiento del XML
        FileHandle fhnd = Gdx.files.local(GestorPartidas.DIR_ALMACENAMINETO + File.separator + "partida_" + indiceSlot + ".xml");
        fhnd.writeString(sb.toString(), false);
        
        //Creacion del objeto partida a refeenciar
        return new Partida(nombrePersonaje,xpActual,nivel,refMazmorra,null);
    }

    
    /** Calcula la matriz de habilidads del personaje.
     *
     * La matriz de habilidades se debe almacenar como una cadena consecutiva de 0's y 1's.
     * Todo: pendiente de establecer la matriz de habilidades
     * @param cadenaHabilidades
     * @return int[][] conversion de la cadena de 0's y 1's del XML a matriz de habilidades
     */
    private int[][] procesaHabilidadesdesdeXML(String cadenaHabilidades)
    {
        return null;
    }
    
}
