package dev.catbit.mosaic.server.builder.event

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.server.builder.GenericBuilder

interface EventModelBuilder<out T : EventModel> : GenericBuilder<T>