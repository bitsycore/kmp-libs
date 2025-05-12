package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass

interface DiContainer {
    fun <T : Any> getDependency(klass: KClass<T>, vararg extraParam: Any = emptyArray()): T
    fun <T : Any> hasDependency(klass: KClass<T>): Boolean
    fun getKeys(): Set<KClass<*>>
}

interface MutableDIContainer : DiContainer {
    fun <T : Any> registerDependency(klass: KClass<T>, instance: T)
    fun <T : Any> registerDependency(klass: KClass<T>, factory: DependencyFactory<T>)
    fun clearDependencies()
    fun removeDependency(key: KClass<*>)
    fun registerDependencyProvider(dependencyProvider: DependencyProvider)
}

internal class DIContainerImpl : MutableDIContainer {
    private val dependenciesList = mutableMapOf<KClass<*>, Holder<*>>()
    private val dependencyProviders = mutableListOf<DependencyProvider>()

    override fun <T : Any> registerDependency(klass: KClass<T>, instance: T) {
        dependenciesList[klass] = InstanceHolder(instance)
    }

    override fun <T : Any> registerDependency(klass: KClass<T>, factory: DependencyFactory<T>) {
        dependenciesList[klass] = FactoryHolder(this, factory)
    }

    override fun <T : Any> hasDependency(klass: KClass<T>): Boolean = dependenciesList.containsKey(klass)

    override fun getKeys(): Set<KClass<*>> = dependenciesList.keys

    override fun clearDependencies() = dependenciesList.clear()

    override fun removeDependency(key: KClass<*>) {
        dependenciesList.remove(key)
    }

    override fun registerDependencyProvider(dependencyProvider: DependencyProvider) {
        dependencyProviders.add(dependencyProvider)
    }

    override fun <T : Any> getDependency(klass: KClass<T>, vararg extraParam: Any): T {
        val provider = dependenciesList[klass]
        if (provider == null) {
            return getFromDependencyProvider(this, klass, extraParam) ?: throw DependencyNotFoundException(klass.toString())
        } else {
            @Suppress("UNCHECKED_CAST")
            return provider.get() as T
        }
    }

    private fun <T : Any> getFromDependencyProvider(diContainer: DiContainer, klass: KClass<T>, extraParam: Array<out Any>): T? {
        for (provider in dependencyProviders.reversed()) {
            val instance = provider.getDependency(diContainer, klass, *extraParam)
            if (instance != null) {
                return instance
            }
        }
        return null
    }

}

class DependencyNotFoundException(message: String) : Exception("Dependency not found: $message")