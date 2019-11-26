package server

interface WebContent{
    fun save()
}
/**
 * @param filename The filename to persist data to.
 */
class ChoirContent(val filename:String):WebContent
{
    // Local collection of members.
    val members = mutableMapOf<Int, MemberDTO>(
        7 to MemberDTO(7, "Kurt"),
        17 to MemberDTO(17, "Sonja")
    )
    val nullMessage = "Given member is null"

    //fun setNewMembers(value:MutableMap<Int, MemberDTO>){members = value}

    fun setDummyMembers(){
        members.put(1, MemberDTO(1, "Nikolai"))
        members.put(2, MemberDTO(2, "Claus"))
        members.put(3, MemberDTO(3, "Jörg"))
        members.put(4, MemberDTO(4, "Morten"))
    }

    override fun save() {
        // Here we will persist the collection to a file.

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // GET /member
    fun getMember():MutableMap<Int, MemberDTO> = members


    // GET /member/3
    fun getMember(id: Int):MutableMap<Int, MemberDTO> {
        val member:MemberDTO? = members.getOrDefault(id, null)
        if(member == null) { nullMessage }
        return mutableMapOf<Int, MemberDTO>((member!!.id to member!!))
    }

    // PUT /member
    fun putMember(member: MemberDTO): MemberDTO {
        if(member == null) { nullMessage }
        if(members.containsKey(member.id)) {
            members.replace(member.id, member)
        }
        return postMember(member)
    }

    // POST /member
    fun postMember(member: MemberDTO): MemberDTO {
        if (member == null) {
            nullMessage
        }
        if (!members.containsKey(member.id)) {
            members[member.id] = member
            return member
        }
        return member
    }

    // DELETE /member/id
    fun deleteMember(member: MemberDTO): MemberDTO? = members.remove(member.id)
}
