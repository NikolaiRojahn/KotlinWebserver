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

    fun getGamer(): MutableMap<Int, GamerDTO> = gamers

    fun getGamer(id: Int): GamerDTO {
        return gamers.getOrDefault(id, null) ?: throw Exception("Member with id $id not found.".format(id))
    }

    fun putGamer(gamer: GamerDTO): GamerDTO {
        if(gamer == null) { nullMessage }
        if(gamers.containsKey(gamer.id)) {
            gamers.replace(gamer.id, gamer)
        }
        return postGamer(gamer)
    }

    fun postGamer(gamer: GamerDTO): GamerDTO {
        if (gamer == null) {
            nullMessage
        }
        if (!gamers.containsKey(gamer.id)) {
            gamers[gamer.id] = gamer
            return gamer
        }
        return gamer
    }
    // deletes a gamer but resturns the vale
    fun deleteGamer(id: Int): GamerDTO {
        val gamer = getGamer(id)
        if(gamer == null) { nullMessage }
        return gamers.remove(gamer.id) ?: throw Exception("Member with id $gamer.id not found.".format(gamer.id))
    }

    override fun save() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}