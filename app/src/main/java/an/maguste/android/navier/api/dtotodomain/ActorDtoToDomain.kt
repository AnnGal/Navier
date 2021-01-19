package an.maguste.android.navier.api.dtotodomain

import an.maguste.android.navier.BuildConfig
import an.maguste.android.navier.api.ActorDto
import an.maguste.android.navier.data.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun convertActorDtoToDomain(actorsDto: List<ActorDto>): List<Actor> =
    withContext(Dispatchers.Default) {
        actorsDto.map { actorDto ->
            Actor(
                id = actorDto.id,
                name = actorDto.name,
                picture = actorDto.image?.let { BuildConfig.IMAGE_URL + actorDto.image }
            )
        }
    }