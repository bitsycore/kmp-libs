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
inline fun <reified T : Any> registerDependency(instance: T) = registerDependency(T::class, instance)
/**
 * Register a dependency with an instance globally.
 */
fun <T : Any> registerDependency(klass: KClass<T>, instance: T) = GLOBAL_CONTAINER.registerDependency(klass, instance)

/**
 * Register a dependency with a constructor globally.
 * The constructor will be called when the dependency is requested.
 * Use get() in the constructor to retrieve a dependency.
 */
inline fun <reified T : Any> registerDependency(noinline factory: DependencyFactory<T>) =
    registerDependency(T::class, factory)

/**
 * Register a dependency with a constructor globally.
 * The constructor will be called when the dependency is requested.
 * Use get() in the constructor to retrieve a dependency.
 */
fun <T : Any> registerDependency(klass: KClass<T>, factory: DependencyFactory<T>) =
    GLOBAL_CONTAINER.registerDependency(klass, factory)

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
fun <T : Any> getDependency(klass: KClass<T>, vararg extraParam: Any): T = GLOBAL_CONTAINER.getDependency<T>(klass, *extraParam)