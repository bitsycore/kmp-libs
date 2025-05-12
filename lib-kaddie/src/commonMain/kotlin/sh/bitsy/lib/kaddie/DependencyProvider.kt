package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass

interface DependencyProvider {
	fun <T : Any> getDependency(parentDependencies: DiContainer, klass: KClass<T>, vararg extraParam: Any): T?
}

internal expect fun getDefaultDependencyProviders(): List<DependencyProvider>