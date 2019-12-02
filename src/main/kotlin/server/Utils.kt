package server

class Utils(){
//    fun getMemberAsMap(member: MemberDTO):MutableMap<Int, MemberDTO>{
//        return mutableMapOf<Int, MemberDTO>((member.id to member))
//    }
    fun getMemberAsMap(anyValue: Any?):MutableMap<Any, Any>{
        if(anyValue == null) return mutableMapOf()
        if(anyValue is MutableMap<*, *>){
            return anyValue as MutableMap<Any, Any>
        }
        return mutableMapOf<Any, Any>(0 to anyValue)
    }
}