package unpsjb.ing.tnt.ligadeportiva.listado

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import unpsjb.ing.tnt.ligadeportiva.R
import unpsjb.ing.tnt.ligadeportiva.basededatos.Partido

class PartidoListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<PartidoListAdapter.PartidoViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var partidos = emptyList<Partido>() // Copia cache de los partidos

    inner class PartidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val partidoItemView: TextView = itemView.findViewById(R.id.textView_resultado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartidoViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return PartidoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PartidoViewHolder, position: Int) {
        val partido = partidos[position]
        val text = "${partido.equipoA_nombre} ${partido.equipoA_marcador} - ${partido.equipoB_marcador} ${partido.equipoB_nombre}"
        holder.partidoItemView.text = text
    }

    internal fun setPartidos(partidos: List<Partido>) {
        this.partidos = partidos
        notifyDataSetChanged()
    }

    override fun getItemCount() = partidos.size
}

