package sh.bitsy.lib.kaddie

inline fun <reified T : Any> MutableDIContainer.registerDependency(instance: T) =
    registerDependency(T::class, instance)

inline fun <reified T : Any> MutableDIContainer.registerDependency(noinline constructor: DiContainer.(Array<out Any>) -> T) =
    registerDependency(T::class, constructor)

inline fun <reified T : Any> MutableDIContainer.removeDependency() = removeDependency(T::class)

inline fun <reified T : Any> DiContainer.getDependency(vararg extraParam: Any): T = getDependency(T::class, extraParam)