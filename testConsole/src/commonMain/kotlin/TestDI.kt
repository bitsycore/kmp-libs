import sh.bitsy.lib.kaddie.get
import sh.bitsy.lib.kaddie.getDependency
import sh.bitsy.lib.kaddie.registerDependency
import sh.bitsy.lib.kloggy.LogLevels
import kotlin.reflect.KClass

class UserRepository {
    fun findById(id: Int): String = "User$id"
}

class UserServices(private val userRepository: UserRepository) {
    fun getUser(id: Int): String = userRepository.findById(id)

    fun test () {
        val clazz: KClass<out UserServices> = this::class
        getDependency(clazz)
        println("Test")
    }
}

object HelloWorld {
    fun sayHello() {
        println("Hello, world!")
    }

    class Test50 {
        fun sayHello() {
            println("Hello, world!")
        }
    }
}

object TestDI {


    fun testDI(test: Int, test2: Int, test3: Int, test4: Int, test5: Int, test6: Int, test7: Int, test8: Int, test9: Int, lvl1 : LogLevels, lvl2 : LogLevels, lvl3 : LogLevels) : LogLevels {
        logFrame()

        registerDependency { UserRepository() }
        registerDependency { UserServices(get()) }
        registerDependency(HelloWorld)
        registerDependency(HelloWorld.Test50())

        val username = getDependency<UserServices>().getUser(125)
        println("Username: $username")
        val test = getDependency<HelloWorld>()
        test.sayHello()
        val test50 = getDependency<HelloWorld.Test50>()
        test50.sayHello()

        return LogLevels.Debug
    }

}