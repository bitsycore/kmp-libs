package sh.bitsy.lib.kaddie

import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

// ======================================================================
// COPY OF androidMain VERSION UNTIL COMMON JVM MAIN
// ======================================================================

fun KFunction<*>.getDependenciesAsMap(diContainer: DiContainer, extraParam: Array<out Any>): Map<KParameter, Any> {

	val dependencies = mutableMapOf<KParameter, Any>()

	parameters.forEach { param ->
		val paramClass =
			(if (param.kind == KParameter.Kind.INSTANCE) (param.type.classifier as KClass<*>).java.enclosingClass.kotlin
			else param.type.classifier as? KClass<*>)
				?: error("Cannot get type for parameter ${param.name}")

		// Check if that dependency was already provided
		try {
			diContainer.get(paramClass, extraParam).let {
				dependencies[param] = it
				return@forEach
			}
		} catch (_: Exception) {
		}

		// Ignore optional parameters
		if (!param.isOptional) {
			dependencies[param] = diContainer.get(paramClass, extraParam)
		}
	}
	return dependencies
}

fun <T : Any> KClass<T>.getConstructorForReflection() = primaryConstructor ?: constructors.firstOrNull() ?: emptyConstructor

val <T : Any> KClass<T>.emptyConstructor: KFunction<T>?
	get() = constructors.firstOrNull { it.parameters.isEmpty() }

val <R> KCallable<R>.haveOptionalParam: Boolean
	get() {
		parameters.forEach {
			if (it.isOptional) {
				return true
			}
		}
		return false
	}