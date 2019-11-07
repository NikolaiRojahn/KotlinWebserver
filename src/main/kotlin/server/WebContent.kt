package server

interface WebContent
{
    // POST
    fun save() // persist the content to file/database
}

class ChoirContent:WebContent
{
    override fun save() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // GET /member
    fun getMember():List<Member> = TODO("TBD")

    // GET /member/3
    fun getMember(id: Int):Member? = TODO("TBD")

    // PUT /member
    fun putMember(member: Member): Member = TODO("TBD")

}

// simpel DTO eksempel:
data class Member(val id: Int, val name: String)