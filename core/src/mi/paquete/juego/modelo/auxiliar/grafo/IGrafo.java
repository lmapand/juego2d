/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mi.paquete.juego.modelo.auxiliar.grafo;

import java.util.List;

/** *  Interfaz para un Grafo.El tipo V se refiere a cualquier tipo que pueda representar un Vértice.
 * 
 * Posiblemente
 haya que crear una interfaz para el mismo en versiones posteriores.
 * 
 *
 * @author luis
 * @param <V> sublcalse de Vértice
 */
public interface IGrafo<V>
{
    
    public static final int INDICE_NO_ENCONTRADO=-1;
    
    /** Obtiene el número de vértices del Grafo.
     * 
     * @return int 
     */
    public int getSize();
    
    /** Obtiene la lista de Vërtices del grafo
     * 
     * @return V[] Lista de vértices que forman el grafo
     */
    public List<V> getVertices();
    
    /** Ontiene el vértice que en el conjunto de Vërtices tiene el índice indicado
     * 
     * @param indice int Indice del vértice buscado
     * @return V o null si no existe dicho vértice
     */
    public V getVertice(int indice);
    
    
    /** Obtiene el índice de un vértice.
     * 
     * @param vertice V Vértice a buscar
     * @return int indice del vértice buscado o INDICE_NO_ENCONTRADO si no se encuentra.
     */
    public int getIndice(V vertice);
    
    
    /** Obtiene los índices de los vértices vecinos al vértice cuyo índoce solicitamos.
     * 
     * @param indice int indoce del Vértice del cual queremos obtener sus vecinos
     * @return Integer[], lista de los índices de los vértices vecinos
     */
    public List<Integer> getIndicesVecinos(int indice);
    
    
    /** Obtiene el grado del Vértice con el índice indicado.
     * 
     * @param indice int indoce del vértice a analizar
     * @return int grado del vértice (número de aristas incidentes)
     */
    public int getGrado(int indice);
    
    
    /** Obtiene la matriz de Adyacencia asociada con este grafo.
     * 
     * @return int[][] array representacndo la matriz de Adyacencia.
     */
    public int[][] getMatrizAdyacencia();
    
    
    /** Metodo de prueba, que imprime la matriz de adyacencia
     * 
     */
    public void imprimeMatrizAdyacencia();
    
    public void imprimeAristas();
    
}
