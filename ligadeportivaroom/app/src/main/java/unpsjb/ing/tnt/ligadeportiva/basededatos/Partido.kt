package unpsjb.ing.tnt.ligadeportiva.basededatos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_partido")
data class Partido(
    @PrimaryKey(autoGenerate = true)
    var partidoId: Int = 0,

    @ColumnInfo(name = "equipoA_nombre")
    var equipoA_nombre:String,
    @ColumnInfo(name = "equipoA_marcador")
    var equipoA_marcador: Int,

    @ColumnInfo(name = "equipoB_nombre")
    var equipoB_nombre:String,
    @ColumnInfo(name = "equipoB_marcador")
    var equipoB_marcador: Int

)