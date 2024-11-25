package net.wh64.chain

import android.graphics.drawable.Icon
import android.os.Bundle
import android.text.Layout
import android.webkit.WebSettings.TextSize
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.vector.VectorComposable
import androidx.compose.ui.input.key.Key.Companion.Button1
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.wh64.chain.R.drawable
import net.wh64.chain.ui.theme.ChainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChainTheme {
                Scaffold(
                    bottomBar = {},
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    textBox()
                    Button()
                    button1()
                }
            }
        }
    }
}
@Composable
private fun textBox() {
    Box(
        modifier = Modifier
            .width(600.dp)
            .height(250.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.5f)
                .height(100.dp)
                .background(Color.DarkGray)
        )
        Text(text = "LOGO")
    }
}
@Composable
private fun button1() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Column(Modifier
            .fillMaxWidth()
            .height(100.dp)
            .width(300.dp)
            .weight(1f)){
            Button(onClick = {}, modifier = Modifier.wrapContentSize()) {
                Icon(
                    Icons.Rounded.AccountBox,
                    contentDescription = stringResource(drawable.id)
                )
                Text("          아이디를 입력해주세요           ")
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Column(Modifier
                .fillMaxWidth()
                .height(100.dp)
                .width(400.dp)
                .weight(1f)){
                Button(onClick = {}, modifier = Modifier.wrapContentSize()) {
                    Icon(
                        Icons.Rounded.Email,
                        contentDescription = stringResource(drawable.passwd)
                    )
                    Text( "          비밀번호를 입력해주세요       ",)
                }
            }
        }
    }
}
@Composable
private fun Button(){
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
        Column(Modifier
            .fillMaxWidth()
            .height(100.dp)
            .width(200.dp)
            .weight(1f)) {}
        Button(onClick = {}, modifier = Modifier.wrapContentSize()) {
            Icon(
                Icons.Rounded.AccountCircle,
                contentDescription = stringResource(drawable.login)
            )
            Text("Login")
        }
        }


    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
        Column(Modifier
            .fillMaxWidth()
            .height(100.dp)
            .width(200.dp)
            .weight(1f)) {}
    Button(onClick = {}, modifier = Modifier.wrapContentSize()) {
        Icon(
            Icons.Rounded.Edit,
            contentDescription = stringResource(drawable.register)
        )
        Text("Register")
    }
    }
}

