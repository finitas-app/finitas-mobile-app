package pl.finitas.app.auth_feature.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.auth_feature.presentation.AuthType
import pl.finitas.app.auth_feature.presentation.AuthViewModel

@Composable
fun BoxScope.SignUpPanel(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, color = Color.White.copy(.1f), RoundedCornerShape(10.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Colors.mirrorSpendingList.copy(.39f),
                        Colors.backgroundDark.copy(.60f),
                    )
                ),
            )
            .padding(vertical = 60.dp)
            .padding(horizontal = 20.dp)
    ) {
        Column {
            AuthInput(
                value = viewModel.credentialsState.login,
                label = "Login",
                inputBorderColor = Brush.linearGradient(
                    listOf(
                        Color(0XFF2C7B99).copy(.5f),
                        Color(0XFF635B7D).copy(.5f),
                        Color(0XFFF85784).copy(.5f),
                    )
                ),
                onValueChange = viewModel::setLogin
            )

            AuthInput(
                value = viewModel.credentialsState.password,
                label = "Password",
                inputBorderColor = Brush.linearGradient(
                    listOf(
                        Color(0xFFFBA776).copy(.5f),
                        Color(0xFF76A470).copy(.5f),
                        Color(0xFF299182).copy(.5f),
                    )
                ),
                onValueChange = viewModel::setPassword,
                modifier = Modifier.padding(top = 11.dp)
            )

            AuthButton(
                text = "Sigh up",
                onClick = viewModel::signUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)

            )

            AuthButton(
                text = "Return to login",
                onClick = { viewModel.changeAuthType(AuthType.SignIn) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)

            )
        }
    }
}
