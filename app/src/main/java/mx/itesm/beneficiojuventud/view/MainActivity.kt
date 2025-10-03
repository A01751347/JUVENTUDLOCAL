package mx.itesm.beneficiojuventud.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.core.Amplify
import mx.itesm.beneficiojuventud.ui.theme.BeneficioJuventudTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeneficioJuventudTheme {
                AppNav()
            }
        }
    }
}

@Composable
private fun AppNav() {
    val nav = rememberNavController()
    var isCheckingAuth by remember { mutableStateOf(true) }
    var isSignedIn by remember { mutableStateOf(false) }

    // Verificar si hay sesión activa al iniciar
    LaunchedEffect(Unit) {
        Amplify.Auth.fetchAuthSession(
            { session ->
                isSignedIn = session.isSignedIn
                isCheckingAuth = false
                Log.d("MainActivity", "Sesión activa: ${session.isSignedIn}")
            },
            { error ->
                Log.e("MainActivity", "Error verificando sesión", error)
                isSignedIn = false
                isCheckingAuth = false
            }
        )
    }

    if (isCheckingAuth) {
        // Pantalla de carga mientras verifica la sesión
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Navegar según el estado de autenticación
        val startDestination = if (isSignedIn) {
            Screens.Onboarding.route
        } else {
            Screens.MainMenu.route
        }

        NavHost(navController = nav, startDestination = startDestination) {
            composable(Screens.MainMenu.route) { MainMenu(nav) }
            composable(Screens.Login.route) { Login(nav) }
            composable(Screens.Register.route) { Register(nav) }
            composable(Screens.ForgotPassword.route) { ForgotPassword(nav) }
            composable(Screens.RecoveryCode.route) { RecoveryCode(nav) }
            composable(Screens.NewPassword.route) { NewPassword(nav) }
            composable(Screens.Onboarding.route) { Onboarding(nav) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    BeneficioJuventudTheme {
        AppNav()
    }
}