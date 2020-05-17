package mi.paquete.juego.pantallas.escenarios.auxiliar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.List;

import mi.paquete.juego.Juego2D;
import mi.paquete.juego.Partida;
import mi.paquete.juego.modelo.Mundo;


/** Clase para la representacion y gestion de las partidas almacenadas.
 *
 * Soporta la representacion de las partidas y se encarga de gestionar la seleccion de Slots.
 *
 */
public class TablaPartidas extends Table
{
    
    private int partidaSelec= Juego2D.PARTIDA_NO_SELECCINADA;
    private final ImageButton[] selectores;
    
    /** Crea un tabla para la seleccion de las partidas del juego.
     *
     *
     * @param partidas Partida[] lista de partidas a mostrar
     * @param mostrarTodas boolean indica si hay que habilitar la selleccion de todas(true)
     *  o sólo se permite la seleccion del asque tienen conteido(false)
     */
    public TablaPartidas(List<Partida> partidas, boolean mostrarTodas)
    {
        this.selectores=new ImageButton[partidas.size()];
        
        for(int x=0;x<this.selectores.length;x++)
        {
            if(x%2==0)
                this.row();
            this.selectores[x]=new ImageButton(Mundo.SKIN_PROPIO.get("Estilo boton pequeno seleccion", ImageButton.ImageButtonStyle.class));
            
            //desabilitamos el boton en caso de que el slot esté vacío
            if(!mostrarTodas && partidas.get(x)==null)
            {
                Gdx.app.log("SLOT "+x, "Boton desabilitado");
                this.selectores[x].setDisabled(true);
                this.selectores[x].setVisible(false);
            }
            //Nombrado y oyente de evento
            this.selectores[x].setName("Selector"+x);
            this.selectores[x].addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    ImageButton btPulsado = (ImageButton)event.getTarget();
                    
                    for(int i=0;i<selectores.length;i++)
                    {
                        if(btPulsado.getName().equalsIgnoreCase(selectores[i].getName()))
                        {
                            btPulsado.setChecked(true);
                            partidaSelec=i;
                        }
                        else
                            selectores[i].setChecked(false);
                    }
                    
                }
            });
            
            this.add(this.selectores[x]).right();
            this.add(new TablaPartida(partidas.get(x))).left();
        }
    }
    
    
    public int getPartidaSeleccionada()
    {
        return this.partidaSelec;
    }
    
    
    /** Representacin individual de una partida
     *
     * La representacion consiste en mostrar el nombre del heroe, y el nivel en una fila
     * En la fila siguiente se muestra el ordinal y  nombre de la mazmorra actual.
     *
     * Todo: conseguir igualar las alturas para que ocupen lo mismo las partidas ocupadas que las vacias.
     */
    public static class TablaPartida extends Table
    {
     
        private final Label etqNombreJugador=new Label(null, Mundo.SKIN_PROPIO.get("Estilo etiqueta partida nombre jugador", Label.LabelStyle.class));
        private final Label etqNivelJugador=new Label(null, Mundo.SKIN_PROPIO.get("Estilo etiqueta partida nivel jugador", Label.LabelStyle.class));
        private final Label etqNombreMazmorra=new Label(null, Mundo.SKIN_PROPIO.get("Estilo etiqueta simple", Label.LabelStyle.class));
        
        
        public TablaPartida(Partida partida)
        {
            this.pad(5.0f);
            this.add(etqNombreJugador).left();
            this.add(etqNivelJugador).right();
            this.row();
            this.add(etqNombreMazmorra).colspan(2);
            if(null!=partida)
            {
                etqNombreJugador.setText(partida.getNombrePersonaje());
                etqNivelJugador.setText("Nivel:" + partida.getNivel());
                etqNombreMazmorra.setText(Mundo.getInstance().getMapaMazmoras().get(partida.getRefMazmorra()).nombre);
            }
            else
                etqNombreJugador.setText("ESPACIO VACIO");
        }
    }
}
