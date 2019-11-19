package server

//DENNE SERVER ER IKKE SAT OP TIL AT KØRE METODER FRA CHOIRCONTENT ENDNU - DE KAN TESTES/KØRES FRA MAIN METODEN I CHOIR CONTENT!!!
import kotlinx.coroutines.*
import java.net.ServerSocket
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions


enum class Method{GET, PUT, POST, DELETE, NONHTTP}

/**
 * Server extends CoroutineScope so we can use Coroutines to handle requests.
 */
class Server (val port: Int = 4711) : CoroutineScope{
    private val job = Job() // Creates a job in an active state. As long as it is active, the server is running.
    override val coroutineContext: CoroutineContext
        get() = job
//    var running = true
    val webContents: MutableMap<String, WebContent> = mutableMapOf("member" to ChoirContent(""))

    val json = JSON()
    val utils = Utils()
    val content = ChoirContent("")
    val reflection = Reflection()

    suspend fun handle(request:Request, response:Response)
    {
        // Coroutines need a context to store state of local variables if suspended.
        withContext(Dispatchers.Default){
            /* PSEUDO:
        1. Build the function name from http method and request resource.
        2. Iterate through published contents to see if we have a match.
        3a. Call the method if match is found.
        3b. Tell user to stop bugging us with illegitimate requests.
        */

            val requestMethod: Method = request.method

            //println(json.fromJsonToClass(MemberDTO::class, request.json))
            //println(MemberDTO(17, "Sonja"))

            when (requestMethod) {
                Method.GET -> response.append(json.toJsonFromMap(reflection.callFunction(content, request.method, request.resource, null) as MutableMap<Int, MemberDTO>))
                Method.NONHTTP -> response.append("NONHTTP")
                else -> response.append(json.toJsonFromMap(utils.getMemberAsMap(reflection.callFunction(content, request.method, request.resource, json.fromJsonToClass(MemberDTO::class, request.body)) as MemberDTO)))
            }
            /*val homepage: Homepage = Homepage()
            response.append(homepage.generate(request.resource.substring(1)))*/
            response.send()
        }
    }

    fun start(){

        val serverSocket = ServerSocket(port)
//        while(running)
        while(job.isActive)
        {
            val socket = serverSocket.accept() // blocks until connection is made.
            // We will just fire and forget about our coroutine. If it succeeds, super, if not, TS, that's what F5 is for.
            // launch is the coroutine builder...
            launch{

                handle(Request(socket.getInputStream()), Response(socket.getOutputStream()))
            }

//            thread{
//                handle(Request(socket.getInputStream()), Response(socket.getOutputStream()))
//            }
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
    }

/*
    fun matchFunction(content:Any, method:Method, resource: String): KFunction<*>?
    {
        val parts = resource.split("/")
        println(parts)
        if (parts.isEmpty()) return null
        else {
            val methodName = method.toString().toLowerCase() + (parts[0].capitalize())
            println(methodName)
            val type = content::class
            type.declaredFunctions.forEach { println(it) }
            type.declaredFunctions.filter { it.name == methodName }
                .forEach { println(it.parameters.size) } // Husk første arg er objektets egen reference.
            val function =
                type.declaredFunctions.filter { it.name == methodName }.filter { it.parameters.size == parts.size }
                    .firstOrNull()
            if (function == null) return null
            if (function != null && function.parameters.size > 1) {
                val p = function.parameters[1]
                println(function.parameters[1])
                // her bør vi matche value og parameter type.
                when (p.type.classifier) {
                    Int::class -> {
                        val v1 = parts[1].toInt()
                        function.call(content, v1) // fortæller (vel) at funktionen skal kaldes på den givne klasse.
                    }
                    else -> return null
                }

            } else
                function.call(content)
            return function


        }

    }*/
}

fun main() {
    println("Starting server...")
    val server = Server()
    server.start()
}