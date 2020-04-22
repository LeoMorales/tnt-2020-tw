package unpsjb.ing.dit.tnt.fragmentslibros

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import unpsjb.ing.dit.tnt.fragmentslibros.databinding.FragmentDescripcionLibroBinding

//TODO #1.2: Crear dos Fragments
/**
 * A simple [Fragment] subclass.
 * Use the [DescripcionLibroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DescripcionLibroFragment : Fragment() {

    lateinit var arrbookdesc: Array<String>
    var bookindex = 0

    private var _binding: FragmentDescripcionLibroBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        //val v = inflater.inflate(R.layout.fragment_descripcion_libro, container, false)
        _binding = FragmentDescripcionLibroBinding.inflate(inflater, container, false)
        val view = binding.root

        arrbookdesc = resources.getStringArray(R.array.descripcioneslibros)
        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun cambiarLibro(index: Int) {
        bookindex = index
        binding.descripcionLibroTextView.text = arrbookdesc[bookindex]
    }

}
