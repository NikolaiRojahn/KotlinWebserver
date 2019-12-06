package server

/**
 * @param filename The filename to persist data to.
 */
class ChoirContent(val filename:String):WebContent
{
    //Local collection of members.
    var members = mutableMapOf<Int, MemberDTO>(
        1 to MemberDTO(1, "Nikolai"),
        2 to MemberDTO(2, "Claus"),
        3 to MemberDTO(3, "Jörg"),
        4 to MemberDTO(4, "Morten")
    )
    val nullMessage = "Given member is null"

    fun storeMembers(value:MutableMap<Int, MemberDTO>){members = value}

    // GET /member
    fun getMember():MutableMap<Int, MemberDTO> = members


    // GET /member/3
    fun getMember(id: Int):MemberDTO {
        return members.getOrDefault(id, null) ?: throw Exception("Member with id $id not found.".format(id))
    }

    // PUT /member
    fun putMember(member: MemberDTO): MemberDTO {
        if(member == null) { nullMessage }
        if(members.containsKey(member.id)) {
            members.replace(member.id, member)
            save()
            return member
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
            save()
            //return member
        }
        return member
    }

    // DELETE /member/id
    fun deleteMember(member: MemberDTO): MemberDTO {
        if(member == null) { nullMessage }
        val member: MemberDTO = members.remove(member.id) ?: throw Exception("Member with id $member.id not found.".format(member.id))
        save()
        return member
    }

    override fun save() {
        // Here we will persist the collection to a file.
        utils.writeToFile(filename, JSON().toJsonFromMap(Utils().getMemberAsMap(members)))
    }
}
