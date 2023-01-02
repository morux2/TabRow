package com.example.tabrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tabrow.ui.theme.TabRowTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TabRowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Greeting(
                        { AComposable() },
                        { BComposable() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Greeting(
    contentA: @Composable () -> Unit,
    contentB: @Composable () -> Unit
) {

    var state by rememberSaveable { mutableStateOf(0) }
    val listItems = listOf("tab1", "tab2")
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = listState
    ) {
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
                selectedTabIndex = state, backgroundColor = Color.White
            ) {
                // Add tabs for all of our pages
                listItems.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(text = title) },
                        selected = state == index,
                        onClick = {
                            state = index
                            coroutineScope.launch {
                                if (listState.firstVisibleItemIndex > 4)
                                    listState.scrollToItem(4)
                            }
                        },
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Gray
                    )
                }
            }
        }
        item {
            when (state) {
                0 -> AComposable()
                1 -> BComposable()
            }
        }
    }
}

@Composable
fun AComposable(vm: AViewModel = koinViewModel()) {
    vm.updateMessage()
    vm.viewState.value.let {
        Text(modifier = Modifier.heightIn(min = 1000.dp), text = it.message)
    }

}

@Composable
fun BComposable(vm: BViewModel = koinViewModel()) {
    vm.updateMessage()
    vm.viewState.value.let {
        Text(modifier = Modifier.heightIn(min = 1000.dp), text = it.message)
    }
}

@Preview(heightDp = 100)
@Composable
fun APreview() {
    AComposable(vm = AViewModel())
}


@Preview(heightDp = 100)
@Composable
fun BPreview() {
    BComposable(vm = BViewModel())
}