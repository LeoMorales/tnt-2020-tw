package unpsjb.ing.sistemas.tnt.ciclodevidaapptw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber

class SegundaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda)

        val message = intent.getStringExtra("CONTADOR_STR")
        Timber.i("SEGUNDA --> El mensaje recibido es: ${message}")
    }
}
