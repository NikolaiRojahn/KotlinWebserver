package server

import kotlin.reflect.full.declaredFunctions

class Reflection {
    val json = JSON()
    fun callFunction(content: Any, request: Request): Any? {
        var resource = request.resource
        if(!(request.body.isEmpty() || request.body == null)) { resource += "/" + request.body }
        val method = request.method
        val parts = resource.split("/").filter { !it.isEmpty() }
        if (parts.size == 0) return null

        val methodName = method.toString().toLowerCase() + (parts[0].capitalize())
        val type = content::class

        //type.declaredFunctions.forEach {println( "${it}" + " " + "${it.parameters.size}" + " " + "${parts.size}") }

        val function = type.declaredFunctions
            .filter { it.name == methodName }
            .filter { it.parameters.size == parts.size }
            .firstOrNull()

        if (function == null) { return null }
        if (function.parameters.size > 1) {
            val p = function.parameters[1]
            val classArr = p.type.classifier.toString().split(" ")
            val classTemp = classArr[classArr.size - 1]
            when (p.type.classifier) {
                Int::class -> {
                    val v1 = parts[1].toInt()
                    return function.call(content, v1)
                }
                String::class -> {
                    val v1 = parts[1]
                    return function.call(content, v1)
                }
                else -> {
                    val kClass = Class.forName(classTemp).kotlin
                    val v1 = json.fromJsonToClass(kClass, request.body)
                    return function.call(content, v1)
                }
            }
        } else // no parameters required.
            return function.call(content)
    }
}




//package server
//
//import kotlin.reflect.full.declaredFunctions
//import kotlin.reflect.full.memberFunctions
//
//class Reflection {
//    fun callFunction(content: Any, method: Method, resource: String, parameter: Any?) : Any? {
//        //println(resource)
//        val parts = resource.split("/").filter { !it.isEmpty() }
//        //println(parts)
//        if (parts.size == 0) return null
//
//        val methodName = method.toString().toLowerCase() + (parts[0].capitalize())
//        //println(methodName)
//        val type = content::class
//
//        //type.declaredFunctions.forEach {println( "${it}" + " " + "${it.parameters.size}") }
//
//        val function = type.declaredFunctions
//            .filter { it.name == methodName }.firstOrNull {
//                when(method){
//                    Method.GET -> it.parameters.size == parts.size
//                    else -> it.parameters.size == parts.size + 1
//                }
//            }
//
//        //println(function!!.parameters[1])
//        if (function == null) return null
//        if (function.parameters.size > 1) {
//            //println("******************")
//            val p = function.parameters[1]
//            //println(p.type.classifier)
//            when (p.type.classifier) {
//                Int::class -> {
//                    val v1 = parts[1].toInt()
//                    return function.call(content, v1)
//                }
//                String::class -> {
//                    val v1 = parts[1]
//                    return function.call(content, v1)
//                }
//                MemberDTO::class -> {
//                    val v1 = parameter
//                    return function.call(content, v1)
//                }
//                else -> return null
//            }
//        }
//        else // no parameters required.
//            return function.call(content)
//    }
//}