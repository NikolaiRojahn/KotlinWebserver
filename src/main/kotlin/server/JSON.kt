package server

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.full.memberProperties

class JSON(){
    //To JSON methods
    fun toJsonFromMap(membersMap:MutableMap<Int, MemberDTO>): String{
        var json = ""
        if(membersMap.size > 1) { json += "[\n"}
        membersMap.forEach { json += toJsonBodyByClass(it.value) }
        if(membersMap.size > 1) { json += "]"}
        if(membersMap.size > 1) { json = toJsonRemoveLastComma(json) }
        return json
    }

    private fun toJsonBodyByClass(what: Any): String =
        what::class.memberProperties
            .map { """${indent(4)}"${it.name}": ${toJsonCorrectValueFormat(it.call(what))}""" }
            .joinToString(",\n", "${indent(2)}{\n", "\n${indent(2)}},\n")

    private fun toJsonCorrectValueFormat(value: Any?): String =
        when (value) {
            null -> "null"
            is Int -> value.toString()
            is Double -> value.toString()
            is String -> """"$value""""
            else -> toJsonBodyByClass(value)
        }

    private fun toJsonRemoveLastComma(jsonStr:String): String =
        jsonStr.substring(0, jsonStr.length - 3) + jsonStr.substring(jsonStr.length - 2)

    private fun indent(count: Int): String{
        val strIndent = " "
        var strIndentFinal = ""
        for (x in 0 until count)
            strIndentFinal += strIndent
        return strIndentFinal
    }

    //From JSON methods
    fun fromJsonToClass(type: KClass<*>, json: String): Any? {
        val member = fromJsonToObjectInstance(type, json)
        if (member is MemberDTO)
            return MemberDTO(member.id, member.name)
        return null
    }

    private fun fromJsonToObjectInstance(type: KClass<*>, json: String): Any {
        val pairs = fromJsonNameAndValuesToMap(json)

        val initializer =  type.constructors.first()

        val paramaterValues = arrayOf<Any?>(null, null)
        for (parameter in initializer.parameters) {
            val name = parameter.name
            val index = parameter.index
            val type = parameter.type.classifier
            val text = pairs[name]
            if (type == null || text == null) continue
            val value = fromJsonCorrectValueFormat(text, type)
            paramaterValues[index] = value
        }

        return initializer.call(*paramaterValues)
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