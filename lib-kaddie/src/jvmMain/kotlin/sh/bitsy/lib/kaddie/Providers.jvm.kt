package sh.bitsy.lib.kaddie

actual fun getDefaultDependencyProviders(): List<DependencyProvider> = listOf(
	JvmReflectiveProvider()
)