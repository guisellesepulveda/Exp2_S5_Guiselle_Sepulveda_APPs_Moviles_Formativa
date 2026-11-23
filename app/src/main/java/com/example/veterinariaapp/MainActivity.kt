package com.example.veterinariaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.veterinariaapp.ui.screens.PantallaInicio
import com.example.veterinariaapp.ui.theme.VeterinariaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeterinariaAppTheme {
                PantallaInicio()
            }
        }
    }
}