package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass

interface DiContainer {
    fun <T : Any> get(klass: KClass<T>, vararg extraParam: Any = emptyArray()): T
    fun <T : Any> has(klass: KClass<T>): Boolean
    fun getKeys(): Set<KClass<*>>
}

interface MutableDIContainer : DiContainer {
    fun <T : Any> register(klass: KClass<T>, instance: T)
    fun <T : Any> register(klass: KClass<T>, constructor: DependencyFactory<T>)
    fun clear()
    fun remove(key: KClass<*>)
    fun registerProvider(provider: Provider)
}

class DIContainerImpl : MutableDIContainer {
    private val dependenciesList = mutableMapOf<KClass<*>, Holder<*>>()
    private val providerList = mutableListOf<Provider>()

    override fun <T : Any> register(klass: KClass<T>, instance: T) {
        dependenciesList[klass] = InstanceHolder(instance)
    }

    override fun <T : Any> register(klass: KClass<T>, constructor: DependencyFactory<T>) {
        dependenciesList[klass] = FactoryHolder(this, constructor)
    }

    override fun <T : Any> has(klass: KClass<T>): Boolean = dependenciesList.containsKey(klass)

    override fun getKeys(): Set<KClass<*>> = dependenciesList.keys

    override fun clear() = dependenciesList.clear()

    override fun remove(key: KClass<*>) {
        dependenciesList.remove(key)
    }

    override fun registerProvider(provider: Provider) {
        providerList.add(provider)
    }

    override fun <T : Any> get(klass: KClass<T>, vararg extraParam: Any): T {
        val provider = dependenciesList[klass]
        if (provider == null) {
            return getFromExternalProvider(this, klass, extraParam) ?: throw DependencyNotFoundException(klass.toString())
        } else {
            @Suppress("UNCHECKED_CAST")
            return provider.get() as T
        }
    }

    private fun <T : Any> getFromExternalProvider(diContainer: DiContainer, klass: KClass<T>, extraParam: Array<out Any>): T? {
        for (provider in providerList) {
            val instance = provider.get(diContainer, klass, *extraParam)
            if (instance != null) {
                return instance
            }
        }
        return null
    }

}

class DependencyNotFoundException(message: String) : Exception("Dependency not found: $message")