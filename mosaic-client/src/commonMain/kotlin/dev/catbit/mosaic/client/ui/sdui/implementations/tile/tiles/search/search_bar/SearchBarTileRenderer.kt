package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.search.search_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.textOrNull
import dev.catbit.mosaic.client.ui.composables.material_symbols.MaterialSymbol
import dev.catbit.mosaic.client.ui.modifiers.handPointer
import dev.catbit.mosaic.client.ui.modifiers.styledWith
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.tiles.search.SearchBarTileSchema

object SearchBarTileRenderer : TileRenderer<SearchBarTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(
        tileSchema: SearchBarTileSchema,
    ) {
        with(tileSchema) {
            SearchBar(
                modifier = Modifier
                    .visible(isVisible())
                    .styledWith(style),
                query = query,
                onQueryChange = {
                    triggerEvent(
                        trigger = EventTriggers.onQueryChanged(),
                        data = it
                    )
                    dispatchEvent(SearchBarTileEvents.OnQueryChanged(it))
                },
                onSearch = {
                    triggerEvent(
                        trigger = EventTriggers.onSearch(),
                        data = query
                    )
                },
                placeholder = placeholder.textOrNull(),
                leadingIcon = leadingIcon?.let {
                    @Composable {
                        RenderChild(it)
                    }
                },
                trailingIcon = {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedVisibility(
                            visible = query.isNotEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            IconButton(
                                modifier = Modifier.handPointer(),
                                onClick = {
                                    triggerEvent(EventTriggers.onQueryCleared())
                                    dispatchEvent(SearchBarTileEvents.OnQueryCleared)
                                }
                            ) {
                                MaterialSymbol(
                                    iconName = "clear",
                                    size = 24.dp
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = query.isEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            trailingIcon?.let {
                                RenderChild(it)
                            }
                        }
                    }
                }
            )
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun SearchBar(
        modifier: Modifier = Modifier,
        query: String,
        onQueryChange: (String) -> Unit,
        onSearch: ((String) -> Unit)? = null,
        placeholder: (@Composable () -> Unit)? = null,
        leadingIcon: (@Composable () -> Unit)? = null,
        trailingIcon: (@Composable () -> Unit)? = null,
        enabled: Boolean = true,
        interactionSource: MutableInteractionSource? = null,
        shape: Shape = MaterialTheme.shapes.extraLarge,
        color: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor: Color = contentColorFor(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Surface(
            shape = shape,
            color = color,
            contentColor = contentColor,
            modifier = modifier.height(56.dp)
        ) {
            @Suppress("NAME_SHADOWING")
            val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

            val focused = interactionSource.collectIsFocusedAsState().value
            val focusRequester = remember { FocusRequester() }

            val colors = inputFieldColors()

            val textColor =
                LocalTextStyle.current.color.takeOrElse {
                    colors.textColor(
                        enabled,
                        isError = false,
                        focused = focused
                    )
                }

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester),
                enabled = enabled,
                singleLine = true,
                textStyle = LocalTextStyle.current.merge(TextStyle(color = textColor)),
                cursorBrush = SolidColor(colors.cursorColor(isError = false)),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch?.invoke(query) }),
                interactionSource = interactionSource,
                decorationBox =
                    @Composable { innerTextField ->
                        TextFieldDefaults.DecorationBox(
                            value = query,
                            innerTextField = innerTextField,
                            enabled = enabled,
                            singleLine = true,
                            visualTransformation = VisualTransformation.None,
                            interactionSource = interactionSource,
                            placeholder = placeholder,
                            leadingIcon =
                                leadingIcon?.let { leading ->
                                    { Box(Modifier.offset(x = 4.dp)) { leading() } }
                                },
                            trailingIcon =
                                trailingIcon?.let { trailing ->
                                    { Box(Modifier.offset(x = -(4.dp))) { trailing() } }
                                },
                            shape = SearchBarDefaults.inputFieldShape,
                            colors = colors,
                            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(),
                            container = {},
                        )
                    }
            )
        }
    }
}