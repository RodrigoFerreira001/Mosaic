package dev.catbit.mosaic.server.ksp.constants

import com.squareup.kotlinpoet.ClassName

internal val eventModelClassName = ClassName("dev.catbit.mosaic.core.data.event", "EventModel")
internal val tileModelClassName = ClassName("dev.catbit.mosaic.core.data.tile", "TileModel")

internal val eventBuilderClassName = ClassName("dev.catbit.mosaic.server.builders.event", "EventBuilder")
internal val eventBuilderScopeClassName = ClassName("dev.catbit.mosaic.server.builders.event", "EventBuilderScope")

internal val tileBuilderClassName = ClassName("dev.catbit.mosaic.server.builders.tile", "TileBuilder")
internal val tileBuilderScopeClassName = ClassName("dev.catbit.mosaic.server.builders.tile", "TileBuilderScope")

internal val buildForClassName = ClassName("dev.catbit.mosaic.server.annotations", "BuildFor")