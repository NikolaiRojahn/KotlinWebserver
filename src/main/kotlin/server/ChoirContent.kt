package server

/**
 * @param filename The filename to persist data to.
 */

class ChoirContent(val filename:String):WebContent
{
    // Local collection of members.
    val members:MutableList<MemberDTO> = mutableListOf()

    override fun save() {
        // Here we will persist the collection to a file.

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // GET /member
    fun getMember():List<MemberDTO> = TODO("TBD")

    // GET /member/3
    fun getMember(id: Int):MemberDTO? = members.firstOrNull { m -> m.id == id }

    // PUT /member
    fun putMember(member: MemberDTO): MemberDTO = TODO("TBD") // If we update the collection.

    // POST /member
    fun postMember(member: MemberDTO): MemberDTO = TODO("TBD") // If we add to the collection.

    // DELETE /member
    fun deleteMember(member: MemberDTO): Boolean{
        val index = members.indexOfFirst { m -> m.id == member.id }
        members.removeAt(index)
        return index != -1
    }
}