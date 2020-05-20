package unpsjb.ing.tnt.ligadeportiva.basededatos

import androidx.lifecycle.LiveData

class RepositorioDePartidos(private val partidoDAO: PartidoDAO) {

    // LiveData observada va a notificar a sus observadores cuando los datos cambien.
    val todosLosPartidos: LiveData<List<Partido>> = partidoDAO.getPartidos()

    // La inserción se realiza en un hilo que no sea UI, ya que sino la aplicación se bloqueará. Para
    // informar a los métodos de llamada debemos realizar la inserción en una función suspend.
    // De esta manera, Room garantiza que no se realicen operaciones de larga ejecución
    // en el hilo principal, bloqueando la interfaz de usuario.
    suspend fun insertar(partido: Partido){
        partidoDAO.insertar(partido)
    }

}