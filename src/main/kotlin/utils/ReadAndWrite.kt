package utils

import choir.Address
import choir.Member
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

// reads a txt file
fun readFromFile(path: String, split: String): List<String> =
    File(path)
        .readLines()
        .map { line: String -> line.split(split) }
        .flatten()

// write to a txt file
fun writeToFile(path: String, membersAsJSON: String) //= File(path).writeText(member, Charsets.UTF_8)
{
    File(path).writeText(membersAsJSON, Charsets.UTF_8)
}

// convert a object to a json String
fun toJson(member: MutableList<Member>): String = Gson().toJson(member) //json String

// converts a json String into a String format
fun fromJson(list: String): List<Member> {
    val gson = Gson()
    val jsonString = gson.toJson(list)
    val mType = object : TypeToken<List<Member>>() {}.type
    return gson.fromJson(jsonString, mType)
}

// main
fun main() {
    // list of members
    val m1 = Member("Joe", "Oertel", "28123663", address = Address("Copenhagen", "2800"))
    val m2 = Member("Claus", "Kramath", "28123663", address = Address("Copenhagen", "2800"))
    val m3 = Member("Nikolai", "Rojahn", "28123663", address = Address("Copenhagen", "2800"))
    val m4 = Member("Morten", "Feldt", "28123663", address = Address("Copenhagen", "2800"))
    // mutable members list
    val members = mutableListOf<Member>()
    // add members to list
    members.add(m1)
    members.add(m2)
    members.add(m3)
    members.add(m4)

    writeToFile("member.txt", toJson(members))

    readFromFile("member.txt", " ")
        .forEach {
            println(it)
        }
}