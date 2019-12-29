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
        //val parts = listOf("member", "59")
        val parts = mutableListOf("member", "59")
        val expected : Pair<String?, List<String>?> = Pair("getMember", parts)
        val actual = Reflection().buildMethodName(method, resource)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testGetOneMember()
    {
        content.storeMembers(members)
        //val parts = listOf("member", "7") // expecting to get Kurt
        val parts = mutableListOf("member", "7") // expecting to get Kurt
        val expected = members[7]
        val actual = Reflection().callFunction(content,"getMember", parts)
        Assert.assertEquals(expected, actual)
    }

    @ExperimentalStdlibApi
    @Test
    fun testGetAllMembers()
    {
        content.storeMembers(members)
        //val parts = listOf("member")
        val parts = mutableListOf("member")
        val expected = members
        val actual : Any? = Reflection().callFunction(content, "getMember", parts)
        Assert.assertEquals(expected, actual)
    }

//    @Test
//    fun testPutMember()
//    {
//        content.storeMembers(members)
//        //val parts = listOf("member", "1")
//        val parts = mutableListOf("member","{\n" +
//                "    \"id\": 1,\n" +
//                "    \"name\": \"Nikolai\"\n" +
//                "}")
//        val expected = "Poul"
//        val result = Reflection().callFunction(content, "putMember", parts) as MemberDTO
//        //println(result)
//        //val actual = (result.get(0) as MemberDTO).name
//        val actual = result.name
//        Assert.assertEquals(expected, actual)
//
//    }

    fun testPostMember()
    {

    }


}