package mi.paquete.juego.modelo.bicheria.efectos;

import java.util.ArrayList;
import java.util.List;

import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.auxiliar.Analizador;
import mi.paquete.juego.modelo.bicheria.ElementoJuego;
import mi.paquete.juego.modelo.bicheria.EnemigoGenerico;
import mi.paquete.juego.modelo.bicheria.Fulano;
import mi.paquete.juego.modelo.bicheria.SerFisico;
import mi.paquete.juego.modelo.bicheria.combate.Atacable;
import mi.paquete.juego.modelo.bicheria.combate.Atacante;

public class EfectoAtaqueSuicida extends Efecto
{
    
    private final Atacante atacante;
    private final int dano;
    private final int radioAtaque;
    
    public EfectoAtaqueSuicida(Atacante atacante, int radioAtaque, int dano)
    {
        super(0.0f);
        this.atacante=atacante;
        this.dano=dano;
        this.radioAtaque=radioAtaque;
        
    }
    
    
    /** no necesitamos usar el temporizador, ya que es instantaneo
     *
     * @param delta tiempo trancurrido (no utilizado en este caso)
     */
    @Override
    public List<Object> ejecuta(float delta)
    {
    
        List<Object>resultados=new ArrayList<>();
        
        //Lista d eelementos que hay que crear
        List<ElementoJuego> cosasNuevas=new ArrayList<>();
        
        //El atacante muere y es eliminado: también generar un cadaver
        ((EnemigoGenerico)atacante).setDano(((EnemigoGenerico)atacante).getvidaMaxima());
        entorno.getElementosEliminar().add(((EnemigoGenerico)atacante));
        //Generamos un cadaver
        resultados.add(new EfectoTemporalCadaver( ((SerFisico)atacante).getPosX(),
                                                                   ((SerFisico)atacante).getPosY(),
                                                                   entorno,
                                                                  ((EnemigoGenerico)atacante).getTexturaCadaver()
                                                                  ));
        //Generamos un marcador de Puntos de vida perdidos
         resultados.add(new EfectoTextoDano( ((SerFisico)atacante).getPosX(),
                                                                ((SerFisico)atacante).getPosY(),
                                                                entorno,
                                                                String.valueOf( ((EnemigoGenerico)atacante).getvidaMaxima())
                                                                    ));
                
        
        //Ejecutar el sonido asociado a explosion Todo: crear otra entrada para este tipo de sonidos
        Mundo mundo= Mundo.getInstance();
        mundo.getMapaEfectosSonido().get("explosion globo").play();
        
        //Análiis de heridas, etc.
        for(ElementoJuego ej:entorno.getElementosJuego())
        {
            if(ej instanceof Atacable && !ej.equals(atacante) && Analizador.distancia( ((SerFisico)atacante).getPosX(),
                                                                                ((SerFisico)atacante).getPosY(),
                                                                                ((SerFisico) ej).getPosX(),
                                                                                ((SerFisico) ej).getPosY()) < radioAtaque)
            {
                //Establecer el daño
                ((Atacable)ej).setDano(dano);

                //Generar el efecto visual de daño
                resultados.add(new EfectoTextoDano( ((SerFisico)ej).getPosX(),
                    ((SerFisico)ej).getPosY(),
                    entorno,
                    String.valueOf(dano) ));
                
                //Si ha muerto... generar cadaver (y posible muerte de nuestro heroe)
                if( ((SerFisico)ej).estaMuerto())
                {
                    entorno.getElementosEliminar().add(ej);
                    
                    if( ej instanceof Fulano)
                    {
                        //Todo: HAN MATADO AL FULANO!!!!! -> Fin de partida
                        resultados.add(new EfectoTemporalCadaver( ((SerFisico)ej).getPosX(),
                                ((SerFisico)ej).getPosY(),
                                entorno,
                                ((Fulano)ej).getTexturaCadaver()
                        ));
                        
                        //Mostrar Dialog de fin de partida
                        resultados.add(new EfectoFinPartidaProtaMuerto(this.entorno));
                    }
                    else //Marcamos para eliminar y generamos un cadaver
                    {
                        resultados.add(new EfectoTemporalCadaver( ((SerFisico)ej).getPosX(),
                                ((SerFisico)ej).getPosY(),
                                entorno,
                                ((EnemigoGenerico)ej).getTexturaCadaver()
                        ));
                    }
                }
            }
        }
        
        //añadir todos los elementos nuevos a la lista
        
        this.ejecutado = true;
        this.marcaParaElimiacion();
        return resultados;
    }
}
