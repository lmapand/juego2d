/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mi.paquete.juego.modelo.auxiliar.grafo;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author luis
 */
public class GrafoConPeso<V> extends AbstractGrafo<V>
{
    
    private List<PriorityQueue<AristaPeso>> colas;
    
    public GrafoConPeso(int[][] aristas, V[] vertices) 
    {
        super(aristas, vertices);
        creaColas(aristas, vertices.length);
    }
    
    
    public GrafoConPeso(int[][] aristas, int numeroVertices) 
    {
        super(aristas, numeroVertices);
        creaColas(aristas, numeroVertices);
    }
  
      public GrafoConPeso(List<AristaPeso> aristas, List<V> vertices) 
    {
        super((List)aristas, vertices);
        creaColas(aristas, vertices.size());
        
    }

    public GrafoConPeso(List<AristaPeso> aristas, int numeroVertices) 
    {
        super((List)aristas, numeroVertices);
        creaColas(aristas, numeroVertices);
    }
    
    private void creaColas(int[][] aristas, int numeroVertices)
    {
        this.colas=new ArrayList<PriorityQueue<AristaPeso>>();
        
        //Crear una cola por cada vertice
        for(int i=0;i<numeroVertices;i++)
            colas.add(new PriorityQueue<AristaPeso>());

        
        //Vamos recorriendo el array y colocando cada arista con su correspondiente elemento
        for(int i=0;i<aristas.length;i++)
        {
            int u=aristas[i][0]; //Origen
            int v=aristas[i][1]; //Destino
            int peso=aristas[i][2]; //Peso
            colas.get(u).offer(new AristaPeso(u,v,peso));
        }
    }
    
    
    private void creaColas(List<AristaPeso> aristas, int numeroVertices)
    {
        colas=new ArrayList<PriorityQueue<AristaPeso>>();
    
         //Crear una cola por cada vertice
        for(int i=0;i<numeroVertices;i++)
            colas.add(new PriorityQueue<AristaPeso>());
        
        for(AristaPeso ap:aristas)
            colas.get(ap.u).offer(ap);
                
    }

    /** Obtiene el Arbol de Recubrimiento Minimo desde el Vértice inicial.
     * 
     * @return Arbol de Recubrimiento Mínimo para el Vértice 0.
     */
    public MST getArbolRecubrmientoMinimo()
    {
        return getArbolRecubrimientoMinimo(0);
    
    }

    /** Obtiene el Arbol de Recubrimiento Minimo desde un vértice.
     * 
     * @param indiceVerticeInicio indice del vertice del que queremos el arbol
     * @return  Arbol de Recubrimiento Mínimo para el Vértice indicado
     */    
    public MST getArbolRecubrimientoMinimo(int indiceVerticeInicio)
    {
        /* creamos una lista con los vertices a analizar*/
        List<Integer> t=new ArrayList<Integer>();
        t.add(indiceVerticeInicio);
    
        int numeroVertices=vertices.size();
        int[] padre=new int[numeroVertices];
        for(int i=0;i<numeroVertices;i++)
            padre[i]=-1;
        
        int pesoTotal=0;
    
        List<PriorityQueue<AristaPeso>> colasTemp=clonaColas(this.colas);
        
        
        while(t.size()<numeroVertices)
        {
            int v=-1;
            double pesoMinimo=Double.MAX_VALUE;
            for( int u: t)
            {
                while (!colasTemp.get(u).isEmpty() && t.contains(colasTemp.get(u).peek().v)) 
                {
                    // Remove the edge from queues.get(u) if the adjacent vertex of u is already in T
                    colasTemp.get(u).remove();
                }
                
                if(colasTemp.get(u).isEmpty())  //Vamos al siguiente vertice
                    continue;
                
                
                AristaPeso arista=colasTemp.get(u).peek();
                if(arista.peso < pesoMinimo)
                {
                    v=arista.v;
                    pesoMinimo=arista.peso;
                    padre[v]=u; //Marcamos como visitada
                }
            }
            t.add(v);
            pesoTotal+=pesoMinimo;

        }
        
        return new MST(indiceVerticeInicio, padre, t, pesoTotal);
    }
    
    /** Clona el array de colas  */
    private List<PriorityQueue<AristaPeso>> clonaColas(List<PriorityQueue<AristaPeso>> lasColas)
    {
        List<PriorityQueue<AristaPeso>> colasCopiadas = new ArrayList<PriorityQueue<AristaPeso>>();
        for (int i = 0; i < lasColas.size(); i++) 
        {
            colasCopiadas.add(new PriorityQueue<AristaPeso>());
            for (AristaPeso ap : lasColas.get(i)) 
            {
                colasCopiadas.get(i).add(ap);
            }
        }
        return colasCopiadas;
    }
    
    
    
    /** Obtiene un arbol con los caminos mas cortos.
    * 
    * @param indiceOrigen
    * @return 
    */ 
   public ArbolCaminoMasCorto getCaminoMasCorto(int indiceOrigen)
   {
       //Lista de los indices de los vertices ya analizados
       List<Integer>indicesAnalizados=new ArrayList<Integer>();
       indicesAnalizados.add(indiceOrigen);

       //vertices raiz de los vertices 
       int numeroVertices=vertices.size();
       int[] padre=new int[numeroVertices];
       padre[indiceOrigen]=-1;

       int[] costes=new int [numeroVertices];

       for (int i=0;i<costes.length;i++)
           costes[i]=Integer.MAX_VALUE;

       costes[indiceOrigen]=0;

       List<PriorityQueue<AristaPeso>> colasTemp=clonaColas(colas);
       while(indicesAnalizados.size()<numeroVertices)
       {
           int v=-1;
           int costeMinimo=Integer.MAX_VALUE;
           for(int u : indicesAnalizados)
           {
           
               while(!colasTemp.get(u).isEmpty() && indicesAnalizados.contains(colasTemp.get(u).peek().v) )
                   colasTemp.get(u).remove();
                   
               if(colasTemp.get(u).isEmpty())
                   continue;
               
               AristaPeso arista=colasTemp.get(u).peek();
               if(costes[u] + arista.peso < costeMinimo)
               {
                   v=arista.v;
                   costeMinimo=costes[u]+arista.peso;
                   padre[v]=u;
               }
           }
       
           //Registro del final del analisis de v
           indicesAnalizados.add(v);
           costes[v]=costeMinimo;
       }

       return new ArbolCaminoMasCorto(indiceOrigen, padre, indicesAnalizados, costes);
   }
    
    
    /**
     * 
     * @return lista de las colas de prioridad del grafo
     */
    public List<PriorityQueue<AristaPeso>>getAristasPeso()
    {
        return this.colas;
    }
    
    
    
    
    /** Arbol de tipo recubrimiento Mínimo.
     * 
     */
    public class MST extends Arbol
    {
        private int pesoTotal;
    
        public MST(int verticeRaiz, int[] padre, List<Integer>ordenBusqueda, int pesoTotal)
        {
            super(verticeRaiz, padre, ordenBusqueda);
            this.pesoTotal=pesoTotal;
        
        }
        
        
        public int getPesoTotal() 
        {
            return pesoTotal;
        }
    }
    
    
    /** Arbol para el almacenamiento de Subcaminos.
     * 
     */
    public class ArbolCaminoMasCorto extends Arbol
    {
        private int [] costes;
        
        
        public ArbolCaminoMasCorto(int origen, int[] padre, List<Integer>ordenBusqueda, int[] costes)
        {
            super(origen, padre, ordenBusqueda);
            this.costes=costes;
        }
        
        /** Obtiene el coste del camino hasta el vertice indicado.
         * 
         * @param v indice del vértice a consultar
         * @return coste del camino
         */
        public int getCostes(int v)
        {
            return costes[v];
        }
        
        /** funcion de test para comprobar el correcto funcionamiento del trebello.
         * 
         */
        public void imprimeCaminosTodos()
        {
            System.out.println("Todos los caminos desde "+vertices.get(getRaiz())+"son:" );
            for(int i=0;i< costes.length;i++)
            {
                imprimeCamino(i);
                System.out.println(" (Coste:"+costes[i]+")");
            }
        }
    }
    
    
    
   
    
    
}
