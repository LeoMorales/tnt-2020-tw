package unpsjb.ing.tnt.ligadeportiva

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class NuevoPartidoActivity : AppCompatActivity() {

    private lateinit var equipoA_Nombre: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_partido)

        equipoA_Nombre = findViewById(R.id.edit_nombre_equipo_1)

        val button = findViewById<Button>(R.id.button_save)

        button.setOnClickListener {
            val intentRespuesta = Intent()
            if (TextUtils.isEmpty(equipoA_Nombre.text)) {
                setResult(Activity.RESULT_CANCELED, intentRespuesta)
            } else {
                val equipoA_nombre = equipoA_Nombre.text.toString()
                intentRespuesta.putExtra(EXTRA_RESPUESTA_EQUIPO_1, equipoA_nombre)
                setResult(Activity.RESULT_OK, intentRespuesta)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_RESPUESTA_EQUIPO_1 = "unpsjb.ing.tnt.roompartidos.REPLY_EQUIPO_A"
    }
}
