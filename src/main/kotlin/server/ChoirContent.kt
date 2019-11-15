package server

/**
 * @param filename The filename to persist data to.
 */

class ChoirContent(val filename:String):WebContent
{
    // Local collection of members.
    val members:MutableList<MemberDTO> = mutableListOf()
    val memberIsNull = "Given member is null"

    override fun save() {
        // Here we will persist the collection to a file.

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // GET /member
    fun getMember():List<MemberDTO> = members

    // GET /member/3
    fun getMember(id: Int):MemberDTO? = members.firstOrNull { m -> m.id == id }

    // PUT /member
    fun putMember(member: MemberDTO): MemberDTO {
        if(member == null) { memberIsNull }
        if(members.contains(member)) {
            members[members.indexOf(member)] = member
            return member
        }
            return postMember(member)
    }

    // POST /member
    fun postMember(member: MemberDTO): MemberDTO {
        if(member == null) { memberIsNull }
            members.add(member)
            return member
    }

    // DELETE /member
    fun deleteMember(member: MemberDTO): Boolean{
        val index = members.indexOfFirst { m -> m.id == member.id }
        members.removeAt(index)
        return index != -1
    }
}