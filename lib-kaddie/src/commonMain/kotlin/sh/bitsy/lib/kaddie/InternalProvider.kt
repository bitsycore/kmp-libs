package sh.bitsy.lib.kaddie

typealias DependencyConstructor<T> = DiContainer.(Array<out Any>) -> T

internal interface InternalProvider<T> {
    fun get(): T
}

internal class InstanceProvider<T>(private val instance: T) : InternalProvider<T> {
    override fun get(): T = instance
}

internal class ConstructorProvider<T>(private val diContainer: DiContainer, private val constructor: DependencyConstructor<T>, extraParam: Array<Any> = emptyArray()) : InternalProvider<T> {
    val instance by lazy { constructor.invoke(diContainer, extraParam) }
    override fun get(): T = instance
}