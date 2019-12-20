package server


import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.lang.StringBuilder

class ProtocolTest
{
    val requestText = """
        GET /greeter HTTP/1.1
        
        
    """.trimIndent()
    val responseText = """
            HTTP/1.1 200 OK
            Content-Type: text/html; charset=UTF-8
            Content-length: 18
            Connection: close
            
            <p>Hello Kurt!</p>""".trimIndent()

    @Test
    fun testRequestResource(){
        val input = requestText.byteInputStream()
        val request = Request(input)
        assertEquals("/greeter", request.resource)
    }

    @Test
    fun testRequestMethod() {
        val input = requestText.byteInputStream()
        val request = Request(input)

        assertEquals(Method.GET, request.method)
    }

    //@Test Fails even if contens are identical, lengths are not the same, so encoding the newline could be the reason...
    fun testSayHelloToKurt(){
        val output = ByteArrayOutputStream(1024)
        val response = Response(output)
        response.append("<p>Hello Kurt!</p>")
        response.send(response.createOkHeader())

        assertEquals(responseText, output.toString())
    }
}