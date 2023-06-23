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
package com.example.wear.tiles.hello

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.wear.tiles.DimensionBuilders
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.LayoutElementBuilders.LayoutElement
import androidx.wear.tiles.ModifiersBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TimelineBuilders
import androidx.wear.tiles.material.Button
import androidx.wear.tiles.material.ButtonColors
import com.example.wear.tiles.R
import com.example.wear.tiles.messaging.MessagingRepo
import com.example.wear.tiles.messaging.tile.MessagingTileRenderer
import com.example.wear.tiles.messaging.tile.MessagingTileState
import com.example.wear.tiles.messaging.tile.MessagingTileTheme
import com.example.wear.tiles.tools.WearDevicePreview
import com.google.android.horologist.compose.tools.TileLayoutPreview
import com.google.android.horologist.tiles.CoroutinesTileService

private const val RESOURCES_VERSION = "0"

class HelloWorldTileService : CoroutinesTileService() {

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ResourceBuilders.Resources {
        return ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .build()
    }

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest
    ): TileBuilders.Tile {
        val singleTileTimeline = TimelineBuilders.Timeline.Builder()
            .addTimelineEntry(
                TimelineBuilders.TimelineEntry.Builder()
                    .setLayout(
                        LayoutElementBuilders.Layout.Builder()
                            .setRoot(tileLayout())
                            .build()
                    )
                    .build()
            )
            .build()

        return TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTimeline(singleTileTimeline)
            .build()
    }

    private fun tileLayout(): LayoutElement {
        val text = getString(R.string.hello_tile_body)
        return LayoutElementBuilders.Box.Builder()
            .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText(text)
                    .build()
            )
            .build()
    }

//    @WearDevicePreview
//    @Composable
//    fun MessagingTileRendererPreview() {
//        TileLayoutPreview(
//            state = MessagingTileState(MessagingRepo.knownContacts),
//            resourceState = emptyMap(),
//            renderer = MessagingTileRenderer(LocalContext.current)
//        )
//    }
//
//    private fun searchLayout(
//        context: Context,
//        clickable: ModifiersBuilders.Clickable) =
//        Button.Builder(context, clickable)
//            .setContentDescription(context.getString(R.string.tile_messaging_search))
//            .setIconContent(MessagingTileRenderer.ID_IC_SEARCH)
//            .setButtonColors(ButtonColors.secondaryButtonColors(MessagingTileTheme.colors))
//            .build()
}
