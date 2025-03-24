package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass

interface DiContainer {
    fun <T : Any> get(klass: KClass<T>, vararg extraParam: Any = emptyArray()): T
    fun <T : Any> has(klass: KClass<T>): Boolean
    fun getKeys(): Set<KClass<*>>
}

interface MutableDIContainer : DiContainer {
    fun <T : Any> register(klass: KClass<T>, instance: T)
    fun <T : Any> register(klass: KClass<T>, constructor: DependencyConstructor<T>)
    fun clear()
    fun remove(key: KClass<*>)
    fun registerExternalProvider(externalProvider: ExternalProvider)
}

class DIContainerImpl : MutableDIContainer {
    private val dependencies = mutableMapOf<KClass<*>, SingleProvider<*>>()
    private val externalProviderList = mutableListOf<ExternalProvider>()

    override fun <T : Any> register(klass: KClass<T>, instance: T) {
        dependencies[klass] = InstanceProvider(instance)
    }

    override fun <T : Any> register(klass: KClass<T>, constructor: DependencyConstructor<T>) {
        dependencies[klass] = ConstructorProvider(this, constructor)
    }

    override fun <T : Any> has(klass: KClass<T>): Boolean = dependencies.containsKey(klass)

    override fun getKeys(): Set<KClass<*>> = dependencies.keys

    override fun clear() = dependencies.clear()

    override fun remove(key: KClass<*>) {
        dependencies.remove(key)
    }

    override fun registerExternalProvider(externalProvider: ExternalProvider) {
        externalProviderList.add(externalProvider)
    }

    override fun <T : Any> get(klass: KClass<T>, vararg extraParam: Any): T {
        val provider = dependencies[klass]
        if (provider == null) {
            return getFromExternalProvider(this, klass, extraParam) ?: throw DependencyNotFoundException(klass.toString())
        } else {
            @Suppress("UNCHECKED_CAST")
            return provider.get() as T
        }
    }

    private fun <T : Any> getFromExternalProvider(diContainer: DiContainer, klass: KClass<T>, extraParam: Array<out Any>): T? {
        for (provider in externalProviderList) {
            val instance = provider.get(diContainer, klass, *extraParam)
            if (instance != null) {
                return instance
            }
        }
        return null
    }

}

class DependencyNotFoundException(message: String) : Exception("Dependency not found: $message")