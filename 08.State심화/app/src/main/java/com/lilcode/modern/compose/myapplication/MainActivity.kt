package com.lilcode.modern.compose.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lilcode.modern.compose.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

/**
 * https://www.inflearn.com/course/%EB%AA%A8%EB%8D%98-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%BB%B4%ED%8F%AC%EC%A6%88/lecture/94903?tab=note&volume=1.00&quality=1080
 */
@ExperimentalComposeUiApi // 실험 기능 어노테이션
class MainActivity : ComponentActivity() {

//    private val viewModel by viewModels<MainViewModel>() // 엑티비티와 라이프사이클을 같이 함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HomeScreen()
        }
    }

}

@Composable
fun HomeScreen(viewModel: MainViewModel = viewModel()) {

    var text1: MutableState<String> = remember {
        mutableStateOf("Hello World")
    }


    /**
     * import androidx.compose.runtime.getValue / setValue
     */
    var text2 by remember {
        mutableStateOf("Hello World")
    }

    val (text: String, setText: (String) -> Unit) = remember {
        mutableStateOf("Hello World")
    }

    Column {
        Text(text = text2)
        Button(onClick = {}) {
            Text(text = "클릭")
        }
        TextField(value = text2, onValueChange = {
            text2 = it
        })
    }
}

class MainViewModel : ViewModel() {
    private val _value: MutableState<String> = mutableStateOf("Hello World")
    val value: State<String> = _value
}
