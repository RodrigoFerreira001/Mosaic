package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.network.HttpMethod

/** HTTP `GET`. */
fun httpGet(): HttpMethod = HttpMethod.GET

/** HTTP `POST`. */
fun httpPost(): HttpMethod = HttpMethod.POST

/** HTTP `PUT`. */
fun httpPut(): HttpMethod = HttpMethod.PUT

/** HTTP `DELETE`. */
fun httpDelete(): HttpMethod = HttpMethod.DELETE

/** HTTP `PATCH`. */
fun httpPatch(): HttpMethod = HttpMethod.PATCH

/** HTTP `HEAD`. */
fun httpHead(): HttpMethod = HttpMethod.HEAD

/** HTTP `OPTIONS`. */
fun httpOptions(): HttpMethod = HttpMethod.OPTIONS

/** HTTP `TRACE`. */
fun httpTrace(): HttpMethod = HttpMethod.TRACE

/** HTTP `QUERY`. */
fun httpQuery(): HttpMethod = HttpMethod.QUERY
