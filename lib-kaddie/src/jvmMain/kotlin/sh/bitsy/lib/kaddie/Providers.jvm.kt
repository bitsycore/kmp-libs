package sh.bitsy.lib.kaddie

actual fun getDefaultProviders(): List<Provider> = listOf(
	JvmReflectiveProvider()
)