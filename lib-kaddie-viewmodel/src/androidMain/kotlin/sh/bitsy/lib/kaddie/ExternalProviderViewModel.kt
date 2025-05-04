package sh.bitsy.lib.kaddie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf

@Composable
inline fun <reified VM : ViewModel> viewModelDependency(
	viewModelStoreOwner: ViewModelStoreOwner = LocalContext.current as? ViewModelStoreOwner
		?: LocalViewModelStoreOwner.current
		?: error("No ViewModelStoreOwner found")
): VM =
	getDependency(
		VM::class,
		viewModelStoreOwner
	)

inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModelDependency(): VM =
	getDependency(VM::class, this)

fun <VM : ViewModel> getViewModelDependency(klass: KClass<VM>, viewModelStoreOwner: ViewModelStoreOwner): VM =
	getDependency(klass = klass, viewModelStoreOwner)

fun <VM : ViewModel> getViewModelDependency(clazz: Class<VM>, viewModelStoreOwner: ViewModelStoreOwner): VM =
	getDependency(clazz = clazz, viewModelStoreOwner)

class ExternalProviderViewModel : ExternalProvider {

	override fun <T : Any> get(parentDependencies: DiContainer, klass: KClass<T>, vararg extraParam: Any): T? {
		if (extraParam.isEmpty()) return null
		val viewModelStoreOwner = extraParam[0] as? ViewModelStoreOwner ?: return null
		if (!klass.isSubclassOf(ViewModel::class)) return null
		@Suppress("UNCHECKED_CAST")
		val castedClazz = klass as? KClass<ViewModel> ?: return null
		@Suppress("UNCHECKED_CAST")
		return ifTypeViewModel(viewModelStoreOwner, extraParam, parentDependencies, castedClazz) as? T
	}

	private fun ifTypeViewModel(viewModelStoreOwner: ViewModelStoreOwner, extraParam: Array<out Any>, diContainer: DiContainer, klass: KClass<ViewModel>): ViewModel? {
		val constructor = klass.getConstructorForReflection()

		if ((constructor == null) || constructor.parameters.isEmpty()) {
			val instance = ViewModelProvider(viewModelStoreOwner)[klass]
			return instance
		}

		try {
			val instance = ViewModelProvider(viewModelStoreOwner, viewModelFactoryWithDependencies(constructor, constructor.getDependenciesAsMap(diContainer, extraParam)))[klass]
			return instance
		} catch (e: InvocationTargetException) {
			Log.e("ExternalProviderViewModel", "Error creating ViewModel: ", e.targetException)
			return null
		}
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