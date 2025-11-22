package dev.catbit.mosaic.server.builders.event

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.server.builders.GenericBuilder

interface EventBuilder<out T : EventModel> : GenericBuilder<T>