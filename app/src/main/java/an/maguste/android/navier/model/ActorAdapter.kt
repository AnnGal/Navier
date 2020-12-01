package an.maguste.android.navier.model

import an.maguste.android.navier.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ActorAdapter : RecyclerView.Adapter<ActorViewHolder>() {

    private var actorsList = listOf<Actor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder =
            ActorViewHolder(LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.view_holder_actor, parent, false))

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actorsList[position])
    }

    override fun getItemCount(): Int = actorsList.size

    fun bindActor(newActorsList: List<Actor>) {
        actorsList = newActorsList
        notifyDataSetChanged()
    }
}

class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val image = itemView.findViewById<ImageView>(R.id.ivActorsImage)
    private val name = itemView.findViewById<TextView>(R.id.tvActorFullName)

    fun bind(actor: Actor) {
        image.setImageResource(actor.photo_image)
        name.text = actor.fullName
    }
}
