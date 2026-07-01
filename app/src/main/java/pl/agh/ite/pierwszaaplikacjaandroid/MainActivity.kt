package pl.agh.ite.pierwszaaplikacjaandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.agh.ite.pierwszaaplikacjaandroid.ui.theme.PierwszaAplikacjaAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent określa, co wyświetli się na ekranie
        setContent {
            PierwszaAplikacjaAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KolkoIKrzyzykGra()
                }
            }
        }
    }
}

// Funkcje @Composable
@Composable
fun KolkoIKrzyzykGra() {
    // Stan aplikacji
    // remember pozwala Compose zapamiętać wartości przy odświeżaniu ekranu
    var plansza by remember { mutableStateOf(List(9) { "" }) }
    var kolejKrzyzyka by remember { mutableStateOf(true) }
    var zwyciezca by remember { mutableStateOf<String?>(null) }

    // Funkcja sprawdzająca czy ktoś wygrał
    fun sprawdzWygrana(stanPlanszy: List<String>): String? {
        val wygrywajaceLinie = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // wiersze
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // kolumny
            listOf(0, 4, 8), listOf(2, 4, 6)                   // przekątne
        )

        for (linia in wygrywajaceLinie) {
            val (a, b, c) = linia
            if (stanPlanszy[a].isNotEmpty() && stanPlanszy[a] == stanPlanszy[b] && stanPlanszy[a] == stanPlanszy[c]) {
                return stanPlanszy[a]
            }
        }
        if (!stanPlanszy.contains("")) return "Remis"
        return null
    }

    // Obsługa kliknięcia w pole
    fun kliknijPole(indeks: Int) {
        if (plansza[indeks].isEmpty() && zwyciezca == null) {
            val nowaPlansza = plansza.toMutableList()
            nowaPlansza[indeks] = if (kolejKrzyzyka) "X" else "O"
            plansza = nowaPlansza
            zwyciezca = sprawdzWygrana(nowaPlansza)
            kolejKrzyzyka = !kolejKrzyzyka
        }
    }

    // Główny układ ekranu - układamy elementy pionowo
    Column(
        modifier = Modifier.fillMaxSize(), // na cały ekran
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Nagłówek informujący o stanie gry
        Text(
            text = when {
                zwyciezca == "Remis" -> "Remis!"
                zwyciezca != null -> "Wygrał: $zwyciezca!"
                else -> "Kolej gracza: ${if (kolejKrzyzyka) "X" else "O"}"
            },
            fontSize = 32.sp, //
            fontWeight = FontWeight.Bold, //
            modifier = Modifier.padding(bottom = 32.dp) //
        )

        // Rysowanie planszy 3x3
        for (wiersz in 0..2) {
            Row {
                for (kolumna in 0..2) {
                    val indeks = wiersz * 3 + kolumna
                    Button(
                        onClick = { kliknijPole(indeks) },
                        modifier = Modifier
                            .padding(4.dp)
                            .size(100.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(text = plansza[indeks], fontSize = 40.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Przycisk restartu
        Button(onClick = {
            plansza = List(9) { "" }
            kolejKrzyzyka = true
            zwyciezca = null
        }) {
            Text("Zagraj ponownie", fontSize = 20.sp)
        }
    }
}