package sh.bitsy.lib.kaddie

inline fun <reified T : Any> MutableDIContainer.registerDependency(instance: T) =
    registerDependency(T::class, instance)

inline fun <reified T : Any> MutableDIContainer.registerDependency(noinline constructor: DependencyFactory<T>) =
    registerDependency(T::class, constructor)

inline fun <reified T : Any> MutableDIContainer.removeRegisteredDependency() = removeRegisteredDependency(T::class)

inline fun <reified T : Any> DiContainer.getDependency(vararg extraParam: Any): T = getDependency(T::class, extraParam)