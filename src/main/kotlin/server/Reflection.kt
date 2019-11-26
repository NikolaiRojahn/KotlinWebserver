package server

import kotlin.reflect.full.declaredFunctions

class Reflection {

    fun buildMethodName(method: Method, resource: String) : Pair<String?, List<String>?>{

        // resource is passed in as e.g. /members, we need only the 'members' part.
        val parts = resource.split("/").filter{ it.isNotEmpty() }

        // return the built string or null if parts are empty along with the parts in a Pair object.
        return if (parts.isNotEmpty()) Pair(method.toString().toLowerCase() + (parts[0].capitalize()), parts) else Pair(null, null)

    }

    fun callFunction(content: Any, methodName: String, parts:List<String>, body:String? = null): Any?
    {
        // Determine type of content.
        val type = content::class
        // find function with matching name and no of parameters.
        val function = type.declaredFunctions
            .filter{it.name == methodName}
            .filter{it.parameters.size == parts.size}
            .firstOrNull() ?: return null

        if (function.parameters.size > 1) // above 1 since any function always takes a ref to self implicitly.
        {
            val parameter = function.parameters[1]
            val classArr = parameter.type.classifier.toString().split(" ")
            val classTemp = classArr[classArr.size - 1]

            // switch on the classifier.
            var v1:Any?
            when (parameter.type.classifier)
            {
                // is parameter an integer?
                Int::class -> {
                    v1 = parts[1].toInt()
                }
                // is parameter a string?
                String::class -> {
                    v1 = parts[1]
                }
                // otherwise, we are putting/posting, the argument will be in the body.
                else -> {
                    val kClass = Class.forName(classTemp).kotlin
                    v1 = body?.let { JSON().fromJsonToClass(kClass, it) }

                }
            }
            return function.call(content, v1)
        }
        else // no parameters required.
            return function.call(content)
    }
}