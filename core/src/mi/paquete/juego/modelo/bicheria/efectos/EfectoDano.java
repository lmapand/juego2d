package mi.paquete.juego.modelo.bicheria.efectos;


import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import mi.paquete.juego.modelo.bicheria.ElementoJuego;
import mi.paquete.juego.modelo.bicheria.EnemigoGenerico;
import mi.paquete.juego.modelo.bicheria.Fulano;
import mi.paquete.juego.modelo.bicheria.SerFisico;
import mi.paquete.juego.modelo.bicheria.combate.Atacable;
import mi.paquete.juego.modelo.bicheria.combate.Atacante;
import mi.paquete.juego.pantallas.PantallaAccion;

/** El efecto daño implica que un elemento del juego pierde puntos de vida.
 *
 */
public class EfectoDano extends Efecto
{
    
    private static final float RETARDO_EJECUCIOM= 0.0f;
    
    //Lista de los eleemntos que reciben daño
    private final List<Atacable> atacados;
    //Quien realiza el daño
    private final Atacante atacante;
    
    //Cantidad de daño recibido por los enemigos...
    private final int danoInflingido;
    
    public EfectoDano(Atacante atacante, List<Atacable> atacados, int danoInflingido, PantallaAccion.RenderizadorAccion entorno)
    {
        super( EfectoDano.RETARDO_EJECUCIOM);
        this.entorno=entorno;
        this.atacados=atacados;
        this.atacante=atacante;
        this.danoInflingido=danoInflingido;
    }
    
    /** Aplicar el daño a todas las criaturas que sea necesario.
     *
     * @param delta tiempo transcurrido. No utilizado para este Efecto
     */
    @Override
    public List<Object> ejecuta(float delta)
    {
        
            List<Object>resultado=new ArrayList<>();
        
            Gdx.app.log("EFECTO DANO", "Ejecutando efecto sobre" + this.atacados.size()+" jichos("+this.danoInflingido+")");
            int cont=0;
            //En este caso, como el daño se evalua antes que el dibujado, aprovechamos el bucle para establecer las coordenadas
            for(Atacable victima: this.atacados)
            {
                //Aplicar dano y efecto
                victima.setDano(danoInflingido);
                resultado.add(new EfectoTextoDano( ((SerFisico)victima).getPosX(),
                                                                            ((SerFisico)victima).getPosY(),
                                                                            entorno,
                                                                            String.valueOf(danoInflingido))
                                                                            );
                        
                //Si el atacado ha muerto
                if( ((SerFisico)victima).estaMuerto())
                {
                        //Pasar a lista de eliminado
                    this.entorno.getElementosEliminar().add((ElementoJuego)victima);
                    
                    //Caso de muerte de un bichaco
                    if(victima instanceof EnemigoGenerico)
                            resultado.add(new EfectoTemporalCadaver(((SerFisico)victima).getPosX(),
                                                                                    ((SerFisico)victima).getPosY(),
                                                                                    entorno,
                                                                                    ((EnemigoGenerico)victima).getTexturaCadaver()));
                    else if (victima instanceof Fulano)
                    {
                        //Todo: HAN MATADO AL FULANO!!!!! -> Fin de partida
                        resultado.add(new EfectoTemporalCadaver( ((SerFisico)victima).getPosX(),
                                ((SerFisico)victima).getPosY(),
                                entorno,
                                ((Fulano)victima).getTexturaCadaver()
                        ));
                        
                        resultado.add(new EfectoFinPartidaProtaMuerto(this.entorno));

                    }
                    
                    
                    //Si ademas el atacante es el Prota...
                    if( victima instanceof EnemigoGenerico && atacante instanceof Fulano)
                    {
                        //Dar XP y mostrar animacion
                        ((Fulano) atacante).anhadeXP(((EnemigoGenerico) victima).getRefBicho().xp);
                        resultado.add(new EfectoTextoXP( ((SerFisico)victima).getPosX(),
                                ((SerFisico)victima).getPosY(),
                                entorno,
                                ((EnemigoGenerico) victima).getRefBicho().xp)
                        );
                        
                        
                    }
                }
            }
            this.ejecutado = true;
            this.marcaParaElimiacion();
            
            return resultado;
    }
    
  
}
