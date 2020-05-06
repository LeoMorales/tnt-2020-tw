package unpsjb.ing.dit.tnt.adivinarpelicula.pantallas.juego

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JuegoViewModel: ViewModel() {

    val titulo = MutableLiveData<String>("Película")

    // Película actual
    var pelicula = ""
    // Puntaje actual
    var puntaje = 0
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
            pelicula = peliculasList.removeAt(0)
        }
        // de alguna forma debemos actualizar la película qu ese está mostrando y el puntaje
    }

    /** Metodos para cuando se presionan los botones **/
    fun cuandoSaltea() {
        puntaje--
        siguientePelicula()
    }

    fun cuandoEsCorrecta() {
        puntaje++
        siguientePelicula()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("JuegoViewModel", "JuegoViewModel destruido!")
    }
}