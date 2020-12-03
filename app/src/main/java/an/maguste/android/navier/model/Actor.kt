package an.maguste.android.navier.model

data class Actor(
        val name: String,
        val last_name: String = "",
        val photo_image: Int = 0
){
    val fullName: String
        get() = "$name $last_name"
}
