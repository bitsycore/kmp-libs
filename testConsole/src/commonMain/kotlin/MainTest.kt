import sh.bitsy.lib.katstrace.Stacktrace
import sh.bitsy.lib.kloggy.LogLevels
import sh.bitsy.lib.kloggy.log
import sh.bitsy.test.Hello
import sh.bitsy.test.secondShow

@Suppress("NOTHING_TO_INLINE")
fun logFrame() {
    println("============================================================")
    println(Stacktrace(1))
    println("============================================================")
}

fun main() {
    logFrame()
    secondShow()
    Hello().show()
    val thro = Hello().getAThrowable()
    println("============================================================")
    println(Stacktrace(thro))
    println("============================================================")
    TestDI.testDI(546687754, 1, 1, 1, 1, 1, 1, 1, 1, LogLevels.Info, LogLevels.Info, LogLevels.Info)
    return

    testLog()
    TestDI.testDI(546687754, 1, 1, 1, 1, 1, 1, 1, 1, LogLevels.Info, LogLevels.Info, LogLevels.Info)
}

fun testLog() {
    //language=JSON
    val dummyJson : String = """
        {
          "posts": [
            {
              "content": "This is the content of the first post.",
              "createdAt": "2024-07-26T10:00:00Z",
              "id": 101,
              "title": "First Post"
            },
            {
              "content": "This is the content of the second post.",
              "createdAt": "2024-07-27T11:00:00Z",
              "id": 102,
              "title": "Second Post"
            }
          ],
          "settings": {
            "notifications": {
              "email": true,
              "sms": false
            },
            "theme": "dark"
          },
          "user": {
            "email": "john.doe@example.com",
            "id": 1,
            "isActive": true,
            "name": "John Doe"
          }
        }
    """.trimIndent()
    log("Main", "Hello, world!", LogLevels.Warning)
    log("Core", "Hello, world!", LogLevels.Error)
    log("MemoryHandler", "Hello, world!", LogLevels.Info)
    log("Main", "Hello, world!", LogLevels.Info)
    log("Main", "Hello, world!", LogLevels.Verbose)
    log("Main", "Hello, world!", LogLevels.Debug)
    log("Main", "Server Answer :\n$dummyJson", LogLevels.Debug)
    log("Main", "Hello, world!", LogLevels.Debug)
    log("Main", "Hello, world!", LogLevels.Verbose)
    log("UserManager", "Hello, world!", LogLevels.Warning)
    log("Main", "Hello, world!", LogLevels.Info)
    log("MemoryCleaner", "Hello, world!", LogLevels.Error)
    log("User", "Hello, world!", LogLevels.Warning)

    val stacktrace = Stacktrace().currentFrame

    println("Function Name: ${stacktrace.referencePath}")
    println("Function Name: ${stacktrace.methodName}")
}