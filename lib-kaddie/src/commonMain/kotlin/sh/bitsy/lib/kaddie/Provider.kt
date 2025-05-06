package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass

interface Provider {
	fun <T : Any> get(parentDependencies: DiContainer, klass: KClass<T>, vararg extraParam: Any): T?
}

expect fun getDefaultProviders(): List<Provider>