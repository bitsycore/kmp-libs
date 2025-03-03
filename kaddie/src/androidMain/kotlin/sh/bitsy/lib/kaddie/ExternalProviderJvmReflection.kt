/*==============================================================================

 Copyright (c) Spirtech 2024. All rights reserved.

 THIS FILE IS THE PROPERTY OF SPIRTECH SAS.
 IT CANNOT BE COPIED, TRANSMITTED, USED IN ANY WAY WITHOUT PRIOR WRITTEN AUTHORIZATION FROM SPIRTECH.
 FOR ANY QUESTION, CONTACT: spirtech@spirtech.com

 Author: DWA

 =============================================================================*/

package sh.bitsy.lib.kaddie

import kotlin.reflect.KClass
import kotlin.reflect.jvm.javaConstructor

class ExternalProviderJvmReflection : ExternalProvider {

	val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()

	override fun <T : Any> get(diContainer: DiContainer, clazz: KClass<T>, vararg extraParam: Any): T? {
		try {
			@Suppress("UNCHECKED_CAST")
			dependencies[clazz]?.let { return it as? T }
			buildFromReflection(diContainer, clazz, extraParam).let { return it }
		} catch (_: Exception) {}
		return null
	}

	private fun <T : Any> buildFromReflection(diContainer: DiContainer, clazz: KClass<T>, extraParam: Array<out Any>) : T {
		val constructor = clazz.getConstructorForReflection() ?: throw error("Cannot find constructor for $clazz")

		// Shortcut for empty constructor
		if (constructor.parameters.isEmpty()) {
			val instance = constructor.call()
			dependencies[clazz] = instance
			return instance
		}

		// Get required dependencies
		val dependencies = constructor.getDependenciesAsMap(diContainer, extraParam)

		val instance = if (!clazz.isInner || constructor.haveOptionalParam) {
			constructor.callBy(dependencies)
		} else {
			// Java Inner Class doesn't include kotlin inner class parameter identifier
			// but it is still required for a callBy ... so we need to use the constructor
			// in java directly
			constructor.javaConstructor?.newInstance(*dependencies.values.toTypedArray()) ?:
			throw error("Cannot create instance of class: $clazz")
		}

		return instance
	}
}