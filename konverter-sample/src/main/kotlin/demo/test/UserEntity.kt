package demo.test

import konverter.Konvert
import konverter.Konvertable
import konverter.To

@Konvertable(
    To(name = "LoginDTO", pick = ["username", "password"]),
    To(name = "UserListDTO", omit = ["password"])
)
@Konvert(to = UserVO::class)
data class UserEntity(
    val id: Long,
    @Konvert.Field("name")
    val username: String,
    val password: String,
    @Konvert.By(GenderEnumConverter::class)
    val gender: Int
)

data class UserVO(
    val id: String,
    val name: String,
    val gender: GenderEnum
)

enum class GenderEnum {
    MALE, FEMALE;
}

object GenderEnumConverter : Konvert.KonvertBy<Int, GenderEnum> {
    override fun Int.forward(): GenderEnum {
        return GenderEnum.values()[this]
    }

    override fun GenderEnum.backward(): Int {
        return this.ordinal
    }
}


