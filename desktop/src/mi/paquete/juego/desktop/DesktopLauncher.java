package mi.paquete.juego.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import mi.paquete.juego.Juego2D;

public class DesktopLauncher
{
	
	//Las propiedades de pantalla las vamos a especificar aqui
	
	
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title="SEÑORES DE LA EMPANADA";
		//Todo: ver qué configuración es más beneficioso para el desarollo común
		
		//config.width= Mundo.ANCHO_MUNDO;
		//config.height=Mundo.ALTO_MUNDO;
		Graphics.DisplayMode modoPantalla = LwjglApplicationConfiguration.getDesktopDisplayMode();
		
		config.width = modoPantalla.width;
		config.height = modoPantalla.height;
		config.addIcon("sdle_icono_48.png", Files.FileType.Internal);
		config.resizable=true;
		
		Juego2D elJuego=new Juego2D();
		new LwjglApplication( elJuego, config);
	}
}
