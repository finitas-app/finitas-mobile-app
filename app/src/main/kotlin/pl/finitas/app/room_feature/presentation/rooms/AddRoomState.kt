package pl.finitas.app.room_feature.presentation.rooms

data class AddRoomState(
    val addRoomOption: AddRoomOption,
    val title: String,
    val invitationLink: String,
)

enum class AddRoomOption {
    Join,
    Create,
}