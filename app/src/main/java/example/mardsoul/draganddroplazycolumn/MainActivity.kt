package example.mardsoul.draganddroplazycolumn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import example.mardsoul.draganddroplazycolumn.ui.LazyColumnApp
import example.mardsoul.draganddroplazycolumn.ui.theme.ExampleLazyColumnTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			ExampleLazyColumnTheme {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
					LazyColumnApp(modifier = Modifier.padding(innerPadding))
				}
			}
		}
	}
}