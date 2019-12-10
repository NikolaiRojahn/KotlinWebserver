package server

import javafx.application.Application.launch
import kotlinx.coroutines.*
import utils.*
import java.net.ServerSocket
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.*


enum class Method{GET, PUT, POST, DELETE, NONHTTP}

/**
 * Server extends CoroutineScope so we can use Coroutines to handle requests.
 */
class Server (val port: Int = 4711) : CoroutineScope{
    private val job = Job() // Creates a job in an active state. As long as it is active, the server is running.
    override val coroutineContext: CoroutineContext
        get() = job
    val webContents: MutableMap<String, Any> = mutableMapOf(
    "member" to ChoirContent("members.txt"),
    "gamer" to ClubContent("gamers.txt")
)

    val json = JSON()
    val utilsClass = Utils()
    //val reflection = Reflection()

    suspend fun handle(request:Request, response:Response)
    {
        // Coroutines need a context to store state of local variables if suspended.
        withContext(Dispatchers.Default){
            /* PSEUDO:
            1. Build the function name from http method and request resource.
            2. Get content from map and check if a method matches.
            3a. Call the method if match is found.
            4a. Convert result of method call to json.
            3b. Tell user to stop bugging us with illegitimate requests.
            */

            if(request.resource == "/stop"){
                stop()
            }
            val reflection = Reflection()
            val (methodName, parts) = reflection.buildMethodName(request.method, request.resource)
            val content = webContents[parts?.get(0)]
            if (content != null) {
//                val result: Any? = methodName?.let {
//                    if (parts != null) {
//                        println(it)
//                        println(parts)
//                        println(content)
//                        reflection.callFunction(content, it, parts, request.body)
//                    }
//                }
//                val result: Any? = parts?.let {
//                    if (methodName != null) reflection.callFunction(content, methodName, it, request.body)
//                }
                var result: Any? = null
                if (methodName != null && parts != null) {
                    result = reflection.callFunction(content, methodName, parts, request.body)
                }



            //VIRKER!!!--------------
//                var content: Any? = null
//
//                if(request.resource != ""){
//                    val contentArr = request.resource.split("/")
//                    content = webContents.getOrDefault(contentArr[1], null)
//
//                    val result: Any? = reflection.callFunction(content as Any, request)
            //VIRKER!!!--------------
                    response.append(json.toJsonFromMap(utilsClass.getMemberAsMap(result)))
                } else {
                   //do something to let user know, that his request was bad.
                    response.append("NONHTTP")
                }
            }
            response.send()
        }

    fun start(){

        val serverSocket = ServerSocket(port)

        while(job.isActive)
        {
            val socket = serverSocket.accept() // blocks until connection is made.
            // We will just fire and forget about our coroutine. If it succeeds, super, if not, TS, that's what F5 is for.
            // launch is the coroutine builder...
            launch{

                handle(Request(socket.getInputStream()), Response(socket.getOutputStream()))
            }
        }
    }

    /**
     * Stops the server. All pending requests handed off to coroutines are destroyed.
     */
    fun stop(){
//        running = false
        println("Stopping server...")
        job.cancel() // Cancels the job of having the server listening for incoming connections.
                     // If we use complete() it will wait for its children coroutines to complete.

        print("Server stopped!")
        System.exit(-1)
    }
}

fun main() {
    println("Starting server...")
    val server = Server()
    server.start()
}