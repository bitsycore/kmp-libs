package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass
import kotlin.reflect.jvm.javaConstructor

class ExternalProviderJvmReflection : ExternalProvider {

	val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()

	override fun <T : Any> get(parentDependencies: DiContainer, klass: KClass<T>, vararg extraParam: Any): T? {
		try {
			@Suppress("UNCHECKED_CAST")
			dependencies[klass]?.let { return it as? T }
			buildFromReflection(parentDependencies, klass, extraParam).let { return it }
		} catch (_: Exception) {}
		return null
	}

	private fun <T : Any> buildFromReflection(parentDependencies: DiContainer, klass: KClass<T>, extraParam: Array<out Any>) : T {
		val constructor = klass.getConstructorForReflection() ?: throw error("Cannot find constructor for $klass")

		// Shortcut for empty constructor
		if (constructor.parameters.isEmpty()) {
			val instance = constructor.call()
			dependencies[klass] = instance
			return instance
		}

		// Get required dependencies
		val dependenciesMap = constructor.getDependenciesAsMap(parentDependencies, extraParam)

		val instance = if (!klass.isInner || constructor.haveOptionalParam) {
			constructor.callBy(dependenciesMap)
		} else {
			// Java Inner Class doesn't include kotlin inner class parameter identifier
			// but it is still required for a callBy ... so we need to use the constructor
			// in java directly
			constructor.javaConstructor?.newInstance(*dependenciesMap.values.toTypedArray()) ?:
			throw error("Cannot create instance of class: $klass")
		}

		dependencies[klass] = instance

		return instance
	}
}