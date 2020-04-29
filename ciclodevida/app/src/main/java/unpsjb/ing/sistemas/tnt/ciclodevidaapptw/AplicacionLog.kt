package unpsjb.ing.sistemas.tnt.ciclodevidaapptw

import android.app.Application
import timber.log.Timber

// TODO #2: Crear la clase Aplicación
class AplicacionLog : Application(){

    // TODO #4: Inicializar Timber (sobreescribir el onCreate) --> Ctrl + 'o' (Override methods)
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}