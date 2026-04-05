package dev.catbit.mosaic.endpoints.local_compositions

import dev.catbit.mosaic.server.builder.composition_local.compositionLocalOf

val LocalNameProvider = compositionLocalOf<String> { error("Name not provided") }