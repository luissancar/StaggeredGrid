package com.example.staggeredgrid

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.staggeredgrid.ui.theme.StaggeredGridTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StaggeredGridTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val orientation = resources.configuration.orientation
                    StaggeredGrid(orientation)


                }
            }
        }
    }
}



@Composable
fun StaggeredGrid(orientacion : Int) {


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(MaterialTheme.colors.primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Ejemplo Staggered Grid",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(5.dp)
        ) {
            StaggeredGrid(
                orientacion=orientacion,
               numColumnsHorizontal = 4,
                numColumnsVertical = 2,//put the how many column you want
                modifier = Modifier.padding(5.dp)
            ) {
                staggeredText.forEach { text ->
                    val rnd = Random()
                    val color: Int = android.graphics.Color.argb(255,
                        rnd.nextInt(256),
                        rnd.nextInt(256),
                        rnd.nextInt(256)
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        backgroundColor = Color(color = color),
                        elevation = 10.dp,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = text,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}











@Composable
fun StaggeredGrid(orientacion: Int,
    modifier: Modifier = Modifier,
    numColumnsVertical: Int = 2,
    numColumnsHorizontal: Int = 3,
    content: @Composable () -> Unit
) {
   var numColumns=0
    if (orientacion == Configuration.ORIENTATION_LANDSCAPE) {
        numColumns=numColumnsHorizontal
    }
    else
    {
        numColumns=numColumnsVertical
    }
    Layout(
        content = content,
        modifier = modifier
    ) { measurable, constraints ->
        val columnWidth = (constraints.maxWidth / numColumns)
        val itemConstraints = constraints.copy(maxWidth = columnWidth)
        val columnHeights = IntArray(numColumns) { 0 }
        val placeables = measurable.map { measurable ->
            val column = shortTestColumn(columnHeights)
            val placeable = measurable.measure(itemConstraints)
            columnHeights[column] += placeable.height
            placeable
        }

        val height = columnHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight)
            ?: constraints.minHeight

        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            val columnYPointers = IntArray(numColumns) { 0 }
            placeables.forEach { placeable ->
                val column = shortTestColumn(columnYPointers)

                placeable.place(
                    x = columnWidth * column,
                    y = columnYPointers[column]
                )

                columnYPointers[column] += placeable.height
            }
        }
    }
}

private fun shortTestColumn(columnHeights: IntArray): Int {
    var minHeight = Int.MAX_VALUE
    var columnIndex = 0

    columnHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            columnIndex = index
        }
    }

    return columnIndex
}




