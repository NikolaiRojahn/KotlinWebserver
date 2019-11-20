package server

import java.io.OutputStream
import java.lang.StringBuilder

class Response(val outputStream: OutputStream)
{
    val body = StringBuilder()

    fun append(text:String)
    {
        body.append(text)
    }
    fun send()
    {
        val head = """            
            HTTP/1.1 200 OK
            Content-Type: application/json; charset=UTF-8
            Content-length: ${body.length}
            Connection: keep-alive
        """.trimIndent()

        val writer = outputStream.bufferedWriter()
        writer.write(head)
        writer.newLine()
        writer.newLine()
        writer.write(body.toString())
        writer.close()
    }
}
