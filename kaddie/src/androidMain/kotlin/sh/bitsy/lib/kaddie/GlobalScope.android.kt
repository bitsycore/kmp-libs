package sh.bitsy.lib.kaddie

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import kotlin.reflect.KClass

// Java Class<*> extension
fun <T : Any> registerDependency(clazz: Class<T>, dependency: T) = registerDependency(clazz.kotlin, dependency)
fun <T : Any> getDependency(clazz: Class<T>, vararg extraParam: Any = emptyArray()): T = getDependency(clazz.kotlin, extraParam)

@Composable
inline fun <reified VM : ViewModel> viewModelDependency(): VM =
	getDependency(
		VM::class,
		LocalContext.current as? ViewModelStoreOwner
			?: LocalViewModelStoreOwner.current
			?: error("No ViewModelStoreOwner found")
	)

inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModelDependency(): VM =
	getDependency(VM::class, this)

fun <VM : ViewModel> getViewModelDependency(klass: KClass<VM>, viewModelStoreOwner: ViewModelStoreOwner): VM =
	getDependency(klass = klass, viewModelStoreOwner)

fun <VM : ViewModel> getViewModelDependency(clazz: Class<VM>, viewModelStoreOwner: ViewModelStoreOwner): VM =
	getDependency(clazz = clazz, viewModelStoreOwner)