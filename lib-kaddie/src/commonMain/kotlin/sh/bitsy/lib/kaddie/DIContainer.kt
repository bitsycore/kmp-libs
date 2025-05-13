package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass

interface DiContainer {
    fun <T : Any> getDependency(klass: KClass<T>, vararg extraParam: Any = emptyArray()): T
    fun <T : Any> hasRegisteredDependency(klass: KClass<T>): Boolean
    fun getRegisteredDependenciesKeys(): Set<KClass<*>>
}

interface MutableDIContainer : DiContainer {
    fun <T : Any> registerDependency(klass: KClass<T>, instance: T)
    fun <T : Any> registerDependency(klass: KClass<T>, factory: DependencyFactory<T>)
    fun clearRegisteredDependencies()
    fun removeRegisteredDependency(klass: KClass<*>)
    fun registerDependencyProvider(dependencyProvider: DependencyProvider)
    fun removeDependencyProvider(dependencyProvider: DependencyProvider)
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

    override fun <T : Any> hasRegisteredDependency(klass: KClass<T>): Boolean = dependenciesList.containsKey(klass)

    override fun getRegisteredDependenciesKeys(): Set<KClass<*>> = dependenciesList.keys

    override fun clearRegisteredDependencies() = dependenciesList.clear()

    override fun removeRegisteredDependency(klass: KClass<*>) {
        dependenciesList.remove(klass)
    }

    override fun registerDependencyProvider(dependencyProvider: DependencyProvider) {
        dependencyProviders.add(dependencyProvider)
    }

    override fun removeDependencyProvider(dependencyProvider: DependencyProvider) {
        dependencyProviders.remove(dependencyProvider)
    }

    override fun <T : Any> getDependency(klass: KClass<T>, vararg extraParam: Any): T {
        val dependency = dependenciesList[klass]
        if (dependency == null) {
            return getFromDependencyProvider(this, klass, extraParam)
        } else {
            @Suppress("UNCHECKED_CAST")
            return dependency.get() as T
        }
    }

    private fun <T : Any> getFromDependencyProvider(diContainer: DiContainer, klass: KClass<T>, extraParam: Array<out Any>): T {
        for (provider in dependencyProviders.reversed()) {
            try {
                val instance = provider.getDependency(diContainer, klass, *extraParam)
                if (instance != null) {
                    return instance
                }
            } catch (e: Exception) {
                throw DependencyInstancingException(e.message ?: "Unknown error")
            }
        }
        throw DependencyNotFoundException(klass.toString())
    }
}

class DependencyInstancingException(message: String) : Exception("Dependency instancing error: $message")
class DependencyNotFoundException(message: String) : Exception("Dependency not found: $message")