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
    fun callFunction(content: Any, method: Method, resource: String) : Any? {
        val parts = resource.split("/").filter { !it.isEmpty() }
        println(parts)
        if (parts.size == 0) return null

        val methodName = method.toString().toLowerCase() + (parts[0].capitalize())
        println(methodName)
        val type = content::class
        val function = type.declaredFunctions
            .filter { it.name == methodName }
            .filter { it.parameters.size == parts.size }
            .firstOrNull()
        if (function == null) return null
        if (function.parameters.size > 1) {
            val p = function.parameters[1]
            println(p.type.classifier)
            when (p.type.classifier) {
                Int::class -> {
                    val v1 = parts[1].toInt()
                    return function.call(content, v1)
                }
                else -> return null
            }
        }
        else return function.call(content)
    }
}