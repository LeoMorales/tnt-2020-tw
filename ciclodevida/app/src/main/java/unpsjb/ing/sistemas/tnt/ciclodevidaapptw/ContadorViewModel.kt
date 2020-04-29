package unpsjb.ing.sistemas.tnt.ciclodevidaapptw

import androidx.lifecycle.ViewModel
import timber.log.Timber

class ContadorViewModel: ViewModel() {

    var contador: Int = 0

    init {
        Timber.i("Estoy constructor del ViewModel")
    }

    fun incrementarContador(){
        contador++
    }
}