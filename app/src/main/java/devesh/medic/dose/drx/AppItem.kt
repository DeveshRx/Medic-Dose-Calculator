package devesh.medic.dose.drx

import kotlinx.serialization.Serializable

@Serializable
data class AppItem(
    val icon: String,
val name: String,
    val appurl: String
    )