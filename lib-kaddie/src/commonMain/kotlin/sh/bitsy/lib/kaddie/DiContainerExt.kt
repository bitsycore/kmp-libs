package sh.bitsy.lib.kaddie

inline fun <reified T : Any> MutableDIContainer.add(instance: T) =
    this@register.add(T::class, instance)

inline fun <reified T : Any> MutableDIContainer.add(noinline constructor: DiContainer.(Array<out Any>) -> T) =
    this@register.add(T::class, constructor)

inline fun <reified T : Any> MutableDIContainer.remove() = remove(T::class)

inline fun <reified T : Any> DiContainer.get(vararg extraParam: Any): T = get(T::class, extraParam)