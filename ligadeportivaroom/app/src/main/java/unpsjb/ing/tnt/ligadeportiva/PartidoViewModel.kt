package unpsjb.ing.tnt.ligadeportiva

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import unpsjb.ing.tnt.ligadeportiva.basededatos.Partido
import unpsjb.ing.tnt.ligadeportiva.basededatos.PartidoRoomDatabase
import unpsjb.ing.tnt.ligadeportiva.basededatos.RepositorioDePartidos

class PartidoViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio: RepositorioDePartidos
    // Usar LiveData y almacenar lo que devuelve getPartidos tiene bastantes beneficios:
    // - Podemos atar un observador a los datos (en lugar de estar verificando cambios continuamente)
    // y solo actualizar la UI cuando los datos cambien.
    // - El Repositorio está totalmente separado de la UI mediante el ViewModel.
    val todosLosPartidos: LiveData<List<Partido>>

    init {
        val partidosDao = PartidoRoomDatabase
            .obtenerDatabase(application, viewModelScope).partidoDao()
        repositorio = RepositorioDePartidos(partidosDao)
        todosLosPartidos = repositorio.todosLosPartidos
    }

    /**
     *
     * Lanzar una nueva corrutina para insertar datos en una forma no-bloqueante
     */
    fun insert(partido: Partido) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.insertar(partido)
    }
}