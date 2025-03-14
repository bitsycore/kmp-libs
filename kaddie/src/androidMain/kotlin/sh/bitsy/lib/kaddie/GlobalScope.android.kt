package sh.bitsy.lib.kaddie

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import kotlin.reflect.KClass

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