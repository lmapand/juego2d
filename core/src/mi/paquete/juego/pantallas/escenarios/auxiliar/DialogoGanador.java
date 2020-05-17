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

public class DialogoGanador extends Dialog
{
    
    private final PantallaAccion.RenderizadorAccion entorno;
    
    public DialogoGanador(PantallaAccion.RenderizadorAccion entorno)
    {
        super("HAS ENCONTRADO LA EMPANADA", Mundo.SKIN_PROPIO.get("Estilo dialogo", WindowStyle.class));
       
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
        tabla.add(new Label("Lo has conseguido, "+entorno.getProta().getNombre()+"!!", Mundo.SKIN_PROPIO.get("Estilo etiqueta ventana", Label.LabelStyle.class)));
        tabla.row();
        tabla.add(new Label("La empanada es tuya, y ahora podrás conseguir el titulo que tanto buscabas. ", Mundo.SKIN_PROPIO.get("Estilo etiqueta ventana", Label.LabelStyle.class)));
        tabla.row();
        tabla.add(new Label("Tus hazanas serán recordadas durante los proximos 10 minutos. ", Mundo.SKIN_PROPIO.get("Estilo etiqueta ventana", Label.LabelStyle.class)));
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
       tabla.add(tablaBotones);
    }
}
