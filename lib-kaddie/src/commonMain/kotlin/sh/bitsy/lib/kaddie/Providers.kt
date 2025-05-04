package sh.bitsy.lib.kaddie

typealias DependencyConstructor<T> = DiContainer.(Array<out Any>) -> T

interface SingleProvider<T> {
    fun get(): T
}

class InstanceProvider<T>(private val instance: T) : SingleProvider<T> {
    override fun get(): T = instance
}

class ConstructorProvider<T>(private val diContainer: DiContainer, private val constructor: DependencyConstructor<T>, extraParam: Array<Any> = emptyArray()) : SingleProvider<T> {
    val instance by lazy { constructor.invoke(diContainer, extraParam) }
    override fun get(): T = instance
}