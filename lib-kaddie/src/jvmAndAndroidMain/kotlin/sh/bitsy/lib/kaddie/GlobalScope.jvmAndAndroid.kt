package sh.bitsy.lib.kaddie

// Java Class<*> extension
fun <T : Any> registerDependency(clazz: Class<T>, dependency: T) = add(clazz.kotlin, dependency)
fun <T : Any> getDependency(clazz: Class<T>, vararg extraParam: Any = emptyArray()): T = getDependency(clazz.kotlin, extraParam)