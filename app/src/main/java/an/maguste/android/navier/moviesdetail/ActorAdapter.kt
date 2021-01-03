package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.R
import an.maguste.android.navier.data.Actor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class ActorAdapter : RecyclerView.Adapter<ActorViewHolder>() {

    private var actorsList = listOf<Actor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder =
        ActorViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.view_holder_actor, parent, false)
        )

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actorsList[position])
    }

    override fun getItemCount(): Int = actorsList.size

    fun bindActor(newActorsList: List<Actor>) {
        actorsList = newActorsList
        notifyDataSetChanged()
    }
}

class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val image = itemView.findViewById<ImageView>(R.id.ivActorsImage)
    private val name = itemView.findViewById<TextView>(R.id.tvActorFullName)

    fun bind(actor: Actor) {
        actor.picture?.let {
            Glide.with(itemView.context)
                .load(actor.picture)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .apply(imageOption)
                .into(image)
        }

        name.text = actor.name
    }

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.empty_photo)
            .fallback(R.drawable.empty_photo)
            .centerCrop()
    }
}
