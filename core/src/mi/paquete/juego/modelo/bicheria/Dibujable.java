package mi.paquete.juego.modelo.bicheria;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Dibujable
{
    
    public static final int ORDEN_SIEMPRE_ATRAS =-1;
    public static final int ORDEN_APOYADO_SUELO=0;
    public static final int ORDEN_FLOTANDO_SOBRE_SUELO=1;
    public static final int ORDEN_SUPERPUESTO =2;
    
    /**Realiza el proceso de dibujado
     *
      * @param batch SpriteBatch referencia al proceso por lotes de dibujado
     * @param delta flot desfase de tiempo desde el ultimo frame
     */
    public void dibuja(SpriteBatch batch, float delta );
    
    /** Codigo a ejecutar si este dibujable necesita eliminar algun recurso gráfico temporal
     *
     */
    public void eliminaRecursosGraficos();
    
    /** Posicion X de dibujado.
     *
     * @return float
     */
    public float getXSprite();
    
    /** Posicion Y de dibujado.
     *
     * @return float
     */
    public float getYSprite();
    
    /** Indica si este dibujable es visible.
     *
      * @return boolean
     */
    public boolean esVisible();
    
    /** Indica el inidice Z de dibujado de esta entidad.
     *
     *  Todo: implementar el algoritmo de ordenacionNO utilizado
     * @return int una de las constantes Dibujable::
     */
    public int getOrdenDibujado();
}


