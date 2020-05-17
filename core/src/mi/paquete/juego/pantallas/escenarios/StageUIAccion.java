package mi.paquete.juego.pantallas.escenarios;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.modelo.auxiliar.Analizador;
import mi.paquete.juego.modelo.bicheria.Fulano;

public class StageUIAccion extends Stage
{
    private final Label etqExp= new Label( "XP:", Mundo.SKIN_PROPIO.get("Estilo etiqueta simple", Label.LabelStyle.class));
    
    private final Label txtPuntosVida=new Label(null, Mundo.SKIN_PROPIO.get("Estilo etiqueta simple", Label.LabelStyle.class));
    private final Label txtMana=new Label(null, Mundo.SKIN_PROPIO.get("Estilo etiqueta simple", Label.LabelStyle.class));
    private final Label txtXP=new Label(null, Mundo.SKIN_PROPIO.get("Estilo etiqueta simple", Label.LabelStyle.class));
    
    private ProgressBar pbVida;
    private ProgressBar pbMana;
    private ProgressBar pbExp;
    
    private Touchpad tp;
    
    private final Image btPausa;
    private final Image btBolaFuego;
    private final Image btVareada;
    
    private final Skin skinLocal=new Skin();
    
    private Fulano prota;
    
    public StageUIAccion(int ancho, int alto, Fulano prota)
    {
        //super(new FitViewport(ancho, alto));
        //Todo eleiminar los 'numeros mágicos'
        super(new FitViewport(ancho*2,alto*2));
        
        this.prota=prota;
       
        /** Creacin de los botones*/
        this.btPausa =new Image(Mundo.SKIN_PROPIO.getDrawable("boton pausa"));
        this.btBolaFuego =new Image(Mundo.SKIN_PROPIO.getDrawable("boton bola fuego"));
        this.btVareada =new Image(Mundo.SKIN_PROPIO.getDrawable("boton vareada desactivado"));
        
        
        /* Creacion de la tabla:*/
        Table tabla=creaTablaGenerica();
        
        /* Ampliacion de la tabla para contener los controles en pantalla*/
        if(Gdx.app.getType()== Application.ApplicationType.Android)
            ampliaTablaAndroid(tabla);
        
        
        /* Colocacion de la tabla*/
        tabla.top().left();
        tabla.setFillParent(true);
        this.addActor(tabla);
    }
    
    /** Actualizacion por las bravas de los valores (mas rápido y mantenible).
     *
     * Se debe llamar antes del render del stage.
     * Se declara a nivel de paquete.
     *
     * //Todo: ajustar por separado los valors máximos, sólo cuando sea necesario (SI DA TIEMPO)
     */
    public void actualiza()
    {
        this.act();
        //Barra de vida...
        this.pbVida.setRange(0, this.prota.getvidaMaxima());
        this.pbVida.setValue(prota.getvidaActual());
        if(pbVida.getPercent()>0.66)
            this.pbVida.setStyle(Mundo.SKIN_PROPIO.get("Barra progreso vida bien", ProgressBar.ProgressBarStyle.class));
        else if(pbVida.getPercent()>0.33)
            this.pbVida.setStyle(Mundo.SKIN_PROPIO.get("Barra progreso vida regular", ProgressBar.ProgressBarStyle.class));
        else
            this.pbVida.setStyle(Mundo.SKIN_PROPIO.get("Barra progreso vida mal", ProgressBar.ProgressBarStyle.class));
        
        this.txtPuntosVida.setText( ""+prota.getvidaActual()+"/"+prota.getvidaMaxima());
        //Barra de maná
        this.pbMana.setRange(0, prota.getvidaMaxima());
        this.pbMana.setValue(prota.getManaActual());
    
        this.txtMana.setText( ""+prota.getManaActual()+"/"+prota.getManaMaximo());
        //BarraXP
        this.pbExp.setValue(prota.getXP());
        this.txtXP.setText( ""+prota.getXP());
    }
    
    
    private void procesaMovimiento(float padX, float padY )
    {
        //Caso en el que no hay que evaluar nada
        if(prota==null || prota.estaAtacando())
            return;
        
        //Valor por defecto para una distancia de menos del 60% del borde
        int accionDesplazamiento=Mundo.ACCION_PARADO;
        
        //Evaluacion del encaramiento resultante
        if(Analizador.distancia(0,0,padX,padY )>=0.6f)
            accionDesplazamiento=Analizador.encaramientoTouchPad(Analizador.anguloResultante(0,0,padX,padY));

        prota.setAccionDesplazamiento(accionDesplazamiento);
    }
    
    /** Crea la tabla para entornos sin teclado.
     *
     * Esta tabla incluye el TouchPad y los botones necesarios para el manejo del juego
     *
     * @return Table tabla de la UI
     */
    public void ampliaTablaAndroid(Table tabla)
    {
        //Añadimos boton de pausa
        this.btPausa.addListener(new ClickListener()
        {
        
        });
        
        tabla.add(this.btPausa).right().bottom();
    
       
        //Creacion del TouchPad
        Mundo mundo=Mundo.getInstance();
       
        this.tp=new Touchpad(10,Mundo.SKIN_PROPIO.get("touchpad", Touchpad.TouchpadStyle.class));
        this.tp.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                procesaMovimiento(tp.getKnobPercentX(), tp.getKnobPercentY());
            
            }
        });
        tabla.row().expandY();
        tabla.row();
        tabla.add(tp).colspan(3).bottom().left();
        tabla.add().colspan(7);

        //Botonera
        Table tablaBotonera=new Table();
        
        this.btBolaFuego.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                prota.iniciaAtaque(Input.Keys.SPACE);
            }
        });
        tablaBotonera.add(this.btBolaFuego).right();
        
        tablaBotonera.row().expandY();
        tablaBotonera.add(this.btVareada).right();
        
        tabla.add(tablaBotonera).right().bottom();
    }
    
    
    /** Crea la tabla genérica para entornos con teclado, donde no son necesarios botones ni TouchPad
     *
     * @return Table tabla de la UI
     */
    public Table creaTablaGenerica()
    {
        Table tabla=new Table();
    
        //Vida:
        Image imagen=new Image(Mundo.SKIN_PROPIO.getDrawable("imagen vida"));
        tabla.add(imagen);
        pbVida=new ProgressBar(0.0f, prota.getvidaMaxima(), 1.0f,false, Mundo.SKIN_PROPIO.get("Barra progreso vida bien", ProgressBar.ProgressBarStyle.class));
        pbVida.setValue(prota.getvidaActual());
        tabla.add(pbVida);
        tabla.add(txtPuntosVida).padRight(10);
    
        //Mana
        
        imagen=new Image(Mundo.SKIN_PROPIO.getDrawable("imagen mana"));
        tabla.add(imagen);
        pbMana=new ProgressBar(0.0f, prota.getManaMaximo(), 1.0f,false, Mundo.SKIN_PROPIO.get("Barra progreso mana", ProgressBar.ProgressBarStyle.class));
        pbMana.setValue(prota.getManaActual());
        tabla.add(pbMana);
        tabla.add(txtMana).padRight(10);
    
        //XP
        tabla.add(etqExp);
        pbExp = new ProgressBar(0, 100, 1, false, Mundo.SKIN_PROPIO.get("Barra progreso XP", ProgressBar.ProgressBarStyle.class));
        pbExp.setValue(prota.getXP());
        tabla.add(pbExp);
        tabla.add(txtXP);
        tabla.add().expandX();
        
        return tabla;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
   
    
    
    
}
