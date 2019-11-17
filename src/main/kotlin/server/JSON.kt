package server

import kotlin.reflect.full.memberProperties

class JSON(){
    fun mapToJson(membersMap:MutableList<MemberDTO>): String{
        var json = "[\n"

        for (member in membersMap){
            json += toJson(member)
        }

        json += "]"

        return json
    }

//    fun toJsonCorrectFormat(what: Any): String{
//        what
//            .
//    }


    fun toJson(what: Any): String =
        what::class.memberProperties
            .map { """  "${it.name}": ${jsonValueOf(it.call(what))}""" }
            .joinToString(",\n", "{\n", "\n}\n")

    fun jsonValueOf(value: Any?): String =
        when (value) {
            null -> "null"
            is Int -> value.toString()
            is Double -> value.toString()
            is String -> """"$value""""
            else -> toJson(value)
        }
}