package example.mardsoul.draganddroplazycolumn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import example.mardsoul.draganddroplazycolumn.ui.LazyColumnKeysApp
import example.mardsoul.draganddroplazycolumn.ui.theme.ExampleLazyColumnKeysTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			ExampleLazyColumnKeysTheme {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
					LazyColumnKeysApp(modifier = Modifier.padding(innerPadding))
				}
			}
		}
	}
}