package sh.bitsy.lib.kaddie

inline fun <reified T : Any> MutableDIContainer.register(instance: T) =
    register(T::class, instance)

inline fun <reified T : Any> MutableDIContainer.register(noinline constructor: DiContainer.() -> T) =
    register(T::class, constructor)

inline fun <reified T : Any> MutableDIContainer.remove() = remove(T::class)

inline fun <reified T : Any> DiContainer.get(): T = get(T::class)