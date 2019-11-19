package server

import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.memberFunctions

/*fun listFunctions(content: Any) {
    val contentType = content::class
    println(contentType.simpleName)
    contentType.memberFunctions.forEach {
        println(it)
    }
}*/
class Reflection {
    fun callFunction(content: Any, method: Method, resource: String, parameter: Any?) : Any? {
        //println(resource)
        val parts = resource.split("/").filter { !it.isEmpty() }
        //println(parts)
        if (parts.size == 0) return null

        val methodName = method.toString().toLowerCase() + (parts[0].capitalize())
        //println(methodName)
        val type = content::class

        //type.declaredFunctions.forEach {println( "${it}" + " " + "${it.parameters.size}") }

        val function = type.declaredFunctions
            .filter { it.name == methodName }.firstOrNull {
                when(method){
                    Method.GET -> it.parameters.size == parts.size
                    else -> it.parameters.size == parts.size + 1
                }
            }

        //println(function!!.parameters[1])
        if (function == null) return null
        if (function.parameters.size > 1) {
            //println("******************")
            val p = function.parameters[1]
            //println(p.type.classifier)
            when (p.type.classifier) {
                Int::class -> {
                    val v1 = parts[1].toInt()
                    return function.call(content, v1)
                }
                MemberDTO::class -> {
                    //println("HEJ")
                    //println(parameter)
                    val v1 = parameter
                    return function.call(content, v1)
                }
                else -> return null
            }
        }
        else return function.call(content)
    }
}