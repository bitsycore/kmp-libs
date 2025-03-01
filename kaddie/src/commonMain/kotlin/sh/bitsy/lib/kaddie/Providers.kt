package sh.bitsy.lib.kaddie

interface Provider<T> {
    fun get(): T
}

class InstanceProvider<T>(private val instance: T) : Provider<T> {
    override fun get(): T = instance
}

class ConstructorProvider<T>(private val diContainer: DiContainer, private val constructor: DiContainer.() -> T) :
    Provider<T> {
    val instance by lazy { constructor.invoke(diContainer) }
    override fun get(): T = instance
}