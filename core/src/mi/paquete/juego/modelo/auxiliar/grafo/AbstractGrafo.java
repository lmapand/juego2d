/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mi.paquete.juego.modelo.auxiliar.grafo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author luis
 */
public class AbstractGrafo<V> implements IGrafo<V>
{

    protected List<V>vertices;
    protected List<List<Integer>>vecinos;
    
    /** COnstruye el grafo en base a vértices y aristas contenidas en un array.
     * 
     * 
     * @param aristas int [][] matriz de Aristas
     * @param vertices V[] matriz de Vértices
     */
    public AbstractGrafo(int[][] aristas, V[] vertices)
    {
        this.vertices=new ArrayList<V>();
        for(int i=0;i<vertices.length;i++)
        {
            this.vertices.add(vertices[i]);

            this.creaListasAdyacencia(aristas, vertices.length);
        }
    }

    public AbstractGrafo(List<Arista>aristas, List<V> vertices)
    {
        this.vertices=vertices;
        this.creaListasAdyacencia(aristas, vertices.size());

    }

    public AbstractGrafo(List<Arista>aristas, int numeroVertices)
    {
        this.vertices=new ArrayList<V>();
        for(int i=0;i<numeroVertices;i++)
            vertices.add( (V)(new Integer(i) ));
     
        
        this.creaListasAdyacencia(aristas, numeroVertices);
     }
     

    public AbstractGrafo(int[][] aristas, int numeroVertices)
    {
        this.vertices=new ArrayList<V>();
        for(int i=0;i<numeroVertices;i++)
            vertices.add( (V)(new Integer(i) ));
     
        
        this.creaListasAdyacencia(aristas, numeroVertices);
     }
     
    
    
    @Override
    public int getSize() 
    {
        return this.vertices.size();
    }

    @Override
    public List<V> getVertices() 
    {
        return this.vertices;
    }

    @Override
    public V getVertice(int indice)
    {
        return this.vertices.get(indice);
    }

    @Override
    public int getIndice(V vertice) 
    {
        return this.vertices.indexOf(vertice);
    }

    @Override
    public List<Integer> getIndicesVecinos(int indice) 
    {
        return this.vecinos.get(indice);
    }

    @Override
    public int getGrado(int indice) 
    {
        return this.vecinos.get(indice).size();
    }

    @Override
    public int[][] getMatrizAdyacencia() 
    {
        int[][] matrizAdy=new int[this.vertices.size()][this.vertices.size()];
        
        /* Como los elementos se inicializan en 0, sólo tenemos que obtener los 
        elementos registradospara y referenciarlos con un '1'*/
        for(int x=0;x<this.vecinos.size();x++)
        {
            for(int y=0;y<this.vecinos.get(x).size();y++)
            {
                int v=this.vecinos.get(x).get(y);
                matrizAdy[x][v]=1;
            }
        }
        return matrizAdy;
    }

    @Override
    public void imprimeMatrizAdyacencia() 
    {
        int[][] mat=this.getMatrizAdyacencia();
        
        for(int x=0;x<this.vertices.size();x++)
        {
            System.out.println();
            for(int y=0;y<this.vertices.size();y++)
            {
                System.out.print("\t"+mat[x][y]);
            }
        }
        System.out.println();
    }

    @Override
    public void imprimeAristas() 
    {
        for(int x=0;x<this.vecinos.size();x++)
        {
            System.out.print("Vertice "+x+": ");
            for(int y=0;y<this.vecinos.get(x).size();y++)
            {
                System.out.print("(" + x +", "+this.vecinos.get(x).get(y)+")");
            }
            System.out.println();
        }
    }
    
    
    /** Crea la lista de aristas en base a un array bidimensional.
     * 
     * @param aristas int[][] matriz de adyacencia.
     */
    private void creaListasAdyacencia(int[][] aristas, int numVertices )
    {
        this.vecinos=new ArrayList<List<Integer>>();
        
        /* Creamos un arrayList por cada Vértice*/
        for(int i=0;i<numVertices;i++)
            this.vecinos.add(new ArrayList<Integer>());

        /* Rellenamos los arrayLists previamente creados */
        for(int i=0;i<aristas.length;i++)
        {
            int u=aristas[i][0];
            int v=aristas[i][1];
            this.vecinos.get(u).add(v);
        }
    }
    
    private void creaListasAdyacencia(List<Arista> aristas, int numVertices)
    {
        this.vecinos=new ArrayList<List<Integer>>();
    
        /* Creamos un arrayList por cada Vértice*/
        for(int i=0;i<numVertices;i++)
            this.vecinos.add(new ArrayList<Integer>());
        
        
        for(Arista unaArista:aristas)
            this.vecinos.get(unaArista.u).add(unaArista.v);
    }
    
    
    /** Obtiene un arbol DFS (Deep First Search) partieno del vértice v.
     * 
     * @param v indice del vértice a a analizar.
     * @return Arbol
     */
    public Arbol dfs(int v)
    {
        List<Integer> ordenBusqueda=new ArrayList<>();
        int[] padre=new int[this.vertices.size()];

        /* inicializacin de los padres a -1*/
        for(int i=0;i<padre.length;i++)
            padre[i]=-1;

        boolean[] visitado=new boolean[this.vertices.size()];
        
        dfs(v, padre, ordenBusqueda, visitado);
        
        return new Arbol(v, padre, ordenBusqueda);
    
    }
    
    /** Helper del método recursivo
     * 
     * @param v 
     * @param padre
     * @param ordenBusqueda
     * @param visitado 
     */
    private void dfs(int v, int padre[], List<Integer> ordenBusqueda, boolean[] visitado)
    {
        ordenBusqueda.add(v);
        visitado[v]=true;
        for(int i: vecinos.get(v))
        {
            if(!visitado[i])
            {
                padre[i]=v;
                dfs(i, padre,ordenBusqueda,visitado);
            }
        }
    }
    

    public Arbol bfs(int v)
    {
        List<Integer> ordenBusqueda=new ArrayList<>();
        int[] padre=new int[this.vertices.size()];

        /* inicializacin de los padres a -1*/
        for(int i=0;i<padre.length;i++)
            padre[i]=-1;

        boolean[] visitado=new boolean[this.vertices.size()];
        
        
        LinkedList<Integer>cola=new LinkedList<>();
        
        cola.offer(v);
        visitado[v]=true;
        
        
        while(!cola.isEmpty())
        {
            int u=cola.poll();
            ordenBusqueda.add(u);
            for(int w:this.vecinos.get(u))
            {
                if(!visitado[w])
                {
                    cola.offer(w);
                    padre[w]=u;
                    visitado[w]=true;
                }
            }
        }
        
        return new Arbol(v, padre, ordenBusqueda);
    
    }
    
    
    
    /** Clase para modelar una arista.
     * 
     * Recordemos que ua clase interna puede ser estática.
     * - Las clases internas estáticas sólo pueden acceder a los miembros estáticos dela clase contenedora.
     * - Se pueden instanciar miembros de la clase interna sin que exista un miembro de la clase contenedora.
     */
    public static class Arista
    {
        public int u; //Vertice inicial dela arista
        public int v; //Vertice final de la arista
        
        public Arista(int u, int v)
        {
            this.u=u;
            this.v=v;
        }
        
        /* FIN DE LA CLASE INTERNA ARISTA*/
    }
    
    
    public class Arbol
    {
        private int raiz;
        private int[] padre;
        private List<Integer>ordenBusqueda;
    
        
        public Arbol(int raiz, int[] padre, List<Integer>ordenBusqueda)
        {
            this.raiz=raiz;
            this.padre=padre;
            this.ordenBusqueda=ordenBusqueda;
        }
        
        public Arbol(int raiz, int[]padre)
        {
            this.raiz=raiz;
            this.padre=padre;
        }
        
        public int getRaiz()
        {
            return this.raiz;
        }
        
        public int getPadre(int v)
        {
            return padre[v];
        }
        
        public List<Integer>getOrdenBusqueda()
        {
            return this.ordenBusqueda;
        }
        
        public int getNumeroVerticesENcontrados()
        {
            return this.ordenBusqueda.size();
        }
        
        public List<V>getCamino(int index)
        {
            ArrayList<V>camino=new ArrayList<>();
            
            do
            {
                camino.add(vertices.get(index));
                index=this.padre[index];
            }
            while(index!=-1);
        
            return camino;
        }
                
        public void imprimeCamino(int indice)
        {
            List<V> camino=this.getCamino(indice);
            System.out.print("Un camino desde "+ vertices.get(raiz) + " hasta " + vertices.get(indice)+": ");
            for(int i=camino.size()-1;i>=0;i--)
                System.out.println(camino.get(i)+" ");
        }
        
        public void imprimeArbol()
        {
            System.out.println("Raiz es :" + vertices.get(raiz));
            System.out.print("Aristas");
            for(int i=0;i<padre.length;i++)
            {
                if(padre[i]!=-1)
                    System.out.print("("+ vertices.get(padre[i])+", "+vertices.get(i)+")");
            }
        }
        /* FIN DE LA CLASE INTERNA ARBOL*/
    }
    
}
