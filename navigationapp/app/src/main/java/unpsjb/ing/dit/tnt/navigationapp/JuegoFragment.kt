package unpsjb.ing.dit.tnt.navigationapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import unpsjb.ing.dit.tnt.navigationapp.databinding.FragmentJuegoBinding

/**
 * A simple [Fragment] subclass.
 */
class JuegoFragment : Fragment() {

    private var _binding: FragmentJuegoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJuegoBinding.inflate(inflater, container, false)
        val view = binding.root

        /*
        binding.juegoButton.setOnClickListener(
            // TODO #5.2: Navegar al hacer click
            //Navigation.createNavigateOnClickListener(R.id.portada_dest, null)
            // TODO #7.3: Navegar utilizando accion (comentar la línea de arriba)
            Navigation.createNavigateOnClickListener(R.id.action_juego_dest_to_finalFragment, null)
        )
        */

        // TODO #8.5: Navegar utilizando Direction Class (Comentar la linea de arriba)
        binding.juegoButton.setOnClickListener{
            val puntajeA = 1
            val action = JuegoFragmentDirections.actionJuegoDestToFinalFragment(puntajeA)
            findNavController().navigate(action)
        }

        // TODO #8.4: Obtener el argumento
        val safeArgs: JuegoFragmentArgs by navArgs()
        binding.mensajeTextView.text = safeArgs.argPrueba
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
