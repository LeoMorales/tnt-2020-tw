package unpsjb.ing.dit.tnt.adivinarpelicula.pantallas.puntaje

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import unpsjb.ing.dit.tnt.adivinarpelicula.R
import unpsjb.ing.dit.tnt.adivinarpelicula.databinding.FragmentPuntajeBinding

/**
 * A simple [Fragment] subclass.
 */
class PuntajeFragment : Fragment() {

    private lateinit var viewModel: PuntajeViewModel
    private lateinit var viewModelFactory: PuntajeViewModelFactory
    private lateinit var binding: FragmentPuntajeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*
        * Nota: En esta app ejemplo no es necesario utilizar un ViewModelFactory
        * para el PuntajeViewModel, ya que podemos asignar directamente el puntaje
        * a la variable viewModel.score directamente.
        * Pero a veces necesitamos los datos inmnediatamente cuando el viewModel
        * es inicializado.
        * */
        viewModelFactory = PuntajeViewModelFactory(PuntajeFragmentArgs.fromBundle(arguments!!).puntajeA)
        viewModel = ViewModelProvider(this,viewModelFactory).get(PuntajeViewModel::class.java)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_puntaje, container, false)

        binding.puntajeText.text = viewModel.score.toString()

        binding.jugarDeNuevoButton.setOnClickListener{
            viewModel.onJugarDeNuevo()
        }

        viewModel.eventoJugarDeNuevo.observe(viewLifecycleOwner, Observer { jugarDeNuevo ->
            if(jugarDeNuevo){
                findNavController().navigate(PuntajeFragmentDirections.actionPuntajeHaciaJuego())
                //Log.i("Puntaje", "Jugar de nuevo!")
            }
        })
        return binding.root
    }

}
