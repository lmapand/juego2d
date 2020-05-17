package mi.paquete.juego.pantallas;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

import mi.paquete.juego.Juego2D;

public abstract class PantallaJuego implements Screen
{
    
    //La cámara a utilizar
    protected OrthographicCamera camara =new OrthographicCamera();
    
    //Declarar el Renderer personalizado que va en esta pantalla
    protected Juego2D juego;
    protected final Renderizador renderizador;
    
    
    public PantallaJuego(Juego2D juego)
    {
        this.juego=juego;
        this.camara=new OrthographicCamera();
        this.renderizador=creaRenderizador();
    }
    
    abstract protected PantallaJuego.Renderizador creaRenderizador();
    
    
    @Override
    public void show()
    {
    
    }
    
    @Override
    public void render(float delta)
    {
        this.renderizador.render(delta);
    }
    
    @Override
    public void resize(int width, int height)
    {
        //Aqui el comportamiento tiene que ser común a todas
        // Especificaremos un comportamiento estandarizado
        this.renderizador.resize(width, height );
    }
    
    @Override
    public void pause()
    {
    }
    
    @Override
    public void resume()
    {
    
    }
    
    @Override
    public void hide()
    {
    
    }
    
    @Override
    public void dispose()
    {
        //Delegamos la eliminacion de recursos al renderizador
        this.renderizador.dispose();
    }
    
    public static abstract class Renderizador
    {
        abstract void render(float delta);
        abstract void resize(int width, int height);
        abstract void dispose();
    }
}
