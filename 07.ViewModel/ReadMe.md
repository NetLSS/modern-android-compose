# 2022-04-06

```kt
package com.lilcode.modern.compose.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "first") {

                composable(route = "first") {
                    FirstScreen(navController = navController)
                }

                composable(route = "second") {
                    SecondScreen(navController = navController)
                }

                composable(route = "third/{value}") { backStackEntry ->
                    val param = backStackEntry.arguments?.getString("value") ?: ""

                    ThirdScreen(
                        navController = navController,
                        value = param
                    )
                }

            }
        }
    }

}

// 각 화면에서 callback 을 줘서 하는 방법 도 있음 이건 TODO 해보기

@Composable
fun FirstScreen(navController: NavController) {

    val (value, setValue) = remember {
        mutableStateOf(value = "")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "첫 화면")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate(route = "second")
        }) {
            Text(text = "두 번째!")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = value, onValueChange = setValue)

        Button(onClick = {
            if (value.isEmpty()) {
                return@Button
            }
            navController.navigate(route = "third/$value")
        }) {
            Text(text = "세 번째!")
        }

    }
}

@Composable
fun SecondScreen(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "두 번째 화면")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigateUp()
//            navController.popBackStack()
//            navController.navigate(route = "first")
        }) {
            Text(text = "뒤로 가기")
        }
    }
}

@Composable
fun ThirdScreen(navController: NavController, value: String) {

    val (isFirstSnackBar, setIsFirstSnackBar) = remember {
        mutableStateOf(true)
    }

    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState
    ) {


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "세 번째 화면")

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = value)

            Button(onClick = {
                navController.popBackStack()
//            navController.navigate(route = "first")
            }) {
                Text(text = "뒤로 가기")
            }

            scope.launch {
                if (isFirstSnackBar) {
                    setIsFirstSnackBar(false)
                    scaffoldState.snackbarHostState.showSnackbar(value)
                }
            }
        }
    }

}
```