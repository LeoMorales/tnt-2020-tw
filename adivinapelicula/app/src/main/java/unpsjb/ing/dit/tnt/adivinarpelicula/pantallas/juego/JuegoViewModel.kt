package unpsjb.ing.dit.tnt.adivinarpelicula.pantallas.juego

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JuegoViewModel: ViewModel() {

    val titulo = MutableLiveData<String>("Película")

    // Película actual
    private var _pelicula = MutableLiveData<String>("")
    val pelicula: LiveData<String>
        get() = _pelicula

    // Puntaje actual
    private  var _puntaje = MutableLiveData<Int>(0)
    val puntaje: LiveData<Int>
        get() = _puntaje

    // Puntaje actual
    private  var _eventoJuegoFinalizado = MutableLiveData<Boolean>(false)
    val eventoJuegoFinalizado: LiveData<Boolean>
        get() = _eventoJuegoFinalizado

    // Lista de películas
    private lateinit var peliculasList: MutableList<String>

    /**
     * Reiniciar la lista de películas y ordenarlas de forma aleatória
     */
    private fun resetList() {
        peliculasList = mutableListOf(
            "El padrino",
            "El rey León",
            "El día de la marmota",
            "La llamada",
            "El señor de los anillos: El retorno del rey",
            "El club de la pelea",
            "Matrix",
            "La vida es bella",
            "El silencio de los inocentes",
            "Volver al futuro",
            "Corazón valiente",
            "Una mente brillante",
            "Casino",
            "El secreto de sus ojos"
        )
        peliculasList.shuffle()
    }

    init {

        //_pelicula.value = ""
        //_puntaje.value = 0
        resetList()
        siguientePelicula()
        Log.i("JuegoViewModel", "JuegoViewModel creado!")
    }
    /**
     * Pasar a la prox película
     */
    private fun siguientePelicula() {
        if (!peliculasList.isEmpty()) {
            //Seleccionar y remover una película de la lista
            _pelicula.value = peliculasList.removeAt(0)
        } else {
            finalizoElJuego()

        }
    }

    fun finalizoElJuego() {
        _eventoJuegoFinalizado.value = true
    }

    /** Metodos para cuando se presionan los botones **/
    fun cuandoSaltea() {
        _puntaje.value = puntaje.value?.minus(1)
        siguientePelicula()
    }

    fun cuandoEsCorrecta() {
        _puntaje.value = _puntaje.value?.plus(1)
        siguientePelicula()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("JuegoViewModel", "JuegoViewModel destruido!")
    }

    fun seCompletoElJuego() {
        _eventoJuegoFinalizado.value = false
    }
}