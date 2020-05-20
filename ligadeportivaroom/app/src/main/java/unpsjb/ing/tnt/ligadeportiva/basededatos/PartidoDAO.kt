package unpsjb.ing.tnt.ligadeportiva.basededatos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface  PartidoDAO {
    // LiveData como ya hemos visto anteriormente va a contener nuestros
    // datos y permitir que estos sean observados dentro de un ciclo de vida dado.
    // Siempre guarda / almacena en caché la última versión de los datos.
    // Notifica a sus observadores activos cuando los datos han cambiado.
    // Dado que estamos obteniendo el contenido completo de la base de datos,
    // se nos notifica cada vez que algun dato haya cambiado.
    @Query("SELECT * from tabla_partido ORDER BY partidoId ASC")
    fun getPartidos(): LiveData<List<Partido>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(partido: Partido)

    @Query("DELETE FROM tabla_partido")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(partidoId) from tabla_partido")
    suspend fun cantidadDePartidos(): Int
}

/*
* Acerca de las suspend functions:
* A suspending function is just a regular Kotlin function
* with an additional suspend modifier which indicates that
* the function can suspend the execution of a coroutine.
* Suspending functions can invoke any other regular functions,
* but to actually suspend the execution, it has to be another
* suspending function.
*
* https://sourcediving.com/kotlin-coroutines-in-android-e2d5bb02c275
* */