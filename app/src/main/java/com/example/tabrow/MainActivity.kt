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
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
                var tabSelected by rememberSaveable { mutableStateOf(0) }
                val listState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
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
                                if (listState.firstVisibleItemIndex > 4)
                                    listState.scrollToItem(4)
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
    tabSelected: Int,
    onClickTab: (Int) -> Unit
) {
    val listItems = listOf("tab1", "tab2")
    with(listScope) {
        item {
            Text(modifier = Modifier.heightIn(min = 100.dp), text = "disappear1")
        }
        item {
            Text(modifier = Modifier.heightIn(min = 100.dp), text = "disappear2")
        }
        item {
            Text(modifier = Modifier.heightIn(min = 100.dp), text = "disappear3")
        }
        item {
            Text(modifier = Modifier.heightIn(min = 100.dp), text = "disappear4")
        }
        stickyHeader {
            TabRow(
                selectedTabIndex = tabSelected, backgroundColor = Color.White
            ) {
                listItems.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(text = title) },
                        selected = tabSelected == index,
                        onClick = { onClickTab(index) },
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Gray
                    )
                }
            }
        }
        when (tabSelected) {
            0 -> showAComposable(
                aViewModel.viewState.value,
                this,
                object : AViewModel.aClickEvent {
                    override fun update() = aViewModel.updateMessage()
                }
            )
            1 -> showBComposable(
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
        item { Text(modifier = Modifier.heightIn(min = 1000.dp), text = viewState.message) }
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
        item { Text(modifier = Modifier.heightIn(min = 1000.dp), text = viewState.message) }
        item {
            Button(onClick = { bClickEvent.update() }) {
                Text("BBBBB")
            }
        }
    }
}
