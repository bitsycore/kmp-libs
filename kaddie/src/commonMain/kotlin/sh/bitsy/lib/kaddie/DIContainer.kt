package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass

interface DiContainer {
    fun <T : Any> get(key: KClass<T>): T
    fun <T : Any> has(key: KClass<T>): Boolean
    fun getKeys(): Set<KClass<*>>
}

interface MutableDIContainer : DiContainer {
    fun <T : Any> register(klass: KClass<T>, instance: T)
    fun <T : Any> register(klass: KClass<T>, constructor: DiContainer.() -> T)
    fun clear()
    fun remove(key: KClass<*>)
}

class DIContainerImpl : MutableDIContainer {
    private val dependencies = mutableMapOf<KClass<*>, Provider<*>>()

    override fun <T : Any> register(klass: KClass<T>, instance: T) {
        dependencies[klass] = InstanceProvider(instance)
    }

    override fun <T : Any> register(klass: KClass<T>, constructor: DiContainer.() -> T) {
        dependencies[klass] = ConstructorProvider(this, constructor)
    }

    override fun <T : Any> get(key: KClass<T>): T {
        val provider = dependencies[key] ?: throw DependencyNotFoundException(key.toString())
        @Suppress("UNCHECKED_CAST")
        return provider.get() as T
    }

    override fun <T : Any> has(key: KClass<T>): Boolean = dependencies.containsKey(key)

    override fun getKeys(): Set<KClass<*>> {
        return dependencies.keys
    }

    override fun clear() = dependencies.clear()

    override fun remove(key: KClass<*>) {
        dependencies.remove(key)
    }

    class DependencyNotFoundException(message: String) : Exception("Dependency not found: $message")
}