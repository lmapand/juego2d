package mi.paquete.juego;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication
{
	
	private static final int VALOR_ANCHO_DEFACTO=1024;
	
	private Juego2D elJuego;
	
	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		config.useAccelerometer=false;
		config.useCompass=false;
		config.useWakelock=true; //Recordar, para que no pare en caso de inactividad
		
		elJuego=new Juego2D();
		
		initialize(elJuego, config);
	}
}
