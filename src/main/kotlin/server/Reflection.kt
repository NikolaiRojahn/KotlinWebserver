package server

import kotlin.reflect.full.declaredFunctions

class Reflection {

    //val json = JSON()

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
                    //val v1 = json.fromJsonToClass(kClass, request.body)
                    val v1 = JSON().fromJsonToObjectInstance(kClass, request.body)
                    return function.call(content, v1)
                }
            }
        } else // no parameters required.
            return function.call(content)
    }

//    fun buildMethodName(method: Method, resource: String) : Pair<String?, List<String>?>{
//
//        // resource is passed in as e.g. /members, we need only the 'members' part.
//        val parts = resource.split("/").filter{ it.isNotEmpty() }
//
//        // return the built string or null if parts are empty along with the parts in a Pair object.
//
//        return if (parts.isNotEmpty()) Pair(method.toString().toLowerCase() + (parts[0].capitalize()), parts) else Pair(null, null)
//
//    }
//
//    fun callFunction(content: Any, methodName: String, parts:List<String>, body:String? = null): Any?
//    {
//        // Determine type of content.
//        val type = content::class
//        // find function with matching name and no of parameters.
//        val function = type.declaredFunctions
//            .filter{it.name == methodName}
//            .filter{it.parameters.size == parts.size}
//            .firstOrNull() ?: return null
//
//        println(type)
//        println(function)
//
//        if (function.parameters.size > 1) // above 1 since any function always takes a ref to self implicitly.
//        {
//            val parameter = function.parameters[1]
//            val classArr = parameter.type.classifier.toString().split(" ")
//            val classTemp = classArr[classArr.size - 1]
//
//            // switch on the classifier.
//            var v1:Any?
//            when (parameter.type.classifier)
//            {
//                // is parameter an integer?
//                Int::class -> {
//                    v1 = parts[1].toInt()
//                }
//                // is parameter a string?
//                String::class -> {
//                    v1 = parts[1]
//                }
//                // otherwise, we are putting/posting, the argument will be in the body.
//                else -> {
//                    val kClass = Class.forName(classTemp).kotlin
//                    v1 = body?.let { JSON().fromJsonToObjectInstance(kClass, it) }
//                    println(v1)
//                }
//            }
//            return function.call(content, v1)
//        }
//        else // no parameters required.
//            return function.call(content)
//    }
}