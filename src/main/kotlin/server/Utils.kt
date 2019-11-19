package server

class Utils(){
    fun getMemberAsMap(member: MemberDTO):MutableMap<Int, MemberDTO>{
        return mutableMapOf<Int, MemberDTO>((member!!.id to member!!))
    }
}