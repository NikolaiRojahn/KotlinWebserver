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


    fun append(text:String)
    {
        body.append(text)
    }
    fun send()
    {
        val head = """            
            HTTP/1.1 200 OK
            Content-Type: text/html; charset=UTF-8
            Content-length: ${body.length}            
            Connection: close
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
