package server

import java.net.ServerSocket
import kotlin.concurrent.thread
import server.ChoirContent

enum class Method{GET, PUT, POST, DELETE}

class Server (val port: Int = 4711){
    var running = true

    fun handle(request:Request, response:Response){
//        println(request.resource)
        val homepage: Homepage = Homepage()
        response.append(homepage.generate(request.resource.substring(1)))
        response.send()
    }

    fun start(){
        val serverSocket = ServerSocket(port)
        while(running)
        {
            val socket = serverSocket.accept()
            thread{
                handle(Request(socket.getInputStream()), Response(socket.getOutputStream()))
            }
        }
    }

    fun stop(){
        TODO("implement Server().stop()")
    }
}
fun main(){
    println("Starting server...")
    Server().start()
}