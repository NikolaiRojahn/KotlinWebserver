package server

interface WebContent
{

    fun save() // persist the content to file/database
}

/**
 * @param filename The filename to persist data to.
 */
class ChoirContent(val filename:String):WebContent
{
    // Local collection of members.
    val members:List<Member> = List<Member>()

    override fun save() {
        // Here we will persist the collection to a file.

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // GET /member
    fun getMember():List<Member> = TODO("TBD")

    // GET /member/3
    fun getMember(id: Int):Member? = members.firstOrNull { m -> m.id == id }

    // PUT /member
    fun putMember(member: Member): Member = TODO("TBD") // If we update the collection.

    // POST /member
    fun postMember(member: Member): Member = TODO("TBD") // If we add to the collection.

    // DELETE /member
    fun deleteMember(member: Member): Member = TODO("TBD")
}

// simpel DTO eksempel:
data class Member(val id: Int, val name: String)