package com.lilcode.modern.compose.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.contextaware.withContextAvailable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

            val sec = viewModel.sec.value
            val milli = viewModel.milli.value
            val isRunning = viewModel.isRunning.value
            val labTimes = viewModel.lapTimes

            MainScreen(
                sec = sec,
                milli = milli,
                isRunning = isRunning,
                labTimes = labTimes,
                onReset = { viewModel.reset() },
                onLapTime = { viewModel.recordLabTime() },
                onToggle = { running ->
                    if (running) {
                        viewModel.pause()
                    } else {
                        viewModel.start()
                    }
                }
            )
        }
    }

}

class MainViewModel : ViewModel() {
    private var time = 0
    private var timerTask: Timer? = null

    private val _sec = mutableStateOf(0)
    val sec: State<Int> = _sec

    private val _milli = mutableStateOf(0)
    val milli: State<Int> = _milli

//    private val _lapTimes = mutableStateOf(mutableListOf<String>())
//    val lapTimes: State<List<String>> = _lapTimes

    private val _lapTimes = mutableStateListOf<String>()
    val lapTimes: List<String> = _lapTimes

    // start ?????? ??????
    private val _isRunning = mutableStateOf(false)
    val isRunning: State<Boolean> = _isRunning

    // Lap time ????????? ?????? ?????? ????????? ?????? ????????? ??????
    private var lap = 1

    fun start() {
        _isRunning.value = true
        timerTask = timer(period = 10) {
            time++
            _sec.value = time / 100
            _milli.value = time % 100
        }
    }

    fun pause() {
        _isRunning.value = false
        timerTask?.cancel()
    }

    fun reset() {
        _isRunning.value = false
        timerTask?.cancel()

        time = 0
        _sec.value = 0
        _milli.value = 0

        _lapTimes.clear()
        lap = 1
    }

    fun recordLabTime() {
        _lapTimes.add(0, "${lap++} LAP : ${sec.value}.${milli.value}")
    }
}

@Composable
fun MainScreen(
    sec: Int,
    milli: Int,
    isRunning: Boolean,
    labTimes: List<String>,
    onReset: () -> Unit,
    onToggle: (Boolean) -> Unit,
    onLapTime: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("StopWatch") })
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text("$sec", fontSize = 100.sp)
                Text("$milli")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.weight(1f) // ????????? ?????? ??????
                    .verticalScroll(rememberScrollState())
            ) {
                labTimes.forEach { labTime ->
                    Text(labTime)
                }
            }

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = { onReset() }, backgroundColor = Color.Red
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_refresh_24),
                        contentDescription = "reset"
                    )
                }

                FloatingActionButton(
                    onClick = { onToggle(isRunning) }, backgroundColor = Color.Green
                ) {
                    Image(
                        painter = painterResource(
                            id =
                            if (isRunning) R.drawable.ic_baseline_pause_24
                            else R.drawable.ic_baseline_play_arrow_24
                        ),
                        contentDescription = "start/pause"
                    )
                }

                Button(onClick = { onLapTime() }) {
                    Text("??? ??????")
                }
            }
        }

    }
}

@Preview
@Composable
fun preview() {
    MainScreen(
        sec = 10,
        milli = 1,
        isRunning = false,
        labTimes = (1..50).map { "$it" },
        onReset = {},
        onToggle = {},
        onLapTime = {}
    )
}