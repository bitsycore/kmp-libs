package sh.bitsy.lib.kaddie

actual fun getExternalProviders(): List<ExternalProvider> = listOf(ExternalProviderJvmReflection(), ExternalProviderReflectionViewModel())
