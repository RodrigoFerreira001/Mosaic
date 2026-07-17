package dev.catbit.mosaic.sample.core.schemas.tiles.code

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Renders a block of source code with syntax highlighting.
 *
 * **Updatable fields (via UpdateTiles):** `style: StyleSchema`,
 * `visibility: TileSchema.Visibility`, `code: String`, `language: Language`, `theme: Theme`
 *
 * **Notes:** [language] and [theme] are Mosaic-owned enums decoupled from any specific
 * highlighting library; the client renderer maps them to whatever syntax-highlighting
 * implementation it uses. Dark/light variant selection is left to the renderer (e.g. following
 * the system theme), not encoded in [theme].
 */
@Immutable
@Serializable
@SerialName("CodeViewer")
data class CodeViewerTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("code") val code: String,
    @SerialName("language") val language: Language,
    @SerialName("theme") val theme: Theme,
) : TileSchema {

    @Serializable
    enum class Language {
        DEFAULT,
        C,
        CPP,
        DART,
        JAVA,
        KOTLIN,
        RUST,
        CSHARP,
        COFFEESCRIPT,
        JAVASCRIPT,
        PERL,
        PYTHON,
        RUBY,
        SHELL,
        SWIFT,
        TYPESCRIPT,
        GO,
        PHP,
    }

    @Serializable
    enum class Theme {
        DARCULA,
        MONOKAI,
        NOTEPAD,
        MATRIX,
        PASTEL,
        ATOM_ONE,
    }
}
