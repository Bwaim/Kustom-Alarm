/*
 * Copyright 2023 Dev Bwaim team
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

package dev.bwaim.kustomalarm.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue.Open
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.bwaim.kustomalarm.R.string
import dev.bwaim.kustomalarm.compose.HorizontalDivider
import dev.bwaim.kustomalarm.compose.KAlarmPreviews
import dev.bwaim.kustomalarm.compose.KaBackground
import dev.bwaim.kustomalarm.compose.theme.KustomAlarmTheme
import dev.bwaim.kustomalarm.settings.navigation.SettingsNavigationDrawerItem
import dev.bwaim.kustomalarm.ui.resources.R.drawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun KaNavigationDrawer(
    navigationDrawerItems: List<NavigationDrawerItem>,
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val selectedItem: MutableState<NavigationDrawerItem?> = remember { mutableStateOf(null) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                AppDrawerItem()
                HorizontalDivider()

                navigationDrawerItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(text = stringResource(id = item.labelRes)) },
                        selected = item == selectedItem.value,
                        onClick = {
                            selectedItem.value = item
                            item.action(navController)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    )
                }
            }
        },
        modifier = modifier,
        drawerState = drawerState,
        content = content,
    )
}

@Composable
private fun AppDrawerItem() {
    Row(
        modifier = Modifier
            .height(56.dp)
            .padding(NavigationDrawerItemDefaults.ItemPadding)
            .padding(start = 16.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = drawable.ic_klock),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(20.dp)),
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = stringResource(id = string.app_name),
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Composable
@KAlarmPreviews
private fun PreviewKaNavigationDrawer() {
    KustomAlarmTheme {
        KaNavigationDrawer(
            navigationDrawerItems = listOf(
                SettingsNavigationDrawerItem(),
            ),
            navController = rememberNavController(),
            drawerState = DrawerState(Open),
            scope = rememberCoroutineScope(),
        ) {
            KaBackground {
                Text(text = "Test Navigation Drawer")
            }
        }
    }
}
