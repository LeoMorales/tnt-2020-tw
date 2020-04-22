package unpsjb.ing.dit.tnt.fragmentslibros

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import unpsjb.ing.dit.tnt.fragmentslibros.databinding.FragmentAutorBinding

// TODO #1.1: Crear dos Fragments
/**
 * A simple [Fragment] subclass.
 * Use the [AutorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AutorFragment : Fragment() {

    private var _binding: FragmentAutorBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // TODO #4.1: Habilitar view binding en fragments
        //setContentView(view)
        //return inflater.inflate(R.layout.fragment_autor, container, false)
        _binding = FragmentAutorBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.BioyCasaresRadioButton.setOnClickListener{
            click_en_radio_button(R.id.BioyCasaresRadioButton) }
        return view


    }

    // TODO # 5.1: Responder al click utilizando método de la Interface
    private fun click_en_radio_button(id_radio_button: Int) {
        val index = when (id_radio_button){
            R.id.BioyCasaresRadioButton -> 2
            else -> 0
        }
        val activity = getActivity()
        if (activity is Cordinadora) {
            activity.onCambioDeAutor(index)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}
