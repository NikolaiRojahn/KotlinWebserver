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
    fun String.utf8Length = this.toByteArray(Charsets.UTF_8).size
    fun append(text:String)
    {
        body.append(text)
        contentLength = text.utf8Length()
    }
    fun send()
    {
        val head = """            
            HTTP/1.1 200 OK
            Content-Type: application/json; charset=UTF-8
            Content-length: ${contentLength}
            Connection: keep-alive
            Access-Control-Allow-Origin: *
        """.trimIndent()

        val writer = outputStream.bufferedWriter()
        writer.write(head)
        writer.newLine()
        writer.newLine()
        writer.write(body.toString())
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
