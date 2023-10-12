package pl.finitas.app.core.presentation.components.inputs

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.finitas.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstructorInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it.take(20)) },
        textStyle = TextStyle(
            fontSize = 20.sp,
            color = Color.White.copy(alpha = 0.3f),

        ),
        modifier = Modifier
            .border(
                1.dp,
                Color.White.copy(alpha = 0.3f),
                RoundedCornerShape(10.dp),
            ),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = colorResource(id = R.color.background_dark),
            cursorColor = Color.Black,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        ),
    )
}

@Preview
@Composable
fun ConstructorInputPreview() {

}
