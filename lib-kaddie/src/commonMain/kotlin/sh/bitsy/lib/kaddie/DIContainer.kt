package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass

interface DiContainer {
    fun <T : Any> get(klass: KClass<T>, vararg extraParam: Any = emptyArray()): T
    fun <T : Any> has(klass: KClass<T>): Boolean
    fun getKeys(): Set<KClass<*>>
}

interface MutableDIContainer : DiContainer {
    fun <T : Any> add(klass: KClass<T>, instance: T)
    fun <T : Any> add(klass: KClass<T>, factory: DependencyFactory<T>)
    fun clear()
    fun remove(key: KClass<*>)
    fun registerDependencyProvider(dependencyProvider: DependencyProvider)
}

class DIContainerImpl : MutableDIContainer {
    private val dependenciesList = mutableMapOf<KClass<*>, Holder<*>>()
    private val dependencyProviders = mutableListOf<DependencyProvider>()

    override fun <T : Any> add(klass: KClass<T>, instance: T) {
        dependenciesList[klass] = InstanceHolder(instance)
    }

    override fun <T : Any> add(klass: KClass<T>, factory: DependencyFactory<T>) {
        dependenciesList[klass] = FactoryHolder(this, factory)
    }

    override fun <T : Any> has(klass: KClass<T>): Boolean = dependenciesList.containsKey(klass)

    override fun getKeys(): Set<KClass<*>> = dependenciesList.keys

    override fun clear() = dependenciesList.clear()

    override fun remove(key: KClass<*>) {
        dependenciesList.remove(key)
    }

    override fun registerDependencyProvider(dependencyProvider: DependencyProvider) {
        dependencyProviders.add(dependencyProvider)
    }

    override fun <T : Any> get(klass: KClass<T>, vararg extraParam: Any): T {
        val provider = dependenciesList[klass]
        if (provider == null) {
            return getFromDependencyProvider(this, klass, extraParam) ?: throw DependencyNotFoundException(klass.toString())
        } else {
            @Suppress("UNCHECKED_CAST")
            return provider.get() as T
        }
    }

    private fun <T : Any> getFromDependencyProvider(diContainer: DiContainer, klass: KClass<T>, extraParam: Array<out Any>): T? {
        for (provider in dependencyProviders) {
            val instance = provider.get(diContainer, klass, *extraParam)
            if (instance != null) {
                return instance
            }
        }
        return null
    }

}

class DependencyNotFoundException(message: String) : Exception("Dependency not found: $message")