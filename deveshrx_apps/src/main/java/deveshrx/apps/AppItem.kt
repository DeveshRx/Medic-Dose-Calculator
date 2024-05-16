package deveshrx.apps

import kotlinx.serialization.Serializable

@Serializable
data class AppItem(
    val icon: String,
val name: String,
    val appurl: String
    )