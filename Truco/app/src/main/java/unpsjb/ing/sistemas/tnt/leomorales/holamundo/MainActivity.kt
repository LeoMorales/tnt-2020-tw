package unpsjb.ing.sistemas.tnt.leomorales.holamundo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import unpsjb.ing.sistemas.tnt.leomorales.holamundo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var vista: ActivityMainBinding
    private var puntaje: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        //tantosImage = findViewById(R.id.tantosImageView)
        //tantosImage.setImageResource(R.drawable.ic_tantos_1)
        vista = ActivityMainBinding.inflate(layoutInflater)
        val view = vista.root
        setContentView(view)

        // contenido inicial de la vista:
        vista.encabezadoTextView.text = "Puntaje"
        vista.tantosImageView.setImageResource(R.drawable.ic_tantos_vacio)

        // conectar el evento de click:
        vista.unPuntoMasButton.setOnClickListener { responder_click() }

    }

    private fun responder_click() {
        // aumenta el tanteador...

        //vista.encabezadoTextView.text = "Hice click!"
        puntaje = if ( puntaje == 5 ) 0 else puntaje + 1

        var resursoImg = when (puntaje){
            1 -> R.drawable.ic_tantos_1
            2 -> R.drawable.ic_tantos_2
            3 -> R.drawable.ic_tantos_3
            4 -> R.drawable.ic_tantos_4
            5 -> R.drawable.ic_tantos_5
            else -> R.drawable.ic_tantos_vacio
        }

        vista.tantosImageView.setImageResource(resursoImg)
        
    }
}
