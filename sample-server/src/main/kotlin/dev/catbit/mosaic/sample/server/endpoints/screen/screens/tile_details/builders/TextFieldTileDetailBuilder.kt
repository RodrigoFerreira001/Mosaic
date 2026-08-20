package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.event.builders.overlays.snackbar.DisplaySnackbar
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.mappedIncomingTileUpdateData
import dev.catbit.mosaic.server.builder.icon.icon
import dev.catbit.mosaic.server.builder.placement.arrangeHorizontallySpacedBy
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.grouping.FlowRow
import dev.catbit.mosaic.server.builder.tile.builders.inputs.TextField
import dev.catbit.mosaic.server.builder.tile.builders.inputs.errorTextField
import dev.catbit.mosaic.server.builder.tile.builders.inputs.filledTextField
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardCapitalizationCharacters
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardCapitalizationNone
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardCapitalizationSentences
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardCapitalizationUnspecified
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardCapitalizationWords
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardImeActionDone
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardImeActionGo
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardImeActionNext
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardImeActionPrevious
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardImeActionSearch
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardImeActionSend
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardOptions
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardTypeAscii
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardTypeDecimal
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardTypeEmail
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardTypeNumber
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardTypeNumberPassword
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardTypePassword
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardTypePhone
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardTypeText
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardTypeUnspecified
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardTypeUri
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardVisualTransformationCustom
import dev.catbit.mosaic.server.builder.tile.builders.inputs.keyboardVisualTransformationPassword
import dev.catbit.mosaic.server.builder.tile.builders.inputs.normalTextField
import dev.catbit.mosaic.server.builder.tile.builders.inputs.outlinedTextField
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyLabelMedium

object TextFieldTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "TextField"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                description = "A Material 3 text field (filled or outlined) with rich decoration and full " +
                    "keyboard configuration. value is fully server-controlled: on every OnTextChanged, the " +
                    "server needs to resend value via UpdateTiles to keep the displayed text in sync — " +
                    "otherwise the field simply ignores what the user visually typed on the next render. " +
                    "Every configurable property below has its own live example."
            )

            ShowroomSectionTitle("Live demo — value synced to a preview")
            ShowroomDemoCard(title = "Type — the preview below mirrors the value live") {
                TextField(
                    id = "text_field_demo_input",
                    label = "Your name",
                    placeholder = "Type here",
                    kind = outlinedTextField(),
                    events = {
                        UpdateTiles(
                            trigger = EventTriggers.onTextChanged(),
                            updates = {
                                update(
                                    tileId = "text_field_demo_preview",
                                    updateData = mappedIncomingTileUpdateData("text" to "<//>")
                                )
                            }
                        )
                    }
                )
                SimpleText(
                    id = "text_field_demo_preview",
                    text = "The preview appears here as you type"
                )
            }

            ShowroomSectionTitle("kind — FILLED vs OUTLINED")
            ShowroomDemoCard(title = "The 2 visual variants") {
                FlowRow(horizontalArrangement = arrangeHorizontallySpacedBy(16)) {
                    LabeledField("filledTextField()") {
                        TextField(label = "Filled", kind = filledTextField())
                    }
                    LabeledField("outlinedTextField()") {
                        TextField(label = "Outlined", kind = outlinedTextField())
                    }
                }
            }

            ShowroomSectionTitle("state — NORMAL vs ERROR")
            ShowroomDemoCard(title = "ERROR switches to Material's error styling") {
                FlowRow(horizontalArrangement = arrangeHorizontallySpacedBy(16)) {
                    LabeledField("normalTextField()") {
                        TextField(label = "Email", state = normalTextField(), supportingText = "We'll never share it")
                    }
                    LabeledField("errorTextField()") {
                        TextField(label = "Email", state = errorTextField(), supportingText = "Invalid email address")
                    }
                }
            }

            ShowroomSectionTitle("enabled")
            ShowroomDemoCard(title = "enabled = false") {
                TextField(label = "Read-only", value = "Can't edit this", enabled = false)
            }

            ShowroomSectionTitle("leadingIcon / trailingIcon — plain vs clickable")
            ShowroomDemoCard(title = "clickableLeadingIcon and clickableTrailingIcon fire their own trigger") {
                FlowRow(horizontalArrangement = arrangeHorizontallySpacedBy(16)) {
                    LabeledField("leadingIcon (plain)") {
                        TextField(label = "Amount", leadingIcon = icon("attach_money"))
                    }
                    LabeledField("trailingIcon, clickableTrailingIcon = true (default)") {
                        TextField(
                            label = "Search",
                            trailingIcon = icon("close"),
                            events = {
                                DisplaySnackbar(trigger = EventTriggers.onTrailingIconClick(), message = "Trailing icon clicked — clear it server-side")
                            }
                        )
                    }
                    LabeledField("leadingIcon, clickableLeadingIcon = true") {
                        TextField(
                            label = "Location",
                            leadingIcon = icon("my_location"),
                            clickableLeadingIcon = true,
                            events = {
                                DisplaySnackbar(trigger = EventTriggers.onLeadingIconClick(), message = "Leading icon clicked — use my location")
                            }
                        )
                    }
                }
            }

            ShowroomSectionTitle("prefixText / suffixText")
            ShowroomDemoCard(title = "Fixed text inside the field, before/after the value") {
                FlowRow(horizontalArrangement = arrangeHorizontallySpacedBy(16)) {
                    LabeledField("prefixText") {
                        TextField(label = "Website", prefixText = "https://")
                    }
                    LabeledField("suffixText") {
                        TextField(label = "Weight", suffixText = "kg")
                    }
                }
            }

            ShowroomSectionTitle("minLines / maxLines")
            ShowroomDemoCard(title = "minLines = 3, maxLines = 5 — a multi-line comment box") {
                TextField(label = "Comment", placeholder = "Write a few lines...", minLines = 3, maxLines = 5)
            }

            ShowroomSectionTitle("visualTransformation")
            ShowroomDemoCard(title = "None (default), password dots, custom mask") {
                FlowRow(horizontalArrangement = arrangeHorizontallySpacedBy(16)) {
                    LabeledField("keyboardVisualTransformationPassword()") {
                        TextField(
                            label = "Password",
                            value = "hunter2",
                            visualTransformation = keyboardVisualTransformationPassword(),
                            keyboardOptions = keyboardOptions(keyboardType = keyboardTypePassword())
                        )
                    }
                    LabeledField("keyboardVisualTransformationCustom(\"###-###\")") {
                        TextField(
                            label = "Code",
                            value = "123456",
                            visualTransformation = keyboardVisualTransformationCustom("###-###"),
                            keyboardOptions = keyboardOptions(keyboardType = keyboardTypeNumber())
                        )
                    }
                }
            }

            ShowroomSectionTitle("keyboardOptions.keyboardType — every layout")
            ShowroomDemoCard(title = "Tap into each field to see the native keyboard switch (most visible on Android/iOS)") {
                Column(arrangement = arrangeVerticallySpacedBy(12)) {
                    listOf(
                        "Unspecified" to keyboardTypeUnspecified(),
                        "Text" to keyboardTypeText(),
                        "Ascii" to keyboardTypeAscii(),
                        "Number" to keyboardTypeNumber(),
                        "Phone" to keyboardTypePhone(),
                        "Uri" to keyboardTypeUri(),
                        "Email" to keyboardTypeEmail(),
                        "Password" to keyboardTypePassword(),
                        "NumberPassword" to keyboardTypeNumberPassword(),
                        "Decimal" to keyboardTypeDecimal()
                    ).forEach { (label, type) ->
                        LabeledField("keyboardType$label()") {
                            TextField(keyboardOptions = keyboardOptions(keyboardType = type))
                        }
                    }
                }
            }

            ShowroomSectionTitle("keyboardOptions.capitalization — every strategy")
            ShowroomDemoCard(title = "Type lowercase text into each and watch auto-capitalization behave differently") {
                Column(arrangement = arrangeVerticallySpacedBy(12)) {
                    listOf(
                        "keyboardCapitalizationUnspecified()" to keyboardCapitalizationUnspecified(),
                        "keyboardCapitalizationNone()" to keyboardCapitalizationNone(),
                        "keyboardCapitalizationCharacters()" to keyboardCapitalizationCharacters(),
                        "keyboardCapitalizationWords()" to keyboardCapitalizationWords(),
                        "keyboardCapitalizationSentences()" to keyboardCapitalizationSentences()
                    ).forEach { (label, capitalization) ->
                        LabeledField(label) {
                            TextField(keyboardOptions = keyboardOptions(capitalization = capitalization))
                        }
                    }
                }
            }

            ShowroomSectionTitle("keyboardOptions.imeAction — fires a matching keyboard trigger")
            ShowroomDemoCard(title = "Type something and press the IME action key (Enter/Go/Search/etc)") {
                Column(arrangement = arrangeVerticallySpacedBy(12)) {
                    LabeledField("keyboardImeActionSearch() → onKeyboardSearch") {
                        TextField(
                            placeholder = "Search...",
                            keyboardOptions = keyboardOptions(imeAction = keyboardImeActionSearch()),
                            events = { DisplaySnackbar(trigger = EventTriggers.onKeyboardSearch(), message = "onKeyboardSearch fired") }
                        )
                    }
                    LabeledField("keyboardImeActionGo() → onKeyboardGo") {
                        TextField(
                            placeholder = "URL",
                            keyboardOptions = keyboardOptions(imeAction = keyboardImeActionGo()),
                            events = { DisplaySnackbar(trigger = EventTriggers.onKeyboardGo(), message = "onKeyboardGo fired") }
                        )
                    }
                    LabeledField("keyboardImeActionSend() → onKeyboardSend") {
                        TextField(
                            placeholder = "Message",
                            keyboardOptions = keyboardOptions(imeAction = keyboardImeActionSend()),
                            events = { DisplaySnackbar(trigger = EventTriggers.onKeyboardSend(), message = "onKeyboardSend fired") }
                        )
                    }
                    LabeledField("keyboardImeActionNext() → onKeyboardNext / keyboardImeActionPrevious() → onKeyboardPrevious") {
                        Column(arrangement = arrangeVerticallySpacedBy(8)) {
                            TextField(
                                placeholder = "Field 1",
                                keyboardOptions = keyboardOptions(imeAction = keyboardImeActionNext()),
                                events = { DisplaySnackbar(trigger = EventTriggers.onKeyboardNext(), message = "onKeyboardNext fired") }
                            )
                            TextField(
                                placeholder = "Field 2",
                                keyboardOptions = keyboardOptions(imeAction = keyboardImeActionPrevious()),
                                events = { DisplaySnackbar(trigger = EventTriggers.onKeyboardPrevious(), message = "onKeyboardPrevious fired") }
                            )
                        }
                    }
                    LabeledField("keyboardImeActionDone() → onKeyboardDone") {
                        TextField(
                            placeholder = "Last field",
                            keyboardOptions = keyboardOptions(imeAction = keyboardImeActionDone()),
                            events = { DisplaySnackbar(trigger = EventTriggers.onKeyboardDone(), message = "onKeyboardDone fired") }
                        )
                    }
                }
            }
            ShowroomNote(
                text = "keyboardImeActionUnspecified()/keyboardImeActionDefault()/keyboardImeActionNone() are " +
                    "also valid values, but none of them dispatch a keyboard trigger — they only change which " +
                    "action glyph the platform keyboard shows."
            )

            ShowroomSectionTitle("Code sample")
            ShowroomCode(
                """
                TextField(
                    id = "password",
                    label = "Password",
                    kind = outlinedTextField(),
                    visualTransformation = keyboardVisualTransformationPassword(),
                    keyboardOptions = keyboardOptions(
                        keyboardType = keyboardTypePassword(),
                        imeAction = keyboardImeActionDone()
                    ),
                    trailingIcon = icon("visibility"),
                    clickableTrailingIcon = true,
                    events = {
                        // OnTextChanged carries the new text as incomingData — UpdateTiles(value = ...) here
                        // to keep it in sync; DisplaySnackbar/etc on onKeyboardDone or onTrailingIconClick.
                    }
                )
                """
            )

            ShowroomRelated(
                names = listOf("Checkbox", "DropdownList", "SearchBar"),
                destination = "tileDetails"
            )
        }
    }
}

private fun TileSchemaBuilderScope.LabeledField(caption: String, content: TileSchemaBuilderScope.() -> Unit) {
    Column(arrangement = arrangeVerticallySpacedBy(6)) {
        SimpleText(text = caption, typography = typographyLabelMedium(), color = color(themeColorOnSurfaceVariant()))
        content()
    }
}
