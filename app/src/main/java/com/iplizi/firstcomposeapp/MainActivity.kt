@file:OptIn(
    ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class,
    ExperimentalMotionApi::class
) // даёт возможность использовать экспереминтальные функции

package com.iplizi.firstcomposeapp

import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.iplizi.firstcomposeapp.ui.theme.DarkGrey
import kotlinx.coroutines.delay
import java.lang.Math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                color = Color(0xFF101010),
                modifier = Modifier.fillMaxSize()
            ){
                Box(
                    contentAlignment = Alignment.Center
                ){
                    Timer(
                        totalTime = 100L * 1000L,
                        handleColor = Color.Green,
                        inactiveBarColor = Color.DarkGray,
                        activeBarColor = Color(0xFF37B900),
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }

    }

}

@Composable
fun Timer(
    totalTime: Long,
    handleColor: Color,
    inactiveBarColor: Color,
    activeBarColor: Color,
    modifier: Modifier = Modifier,
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var value by remember { mutableStateOf(initialValue) }
    var currentTime by remember { mutableStateOf(totalTime) }
    var isTimeRunning by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = currentTime, key2 = isTimeRunning){
        if(currentTime > 0 && isTimeRunning){
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .onSizeChanged {
                size = it
            }
    ) {
        Canvas(modifier = modifier) {
            drawArc( // дуга серая
                color = inactiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc( // дуга зелёная
                color = activeBarColor,
                startAngle = -215f,
                sweepAngle = 250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            val center = Offset(size.width / 2f, size.height / 2f)
            val beta = (250f * value + 145f) * (PI / 180f).toFloat()
            val r = size.width / 2f
            val a = cos(beta) * r
            val b = sin(beta) * r

            drawPoints(
                listOf(Offset(center.x + a, center.y + b)),
                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round
            )
        }

        Text(
            text = (currentTime / 1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Button(
            onClick = {
                      if(currentTime <= 0L){
                          currentTime = totalTime
                          isTimeRunning = true
                      } else{
                          isTimeRunning = !isTimeRunning
                      }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (!isTimeRunning || currentTime <= 0L) {
                    Color.Green
                } else {
                    Color.Red
                }
            )
        ) {
            Text(
                text = if (isTimeRunning && currentTime >= 0L) "Stop"
                else if (!isTimeRunning && currentTime >= 0) "Start"
                else "Restart"
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    FirstComposeAppTheme {
//        Greeting("Compose")
//    }
//}

//Column(
//modifier = Modifier
//.background(Color.Yellow)
//.fillMaxHeight(0.5f)
//.width(300.dp) // если установить миллиард то будет на всю ширину родителя
////                   .requiredWidth(600.dp) она станет миллиардом
//.border(5.dp, Color.Magenta)
//.padding(5.dp)
//.border(5.dp, Color.Blue)
//.padding(5.dp)
//.border(10.dp, Color.Green)
//.padding(10.dp) // модификатор работает последовально
//) {
//    Text(
//        text = "Hello",
//        modifier = Modifier
//            .clickable {
//                Toast.makeText(applicationContext, "click", Toast.LENGTH_SHORT).show()
//            }
//    ) // modifier = Modifier.offset(50.dp, 20.dp)  марджин который не двигает остальные элементы, то есть если оно залезет на другой текст то так и будет, в отличии от марджина в xml
//    Spacer(Modifier.height(50.dp)) // пустое пространство
//    Text(text = "World")
//}
//
//}   Column(
//modifier = Modifier
//.background(Color.Yellow)
//.fillMaxHeight(0.5f)
//.width(300.dp) // если установить миллиард то будет на всю ширину родителя
////                   .requiredWidth(600.dp) она станет миллиардом
//.border(5.dp, Color.Magenta)
//.padding(5.dp)
//.border(5.dp, Color.Blue)
//.padding(5.dp)
//.border(10.dp, Color.Green)
//.padding(10.dp) // модификатор работает последовально
//) {
//    Text(
//        text = "Hello",
//        modifier = Modifier
//            .clickable {
//                Toast.makeText(applicationContext, "click", Toast.LENGTH_SHORT).show()
//            }
//    ) // modifier = Modifier.offset(50.dp, 20.dp)  марджин который не двигает остальные элементы, то есть если оно залезет на другой текст то так и будет, в отличии от марджина в xml
//    Spacer(Modifier.height(50.dp)) // пустое пространство
//    Text(text = "World")
//}


//val painter = painterResource(id = R.drawable.img)
//val description = "This is my first Compose image"
//val title = "This is my first Compose image"
//
//Box(modifier = Modifier
//.fillMaxWidth(0.5f)
//.padding(16.dp)) {
//    paintImage(
//        painter = painter,
//        contentDescription = description,
//        title = title
//    )
//}

//@Composable
//fun paintImage(
//    painter: Painter,
//    contentDescription: String,
//    title: String,
//    modifier: Modifier = Modifier
//
//){
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(15.dp),
//        elevation = 5.dp // shadow
//    ){
//        Box(modifier = Modifier.height(200.dp)){
//            Image(
//                modifier = Modifier.fillMaxWidth(),
//                painter = painter,
//                contentDescription = contentDescription,
//                contentScale = ContentScale.FillWidth
//            )
//
//            Box(modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    Brush.verticalGradient(
//                        colors = listOf(
//                            Color.Transparent,
//                            Color.Black
//                        ),
//                        startY = 50f
//                    )
//                ))
//
//            Box(
//                modifier = Modifier.fillMaxSize().padding(12.dp),
//                contentAlignment = Alignment.BottomStart
//            ){
//                Text(title, style = TextStyle(color = Color.White, fontSize = 16.sp))
//            }
//
//        }
//    }


//val fontFamily = FontFamily(
//    Font(R.font.lato_thin, FontWeight.Thin),
//    Font(R.font.lato_light, FontWeight.Light),
//    Font(R.font.lato_regular, FontWeight.Normal),
//    Font(R.font.lato_bold, FontWeight.Bold),
//    Font(R.font.lato_italic, FontWeight.Normal, FontStyle.Italic),
//    Font(R.font.lato_bolditalic, FontWeight.Bold, FontStyle.Italic),
//    Font(R.font.lato_lightitalic, FontWeight.Light, FontStyle.Italic),
//    Font(R.font.lato_thinitalic, FontWeight.Thin, FontStyle.Italic),
//    Font(R.font.lato_black, FontWeight.ExtraBold),
//    Font(R.font.lato_blackitalic, FontWeight.ExtraBold, FontStyle.Italic)
//)

//Box(
//modifier = Modifier
//.fillMaxSize()
//.background(Color(0x0FF101010)),
//contentAlignment = Alignment.Center
//) {
//    Text(
//        text = buildAnnotatedString {
//            withStyle(
//                style = SpanStyle(
//                    color = Color.Green,
//                    fontSize = 50.sp
//                )
//            ) {
//                append("S")
//            }
//            append("tyling ")
//
//            withStyle(
//                style = SpanStyle(
//                    color = Color.Green,
//                    fontSize = 50.sp
//                )
//            ) {
//                append("T")
//            }
//            append("text")
//        },
//        color = Color.White,
//        fontSize = 30.sp,
//        fontFamily = fontFamily,
//        fontWeight = FontWeight.Normal,
//        fontStyle = FontStyle.Italic,
//        textAlign = TextAlign.Center,
//        textDecoration = TextDecoration.Underline
//    )
//}

//Column(modifier = Modifier.fillMaxSize()) {
//    val color = remember{ mutableStateOf(Color.Yellow) }
//    ColorBox(
//        Modifier.weight(1f).fillMaxSize()
//    ){
//        color.value = it
//    }
//    Box(modifier = Modifier
//        .background(color.value)
//        .weight(1f)
//        .fillMaxSize())
//}

//@Composable
//fun ColorBox(
//    modifier: Modifier = Modifier,
//    updateColor: (Color) -> Unit
//) {
//
////        val color =
////            remember { mutableStateOf(Color.Yellow) } // запоминает последнее значение, и после рекомпозиции не сбрасывает опять на жёлтый
//
//    val fontFamily = FontFamily(
//        Font(R.font.lato_thin, FontWeight.Thin),
//        Font(R.font.lato_light, FontWeight.Light),
//        Font(R.font.lato_regular, FontWeight.Normal),
//        Font(R.font.lato_bold, FontWeight.Bold),
//        Font(R.font.lato_italic, FontWeight.Normal, FontStyle.Italic),
//        Font(R.font.lato_bolditalic, FontWeight.Bold, FontStyle.Italic),
//        Font(R.font.lato_lightitalic, FontWeight.Light, FontStyle.Italic),
//        Font(R.font.lato_thinitalic, FontWeight.Thin, FontStyle.Italic),
//        Font(R.font.lato_black, FontWeight.ExtraBold),
//        Font(R.font.lato_blackitalic, FontWeight.ExtraBold, FontStyle.Italic)
//    )
//
//    Box(modifier = modifier
//        .background(Color.Red)
//        .clickable {
//
//            updateColor(
//                Color(
//                    Random.nextFloat(),
//                    Random.nextFloat(),
//                    Random.nextFloat(),
//                    1f
//                )
//            )
//
//        },
//        contentAlignment = Alignment.Center
//    ){
//        Text(text = buildAnnotatedString {
//            withStyle(
//                style = SpanStyle(
//                    color = Color.Blue,
//                    fontSize = 50.sp
//                )
//            ) {
//                append("C")
//            }
//            append("lick and change the color!")
//        },
//            color = Color.White,
//            fontSize = 30.sp,
//            fontFamily = fontFamily,
//            fontWeight = FontWeight.Normal,
//            fontStyle = FontStyle.Italic,
//            textAlign = TextAlign.Center,
//            textDecoration = TextDecoration.Underline)
//
//    }
//}


//val scaffoldState = rememberScaffoldState()
//var textFieldState by remember{ mutableStateOf("") }
//val scope = rememberCoroutineScope()
//
//Scaffold(modifier = Modifier.fillMaxSize(),
//scaffoldState = scaffoldState
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 30.dp)
//    ){
//        TextField(
//            value = textFieldState,
//            onValueChange = {
//                textFieldState = it
//            },
//            label = {
//                Text("Enter your name")
//            },
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = {
//
//            scope.launch{
//                scaffoldState.snackbarHostState.showSnackbar("Hello $textFieldState", duration = SnackbarDuration.Short)
//            }
//
//        }){
//            Text(text = "Apply")
//        }
//
//    }
//}


//
//LazyColumn {
//
//    itemsIndexed(
//        listOf("This", "is", "Jetpack", "Compose")
//    ){ index, string ->
//        Text(
//            text = string,
//            fontSize = 24.sp,
//            fontWeight  = FontWeight.Bold,
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 24.dp)
//        )
//    }
//
//}


//val constraints = ConstraintSet{
//    val greenBox = createRefFor("greenbox")
//    val redBox = createRefFor("redbox")
//
//    constrain(greenBox){
//        top.linkTo(parent.top, 200.dp)
//        start.linkTo(parent.start)
//        end.linkTo(parent.end)
//
//        width = Dimension.value(100.dp)
//        height = Dimension.value(100.dp)
//    }
//
//    constrain(redBox){
//        top.linkTo(greenBox.bottom, 100.dp)
//        start.linkTo(parent.start) // старт = лево, энд = право
//        end.linkTo(parent.end)
//
//        width = Dimension.value(100.dp)
//        height = Dimension.value(100.dp)
//    }
//
//}
//
//ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {
//
//    Box(modifier = Modifier
//        .background(Color.Green)
//        .layoutId("greenbox") // то самое айди
//    )
//
//    Box(modifier = Modifier
//        .background(Color.Red)
//        .layoutId("redbox") // то самое айди
//    )
//
//}


//var sizeState by remember{ mutableStateOf(200.dp) }
//val size by animateDpAsState(
//    targetValue = sizeState,
//    animationSpec = tween(
//        durationMillis = 3000,
//        delayMillis = 300,
//        easing = LinearOutSlowInEasing
//    )
//)
//
//val infiniteTransition = rememberInfiniteTransition()
//val color by infiniteTransition.animateColor(
//    initialValue = Color.Red,
//    targetValue = Color.Green,
//    animationSpec = infiniteRepeatable(
//        tween(durationMillis = 2000),
//        repeatMode = RepeatMode.Reverse
//    )
//)
//
//Box(modifier = Modifier
//.size(size)
//.background(color),
//contentAlignment = Alignment.Center){
//
//    Button(onClick = {
//        sizeState += 50.dp
//    }) {
//        Text(text = "Increase Size")
//    }
//
//}


//
//setContent {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier.fillMaxSize()
//    ){
//        Column {
//            CircularProgressBar(percentage = 0.8f, number = 100)
//            Spacer(modifier = Modifier.height(16.dp))
//            CircularProgressIndicator(
//                color = Color.Green,
//                strokeWidth = 8.dp,
//                modifier = Modifier.size(100.dp)
//            )
//        }
//    }
//}

//@Composable
//fun CircularProgressBar(
//    percentage: Float,
//    number: Int,
//    fontSize: TextUnit = 28.sp,
//    radius: Dp = 50.dp,
//    color: Color = Color.Green,
//    strokeWidth: Dp = 8.dp,
//    animDuration: Int = 1000,
//    animDelay: Int = 0
//){
//    var animationPlayed by remember{ mutableStateOf(false) }
//    val curPercentage = animateFloatAsState(
//        targetValue = if(animationPlayed) percentage else 0f,
//        animationSpec = tween(
//            durationMillis = animDuration,
//            delayMillis = animDelay
//        )
//    )
//
//    LaunchedEffect(key1 = true){
//        animationPlayed = true
//    }
//
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier
//            .size(radius * 2)
//    ){
//        Canvas(
//            modifier = Modifier
//                .size(radius * 2f)
//        ){
//            drawArc(
//                color = color,
//                -90f,
//                360 * curPercentage.value,
//                useCenter = false,
//                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
//            )
//        }
//        Text(
//            text = (curPercentage.value * number).toInt().toString(),
//            color = Color.Black,
//            fontSize = fontSize,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}


//val windowInfo = rememberWindowInfo()
//Box(
//contentAlignment = Alignment.Center,
//modifier = Modifier
//.fillMaxSize()
//.background(Color.Black)
//.padding(16.dp)
//){
//    Row(
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier
//            .height(125.dp)
//            .border(1.dp, Color.Green, RoundedCornerShape(10.dp))
//            .padding(30.dp)
//    ){
//        var volume by remember{ mutableStateOf(0f) }
//        val barCount = 20
//
//        MusicKnob(
//            modifier = Modifier.size(100.dp)
//        ){
//            volume = it
//        }
//        Spacer(modifier = Modifier.width(20.dp))
//        VolumeBar(
//            modifier = Modifier
//                .fillMaxSize()
//                .height(30.dp),
//            activeBars = (barCount * volume).roundToInt(),
//            barCount = barCount
//        )
//    }
//
//}

//@Composable
//fun VolumeBar(
//    modifier: Modifier = Modifier,
//    activeBars: Int = 0,
//    barCount: Int = 10
//){
//    BoxWithConstraints(
//        contentAlignment = Alignment.Center,
//        modifier = modifier
//    ) {
//        val barWidth = remember{ constraints.maxWidth / (2f * barCount)}
//
//        Canvas(
//            modifier = modifier
//        ){
//            for(i in 0 until barCount){
//                drawRoundRect(
//                    color = if(i in 0..activeBars) Color.Green else Color.DarkGray,
//                    topLeft = Offset(i * barWidth * 2f + barWidth / 2f, 0f),
//                    size = Size(barWidth, constraints.maxHeight.toFloat()),
//                    cornerRadius = CornerRadius(0.05f)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun MusicKnob(
//    modifier: Modifier = Modifier,
//    limitingAngle: Float = 25f,
//    onValueChange: (Float) -> Unit
//){
//    var rotation by remember{ mutableStateOf(limitingAngle) }
//    var touchX by remember{ mutableStateOf(0f) }
//    var touchY by remember{ mutableStateOf(0f) }
//    var centerX by remember{ mutableStateOf(0f) }
//    var centerY by remember{ mutableStateOf(0f) }
//
//
//    Image(
//        painter = painterResource(id = R.drawable.img),
//        contentDescription = "Music Compose knob",
//        modifier = modifier
//            .fillMaxSize()
//            .onGloballyPositioned {
//                val windowBounds = it.boundsInWindow()
//
//                centerX = windowBounds.size.width / 2f
//                centerY = windowBounds.size.height / 2f
//            }
//            .pointerInteropFilter { event ->
//                touchX = event.x
//                touchY = event.y
//                val angle = -atan2(centerX - touchX, centerY - touchY) * (180f / PI).toFloat()
//
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN,
//                    MotionEvent.ACTION_MOVE -> {
//                        if (angle !in -limitingAngle..limitingAngle) {
//                            val fixedAngle = if (angle in -180f..limitingAngle) {
//                                360 + angle
//                            } else {
//                                angle
//                            }
//                            rotation = fixedAngle
//
//                            val percent = (fixedAngle - limitingAngle) / (360f - 2 * limitingAngle)
//                            onValueChange(percent)
//                            true
//                        } else false
//                    }
//                    else -> false
//                }
//            }
//            .rotate(rotation)
//    )
//}


//@Composable
//fun DropDown(
//    text: String,
//    modifier: Modifier = Modifier,
//    initiallyOpened: Boolean = false,
//    content: @Composable () -> Unit
//) {
//    var isOpen by remember{ mutableStateOf(initiallyOpened) }
//
//    val alpha = animateFloatAsState(
//        targetValue = if(isOpen) 1f else 0f,
//        animationSpec = tween(
//            durationMillis = 300
//        )
//    )
//    val rotateX = animateFloatAsState(
//        targetValue = if(isOpen) 0f else -90f,
//        animationSpec = tween(
//            durationMillis = 300
//        )
//    )
//
//    Column(
//        modifier = modifier.fillMaxWidth()
//    ){
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//        ){
//            Text(
//                text = text,
//                color = Color.White,
//                fontSize = 16.sp
//            )
//
//            Icon(
//                imageVector = Icons.Default.ArrowDropDown,
//                contentDescription = "Open or close the drop down",
//                tint = Color.White,
//                modifier = Modifier
//                    .clickable {
//                        isOpen = !isOpen
//                    }
//                    .scale(1f, if (isOpen) -1f else 1f) // поворачиваем иконку
//
//            )
//        }
//        Spacer(modifier = Modifier.height(10.dp))
//
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//                .graphicsLayer {
//                    transformOrigin = TransformOrigin(0.5f, 0f)
//                    rotationX = rotateX.value
//                }
//                .alpha(alpha.value)
//        ){
//            content()
//        }
//    }
//
//}

//Surface(
//color = Color(0x0FF101010),
//modifier = Modifier.fillMaxSize()
//){
//    DropDown(
//        text = "Hello 3D Drop Down!",
//        modifier = Modifier.padding(15.dp)
//    ) {
//        Text(
//            text = "This is content",
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(100.dp)
//                .background(Color.Green),
//            textAlign = TextAlign.Center,
//            fontSize = 30.sp
//        )
//    }
//}


//val moonScrollSpeed = 0.08f
//val midBgScrollSpeed = 0.03f
//
//var moonOffset by remember{ mutableStateOf(0f) }
//var midBgOffset by remember{ mutableStateOf(0f) }
//
//val imageHeight = (LocalConfiguration.current.screenWidthDp * (2f / 3f)).dp
//val lazyListState = rememberLazyListState()
//
//val nestedScrollConnection = object: NestedScrollConnection{
//    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//        val delta = available.y
//        val layoutInfo = lazyListState.layoutInfo
//        if(lazyListState.firstVisibleItemIndex == 0){
//            return Offset.Zero
//        }
//        if(layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1){
//            return Offset.Zero
//        }
//        moonOffset += delta * moonScrollSpeed
//        midBgOffset += delta * midBgScrollSpeed
//        return Offset.Zero
//    }
//}
//
//
//LazyColumn(
//modifier = Modifier
//.fillMaxWidth()
//.nestedScroll(nestedScrollConnection),
//state = lazyListState
//){
//    items(10){
//        Text(
//            text = "Sample item",
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        )
//    }
//
//    item{
//        Box(
//            modifier = Modifier
//                .clipToBounds()
//                .fillMaxWidth()
//                .height(imageHeight + midBgOffset.toDp())
//                .background(
//                    verticalGradient(
//                        listOf(
//                            Color(0xFF15F186),
//                            Color(0xFF1DE9B6)
//                        )
//                    )
//                )
//        ){
//            Image(
//                painter = painterResource(id = R.drawable.img),
//                contentDescription = "Compose Image",
//                contentScale = ContentScale.FillWidth,
//                alignment = Alignment.BottomCenter,
//                modifier = Modifier.matchParentSize()
//                    .graphicsLayer {
//                        translationY = moonOffset
//                    }
//            )
//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher_background),
//                contentDescription = "Compose Image",
//                contentScale = ContentScale.FillWidth,
//                alignment = Alignment.BottomCenter,
//                modifier = Modifier.matchParentSize()
//                    .graphicsLayer {
//                        translationY = moonOffset
//                    }
//            )
//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher_foreground),
//                contentDescription = "Compose Image",
//                contentScale = ContentScale.FillWidth,
//                alignment = Alignment.BottomCenter,
//                modifier = Modifier.matchParentSize()
//            )
//        }
//    }
//    items(20){
//        Text(
//            text = "Sample item",
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        )
//    }
//}

//private fun Float.toDp(): Dp {
//    return (this / Resources.getSystem().displayMetrics.density).dp
//}


//var items by remember {
//    mutableStateOf(
//        (1..20).map {
//            ListItem(
//                title = "Item $it",
//                isSelected = false
//            )
//        }
//    )
//}
//
//LazyColumn(
//modifier = Modifier
//.fillMaxSize()
//){
//    items(items.size){ index ->
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable{
//                    items = items.mapIndexed{ j, item ->
//                        if(index == j){
//                            item.copy(isSelected = !item.isSelected) // копируем прошлое значение и изменяем выбрано или нет на противоположный вариант
//                        } else item
//                    }
//                }
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ){
//            Text(text = items[index].title)
//            if(items[index].isSelected){
//                Icon(
//                    imageVector = Icons.Default.Check,
//                    contentDescription = "selected",
//                    tint = Color.Green,
//                    modifier = Modifier.size(20.dp)
//                )
//            }
//        }
//    }
//}


//val permissionState = rememberMultiplePermissionsState(
//    permissions = listOf(
//        Manifest.permission.RECORD_AUDIO,
//        Manifest.permission.CAMERA
//    )
//)
//
//val lifecycleOwner = LocalLifecycleOwner.current
//DisposableEffect(key1 = lifecycleOwner){
//    val observer = LifecycleEventObserver{_, event ->
//        if(event == Lifecycle.Event.ON_START){
//            permissionState.launchMultiplePermissionRequest()
//        }
//    }
//    lifecycleOwner.lifecycle.addObserver(observer)
//
//    onDispose {
//        lifecycleOwner.lifecycle.removeObserver(observer)
//    }
//
//}
//
//Column(
//modifier = Modifier
//.fillMaxSize()
//.padding(16.dp),
//horizontalAlignment = Alignment.CenterHorizontally,
//verticalArrangement = Arrangement.Center
//){
//    permissionState.permissions.forEach { perm ->
//        when(perm.permission){
//            Manifest.permission.CAMERA -> {
//                when{
//                    perm.status.isGranted -> {
//                        Text(text = "Camera permission accepted", fontSize = 20.sp)
//                    }
//                    perm.status.shouldShowRationale -> {
//                        Text(text = "Camera permission is needed for your mom's porn videos", fontSize = 20.sp)
//                    }
//                    perm.isPermanentlyDenied() -> {
//                        Text(text = "Camera permission was permanently denied and your mom can't work!", fontSize = 20.sp)
//                    }
//                }
//            }
//            Manifest.permission.RECORD_AUDIO -> {
//                when{
//                    perm.status.isGranted -> {
//                        Text(text = "Record audio permission accepted", fontSize = 20.sp)
//                    }
//                    perm.status.shouldShowRationale -> {
//                        Text(text = "Record audio permission is needed for your mom's porn videos", fontSize = 20.sp)
//                    }
//                    perm.isPermanentlyDenied() -> {
//                        Text(text = "Record audio permission was permanently denied and your mom can't work!", fontSize = 20.sp)
//                    }
//                }
//            }
//        }
//    }
//}


//if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        items(10) {
//            Text(
//                text = "Item $it",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.Cyan)
//                    .padding(16.dp)
//            )
//        }
//
//        items(10) {
//            Text(
//                text = "Item $it",
//                fontSize = 25.sp,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.Green)
//                    .padding(16.dp)
//            )
//        }
//
//
//    }
//}else{
//    Row(
//        modifier = Modifier.fillMaxWidth()
//    ){
//        LazyColumn(
//            modifier = Modifier.weight(1f)
//        ) {
//            items(10) {
//                Text(
//                    text = "Item $it",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color.Cyan)
//                        .padding(16.dp)
//                )
//            }
//        }
//
//        LazyColumn(
//            modifier = Modifier.weight(1f)
//        ) {
//            items(10) {
//                Text(
//                    text = "Item $it",
//                    fontSize = 25.sp,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color.Green)
//                        .padding(16.dp)
//                )
//            }
//        }
//
//    }
//}


//Column {
//    var progress by remember {
//        mutableStateOf(0f)
//    }
//    ProfileHeader(progress = progress)
//    Spacer(modifier = Modifier.height(32.dp))
//    Slider(
//        value = progress,
//        onValueChange = {
//            progress = it
//        },
//        modifier = Modifier.padding(horizontal = 32.dp)
//    )
//}

//@Composable
//fun ProfileHeader(progress: Float) {
//    val context = LocalContext.current
//    val motionScene = remember {
//        context.resources
//            .openRawResource(R.raw.motion_scene)
//            .readBytes()
//            .decodeToString()
//    }
//    MotionLayout(
//        motionScene = MotionScene(content = motionScene),
//        progress = progress,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        val properties = motionProperties(id = "profile_pic")
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.DarkGray)
//                .layoutId("box")
//        )
//        Image(
//            painter = painterResource(id = R.drawable.img),
//            contentDescription = null,
//            modifier = Modifier
//                .clip(CircleShape)
//                .border(
//                    width = 2.dp,
//                    color = properties.value.color("background"),
//                    shape = CircleShape
//                )
//                .layoutId("profile_pic")
//        )
//        Text(
//            text = "Motion Compose Layout",
//            fontSize = 24.sp,
//            modifier = Modifier.layoutId("username"),
//            color = properties.value.color("background")
//        )
//    }
//}


//val viewModel = viewModel<MainViewModel>()
//val state = viewModel.state
//
//LazyColumn(
//modifier = Modifier.fillMaxSize()
//){
//    items(state.items.size) { i ->
//        val item = state.items[i]
//
//        if(i >= state.items.size -1 && !state.endReached && !state.isLoading) {
//            viewModel.loadNextItems()
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ){
//            Text(
//                text = item.title,
//                fontSize = 20.sp,
//                color = Color.Black
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(item.description)
//        }
//    }
//    item{
//        if(state.isLoading){
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                horizontalArrangement = Arrangement.Center
//            ){
//                CircularProgressIndicator()
//            }
//        }
//    }
//}


//@Composable
//fun Navigation() {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "splash_screen"){
//        composable("splash_screen"){
//            SplashScreen(navController = navController)
//        }
//        composable("main_screen"){
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
//                Text(text = "MAIN SCREEN", color = Color.White)
//            }
//        }
//    }
//}
//
//@Composable
//fun SplashScreen(navController: NavController) {
//
//    val scale = remember{ Animatable(1f) }
//
//    LaunchedEffect(key1 = true) {
//        scale.animateTo(
//            targetValue = 1.4f,
//            animationSpec = tween(
//                durationMillis = 500,
//                easing = {
//                    OvershootInterpolator(2f).getInterpolation(it)
//                }
//            )
//        )
//        delay(3000L)
//        navController.navigate("main_screen")
//    }
//
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier.fillMaxSize()
//    ){
//        Image(
//            painter = painterResource(id = R.drawable.img),
//            contentDescription = "logo",
//            modifier = Modifier.scale(scale.value)
//        )
//    }
//}


//@Composable
//fun Greeting(name: String) {
//    Column(modifier = Modifier
//        .fillMaxWidth(1f)
//        .height(500.dp)
//        .background(Color.DarkGray),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceBetween){
//
//        Text(text = "Hello $name!")
//        Text(text = "Hello, Column")
//
//        Row(modifier = Modifier
//            .size(100.dp, 50.dp)
//            .background(Color.Blue),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            verticalAlignment = Alignment.CenterVertically){
//            Text(text = "Hello,")
//            Text(text = "Row")
//        }
//    }
//
//}

//Surface(color = DarkGrey, modifier = Modifier.fillMaxSize()){
//    Navigation()
//}