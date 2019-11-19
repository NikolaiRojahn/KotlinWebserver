package choir

data class Address(val city: String, val zipCode: String)
data class Member(val firstName: String, val lastName: String, val phone: String, val address: Address)

