package unpsjb.ing.dit.tnt.adivinarpelicula.pantallas.juego

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

        binding.juegoViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.subtituloText.text = viewModel.titulo.value
        binding.finJuegoButton.setOnClickListener { onFinDelJuego() }

        viewModel.eventoJuegoFinalizado.observe(viewLifecycleOwner, Observer { esFinalizado ->
            Log.i("Fragment", "esFinalizado"+esFinalizado)
            if (esFinalizado) onFinDelJuego()
        })


        return binding.root
    }

    /**
     * Finaliza el juego...
     */
    private fun juegoFinalizado() {
        Toast.makeText(activity, "Juego finalizado!", Toast.LENGTH_SHORT).show()
        val action = JuegoFragmentDirections.actionJuegoHaciaPuntaje()
        action.puntajeA = viewModel.puntaje.value!!
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun onFinDelJuego() {
        juegoFinalizado()
    }


}
