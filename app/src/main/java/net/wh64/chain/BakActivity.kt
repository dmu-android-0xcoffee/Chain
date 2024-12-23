package net.wh64.chain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import net.wh64.chain.ui.theme.ChainTheme

class BakActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChainTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                }
//                Scaffold(
//                    bottomBar = {},
//                    modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    textBox()
//                    LoginButton()
//                    LoginButton2()
//                    LoginButton3()
//                    LoginButton4()
//                    LoginButton5()
//                    LoginScreen()
//                }
            }
        }
    }
}

//@Composable
//private fun textBox() {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top=50.dp)
//            .padding(80.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(250.dp, 130.dp)
//                .background(Color.DarkGray)
//        )
//        Text(text = "LOGO",
//            style = TextStyle(
//                fontSize = 30.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Black,
//            ),
//            modifier = Modifier.align(Alignment.Center)
//        )
//    }
//}
//@Composable
//private fun LoginButton() {
//    TextButton(onClick = {}, modifier = Modifier.wrapContentSize()) {
//        Text(text = "이메일을 입력하세요")
//    }
//}
//
//@Composable
//private fun LoginButton2() {
//    TextButton(onClick = {}, modifier = Modifier.wrapContentSize()) {
//        Text(text = "아이디를 입력하세요")
//    }
//}
//
//@Composable
//private fun LoginButton3() {
//    TextButton(onClick = {}, modifier = Modifier.wrapContentSize()) {
//        Text(text = "비밀번호를 입력하세요")
//    }
//}
//@Composable
//private fun LoginButton4() {
//    TextButton(onClick = {}, modifier = Modifier.wrapContentSize()) {
//        Text(text = "비밀번호를 다시 입력해 주세요")
//    }
//}
//@Composable
//private fun LoginButton5() {
//    TextButton(onClick = {}, modifier = Modifier.wrapContentSize()) {
//        Text(text = "Register Complete")
//    }
//}
//
//@Composable
//private fun LoginScreen(){
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .wrapContentSize(Alignment.Center)
//        .padding(20.dp)
//    ) {
//        LoginButton()
//        Spacer(modifier = Modifier.height(20.dp))
//        LoginButton2()
//        Spacer(modifier = Modifier.height(20.dp))
//        LoginButton3()
//        Spacer(modifier = Modifier.height(20.dp))
//        LoginButton4()
//        Spacer(modifier = Modifier.height(20.dp))
//        LoginButton5()
//        Spacer(modifier = Modifier.height(20.dp))
//    }
//}
//class SecondActivty : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ChainTheme {
//                Scaffold(
//                    bottomBar = {},
//                    modifier = Modifier.fillMaxSize()
//                ) { innerPadding ->
//                    LogoBox()
//                    LoginButton6()
//                    LoginButton7()
//                    LoginButton8()
//                    LoginButton9()
//                    LoginScreen2()
//                    LoginScreen3()
//                }
//            }
//        }
//    }
//}
//@Composable
//fun LogoBox() {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 50.dp)
//            .background(Color.LightGray)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(250.dp, 130.dp)
//                .align(Alignment.Center)
//        )
//        {
//            Text(
//                text = "LOGO",
//                style = TextStyle(
//                    fontSize = 30.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black
//                ),
//                modifier = Modifier.align(Alignment.Center)
//            )
//        }
//    }
//}
//@Composable
//private fun LoginButton6() {
//    TextButton(onClick = {}, modifier = Modifier.wrapContentSize()) {
//        Text(text = "아이디를 입력하세요")
//    }
//}
//
//@Composable
//private fun LoginButton7() {
//    TextButton(onClick = {}, modifier = Modifier.wrapContentSize()) {
//        Text(text = "비밀번호를 입력하세요")
//    }
//}
//@Composable
//private fun LoginButton8() {
//    TextButton(onClick = {}, modifier = Modifier.wrapContentSize()) {
//        Text(text="Login")
//    }
//}
//@Composable
//private fun LoginButton9() {
//    TextButton(onClick = {}, modifier = Modifier.wrapContentSize()) {
//        Text(text="Register")
//    }
//}
//@Composable
//private fun LoginScreen2(){
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .wrapContentSize(Alignment.Center)
//        .padding(20.dp)
//    ){
//        LoginButton6()
//        Spacer(modifier = Modifier.height(20.dp))
//        LoginButton7()
//        Spacer(modifier = Modifier.height(20.dp))
//    }
//}
//@Composable
//private fun LoginScreen3(){
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(20.dp),
//        verticalArrangement = Arrangement.Bottom,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ){
//        LoginButton8()
//        Spacer(modifier = Modifier.height(20.dp))
//        LoginButton9()
//        Spacer(modifier = Modifier.height(20.dp))
//    }
//}
//class ThirdActivty : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent{
//            ChainTheme {
//                Scaffold(
//                    bottomBar = {},
//                    modifier = Modifier.fillMaxSize()){
//                    innerPadding->
//                    DirectMessage()
//                }
//            }
//
//        }
//    }
//}
//@Composable
//private fun DirectMessage()
//{
//    Box {
//            Box(
//                modifier = Modifier
//                    .size(420.dp, 150.dp)
//                    .background(Color.White)
//            )
//            Text(
//                text = "{}님환영합니다.",
//                modifier = Modifier
//                    .size(40.dp)
//                    .padding(top = 20.dp)
//                    .wrapContentSize(Alignment.Center)
//            )
//            Text(
//                text = "현재 활동중인 친구 {}명",
//                modifier = Modifier
//                    .size(40.dp)
//                    .padding(top = 40.dp)
//                    .wrapContentSize(Alignment.Center)
//            )
//        }
//    }
