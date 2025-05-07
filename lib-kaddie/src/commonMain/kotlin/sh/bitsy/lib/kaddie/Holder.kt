package sh.bitsy.lib.kaddie

typealias DependencyFactory<T> = DiContainer.(Array<out Any>) -> T

internal interface Holder<T> {
    fun get(): T
}

internal class InstanceHolder<T>(private val instance: T) : Holder<T> {
    override fun get(): T = instance
}

internal class FactoryHolder<T>(private val diContainer: DiContainer, private val factory: DependencyFactory<T>, extraParam: Array<Any> = emptyArray()) : Holder<T> {
    val instance by lazy { factory.invoke(diContainer, extraParam) }
    override fun get(): T = instance
}