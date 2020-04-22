package unpsjb.ing.dit.tnt.fragmentslibros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), Cordinadora {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCambioDeAutor(index: Int) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_libros) as DescripcionLibroFragment
        fragment.cambiarLibro(index)

    }
}
