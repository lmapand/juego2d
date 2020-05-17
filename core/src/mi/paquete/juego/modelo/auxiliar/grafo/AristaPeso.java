/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mi.paquete.juego.modelo.auxiliar.grafo;

/**
 *
 * @author luis
 */
public class AristaPeso extends AbstractGrafo.Arista implements Comparable<AristaPeso>
{
    public int peso;
    
    public AristaPeso(int u, int v, int peso)
    {
        super(u,v);
        this.peso = peso;
    }

    @Override
    public int compareTo(AristaPeso acp) 
    {
        return this.peso - acp.peso;
    }
    
}
