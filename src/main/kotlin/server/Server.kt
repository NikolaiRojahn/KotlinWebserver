package server

//DENNE SERVER ER IKKE SAT OP TIL AT KØRE METODER FRA CHOIRCONTENT ENDNU - DE KAN TESTES/KØRES FRA MAIN METODEN I CHOIR CONTENT!!!
import java.net.ServerSocket
import kotlin.concurrent.thread
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions


enum class Method{GET, PUT, POST, DELETE, NONHTTP}

class Server (val port: Int = 4711){
    var running = true

    val json = JSON()
    val utils = Utils()
    val content = ChoirContent("")
    val reflection = Reflection()

    fun handle(request:Request, response:Response){
//        println(request.resource)
        //val homepage: Homepage = Homepage()
        //response.append(homepage.generate(request.resource.substring(1)))
        //content.setDummyMembers()
        val requestMethod: Method = request.method

        //println(json.fromJsonToClass(MemberDTO::class, request.json))
        //println(MemberDTO(17, "Sonja"))

        when (requestMethod) {
            Method.GET -> response.append(json.toJsonFromMap(reflection.callFunction(content, request.method, request.resource, null) as MutableMap<Int, MemberDTO>))
            else -> response.append(json.toJsonFromMap(utils.getMemberAsMap(reflection.callFunction(content, request.method, request.resource, json.fromJsonToClass(MemberDTO::class, request.body)) as MemberDTO)))
        }

        //println(content.getMember())
        //println(request.body)

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

/*    *//**
     * Evaluates the web request and calls corresponding function in the content if it exists.
     *//*
    fun matchFunction(content:Any, method:Method, resource: String): KFunction<*>?
    {
        val parts = resource.split("/")
        println(parts)
        if (parts.isEmpty()) return null
        else{
            val methodName = method.toString().toLowerCase() + (parts[0].capitalize())
            println(methodName)
            val type = content::class
            type.declaredFunctions.forEach{println(it)}
            type.declaredFunctions.filter{it.name == methodName }.forEach{println(it.parameters.size)} // Husk første arg er objektets egen reference.
            val function = type.declaredFunctions.filter{it.name == methodName}.filter{it.parameters.size == parts.size}.
                firstOrNull()
            if ( function == null ) return null
            if (function != null && function.parameters.size > 1)
            {
                val p = function.parameters[1]
                println(function.parameters[1])
                // her bør vi matche value og parameter type.
                when(p.type.classifier){
                    Int::class -> {
                        val v1 = parts[1].toInt()
                        function.call(content, v1) // fortæller (vel) at funktionen skal kaldes på den givne klasse.
                    }
                    else -> return null
                }

            }
            else
                function.call(content)
            return function


        }

    }*/
}
fun main(){
    println("Starting server...")
    Server().start()
}