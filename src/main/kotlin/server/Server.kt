package server

import kotlinx.coroutines.*
import java.lang.reflect.Member
import java.net.ServerSocket
import kotlin.coroutines.CoroutineContext




/**
 * Server extends CoroutineScope so we can use Coroutines to handle requests.
 */
class Server (val port: Int = 4711) : CoroutineScope{
    private val job = Job() // Creates a job in an active state. As long as it is active, the server is running.
    override val coroutineContext: CoroutineContext
        get() = job

    val json = JSON()
    val utils = Utils()
    val content = ChoirContent("")
    val reflection = Reflection()

    suspend fun handle(request:Request, response:Response)
    {
        // Coroutines need a context to store state of local variables if suspended.
        withContext(Dispatchers.Default){

            val requestMethod: Method = request.method

            when (requestMethod) {
                Method.GET -> try { response.append(json.toJsonFromMap(reflection.callFunction(content, request) as MutableMap<Int, MemberDTO>)) }
                catch(ex:Exception) {
                    ex.message?.let { response.append(it) }
                }
                Method.NONHTTP -> response.append("NONHTTP")
                else -> response.append(json.toJsonFromMap(utils.getMemberAsMap(reflection.callFunction(content, request) as MemberDTO)))
            }
            response.send()
        }
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
        println("Stopping server...")
        job.cancel() // Cancels the job of having the server listening for incoming connections.
                     // If we use complete() it will wait for its children coroutines to complete.
        print("Server stopped!")
    }
}

fun ChoirContent.publish(port: Int){
    val server = Server(port)
    server.start()
}

fun main() {
    println("Starting server...")
    val choirContent = ChoirContent("")
    choirContent.publish(4711)
    //val server = Server()
    //server.start()
}