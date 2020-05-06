package unpsjb.ing.dit.tnt.adivinarpelicula.pantallas.puntaje

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PuntajeViewModel(puntajeFinal: Int) : ViewModel() {
    // El puntaje final
    var score = puntajeFinal

    // Puntaje actual
    private  var _eventoJugarDeNuevo = MutableLiveData<Boolean>(false)
    val eventoJugarDeNuevo: LiveData<Boolean>
        get() = _eventoJugarDeNuevo

    init {
        Log.i("ScoreViewModel", "Puntaje final: $puntajeFinal")
    }

    fun onJugarDeNuevo(){
        _eventoJugarDeNuevo.value = true
    }
}