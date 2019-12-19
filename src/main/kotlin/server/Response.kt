package server

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.StringBuilder
import java.net.Socket

class Response(val outputStream: OutputStream)
{
    val body = StringBuilder()
    var contentLength = 0

    // Extension function on String class to return length of UTF_8 representation.
    fun String.utf8Length() = this.toByteArray(Charsets.UTF_8).size
    fun append(text:String)
    {
        body.append(text)
        contentLength = text.utf8Length()
    }

    fun createOptionsHeader():String
    {
        val head:String = """
            HTTP/1.1 204 No Content
            Allow: OPTIONS, GET, HEAD, POST
            Cache-Control: max-age=604800
        """.trimIndent()
        return head
    }

    fun createOkHeader():String
    {
        val head = """            
            HTTP/1.1 200 OK
            Content-Type: application/json; charset=UTF-8
            Content-length: ${contentLength}
            Connection: keep-alive
            Access-Control-Allow-Origin: *
        """.trimIndent()

        return head


    }

    fun send(header:String)
    {
        val writer = outputStream.bufferedWriter()
        writer.write(header)
        if (body != null) {
            writer.newLine()
            writer.newLine()
            writer.write(body.toString())
        }
        writer.close()
    }
}

/*
fun main()
{
    val output = ByteArrayOutputStream(1024)
    val response: Response = Response(output)
    val writer = output.bufferedWriter()
}
*/
