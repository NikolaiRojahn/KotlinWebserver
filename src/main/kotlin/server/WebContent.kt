package server

import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.memberFunctions


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
    private var members : MutableMap<Int, Member> = mutableMapOf()
    fun setMembers(value:MutableMap<Int, Member>){members = value}

    //val members:MutableList<Member> = mutableListOf()




    override fun save() {
        // Here we will persist the collection to a file.

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // GET /member
    fun getMember():List<Member> = TODO("TBD")

    // GET /member/3
    fun getMember(id: Int):Member? = members.getOrDefault(id, null)

    // PUT /member
    fun putMember(member: Member): Member = TODO("TBD") // If we update the collection.

    // POST /member
    fun postMember(member: Member): Member = TODO("TBD") // If we add to the collection.

    // DELETE /member
    fun deleteMember(member: Member): Boolean = members.remove(member.id) != null


    fun listFunctions(content: Any)
    {
        val contentType = content::class
        println(contentType.simpleName)
        contentType.memberFunctions.forEach{
            println(it)
        }
    }
}

// simpel DTO eksempel:
data class Member(val id: Int, val name: String)