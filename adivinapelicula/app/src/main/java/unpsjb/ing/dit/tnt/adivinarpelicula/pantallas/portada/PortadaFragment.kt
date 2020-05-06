package unpsjb.ing.dit.tnt.adivinarpelicula.pantallas.portada

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import unpsjb.ing.dit.tnt.adivinarpelicula.R
import unpsjb.ing.dit.tnt.adivinarpelicula.databinding.FragmentPortadaBinding

/**
 * A simple [Fragment] subclass.
 */
class PortadaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentPortadaBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_portada, container, false)

        binding.comenzarJuegoButton.setOnClickListener {
            findNavController().navigate(PortadaFragmentDirections.actionPortadaHaciaJuego())
        }

        return binding.root

    }

}
