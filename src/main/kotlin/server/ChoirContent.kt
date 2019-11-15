package server

/**
 * @param filename The filename to persist data to.
 */

//class ChoirContent(val filename:String):WebContent
class ChoirContent():WebContent
{
    // Local collection of members.
    val members:MutableList<MemberDTO> = mutableListOf()
    val nullMessage = "Given member is null"

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
    fun getMember(id: Int): MemberDTO? = members.firstOrNull { m -> m.id == id }

    // PUT /member
    fun putMember(member: MemberDTO): MemberDTO {
        if(member == null) { nullMessage }
        if(members.contains(member)) {
            members[members.indexOf(member)] = member
            return member
        }
        return postMember(member)
    }

    // POST /member
    fun postMember(member: MemberDTO): MemberDTO {
        if(member == null) { nullMessage }
        members.add(member)
        return member
    }

    // DELETE /member
    fun deleteMember(id: Int): Boolean{
        val member: MemberDTO? = getMember(id)
        val index = members.indexOfFirst { m -> m.id == member.id }
        members.removeAt(index)
        return index != -1
    }
}

fun main() {
    val content = ChoirContent()
    content.setDummyMembers()
    println(content.putMember(MemberDTO(3,"Jørgen")))
    //listFunctions(content)
    //println(callFunction(content, Method.GET, "/member"))
}