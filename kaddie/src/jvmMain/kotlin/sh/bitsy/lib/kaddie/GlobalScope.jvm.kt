package sh.bitsy.lib.kaddie

// ======================================================================
// COPY OF androidMain VERSION UNTIL COMMON JVM MAIN
// ======================================================================

// Java Class<*> extension
fun <T : Any> registerDependency(clazz: Class<T>, dependency: T) = registerDependency(clazz.kotlin, dependency)
fun <T : Any> getDependency(clazz: Class<T>, vararg extraParam: Any = emptyArray()): T = getDependency(clazz.kotlin, extraParam)