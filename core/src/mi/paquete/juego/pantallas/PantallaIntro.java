package mi.paquete.juego.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import mi.paquete.juego.Juego2D;
import mi.paquete.juego.modelo.Mundo;
import mi.paquete.juego.pantallas.escenarios.StageUIIntro;


/** Pantalla de Introduccion, que aparece al iniciar el juego,
 *
 * Esta pantalla muestra una descripción del objetivo del juego en forma de
 * conversiación en tre el maestro mago y el protagonista.
 *
 * La conversación se puede saltar, en cuyo caso aparecerán directamente los botones de 'NUeva Partida'
 * y 'Cargar Partida'
 *
 */

public class PantallaIntro extends PantallaJuego
{
    
    public PantallaIntro(Juego2D juego)
    {
        super(juego);
    }
    
    @Override
    protected Renderizador creaRenderizador()
    {
        return new RenderizadorIntro();
    }
    
    
    public  class RenderizadorIntro extends PantallaJuego.Renderizador
    {
        private final Stage ui;
        private SpriteBatch sb;
        //Creacion del Stage
        
        public RenderizadorIntro()
        {
            this.sb=new SpriteBatch();
            ui=new StageUIIntro(Mundo.ANCHO_MUNDO_DESKTOP, Mundo.ALTO_MUNDO_DESKTOP, juego, this.sb);
            Gdx.input.setInputProcessor(ui);
        }
        
        @Override
        void render(float delta)
        {
    
            Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            
            
            ui.getViewport().apply();
            ui.act();
            ui.draw();
        }
    
        @Override
        void resize(int width, int height)
        {
            ui.getViewport().update(width, height, true);
            
        }
    
        @Override
        void dispose()
        {
            ui.dispose();
        }
    }
}
