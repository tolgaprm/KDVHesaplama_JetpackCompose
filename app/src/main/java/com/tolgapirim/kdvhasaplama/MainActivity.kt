package com.tolgapirim.kdvhasaplama

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tolgapirim.kdvhasaplama.ui.theme.KDVHesaplamaTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KDVHesaplamaTheme {
                // A surface container using the 'background' color from the theme
                Scaffold {
                    Surface(color = MaterialTheme.colors.background) {
                        App()
                    }
                }
            }
        }
    }
}

@Composable
fun App() {

    val kdvOranRadioButtonText = listOf(
        stringResource(id = R.string.yuzde1),
        stringResource(id = R.string.yuzde8),
        stringResource(id = R.string.yuzde18),
        stringResource(id = R.string.di_er)
    )

    val (selectedKdvOrani, onOptionSelectedKDVOrani) = remember {
        mutableStateOf(kdvOranRadioButtonText[2])
    }

    val islemRadioButtonText = listOf(
        stringResource(id = R.string.kdv_dahil_kdv_haric),
        stringResource(id = R.string.kdv_haric_kdv_dahil)
    )

    val (selectedIslem, onOptionSelectedIslem) = remember { mutableStateOf(islemRadioButtonText[0]) }

    var selectedKdvOran: Float? = null


    var girilenKdvOrani by remember { mutableStateOf("") }

    var girilenTutar by remember {
        mutableStateOf("")
    }


    var sonuc by remember {
        mutableStateOf(0f)
    }
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(top = 28.dp)
                .fillMaxWidth(),
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center

        )

        AltBaslik(R.string.islem)

        Spacer(modifier = Modifier.height(16.dp))

        // işlem Radio Button

        CreateRadioButton(
            radioTextList = islemRadioButtonText,
            selectedOption = selectedIslem,
            onOptionSelected = onOptionSelectedIslem,
            isColumn = true
        )


        // KDV ORANI

        AltBaslik(stringResID = R.string.kdv_orani)



        Row {
            CreateRadioButton(
                radioTextList = kdvOranRadioButtonText,
                selectedOption = selectedKdvOrani,
                onOptionSelected = onOptionSelectedKDVOrani,
                isColumn = false
            )


            /*RadioButton diğer seçeneği seçiliyse
            textFiled oluştur
            yoksa oluşturma
            */
            if (selectedKdvOrani == stringResource(id = R.string.di_er)) {
                OutlinedTextField(
                    value = girilenKdvOrani,
                    onValueChange = { girilenKdvOrani = it },
                    label = {
                        Text(
                            text = stringResource(
                                id = R.string.tutar
                            )
                        )
                    },
                    modifier = Modifier.height(60.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(fontSize = 15.sp),


                    )

                selectedKdvOran = girilenKdvOrani.toFloatOrNull()

            }


        }

        // KDV Hariç Tutar veya Dahil Tutar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            if (selectedIslem == stringResource(id = R.string.kdv_haric_kdv_dahil)) {
                Text(
                    text = stringResource(id = R.string.kdv_haric_tutar),
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold),

                    )
            } else {
                Text(
                    text = stringResource(id = R.string.kdv_dahil_tutar),
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold),

                    )
            }

            Spacer(modifier = Modifier.width(10.dp))

            OutlinedTextField(
                value = girilenTutar, onValueChange = {
                    girilenTutar = it
                },
                label = { Text(text = stringResource(id = R.string.tutar)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }



        selectedKdvOran = when (selectedKdvOrani) {
            stringResource(id = R.string.yuzde1) -> 0.1f
            stringResource(id = R.string.yuzde8) -> 0.8f
            stringResource(id = R.string.yuzde18) -> 0.18f
            else -> girilenKdvOrani.toFloatOrNull()?.div(100)
        }


        val context = LocalContext.current
        // Buttonlar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    sonuc = hesapla(
                        context,
                        selectedIslem,
                        selectedKdvOran,
                        girilenTutar.toFloatOrNull()
                    )
                },
            ) {
                Text(
                    text = stringResource(id = R.string.hesapla),
                    style = MaterialTheme.typography.button.copy(fontSize = 18.sp)
                )
            }

            Button(onClick = {
               onOptionSelectedIslem(context.getString(R.string.kdv_dahil_kdv_haric))
                onOptionSelectedKDVOrani(context.getString(R.string.yuzde18))
                girilenKdvOrani = ""
                girilenTutar =""
                sonuc = 0f

            }) {
                Text(
                    text = stringResource(id = R.string.temizle),
                    style = MaterialTheme.typography.button.copy(fontSize = 18.sp)
                )


            }
        }


        if (sonuc != 0f) {

            val fiyat = NumberFormat.getCurrencyInstance().format(sonuc)
            val tutar = NumberFormat.getCurrencyInstance().format(girilenTutar.toFloat())

            if (selectedIslem == stringResource(id = R.string.kdv_haric_kdv_dahil)) {
                SonucText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    textId = R.string.kdv_haric_tutar_s,
                    tutar
                )

                SonucText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    textId = R.string.kdv_dahil_tutar_s,
                    fiyat

                )

            }else{
                SonucText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    textId = R.string.kdv_haric_tutar_s,
                    fiyat
                )

                SonucText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    textId = R.string.kdv_dahil_tutar_s,
                    tutar

                )
            }


        }


    }

}

@Composable
fun SonucText(modifier: Modifier, textId: Int, fiyat: String?) {

    fiyat?.let {
        Text(
            text = stringResource(id = textId, (it)),
            style = MaterialTheme.typography.h6,
            modifier = modifier,
            fontWeight = FontWeight.SemiBold
        )
    }

}


fun hesapla(
    context: Context,
    selectedIslem: String,
    selectedKdvOrani: Float?,
    tutar: Float?
): Float {
    var sonuc = 0f
    if (selectedKdvOrani != null) {
        if (tutar != null) {
            if (selectedIslem == context.getString(R.string.kdv_haric_kdv_dahil)) {
                sonuc = tutar + (tutar * selectedKdvOrani)

            } else {
                sonuc = tutar / (1+ (selectedKdvOrani))
            }
        } else {
            Toast.makeText(context, "Lütfen tutar kısmını boş bırakmayınız!", Toast.LENGTH_SHORT)
                .show()
        }
    } else {
        Toast.makeText(context, "Lütfen Kdv oranını boş bırakmayınız!", Toast.LENGTH_SHORT).show()
    }

    return sonuc
}




@Composable
fun CreateRadioButton(
    radioTextList: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    isColumn: Boolean,
    textFieldValue: String = "",
) {
    if (isColumn) {
        Column(modifier = Modifier.selectableGroup()) {

            radioTextList.forEach { text ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = text == selectedOption,
                        onClick = { onOptionSelected(text) })
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = text)
                }
                Spacer(modifier = Modifier.height(5.dp))
            }


        }
    } else {
        Row(modifier = Modifier.selectableGroup()) {

            radioTextList.forEach { text ->
                Row(
                    modifier = Modifier
                        .height(48.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = text == selectedOption,
                        onClick = { onOptionSelected(text) })
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = text)
                }
                Spacer(modifier = Modifier.width(13.dp))
            }


        }
    }


}


@Composable
fun AltBaslik(stringResID: Int) {
    Text(
        text = stringResource(id = stringResID),
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        modifier = Modifier.padding(top = 40.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KDVHesaplamaTheme {

        Scaffold {
            Surface(color = MaterialTheme.colors.background) {
                App()
            }
        }
    }
}

