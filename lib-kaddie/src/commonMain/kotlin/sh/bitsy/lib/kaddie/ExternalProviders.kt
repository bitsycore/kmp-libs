package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass

interface ExternalProvider {
	fun <T : Any> get(parentDependencies: DiContainer, klass: KClass<T>, vararg extraParam: Any): T?
}

expect fun getExternalProviders(): List<ExternalProvider>