package mi.paquete.juego.pantallas.escenarios.auxiliar;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.List;

import mi.paquete.juego.Juego2D;
import mi.paquete.juego.Partida;
import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.pantallas.escenarios.StageUIIntro;

public class DialogoNuevaPartida extends Dialog
{
    private Label etqError;
    private TextField txtNombre;
    private final List<Partida> partidas;
    private TextButton btAlAtaque;
    private TablaPartidas selectorPartidas;
    
    public DialogoNuevaPartida(List<Partida> partidas)
    {
        super("NUEVA PARTIDA", Mundo.SKIN_PROPIO.get("Estilo dialogo", Window.WindowStyle.class));
    
        this.partidas=partidas;
        this.padTop(20);
        this.padLeft(20);
        this.padRight(20);
        creaInterfaz();
        this.pack();
    }
    
    
 
    public void creaInterfaz()
    {
        Table tabla=this.getContentTable();
        
        //Crear el cuadro de texto y la etiqueta
        tabla.padTop(20);
        tabla.row();
        tabla.add(new Label("Indica tu nombre, heroe!", Mundo.SKIN_PROPIO.get("Estilo etiqueta ventana", Label.LabelStyle.class)));
        tabla.row();
        this.txtNombre=new TextField(null, Mundo.SKIN_PROPIO.get("Estilo cuadro texto simple", TextField.TextFieldStyle.class));
        tabla.add(this.txtNombre).fill();
        tabla.row();
        tabla.add(new Label("Selecciona un slot para guardar tu progreso", Mundo.SKIN_PROPIO.get("Estilo etiqueta ventana", Label.LabelStyle.class)));
        tabla.row();
        this.selectorPartidas=new TablaPartidas(partidas, true);
        tabla.add(this.selectorPartidas);
        tabla.row();
        this.etqError=new Label(null,Mundo.SKIN_PROPIO.get("Estilo etiqueta ventana error", Label.LabelStyle.class));
        tabla.add(this.etqError);
        //Añadimos los botones
        tabla.row();
        
        Table tablaBotones=new Table();
        btAlAtaque=new TextButton("Al ataque!", Mundo.SKIN_PROPIO.get("Estilo boton grande", TextButton.TextButtonStyle.class));
        btAlAtaque.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                String nombreJugador=txtNombre.getText();
                if(nombreJugador.matches("^\\s*$") || nombreJugador.length()>30 )
                {
                    //Mostrar error de nombre incorrecto
                    etqError.setText("Falta nombre o mayor de 30 caracteres");
                    return;
                }
                //Comprobacion de partida
                int numeroSlot=selectorPartidas.getPartidaSeleccionada();
                if(numeroSlot== Juego2D.PARTIDA_NO_SELECCINADA)
                {
                    etqError.setText("Selecciona un hueco de almacenamiento");
                    return;
                }
                
                //Estabecer los datos de la partida
                cierraDialogo();
            }
        });
        tablaBotones.add(btAlAtaque);
        TextButton btSalir=new TextButton("Cancelar", Mundo.SKIN_PROPIO.get("Estilo boton grande", TextButton.TextButtonStyle.class));
        btSalir.addListener( new ClickListener()
        {
    
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
               txtNombre.setText(null);
               cierraDialogo();
            }
        });
        tablaBotones.add(btSalir);
        tabla.add(tablaBotones);
        //Situamos el foco en el Cuadro de texto
    }
    
    
    private void cierraDialogo()
    {
        this.hide();
        
        if(this.txtNombre.getText().isEmpty())
            ((StageUIIntro)getStage()).estableceDatosNuevaPartida(null, Juego2D.PARTIDA_NO_SELECCINADA);
        else
            ((StageUIIntro)getStage()).estableceDatosNuevaPartida(this.txtNombre.getText(), this.selectorPartidas.getPartidaSeleccionada());
    }
}