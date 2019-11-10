package server

import java.io.InputStream

class Request(inputStream: InputStream)
{

    val resource: String
    val method: Method

    init{
        val bufferedReader = inputStream.bufferedReader()
        val firstLine = bufferedReader.readLine()
        val parts = firstLine.split(" ")
        resource = parts[1]
        method = Method.valueOf(parts[0])
    }
}