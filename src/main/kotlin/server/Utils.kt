package server

class Utils(){
    fun getMemberAsMap(anyValue: Any?):MutableMap<Any, Any>{
        if(anyValue == null) return mutableMapOf()
        if(anyValue is MutableMap<*, *>){
            return anyValue as MutableMap<Any, Any>
        }
        return mutableMapOf<Any, Any>(0 to anyValue)
    }
}