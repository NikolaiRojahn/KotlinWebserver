package server

/**
 * @param filename The filename to persist data to.
 */

//class ChoirContent(val filename:String):WebContent
class ChoirContent():WebContent
{
    // Local collection of members.
    var members:MutableList<MemberDTO> = mutableListOf()

    fun setDummyMembers(){
        members.add(MemberDTO(1, "Nikolai"))
        members.add(MemberDTO(2, "Claus"))
        members.add(MemberDTO(3, "Jörg"))
        members.add(MemberDTO(4, "Morten"))
    }

    override fun save() {
        // Here we will persist the collection to a file.

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // GET /member
    fun getMember():List<MemberDTO> = members

    // GET /member/3
    fun getMember(id: Int):MemberDTO? = members.firstOrNull { m -> m.id == id }

    // PUT /member
    fun putMember(member: MemberDTO): MemberDTO = TODO("TBD") // If we update the collection.

    // POST /member
    fun postMember(member: MemberDTO): MemberDTO = TODO("TBD") // If we add to the collection.

    // DELETE /member
    fun deleteMember(id: Int): Boolean{
        member = getMember(id)
        val index = members.indexOfFirst { m -> m.id == member.id }
        members.removeAt(index)
        return index != -1
    }
}

fun main() {
    val content = ChoirContent()
    content.setDummyMembers()
    println(content.deleteMember(content.getMember()))
    //listFunctions(content)
    //println(callFunction(content, Method.GET, "/member"))
}