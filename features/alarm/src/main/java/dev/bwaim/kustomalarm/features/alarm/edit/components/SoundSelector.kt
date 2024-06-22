/*
 * Copyright 2024 Dev Bwaim team
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

package dev.bwaim.kustomalarm.features.alarm.edit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.bwaim.kustomalarm.compose.extensions.testIdentifier
import dev.bwaim.kustomalarm.ui.resources.R.drawable

@Composable
internal fun SoundSelector(
    title: String,
    onSoundSelectionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onSoundSelectionClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 16.dp)
                .testIdentifier("sound_selector"),
        ) {
            Image(
                painter = painterResource(id = drawable.alarm_clock),
                contentDescription = null,
                modifier = Modifier.weight(PERCENT_33),
            )
            Text(
                text = title,
                modifier = Modifier.weight(PERCENT_66),
            )
        }
    }
}

private const val PERCENT_33 = 0.33f
private const val PERCENT_66 = 0.66f
