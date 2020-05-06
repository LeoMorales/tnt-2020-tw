package unpsjb.ing.dit.tnt.adivinarpelicula.pantallas.juego

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment

import unpsjb.ing.dit.tnt.adivinarpelicula.R
import unpsjb.ing.dit.tnt.adivinarpelicula.databinding.FragmentJuegoBinding

/**
 * A simple [Fragment] subclass.
 */
class JuegoFragment : Fragment() {

    private lateinit var binding: FragmentJuegoBinding
    val viewModel: JuegoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_juego, container, false)

        binding.subtituloText.text = viewModel.titulo.value

        binding.correctaButton.setOnClickListener { onCorrecta() }
        binding.saltearButton.setOnClickListener { onSalteo() }
        binding.finJuegoButton.setOnClickListener { onFinDelJuego() }

        actualizarPuntajeText()
        actualizarPeliculaText()

        return binding.root
    }

    /** Metodos para manejar los eventos de click en los botones **/

    private fun onSalteo() {
        viewModel.cuandoSaltea()
        actualizarPeliculaText()
        actualizarPuntajeText()
    }
    private fun onCorrecta() {
        viewModel.cuandoEsCorrecta()
        actualizarPuntajeText()
        actualizarPeliculaText()
    }

    /**
     * Finaliza el juego...
     */
    private fun juegoFinalizado() {
        Toast.makeText(activity, "Juego finalizado!", Toast.LENGTH_SHORT).show()
        val action = JuegoFragmentDirections.actionJuegoHaciaPuntaje()
        action.puntajeA = viewModel.puntaje
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun onFinDelJuego() {
        juegoFinalizado()
    }

    /** Metodos para actualizar la UI **/

    private fun actualizarPeliculaText() {
        binding.peliculaText.text = viewModel.pelicula
    }

    private fun actualizarPuntajeText() {
        binding.puntajeText.text = viewModel.puntaje.toString()
    }
}
