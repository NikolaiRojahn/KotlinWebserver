package server

/**
 * @param filename The filename to persist data to.
 */
class ClubContent(val filename:String):WebContent
{
    // Local collection of members.
    var gamers = mutableMapOf<Int, GamerDTO>(
        1 to GamerDTO(1, "Nikolai", 1000),
        2 to GamerDTO(2, "Claus", 2000),
        3 to GamerDTO(3, "Jörg", 3000),
        4 to GamerDTO(4, "Morten", 4000)
    )

    val nullMessage = "Given member is null"

    fun storeGamers(value:MutableMap<Int, GamerDTO>){gamers = value}

    fun getGamer(): MutableMap<Int, GamerDTO> = gamers

    fun getGamer(id: Int): GamerDTO {
        return gamers.getOrDefault(id, null) ?: throw Exception("Member with id $id not found.".format(id))
    }

    fun putGamer(gamer: GamerDTO): GamerDTO {
        if(gamer == null) { nullMessage }
        if (gamers.has(gamer))
        {
            gamers.replace(gamer.id, gamer)
            save()
            return gamer
        }
        return postGamer(gamer)
    }

<<<<<<< HEAD
    fun postGamer(gamer: GamerDTO): GamerDTO {
        if (gamer == null) {
            nullMessage
        }
        if (!gamers.has(gamer)) {
            gamers[gamer.id] = gamer
=======
    private fun postGamer(gamer: GamerDTO): GamerDTO {
        var g = GamerDTO(gamer.id, gamer.nickname, gamer.score)
        println(g)
        if (!gamers.containsKey(g.id)) {
            gamers[g.id] = g
>>>>>>> 483f0df9ba566e33e0674fe7ecd328c81cf3b3f2
            save()
            //return gamer
        }
        return gamer
    }

    // Extension method on MutableMap<Int, GamerDTO> to determine the existence of a particular GamerDTO in the map.
    fun MutableMap<Int, GamerDTO>.has(gamer:GamerDTO):Boolean{return this.containsKey(gamer.id)}

    // deletes a gamer and returns the value
    fun deleteGamer(id: Int): GamerDTO {
        val gamer = getGamer(id)
        if(gamer == null) { nullMessage }
        val gamerNew: GamerDTO = gamers.remove(gamer.id) ?: throw Exception("Member with id $gamer.id not found.".format(gamer.id))
        save()
        return gamerNew
    }

    override fun save() {
        utils.writeToFile(filename, JSON().toJsonFromMap(Utils().getMemberAsMap(gamers)))
    }

}