package server

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.full.memberProperties

class JSON(){

    //To JSON methods
    fun toJsonFromMap(membersMap:MutableMap<Any, Any>): String{
        var count = 0
        var json = StringBuilder()
        if(membersMap.size > 1) {
            json.append("[")
        }
        membersMap.forEach {
            count++
            if(membersMap.size == count){
                json.append(toJsonBodyByClass(it.value, false))
            } else {
                json.append(toJsonBodyByClass(it.value, true))
            }}
        if(membersMap.size > 1){
            json.append("]")
        }
        return json.toString()
    }

    private fun toJsonBodyByClass(what: Any, endComma: Boolean): String {
        return what::class.memberProperties
            .map { """"${it.name}": ${toJsonCorrectValueFormat(it.call(what),endComma)}""" }
            .joinToString(",", "{", if(endComma) {"},"} else{"}"}
            )
    }

    private fun toJsonCorrectValueFormat(value: Any?, endComma: Boolean): String =
        when (value) {
            null -> "null"
            is Int -> value.toString()
            is Double -> value.toString()
            is String -> """"$value""""
            else -> toJsonBodyByClass(value, endComma)
        }

    //From JSON methods
    fun fromJsonToObjectInstance(type: KClass<*>, json: String): Any {
        val pairs = fromJsonNameAndValuesToMap(json)

        val initializer =  type.constructors.first()

        val parameterValues= Array<Any?>(initializer.parameters.size){}
        for (parameter in initializer.parameters) {
            val name = parameter.name
            val index = parameter.index
            val type = parameter.type.classifier
            val text = pairs[name]
            if (type == null || text == null) continue
            val value = fromJsonCorrectValueFormat(text, type)
            parameterValues[index] = value
        }

        return initializer.call(*parameterValues)
    }

    private fun fromJsonCorrectValueFormat(text: String, type: KClassifier): Any? =
        when (type) {
            String::class -> text.substring(1, text.length - 1)
            Int::class -> text.toInt()
            Double::class -> text.toDouble()
            else -> null
        }

    private fun fromJsonNameAndValuesToMap(jsonStr: String): MutableMap<String, String> {
        val lines = jsonStr.split("[,{}]".toRegex()).map { it.trim() }.filter { it.isNotBlank() }
        val propertyPairs = mutableMapOf<String, String>()
        for (line in lines) {
            val parts = line.split(":")
            var jsonName = parts[0].trim()
            jsonName = jsonName.substring(1, jsonName.length - 1)
            val jsonValue = parts[1].trim()
            propertyPairs[jsonName] = jsonValue
        }
        return propertyPairs
    }

}