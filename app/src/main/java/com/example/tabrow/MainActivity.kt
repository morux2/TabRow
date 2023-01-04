package com.example.tabrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tabrow.ui.theme.TabRowTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val aViewModel: AViewModel by viewModel()
    private val bViewModel: BViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TabRowTheme {
                var tabSelected by rememberSaveable { mutableStateOf(Screen.A) }
                val listState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = listState
                ) {
                    greeting(
                        listScope = this,
                        aViewModel = aViewModel,
                        bViewModel = bViewModel,
                        tabSelected = tabSelected,
                        onClickTab = {
                            tabSelected = it
                            coroutineScope.launch {
                                if (listState.firstVisibleItemIndex > 5)
                                    listState.scrollToItem(5)
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun greeting(
    listScope: LazyListScope,
    aViewModel: AViewModel,
    bViewModel: BViewModel,
    tabSelected: Screen,
    onClickTab: (Screen) -> Unit
) {
    with(listScope) {
        items(5) {
            Text(modifier = Modifier.heightIn(min = 100.dp), text = "X")
        }
        stickyHeader {
            TabRow(
                selectedTabIndex = tabSelected.ordinal, backgroundColor = Color.White
            ) {
                Screen.values().map { it.name }.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(text = title) },
                        selected = tabSelected.ordinal == index,
                        onClick = { onClickTab(Screen.values()[index]) },
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Gray
                    )
                }
            }
        }
        when (tabSelected) {
            Screen.A -> showAComposable(
                aViewModel.viewState.value,
                this,
                object : AViewModel.aClickEvent {
                    override fun update() = aViewModel.updateMessage()
                }
            )
            Screen.B -> showBComposable(
                bViewModel.viewState.value,
                this,
                object : BViewModel.bClickEvent {
                    override fun update() = bViewModel.updateMessage()
                }
            )
        }
    }
}

fun showAComposable(
    viewState: AViewModel.ViewState,
    listScope: LazyListScope,
    aClickEvent: AViewModel.aClickEvent
) {
    with(listScope) {
        items(10) { Text(modifier = Modifier.heightIn(min = 100.dp), text = viewState.message + it.toString()) }
        item {
            Button(onClick = { aClickEvent.update() }) {
                Text("AAAAA")
            }
        }
    }
}

fun showBComposable(
    viewState: BViewModel.ViewState,
    listScope: LazyListScope,
    bClickEvent: BViewModel.bClickEvent
) {
    with(listScope) {
        items(10) { Text(modifier = Modifier.heightIn(min = 100.dp), text = viewState.message + it.toString()) }
        item {
            Button(onClick = { bClickEvent.update() }) {
                Text("BBBBB")
            }
        }
    }
}

enum class Screen {
    A, B
}
