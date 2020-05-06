package unpsjb.ing.dit.tnt.adivinarpelicula.pantallas.puntaje

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PuntajeViewModelFactory (private val puntajeFinal: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PuntajeViewModel::class.java)) {
            return PuntajeViewModel(puntajeFinal) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}