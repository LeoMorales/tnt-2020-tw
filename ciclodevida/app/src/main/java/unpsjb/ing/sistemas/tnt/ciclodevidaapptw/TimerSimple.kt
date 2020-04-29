package unpsjb.ing.sistemas.tnt.ciclodevidaapptw

import android.os.Handler
import timber.log.Timber

// TODO #6: Crear un Timer
class TimerSimple {

    var contadorSegundos = 0

    /**
     * [Handler] es una clase destinada a procesar una cola de mensajes
     */
    private var handler = Handler()
    private lateinit var runnable: Runnable


    fun startTimer() {
        runnable = Runnable {
            contadorSegundos++
            Timber.i("Timer valor : $contadorSegundos")
            handler.postDelayed(runnable, 1000)
        }

        handler.postDelayed(runnable, 1000)

    }

    fun stopTimer() {
        handler.removeCallbacks(runnable)
    }
}