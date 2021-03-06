package com.lilcode.modern.compose.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilcode.modern.compose.myapplication.ui.theme.MyApplicationTheme

/**
 * https://www.inflearn.com/course/%EB%AA%A8%EB%8D%98-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%BB%B4%ED%8F%AC%EC%A6%88/lecture/94903?tab=note&volume=1.00&quality=1080
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier.background(color = Color.Green)
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Hello")
                Box(
                    modifier = Modifier.fillMaxSize()
                        //.background(color = Color.Red)
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text("12345~~~~~!!!")
                }
            }
        }
    }
}
