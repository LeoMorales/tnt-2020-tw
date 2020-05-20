package unpsjb.ing.tnt.ligadeportiva.basededatos

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Anotar la clase para convertirla en una Database Room
// con una Tabla (entity) de la clase Partido
@Database(entities = arrayOf(Partido::class), version = 1, exportSchema = false)
public abstract class PartidoRoomDatabase : RoomDatabase() {

    abstract fun partidoDao(): PartidoDAO

    companion object {
        // Singleton previene multiples instancias de
        // la base de datos abriendose al mismo tiempo
        @Volatile
        private var INSTANCIA: PartidoRoomDatabase? = null

        fun obtenerDatabase(
            context: Context,
            viewModelScope: CoroutineScope
        ): PartidoRoomDatabase {
            val instanciaTemporal = INSTANCIA
            if (instanciaTemporal != null) {
                return instanciaTemporal
            }
            synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    PartidoRoomDatabase::class.java,
                    "partido_database"
                )
                    .addCallback(PartidoDatabaseCallback(viewModelScope))
                    .build()
                INSTANCIA = instancia
                return instancia
            }
        }

        private class PartidoDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /* importante: destacar el scope como parametro */

            /**
             * Lo que hacemos en esta clase es sobreescribir el método onOpen
             * para cargar la base de datos.
             *
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCIA?.let { database ->
                    scope.launch {
                        cargarBaseDeDatos(database.partidoDao())
                    }
                }
            }

            suspend fun cargarBaseDeDatos(partidoDAO: PartidoDAO) {

                if(partidoDAO.cantidadDePartidos() == 0) {


                    Log.i("PartidoRoomDatabase", "cargarBaseDeDatos")
                    // Borrar el contenido de la base
                    partidoDAO.borrarTodos()

                    // Agregar partidos de ejemplo
                    var partido = Partido(
                        1,  // "LIGA-UNIV-01"
                        "El Deportivo",
                        2,
                        "Club Atletico",
                        3
                    )
                    partidoDAO.insertar(partido)

                    partidoDAO.insertar(
                        Partido(
                            2,  // "LIGA-UNIV-01"
                            "Sacrificio",
                            2,
                            "UNPSJB Trelew",
                            2
                        )
                    )

                    partidoDAO.insertar(
                        Partido(
                            equipoA_nombre = "Equipo Mentira",
                            equipoA_marcador = 2,
                            equipoB_nombre = "Equipo Mentira 2",
                            equipoB_marcador = 4
                        )
                    )
                } // fin comprobacion cantidad

            }
        }

    }
}