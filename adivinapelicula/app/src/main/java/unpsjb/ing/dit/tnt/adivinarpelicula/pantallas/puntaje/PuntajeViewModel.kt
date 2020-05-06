package unpsjb.ing.dit.tnt.adivinarpelicula.pantallas.puntaje

import android.util.Log
import androidx.lifecycle.ViewModel

class PuntajeViewModel(puntajeFinal: Int) : ViewModel() {
    // El puntaje final
    var score = puntajeFinal
    init {
        Log.i("ScoreViewModel", "Puntaje final: $puntajeFinal")
    }
}