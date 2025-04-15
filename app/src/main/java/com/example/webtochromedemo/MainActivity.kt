package com.example.webtochromedemo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.webtochromedemo.ui.theme.WebToChromeDemoTheme
import com.example.webtochromedemo.utility.getAuthRequest
import kotlinx.coroutines.flow.MutableStateFlow
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService


class MainActivity : ComponentActivity() {
    private val myViewModel by viewModels<AuthViewModel>()
    private val getAuthResponse =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val dataIntent = it.data ?: return@registerForActivityResult
            handleAuthResponseIntent(dataIntent)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebToChromeDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    val context = LocalContext.current
                    MainContent(myViewModel.token) {
                        val customTabsIntent = CustomTabsIntent.Builder().build()
                        val authService = AuthorizationService(context)
                        val openAuthPageIntent = authService.getAuthorizationRequestIntent(
                            getAuthRequest(), customTabsIntent
                        )
                        openAuthPage(openAuthPageIntent)
                    }
                }
            }
        }
    }

    private fun handleAuthResponseIntent(intent: Intent) {
        val exception = AuthorizationException.fromIntent(intent)
        val tokenExchangeRequest =
            AuthorizationResponse.fromIntent(intent)?.createTokenExchangeRequest()
        when {
            exception != null -> Toast.makeText(this, exception.error, Toast.LENGTH_SHORT).show()
            tokenExchangeRequest != null -> myViewModel.onAuthCodeReceived(tokenExchangeRequest)
        }
    }

    private fun openAuthPage(intent: Intent) {
        getAuthResponse.launch(intent)
    }
}

@Composable
fun MainContent(
    token: MutableStateFlow<String?>,
    onAuthButtonClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        val text = token.collectAsState()

        text.value?.let {
            Text(text = it)
        } ?: run {
            Text(text = "No token")
        }

        Button(
            modifier = Modifier.padding(10.dp), content = {
                Text(text = "Open Chrome")
            }, onClick = onAuthButtonClicked
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WebToChromeDemoTheme {
        //MainContent(launcher)
    }
}