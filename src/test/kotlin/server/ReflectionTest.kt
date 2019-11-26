package server

import org.junit.Assert
import org.junit.Test
import kotlin.reflect.typeOf

class ReflectionTest
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

    @Test
    fun testBuildMethodName()
    {
        val method = server.Method.GET
        val resource = "/member/59"
        val parts = listOf("member", "59")
        val expected : Pair<String?, List<String>?> = Pair("getMember", parts)
        val actual = Reflection().buildMethodName(method, resource)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testGetOneMember()
    {
        content.storeMembers(members)
        val method = server.Method.GET
        val parts = listOf("member", "7") // expecting to get Kurt
        val expected = members[7]
        val actual = Reflection().callFunction(content,"getMember", parts)
//        println(actual)
        Assert.assertEquals(expected, actual)
    }

    @ExperimentalStdlibApi
    @Test
    fun testGetAllMembers()
    {
        content.storeMembers(members)
        val method = server.Method.GET
        val parts = listOf("member")
        val expected = members
        val actual : Any? = Reflection().callFunction(content, "getMember", parts)
//        print(expected.size.toString() + ", ")
//        if (actual != null)
//            println((actual as MutableMap<Int, MemberDTO>).size.toString())
        Assert.assertEquals(expected, actual)
    }
}