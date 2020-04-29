package unpsjb.ing.sistemas.tnt.ciclodevidaapptw

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    lateinit var botonCompartir: Button
    lateinit var botonPrincipal: Button
    lateinit var botonSiguiente: Button
    lateinit var labelMensaje: TextView


    private lateinit var timer: TimerSimple
    private lateinit var timerObservador: TimerObservador

    //private var contador: Int = 0

    private lateinit var viewModel: ContadorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Log.i("MainActivity", "Estoy en el onCreate")
        // TODO #5: Usar Timber para logs:
        Timber.i("Estoy en onCreate")

        labelMensaje = findViewById(R.id.mensaje_textView)
        botonCompartir = findViewById(R.id.compartir_button)
        botonPrincipal = findViewById(R.id.principal_button)
        botonSiguiente = findViewById(R.id.siguiente_button)
        botonCompartir.setOnClickListener { compartir() }

        // TODO #7: Instanciar un Timer:
        timer = TimerSimple()
        //botonPrincipal.setOnClickListener { arrancar() }

        // TODO #11: Instanciar un timer observador del ciclo de vida:
        timerObservador = TimerObservador(this.lifecycle)

//        if (savedInstanceState != null){
//            contador = savedInstanceState.getInt("contador")
//            Timber.i("Recupere el valor del contador!")
//        }

        botonPrincipal.setOnClickListener { incrementarContador() }

        //TODO #15: Vamos a hacer que el UI Controller conozca al ViewModel:
        viewModel = ViewModelProviders.of(this).get(ContadorViewModel::class.java)

        mostrarContador()
        botonSiguiente.setOnClickListener { pasar_siguiente_actividad() }
    }

    private fun pasar_siguiente_actividad() {
        //val editText = findViewById<EditText>(R.id.editText)
        val message = viewModel.contador.toString()
        val intent = Intent(this, SegundaActivity::class.java)
            //.apply {
        intent.putExtra("CONTADOR_STR", message)
        //}
        startActivity(intent)
    }

    private fun incrementarContador() {
        //contador++
        viewModel.incrementarContador()
        mostrarContador()
    }

    private fun mostrarContador() {
        //mensaje_textView.text = contador.toString()
        mensaje_textView.text = viewModel.contador.toString()
    }

    private fun arrancar() {
        timer.startTimer()
    }

    private fun compartir() {
        labelMensaje.text = "COMPARTIR"
        val shareIntent = ShareCompat.IntentBuilder.from(this)
            .setText("Compartir mi app!")
            .setType("text/plain")
            .intent
        try {
            startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "ActivityNotFoundException",
                Toast.LENGTH_LONG).show()
        }

    }

    override fun onResume() {
        super.onResume()
        Timber.i("Estoy en el onResume")

    }

    override fun onPause() {
        super.onPause()
        Timber.i("Estoy en el onPause")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("Estoy en el onStart")
        timer.startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("Estoy en el onDestroy")

    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("Estoy en el onRestart")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("Estoy en el onStop")
        timer.stopTimer()
    }

/*    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("contador", contador)
        Timber.i("onSaveInstanceState, guardo el contador...")
    }*/
}
