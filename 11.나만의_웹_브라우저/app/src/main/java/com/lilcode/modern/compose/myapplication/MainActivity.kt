package com.lilcode.modern.compose.myapplication

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.contextaware.withContextAvailable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.pow

/**
 * https://www.inflearn.com/course/%EB%AA%A8%EB%8D%98-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%BB%B4%ED%8F%AC%EC%A6%88/lecture/94903?tab=note&volume=1.00&quality=1080
 */
/*
TODO: state hoisting
TODO: ?????? url ??? , text string ?????????
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = viewModel<MainViewModel>()
            HomeScreen(viewModel)
        }
    }

}

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current

    val (inputUrl, setUrl) = rememberSaveable {
        mutableStateOf("https://www.google.com")
    }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "????????? ??? ????????????") },
                actions = {
                    IconButton(onClick = {
                        viewModel.undo()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back",
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = {
                        viewModel.redo()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "forward",
                            tint = Color.White
                        )
                    }
                })
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = inputUrl,
                onValueChange = setUrl,
                label = { Text("https://") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.url.value = inputUrl
                    focusManager.clearFocus()
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            MyWebView(viewModel, scaffoldState)
        }


    }
}

@Composable
fun MyWebView(viewModel: MainViewModel, scaffoldState: ScaffoldState) {

    // ???????????? ???????????????, flow ????????? ?????? ????????? ??????.
//    val scope = rememberCoroutineScope()

    val webView = rememberWebView()
    /*
    key??? ???????????? ????????? ?????? ????????? ??? block ??????
    Unit ?????? ??? ??????

    key1 ??? (???????????? ???????????? ???????????? ???????????????) true  ??? Unit ?????? ?????? ???????????? 1?????? ??????
     */
    LaunchedEffect(key1 = Unit) {
        viewModel.undoSharedFlow.collectLatest {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                scaffoldState.snackbarHostState.showSnackbar("??? ?????? ?????? ??? ??? ??????")
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.redoSharedFlow.collectLatest {
            if (webView.canGoForward()) {
                webView.goForward()
            } else {
                scaffoldState.snackbarHostState.showSnackbar("??? ?????? ????????? ??? ??? ??????")
            }
        }
    }


    // factory ????????? ?????????????????? ??? ?????? ????????????
    // update ??? ???????????? ??? ??? ?????????.
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { webView }, update = { webView ->
            webView.loadUrl(viewModel.url.value)

            // ??? ?????? ?????? ????????? ???????????? 2??? ?????? ??????????????? ????????? ????????? ??? ????????? ????????? ??? ?????? ????????? ??????.
//            scope.launch {
//                viewModel.undoSharedFlow.collectLatest {
//                    if (webView.canGoBack()) {
//                        webView.goBack()
//                    } else {
//                        scaffoldState.snackbarHostState.showSnackbar("??? ?????? ?????? ??? ??? ??????")
//                    }
//                }
//            }
//
//            scope.launch {
//                viewModel.redoSharedFlow.collectLatest {
//                    if (webView.canGoForward()) {
//                        webView.goForward()
//                    } else {
//                        scaffoldState.snackbarHostState.showSnackbar("??? ?????? ????????? ??? ??? ??????")
//                    }
//                }
//            }
        })
}

@Composable
fun rememberWebView(): WebView {
    val context = LocalContext.current
    val webview = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl("https://google.com")
        }
    }
    return webview
}