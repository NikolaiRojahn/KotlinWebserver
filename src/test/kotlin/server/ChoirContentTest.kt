package server

import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class ChoirContentTest
{
    // We need a companion object to store static variables in the test class.
    companion object {
        @JvmStatic
        var content: ChoirContent = ChoirContent("")
        // Setup a small collection of members.
        @JvmStatic
        var members = mutableMapOf<Int, MemberDTO>(
            7 to MemberDTO(7, "Kurt"),
            17 to MemberDTO(17, "Sonja")
        )
    }

    @Before
    fun beforeTest()
    {
        // Before each test, copy the members to a new mutable map and give it to content.
        val copy = mutableMapOf<Int, MemberDTO>()
        copy.putAll(members)
        content.setMembers(copy)
    }

    @Test
    fun testFindMember()
    {
        val member = members[members.keys.first()]
        println("Finding member with id: " + member!!.id.toString())
        Assert.assertEquals(member.id, content.getMember(member.id)!!.id)
    }

    @Test
    fun testRemoveMember()
    {
        val member = members[members.keys.last()]
        println("Deleting member with id: " + member!!.id.toString())
        Assert.assertTrue(content.deleteMember(member))
    }
}



