package mi.paquete.juego.pantallas.escenarios.auxiliar;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.List;

import mi.paquete.juego.Juego2D;
import mi.paquete.juego.Partida;
import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.pantallas.escenarios.StageUIIntro;

public class DialogoCargarPartida extends Dialog
{
    
    private final List<Partida> partidas;
    private TablaPartidas selectorPartida;
    private Label etqError;
    
    
    public DialogoCargarPartida(List<Partida> partidas)
    {
        super("CARGAR PARTIDA", Mundo.SKIN_PROPIO.get("Estilo dialogo", Window.WindowStyle.class));
        this.partidas=partidas;
    
        this.padTop(20);
        this.padLeft(20);
        this.padRight(20);
        creaInterfaz();
        this.pack();
        
    }
    
    private void creaInterfaz()
    {
        Table tabla=this.getContentTable();
    
        //Crear el cuadro de texto y la etiqueta
        tabla.padTop(20);
        tabla.row();
        tabla.add(new Label("Selecciona la partida que quieres continuar", Mundo.SKIN_PROPIO.get("Estilo etiqueta ventana", Label.LabelStyle.class)));
        tabla.row();
        this.selectorPartida=new TablaPartidas(partidas, false);
        tabla.add(this.selectorPartida);
        tabla.row();
        this.etqError=new Label(null,Mundo.SKIN_PROPIO.get("Estilo etiqueta ventana error", Label.LabelStyle.class));
        tabla.add(this.etqError);
        //Añadimos los botones
        tabla.row();
    
        Table tablaBotones=new Table();
        TextButton btCargar=new TextButton("Al ataque!", Mundo.SKIN_PROPIO.get("Estilo boton grande", TextButton.TextButtonStyle.class));
        btCargar.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {

                int numeroSlot=selectorPartida.getPartidaSeleccionada();
                if(numeroSlot== Juego2D.PARTIDA_NO_SELECCINADA)
                {
                    etqError.setText("No se ha seleccionada una partida");
                    return;
                }
            
                //Estabecer los datos de la partida
                hide();
                ((StageUIIntro)getStage()).cargaPartida(numeroSlot);
                
                
            }
        });
        tablaBotones.add(btCargar);
        TextButton btSalir=new TextButton("Cancelar", Mundo.SKIN_PROPIO.get("Estilo boton grande", TextButton.TextButtonStyle.class));
        btSalir.addListener( new ClickListener()
        {
        
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                hide();
            }
        });
        tablaBotones.add(btSalir);
        tabla.add(tablaBotones);
    }
    
}
