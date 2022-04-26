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
            TopAppBar(title = { Text(text = "나만의 웹 브라우저") },
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

    // 뒤로가기 앞으로가기, flow 사용을 위해 코루틴 필요.
//    val scope = rememberCoroutineScope()

    val webView = rememberWebView()
    /*
    key에 전달죄는 객체가 변경 되었을 떄 block 실행
    Unit 한번 만 실행

    key1 에 (컴포저블 수명주기 동이랗게 하고싶다면) true  나 Unit 같은 상수 지정하면 1번만 실행
     */
    LaunchedEffect(key1 = Unit) {
        viewModel.undoSharedFlow.collectLatest {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                scaffoldState.snackbarHostState.showSnackbar("더 이상 뒤로 갈 수 없음")
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.redoSharedFlow.collectLatest {
            if (webView.canGoForward()) {
                webView.goForward()
            } else {
                scaffoldState.snackbarHostState.showSnackbar("더 이상 앞으로 갈 수 없음")
            }
        }
    }


    // factory 화면에 표시해야하는 뷰 객체 인스턴스
    // update 는 컴포저블 될 때 실행됨.
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { webView }, update = { webView ->
            webView.loadUrl(viewModel.url.value)

            // 빈 공백 으로 검색후 뒤로가기 2번 하면 반복적으로 더이상 뒤로갈 수 없다는 스낵바 가 뜨는 버그가 있음.
//            scope.launch {
//                viewModel.undoSharedFlow.collectLatest {
//                    if (webView.canGoBack()) {
//                        webView.goBack()
//                    } else {
//                        scaffoldState.snackbarHostState.showSnackbar("더 이상 뒤로 갈 수 없음")
//                    }
//                }
//            }
//
//            scope.launch {
//                viewModel.redoSharedFlow.collectLatest {
//                    if (webView.canGoForward()) {
//                        webView.goForward()
//                    } else {
//                        scaffoldState.snackbarHostState.showSnackbar("더 이상 앞으로 갈 수 없음")
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