package com.example.tabrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tabrow.ui.theme.TabRowTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appModule = module {
            viewModel { AViewModel() }
            viewModel { BViewModel() }
        }
        startKoin {
            modules(appModule)
        }

        setContent {
            TabRowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Greeting(
                        { AComposeable() },
                        { BComposeable() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun Greeting(
    contentA: @Composable () -> Unit,
    contentB: @Composable () -> Unit
) {
    val listItems by remember { mutableStateOf(listOf("title1", "title2")) }

    val state = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    LazyColumn {
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
                selectedTabIndex = state.currentPage, backgroundColor = Color.White
            ) {
                // Add tabs for all of our pages
                listItems.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(text = title) },
                        selected = state.currentPage == index,
                        onClick = {
                            // Animate to the selected page when clicked
                            coroutineScope.launch {
                                state.animateScrollToPage(index)
                            }
                        },
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Gray
                    )
                }
            }
        }
        item {
            HorizontalPager(
                count = listItems.count(),
                state = state,
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> contentA()
                    1 -> contentB()
                }
            }
        }
    }
}

@Composable
fun AComposeable(vm: AViewModel = koinViewModel()) {
    vm.updateMessage()
    vm.viewState.value?.let {
        Text(modifier = Modifier.heightIn(min = 1000.dp), text = it.message)
    }

}

@Composable
fun BComposeable(vm: BViewModel = koinViewModel()) {
    vm.updateMessage()
    vm.viewState.value?.let {
        Text(modifier = Modifier.heightIn(min = 1000.dp), text = it.message)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TabRowTheme {
        Greeting(
            { AComposeable(vm = AViewModel()) },
            { BComposeable(vm = BViewModel()) }
        )
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun APreview() {
    AComposeable(vm = AViewModel())
}