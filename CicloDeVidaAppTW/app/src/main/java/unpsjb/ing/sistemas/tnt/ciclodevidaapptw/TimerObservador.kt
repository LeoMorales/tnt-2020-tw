package unpsjb.ing.sistemas.tnt.ciclodevidaapptw

import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import timber.log.Timber

// TODO #8: Crear un timer que observe el ciclo de vida del MainActivity
class TimerObservador (lifecycle: Lifecycle) : LifecycleObserver {
    var contadorSegundos = 0

    // TODO #9: En el constructor, registrar el timer como observador del ciclo de vida
    init {
        lifecycle.addObserver(this)
    }
    /**
     * [Handler] es una clase destinada a procesar una cola de mensajes
     */
    private var handler = Handler()
    private lateinit var runnable: Runnable


    // TODO #10: Responder a cambio de estado:
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startTimer() {
        runnable = Runnable {
            contadorSegundos++
            Timber.i("Timer OBSERVADOR valor: $contadorSegundos")
            handler.postDelayed(runnable, 1000)
        }

        handler.postDelayed(runnable, 1000)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopTimer() {
        handler.removeCallbacks(runnable)
    }
}