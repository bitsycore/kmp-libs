@file:JvmName("ExternalProviderReflectionKt")

package sh.bitsy.lib.kaddie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf

class ExternalProviderReflectionViewModel : ExternalProvider {

	override fun <T : Any> get(diContainer: DiContainer, klass: KClass<T>, vararg extraParam: Any): T? {
		if (extraParam.isEmpty()) return null
		val viewModelStoreOwner = extraParam[0] as? ViewModelStoreOwner ?: return null
		if (!klass.isSubclassOf(ViewModel::class)) return null
		@Suppress("UNCHECKED_CAST")
		val castedClazz = klass as? KClass<ViewModel> ?: return null
		@Suppress("UNCHECKED_CAST")
		return ifTypeViewModel(viewModelStoreOwner, extraParam, diContainer, castedClazz) as? T
	}

	private fun ifTypeViewModel(viewModelStoreOwner: ViewModelStoreOwner, extraParam: Array<out Any>, diContainer: DiContainer, klass: KClass<ViewModel>): ViewModel? {
		val constructor = klass.getConstructorForReflection()

		if ((constructor == null) || constructor.parameters.isEmpty()) {
			val instance = ViewModelProvider(viewModelStoreOwner)[klass]
			return instance
		}

		val instance = ViewModelProvider(viewModelStoreOwner, viewModelFactoryWithDependencies(constructor, constructor.getDependenciesAsMap(diContainer, extraParam)))[klass]

		return instance
	}

	private fun viewModelFactoryWithDependencies(
		constructor: KFunction<ViewModel>,
		dependencies: Map<KParameter, Any>
	) = object : ViewModelProvider.Factory {
		override fun <T : ViewModel> create(modelClass: Class<T>): T {
			@Suppress("UNCHECKED_CAST")
			return constructor.callBy(dependencies) as T
		}
	}
}