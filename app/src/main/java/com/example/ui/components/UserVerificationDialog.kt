package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AviatorCardBg
import com.example.ui.theme.AviatorCardBorder
import com.example.ui.theme.AviatorGold
import com.example.ui.theme.AviatorGreen
import com.example.ui.theme.AviatorRedLight
import com.example.ui.theme.AviatorRedPrimary
import com.example.ui.theme.AviatorSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun UserVerificationDialog(
  userId: String,
  site: String,
  dialogErrorMessage: String?,
  onVerifyAndLogin: (password: String) -> Unit,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  var passwordInput by remember { mutableStateOf("") }

  val whatsappNumber = "+94765529447"

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("user_verification_dialog"),
      shape = RoundedCornerShape(24.dp),
      color = AviatorSurface,
      border = androidx.compose.foundation.BorderStroke(1.5.dp, AviatorRedPrimary)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = "Warning",
              tint = AviatorRedPrimary,
              modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "NOT APPROVED",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                color = AviatorRedLight,
                letterSpacing = 1.sp
              )
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ID Details Card
        Card(
          colors = CardDefaults.cardColors(containerColor = AviatorCardBg),
          shape = RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, AviatorCardBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "User ID: $userId",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = AviatorGold
              )
            )
            Text(
              text = "Platform: $site",
              style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(AviatorRedPrimary.copy(alpha = 0.2f))
                .border(1.dp, AviatorRedPrimary, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = "STATUS: PENDING ADMIN APPROVAL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AviatorRedLight
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Your User ID is not approved yet. Please contact Admin via WhatsApp to approve your User ID and get your password.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
          )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // WhatsApp Contact Button
        Button(
          onClick = {
            val message = "Hi Admin, please approve my Aviator User ID: $userId for $site"
            val url = "https://wa.me/94765529447?text=${Uri.encode(message)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            try {
              context.startActivity(intent)
            } catch (_: Exception) {}
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = AviatorGreen,
            contentColor = Color.Black
          )
        ) {
          Icon(
            imageVector = Icons.Default.Message,
            contentDescription = "WhatsApp",
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "CONTACT ADMIN (WHATSAPP)",
            fontWeight = FontWeight.Black,
            fontSize = 13.sp,
            letterSpacing = 0.5.sp
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input Section
        Column(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "ENTER ADMIN PROVIDED PASSWORD:",
            style = MaterialTheme.typography.labelSmall.copy(
              color = AviatorGold,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            ),
            modifier = Modifier.padding(bottom = 6.dp)
          )

          OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            placeholder = { Text("Enter Password", color = TextMuted) },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = AviatorGold
              )
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AviatorGold,
              unfocusedBorderColor = AviatorCardBorder,
              focusedContainerColor = AviatorCardBg,
              unfocusedContainerColor = AviatorCardBg,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )

          if (dialogErrorMessage != null) {
            Text(
              text = dialogErrorMessage,
              color = AviatorRedLight,
              style = MaterialTheme.typography.bodySmall,
              modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // VERIFIED & LOGIN BUTTON
        Button(
          onClick = {
            onVerifyAndLogin(passwordInput.trim())
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
          shape = RoundedCornerShape(16.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = AviatorGold,
            contentColor = Color.Black
          )
        ) {
          Icon(
            imageVector = Icons.Default.VerifiedUser,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "VERIFIED & LOGIN",
            fontWeight = FontWeight.Black,
            fontSize = 15.sp,
            letterSpacing = 0.5.sp
          )
        }
      }
    }
  }
}
