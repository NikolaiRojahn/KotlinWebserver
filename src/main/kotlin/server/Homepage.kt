package server

class Homepage {
    fun generate(name: String): String {
        return """
            <!DOCTYPE html>
            <html>
                <head></head>
                <body>
                    <h1>Hello $name, how are you today?</h1>
                </body>
            </Html>        
        """.trimIndent()
    }
}