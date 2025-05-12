package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass

// =================================
// MARK: DI CONTAINER
// =================================

internal val GLOBAL_CONTAINER = DIContainerImpl().also {
    val providers = getDefaultDependencyProviders()
    providers.forEach { provider -> it.registerDependencyProvider(provider) }
}

fun registerDependencyProvider(provider: DependencyProvider) = GLOBAL_CONTAINER.registerDependencyProvider(provider)

// =================================
// MARK: REGISTER
// =================================

/**
 * Register a dependency with an instance globally.
 */
inline fun <reified T : Any> add(instance: T) = add(T::class, instance)
/**
 * Register a dependency with an instance globally.
 */
fun <T : Any> add(klass: KClass<T>, instance: T) = GLOBAL_CONTAINER.add(klass, instance)

/**
 * Register a dependency with a constructor globally.
 * The constructor will be called when the dependency is requested.
 * Use get() in the constructor to retrieve a dependency.
 */
inline fun <reified T : Any> add(noinline factory: DependencyFactory<T>) =
    add(T::class, factory)

/**
 * Register a dependency with a constructor globally.
 * The constructor will be called when the dependency is requested.
 * Use get() in the constructor to retrieve a dependency.
 */
fun <T : Any> add(klass: KClass<T>, factory: DependencyFactory<T>) =
    GLOBAL_CONTAINER.add(klass, factory)

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
fun <T : Any> getDependency(klass: KClass<T>, vararg extraParam: Any): T = GLOBAL_CONTAINER.get<T>(klass, *extraParam)