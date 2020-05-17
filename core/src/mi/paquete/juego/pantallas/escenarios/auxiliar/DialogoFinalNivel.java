package mi.paquete.juego.pantallas.escenarios.auxiliar;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.pantallas.PantallaAccion;

/** Dialog para el proceso de la pausa.
 *
 * En el momento de hacer la pausa se puede realizar una de las siguientes acciones:
 * - Retomar la partida (salir del estado de pausa)
 * - Salir al menu principal
 */

public class DialogoFinalNivel extends Dialog
{
    
    private final PantallaAccion.RenderizadorAccion entorno;
    
    public DialogoFinalNivel(PantallaAccion.RenderizadorAccion entorno)
    {
        super("HAS ENCONTRADO LA SALIDA", Mundo.SKIN_PROPIO.get("Estilo dialogo", WindowStyle.class));
       
        this.entorno=entorno;
        this.padTop(20);
        this.padLeft(10);
        this.padRight(10);
        creaInterfaz();
        this.pack();
    }
    
    private void creaInterfaz()
    {
        Table tabla=this.getContentTable();
    
        //Crear las etiquetas
        tabla.padTop(5);
        tabla.add(new Label("Muy bien, "+entorno.getProta().getNombre()+"!!", Mundo.SKIN_PROPIO.get("Estilo etiqueta ventana", Label.LabelStyle.class)));
        tabla.row();
        tabla.add(new Label("Has encontrado la llave y alcanzado la salida; prepárate para el siguiente nivel!!", Mundo.SKIN_PROPIO.get("Estilo etiqueta ventana", Label.LabelStyle.class)));
        tabla.row();
        
        //Crear los botones
        Table tablaBotones=new Table();
        Button btSalir=new TextButton("SALIR", Mundo.SKIN_PROPIO.get("Estilo boton grande", TextButton.TextButtonStyle.class));
        btSalir.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                hide();
                entorno.vuelveAInicio();
            }
        });
        tablaBotones.add(btSalir);
        TextButton btContinuar=new TextButton("ADELANTE!", Mundo.SKIN_PROPIO.get("Estilo boton grande", TextButton.TextButtonStyle.class));
        btContinuar.addListener( new ClickListener()
        {
        
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                hide();
                entorno.reiniciaPartida();
                
            }
        });
        tablaBotones.add(btContinuar);
        tabla.add(tablaBotones);
    }
    
    
    
    
}
