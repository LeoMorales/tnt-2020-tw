package unpsjb.ing.tnt.ligadeportiva

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import unpsjb.ing.tnt.ligadeportiva.basededatos.Partido
import unpsjb.ing.tnt.ligadeportiva.listado.PartidoListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var partidoViewModel: PartidoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = PartidoListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        partidoViewModel = ViewModelProvider(this).get(PartidoViewModel::class.java)

        partidoViewModel.todosLosPartidos
            .observe(
                this,
                Observer {
                        partidos ->
                            partidos?.let{ adapter.setPartidos(it) }
                        }
            )

        val fab = findViewById<FloatingActionButton>(R.id.boton_flotante)
        fab.setOnClickListener {
            val intent = Intent(
                this@MainActivity,
                NuevoPartidoActivity::class.java)
            startActivityForResult(intent, Companion.nuevoPartidoActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Companion.nuevoPartidoActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NuevoPartidoActivity.EXTRA_RESPUESTA_EQUIPO_1)?.let {
                val nuevo_partido = Partido(
                    equipoA_nombre = it,
                    equipoA_marcador = 0,
                    equipoB_nombre = "UNPSJB",
                    equipoB_marcador = 4
                )
                partidoViewModel.insert(nuevo_partido)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.vacio_no_guardado,
                Toast.LENGTH_LONG).show()
        }
    }

    //14:
    companion object {
        private const val nuevoPartidoActivityRequestCode = 1
    }

}
