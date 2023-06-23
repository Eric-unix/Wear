/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.wear.tiles.messaging.tile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.wear.tiles.ColorBuilders
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.ModifiersBuilders
import androidx.wear.tiles.ModifiersBuilders.Clickable
import androidx.wear.tiles.ModifiersBuilders.Modifiers
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.material.Button
import androidx.wear.tiles.material.ButtonColors
import androidx.wear.tiles.material.ChipColors
import androidx.wear.tiles.material.CompactChip
import androidx.wear.tiles.material.Text
import androidx.wear.tiles.material.Typography
import androidx.wear.tiles.material.layouts.MultiButtonLayout
import androidx.wear.tiles.material.layouts.PrimaryLayout
import com.example.wear.tiles.R
import com.example.wear.tiles.messaging.Contact
import com.example.wear.tiles.messaging.MessagingRepo
import com.example.wear.tiles.tools.IconSizePreview
import com.example.wear.tiles.tools.WearDevicePreview
import com.example.wear.tiles.tools.emptyClickable
import com.google.android.horologist.compose.tools.LayoutElementPreview
import com.google.android.horologist.compose.tools.TileLayoutPreview
import com.google.android.horologist.tiles.images.drawableResToImageResource
import com.google.android.horologist.tiles.images.toImageResource
import com.google.android.horologist.tiles.render.SingleTileLayoutRenderer

class MessagingTileRenderer(context: Context) :
    SingleTileLayoutRenderer<MessagingTileState, Map<Contact, Bitmap>>(context) {

    override fun renderTile(
        state: MessagingTileState,
        deviceParameters: DeviceParametersBuilders.DeviceParameters
    ): LayoutElementBuilders.LayoutElement {
        return messagingTileLayout(
            context = context,
            deviceParameters = deviceParameters,
            state = state,
            searchButtonClickable = launchActivityClickable("search_button", openSearch())
        )
    }

    override fun ResourceBuilders.Resources.Builder.produceRequestedResources(
        resourceState: Map<Contact, Bitmap>,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
        resourceIds: MutableList<String>
    ) {
        addIdToImageMapping(ID_IC_SEARCH, drawableResToImageResource(R.drawable.ic_search_24))

        resourceState.forEach { (contact, bitmap) ->
            addIdToImageMapping(
                /* id = */ contact.imageResourceId(),
                /* image = */ bitmap.toImageResource()
            )
        }
    }

    companion object {

        internal const val ID_IC_SEARCH = "ic_search"
    }

}

/**
 * Layout definition for the Messaging Tile.
 */
private fun messagingTileLayout(
    context: Context,
    deviceParameters: DeviceParametersBuilders.DeviceParameters,
    state: MessagingTileState,
    //contactClickableFactory: (Contact) -> ModifiersBuilders.Clickable,
    searchButtonClickable: ModifiersBuilders.Clickable,
    //newButtonClickable: ModifiersBuilders.Clickable
) = PrimaryLayout.Builder(deviceParameters)
    .setContent(
        MultiButtonLayout.Builder()
            .apply {
                state.contacts.take(n=4).forEach { contact ->
                    addButtonContent (
                        contactLayout(
                            context = context,
                            contact = contact,
                            clickable = emptyClickable
                        )
                    )
                }
            }
            .addButtonContent(searchLayout(context, searchButtonClickable))
            .build()
    )
    .setPrimaryChipContent(
        CompactChip.Builder(
            context,
            context.getString(R.string.tile_messaging_create_new),
            emptyClickable,
            deviceParameters
        )
            .setChipColors(ChipColors.primaryChipColors(MessagingTileTheme.colors))
            .build()
    )
    .build()

@WearDevicePreview
@Composable
fun MessagingTileRendererPreview() {
    val state = MessagingTileState(MessagingRepo.knownContacts)
    val context = LocalContext.current
    TileLayoutPreview(
        state = state,
        resourceState = mapOf(
            state.contacts[1] to (context.getDrawable(R.drawable.ali)as BitmapDrawable).bitmap,
            state.contacts[2] to (context.getDrawable(R.drawable.taylor)as BitmapDrawable).bitmap,
        ),
        renderer = MessagingTileRenderer(context)
    )
}

private fun searchLayout(
    context: Context,
    clickable: ModifiersBuilders.Clickable) =
    Button.Builder(context, clickable)
        .setContentDescription(context.getString(R.string.tile_messaging_search))
        .setIconContent(MessagingTileRenderer.ID_IC_SEARCH)
        .setButtonColors(ButtonColors.secondaryButtonColors(MessagingTileTheme.colors))
        .build()

@IconSizePreview
@Composable
private fun SearchButtonPreview() {
    LayoutElementPreview(
        searchLayout(
            context = LocalContext.current,
            clickable = emptyClickable
        )
    ){
        addIdToImageMapping(
            MessagingTileRenderer.ID_IC_SEARCH,
            drawableResToImageResource(R.drawable.ic_search_24)
        )
    }
}
private fun contactLayout(
    context: Context,
    contact: Contact,
    clickable: ModifiersBuilders.Clickable,
) = Button.Builder(context, clickable)
    .setContentDescription(contact.name)
    .apply {
        if(contact.avatarUrl != null){
            setImageContent(contact.imageResourceId())
        }
        else{
            setTextContent(contact.initials)
            setButtonColors(
                (ButtonColors.secondaryButtonColors(MessagingTileTheme.colors))
            )
        }
    }
    .build()