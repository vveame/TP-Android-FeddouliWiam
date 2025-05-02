package com.example.projectproduit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.projectproduit.ui.theme.ProjectProduitTheme
import com.example.projectproduit.nav.AppNavigation
import kotlinx.coroutines.delay
import androidx.compose.runtime.DisposableEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectProduitTheme {
                Surface{
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun MyComposable(id: String) {
    LaunchedEffect(id) {
        // Coroutine démarrée quand `id` change
        delay(1000)
        println("Chargement terminé pour $id")
    }
}

@Composable
fun MyDisposable() {
    DisposableEffect(Unit) {
        println("Composé")

        onDispose {
            println("Nettoyé")
        }
    }
}


