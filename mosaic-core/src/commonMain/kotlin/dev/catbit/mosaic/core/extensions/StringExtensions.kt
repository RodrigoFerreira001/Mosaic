package dev.catbit.mosaic.core.extensions

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun randomUuid() = Uuid.random().toHexDashString()