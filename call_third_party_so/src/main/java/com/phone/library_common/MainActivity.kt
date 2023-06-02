package com.phone.library_common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.phone.library_common.ui.theme.RxJava2AndRetrofit2Theme

/**
 * 这个call_third_party_so module可以导入其他project里去运行（因版本有区别，
 * 所以还需要按照此project配置好build.gradle和settings.gradle文件下对应的环境）
 */
class MainActivity : ComponentActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RxJava2AndRetrofit2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }


        val aesKey =
            JavaGetData.nativeAesKey(this@MainActivity, false)
        val databaseEncryptKey =
            JavaGetData.nativeDatabaseEncryptKey(this@MainActivity, false)
        val getString =
            JavaGetData.nativeGetString(this@MainActivity, false)
        LogManager.i(TAG, "aesKey*****$aesKey")
        LogManager.i(TAG, "databaseEncryptKey*****$databaseEncryptKey")
        LogManager.i(TAG, "getString*****$getString")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RxJava2AndRetrofit2Theme {
        Greeting("Android")
    }
}