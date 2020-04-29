package unpsjb.ing.dit.tnt.navigationapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import unpsjb.ing.dit.tnt.navigationapp.databinding.FragmentFinalBinding

/**
 * A simple [Fragment] subclass.
 */
class FinalFragment : Fragment() {

    private var _binding: FragmentFinalBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinalBinding.inflate(inflater, container, false)
        val view = binding.root

        // TODO #8.6: Obtener argumento que se envió en la acción
        val safeArgs: FinalFragmentArgs by navArgs()
        binding.puntajeTextView.text = safeArgs.puntajeA.toString()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
