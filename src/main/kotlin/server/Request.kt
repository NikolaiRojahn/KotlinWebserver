package server

import java.io.BufferedReader
import java.io.InputStream
import java.lang.StringBuilder
import java.nio.charset.Charset

class Request(inputStream: InputStream)
{
    val resource: String
    val method: Method
    var body: String = ""
    val headers = mutableMapOf<String, String>()

    init {
        var line = inputStream.readLine()
        val parts = line.split(" ")
        if (parts.size != 3) { // We MUST have a HTTP Method, a resource and a HTTP version in parts or it is an invalid HTTP request.

            resource = ""

            method = Method.NONHTTP

        }

        else {

            resource = parts[1]

            method = Method.valueOf(parts[0])

        }

        line = inputStream.readLine()
        while (line.isNotEmpty()) {
            val headerParts = line.split(":")
            headers[headerParts[0].trim().toLowerCase()] = headerParts[1].trim().toLowerCase()
            line = inputStream.readLine()
        }

        //read headers here and get Content-Length
        if (method != Method.GET) {
            val contentLenght = headers["content-length"]!!.toInt()
            body = inputStream.readString(contentLenght)
        }
    }
}


    /*var body: String = ""
    val resource: String
    val method: Method
*//*    val json = """
        {
          "id": 17,
          "name": "Sonja"
        }
        """.trimIndent()*//*

    init{
        val bufferedReader = inputStream.bufferedReader()
        var firstLine = bufferedReader.readLine()
        println(firstLine)
        val parts = firstLine.split(" ")
        resource = parts[1]
        method = Method.valueOf(parts[0])
        if(method != Method.GET){
            //TODO
        }
        }*/
