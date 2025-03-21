package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass

// =================================
// MARK: DI CONTAINER
// =================================

internal val container = DIContainerImpl().also {
    val providers = getExternalProviders()
    providers.forEach { provider -> it.registerExternalProvider(provider) }
}

// =================================
// MARK: REGISTER
// =================================

/**
 * Register a dependency with an instance globally.
 */
inline fun <reified T : Any> registerDependency(instance: T) = registerDependency(T::class, instance)
/**
 * Register a dependency with an instance globally.
 */
fun <T : Any> registerDependency(klass: KClass<T>, instance: T) = container.register(klass, instance)

/**
 * Register a dependency with a constructor globally.
 * The constructor will be called when the dependency is requested.
 * Use get() in the constructor to retrieve a dependency.
 */
inline fun <reified T : Any> registerDependency(noinline constructor: DependencyConstructor<T>) =
    registerDependency(T::class, constructor)

/**
 * Register a dependency with a constructor globally.
 * The constructor will be called when the dependency is requested.
 * Use get() in the constructor to retrieve a dependency.
 */
fun <T : Any> registerDependency(klass: KClass<T>, constructor: DependencyConstructor<T>) =
    container.register(klass, constructor)

// =================================
// MARK: GET
// =================================

/**
 * Get a dependency stored globally.
 */
inline fun <reified T : Any> getDependency(vararg extraParam: Any = emptyArray()): T = getDependency(T::class, *extraParam)
/**
 * Get a dependency stored globally.
 */
fun <T : Any> getDependency(klass: KClass<T>, vararg extraParam: Any): T = container.get<T>(klass, *extraParam)