package mi.paquete.juego.modelo.auxiliar;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mi.paquete.juego.modelo.CoordCasilla;
import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.auxiliar.grafo.AristaPeso;
import mi.paquete.juego.modelo.auxiliar.grafo.GrafoConPeso;

/** Clase de ayuda con funciones para el análisis de distancias, visibilidad y análisis de grafos*/

public class Analizador
{
    
    /** ANgulos precalculados pra reducir el tiempo de CPU en el cálculo de ángulos para el TocuhPad*/
    public static final double LIMITE_ANGULO_TOUCHPAD_0=Math.PI/8.0;
    public static final double LIMITE_ANGULO_TOUCHPAD_1=3.0*Math.PI/8.0;
    public static final double LIMITE_ANGULO_TOUCHPAD_2=5.0*Math.PI/8.0;
    public static final double LIMITE_ANGULO_TOUCHPAD_3=7.0*Math.PI/8.0;
    public static final double LIMITE_ANGULO_TOUCHPAD_4=9.0*Math.PI/8.0;
    public static final double LIMITE_ANGULO_TOUCHPAD_5=11.0*Math.PI/8.0;
    public static final double LIMITE_ANGULO_TOUCHPAD_6=13.0*Math.PI/8.0;
    public static final double LIMITE_ANGULO_TOUCHPAD_7=15.0*Math.PI/8.0;
    
    /** Calcula por interpolación cutre el camino entre la casilla origen y la casilla destino.
     *
     * @param origen coordenadas de la casilla de origen de la nterpolación
     * @param destino coordenadas de la casilla final (a la que queremos llegar)
     * @return List[CoordCasilla] lista ordenada de las casillas que forman el camino desde origen a destino
     */
    public static List<CoordCasilla> interpola(CoordCasilla origen, CoordCasilla destino)
    {
        List<CoordCasilla> camino = new ArrayList<>();
        
        //Calculando espacio de interpolacion
        int dx = destino.x - origen.x;
        int dy  = destino.y - origen.y;
        
        //Ver si el maximo es en Y o en X
        int pasos = Math.max(Math.abs(dx), Math.abs(dy));
        float divN =(float) ((pasos == 0) ? 0.0 : 1.0 / pasos);
        float pasoX = dx * divN;
        float pasoY = dy * divN;
        float x = origen.x;
        float y = origen.y;
        for (int i = 0; i <= pasos; i++, x += pasoX, y += pasoY)
            camino.add(new CoordCasilla(Math.round(x), Math.round(y)) );

        return camino;
    }
    
    
    /** Calucla el encaramiento entre casillas colindantes
     *
     * @param origen coordenadas del punto de partida
     * @param destino coordenadas del punto de llegada
     * @return int, una de las constantes Mundo.ACCIION_*
     */
    public static int calculaEncaramiento(CoordCasilla origen, CoordCasilla destino) throws Exception
    {
        int sumOrigen=origen.x +origen.y;
        int sumDestino=destino.x + destino.y;
        switch(sumDestino - sumOrigen)
        {
            case 2:
                return Mundo.ACCION_ARRIBA_DERECHA;
            case 1:
                return origen.x==destino.x ?  Mundo.ACCION_ARRIBA :  Mundo.ACCION_DERECHA;
            case 0:
                return origen.x<destino.x ?  Mundo.ACCION_ABAJO_DERECHA :  Mundo.ACCION_ARRIBA_IZQUIERDA;
            case -1:
                return origen.x==destino.x ?  Mundo.ACCION_ABAJO :  Mundo.ACCION_IZQUIERDA;
            case -2:
                return Mundo.ACCION_ABAJO_IZQUIERDA;
            default:
                throw new Exception("Las casillas analizadas no son colindantes");
        }
    }
    
    public static double distancia(float x1, float y1, float x2, float y2)
    {
        return Math.sqrt( Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2) );
    }
    
    /** Obtiene el encaramiento en funcion del angulo de encaramiento
     *
     * Lo utilizan los 'Mosntruos' para cacular el encaramiento para disparo y ataque fisico.
     *
     * @param anguloRadianes angulo en radianes del encaramiento
     * @return
     */
    public static int calculaEncaramiento(double anguloRadianes)
    {
        //Redondeo para angulo negativo
        if( anguloRadianes<0)
            anguloRadianes+=Math.PI*2;
        
        if(anguloRadianes >= Math.PI/4.0 && anguloRadianes <= 3*Math.PI/4.0 )
            return Mundo.ACCION_ARRIBA;
        else if(anguloRadianes >= 3* Math.PI/4.0 && anguloRadianes <= 5 * Math.PI/4.0)
            return Mundo.ACCION_IZQUIERDA;
        else if(anguloRadianes >= 5 * Math.PI/4.0 && anguloRadianes <= 7 * Math.PI/4.0)
            return Mundo.ACCION_ABAJO;
        else
            return Mundo.ACCION_DERECHA;
    }
    

    public static int encaramientoTouchPad(double anguloRadianes)
    {
        if( anguloRadianes<0)
            anguloRadianes+=Math.PI*2;
    
        
        if(anguloRadianes < Analizador.LIMITE_ANGULO_TOUCHPAD_0)
            return Mundo.ACCION_DERECHA;
        if(anguloRadianes < Analizador.LIMITE_ANGULO_TOUCHPAD_1 )
            return Mundo.ACCION_ARRIBA_DERECHA;
        else if(anguloRadianes < Analizador.LIMITE_ANGULO_TOUCHPAD_2 )
            return Mundo.ACCION_ARRIBA;
        else if(anguloRadianes < Analizador.LIMITE_ANGULO_TOUCHPAD_3)
            return Mundo.ACCION_ARRIBA_IZQUIERDA;
        else if(anguloRadianes < Analizador.LIMITE_ANGULO_TOUCHPAD_4)
            return Mundo.ACCION_IZQUIERDA;
        else if(anguloRadianes < Analizador.LIMITE_ANGULO_TOUCHPAD_5)
            return Mundo.ACCION_ABAJO_IZQUIERDA;
        else if(anguloRadianes < Analizador.LIMITE_ANGULO_TOUCHPAD_6)
            return Mundo.ACCION_ABAJO;
        else if(anguloRadianes < Analizador.LIMITE_ANGULO_TOUCHPAD_7)
            return Mundo.ACCION_ABAJO_DERECHA;
        else
            return Mundo.ACCION_DERECHA;
    }
    
    
    public static double anguloResultante(float origenX, float origenY, float destinoX, float destinoY)
    {
        return Math.atan2(destinoY-origenY, destinoX-origenX );
    }
    
    
    
   
    public static GrafoConPeso<CoordCasilla> generaGrafoTransitabilidad(TiledMapTileLayer mapaTransitable,
                                                                              Map<Integer,CoordCasilla> mapaDirectoVertices,
                                                                              Map<CoordCasilla,Integer>mapaInversoVertices)
    {
        
        CoordCasilla casilla;
        int cont=0;
        for(int x=0;x<mapaTransitable.getWidth();x++)
        {
            for(int y=0;y<mapaTransitable.getHeight();y++)
            {
                if(null!=mapaTransitable.getCell(x,y))
                {
                    casilla=new CoordCasilla(x, y);
                    mapaDirectoVertices.put(cont, casilla);
                    mapaInversoVertices.put(casilla, cont);
                    cont++;
                }
            }
        }
      
        //TEST:
        /*for(Map.Entry<Integer, CoordCasilla>entrada:mapaDirectoVertices.entrySet())
            Gdx.app.log("Indice "+entrada.getKey(), "Coord: ("+entrada.getValue().x+","+entrada.getValue().y+")");*/
            
        //UNa vez evaluadas las casillas posibles, establecemos los Arrays de Adyacencia:
        //Para cada casilla, evaluamos cuantas correspondencias hay:
        int x;
        int y;
        List<AristaPeso>aristas=new ArrayList<>();
        for(Map.Entry<Integer, CoordCasilla>entrada:mapaDirectoVertices.entrySet())
        {
            //Analizamos las ocho casillas adyacentes a esta; si son transitables, añadimos la entrada
            x=entrada.getValue().x;
            y=entrada.getValue().y;
            for(int i=-1;i<=1;i++)
            {
                for(int j=-1;j<=1;j++)
                {
                    //Solo contemplamos desplazamientos horizontales y verticales. Descartamos los desplazamientos 'angulares'
                    if(i==j || i==-j ||
                       x-i<0 || x+i>=mapaTransitable.getWidth() ||
                       y-j<0 || y+j>=mapaTransitable.getHeight())
                        continue;
                    
                    casilla=new CoordCasilla(x+i, y+j);
                    if(null!=mapaTransitable.getCell(casilla.x,casilla.y))
                    {
                        aristas.add(new AristaPeso(entrada.getKey(), mapaInversoVertices.get(casilla),1));
//                        Gdx.app.log("De  "+entrada.getKey(), "a "+mapaDirectoVerticesInversa.get(casilla));
                    }
                }
            }
        }
        
        //Obtener el ArrayLIst del Mapa
        
        
        return new GrafoConPeso<CoordCasilla>(aristas,  new ArrayList<CoordCasilla>(mapaDirectoVertices.values()));
    }
    
    
    /** De moemnto, solo lo usan los enemigos, y solo evalua encaramientos horizontales y verticales.
     *
     * @param desde CoordCasilla del que mira
     * @param hasta CoordCasilla del mirado
     * @return int una delas constantes Mundo.ACCION_*
     */
    public static int calculaEncaramientoParaIr(CoordCasilla desde, CoordCasilla hasta)
    {
        if(desde.compareTo(hasta)==0)
            return Mundo.ACCION_PARADO;
        
        if(desde.x==hasta.x)
        {
            if (desde.y<hasta.y)
                return Mundo.ACCION_ARRIBA;
            else if(desde.y>hasta.y)
                return Mundo.ACCION_ABAJO;
        }
        if(desde.y==hasta.y)
        {
            if (desde.x<hasta.x)
                return Mundo.ACCION_DERECHA;
            else if(desde.x>hasta.x)
                return Mundo.ACCION_IZQUIERDA;
        }
        return -1;
    }
    
    
}
