package devesh.medic.dose.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import devesh.medic.dose.CalcFormulas
import devesh.medic.dose.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculateByWeightCompose(
    weightInput: MutableState<String>,
    adultDose: MutableState<String>,
    onFormulaChange: (CalcFormulas) -> Unit,
    onCalculateButtonClick: () -> Unit,
    onReset: () -> Unit,
    finalResult: MutableState<String>
) {
    val mContext = LocalContext.current
    var text by remember { weightInput }
    var text1 by remember { adultDose }
    var selectedFormula = remember {
        mutableStateOf("yf")
    }

    val sheetState = rememberModalBottomSheetState()
    //val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val supportText = remember {
        mutableStateOf("Child's Weight in Kilogram (Kg)")
    }

    Box {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxSize()

        ) {

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Weight") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                supportingText = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = supportText.value,
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

            )
            OutlinedTextField(
                value = text1,
                onValueChange = { text1 = it },
                label = { Text("Adult Dose") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),

                supportingText = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Adult Dose in milligram (mg)"
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)

            )

            RadioButtonGroupWeight(onFormulaChange = {
                onFormulaChange(it)
                selectedFormula.value = it.id
                if (it.id.equals("wcfp")) {
                    supportText.value = "Child's Weight in Pound"
                } else {
                    supportText.value = "Child's Weight in Kilogram (Kg)"
                }
            })

            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
                Button(
                    onClick = {
                        if (weightInput.value == "" || adultDose.value == "") {
                            Toast
                                .makeText(mContext, "Please Enter Weight or Dose", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            onCalculateButtonClick()
                            showBottomSheet = true
                        }
                    },
                    modifier = Modifier
                        .padding(2.dp)

                ) {
                    Text(text = "Calculate")
                }
                TextButton(
                    onClick = { onReset() },
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text(text = "Reset")
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    BottomSheetResultsWeight(
                        result = finalResult.value,
                        childWeight = weightInput.value.toInt(),
                        adultDose = adultDose.value.toDouble(),
                        formulaType = selectedFormula.value
                    )
                }
            }

       //     Spacer(modifier = Modifier.height(20.dp))
       //     AdMobBannerLarge()
        }


    }

}


val itemsWeightFormulas = listOf(

    CalcFormulas(
        "wcfkg",
        "Clark's Formula (Kg)",
        "It is best when the calculation is not possible from age, also more commonly used"
    ),

    CalcFormulas(
        "wcfp",
        "Clark's Formula (Pound)",
        "It is best when the calculation is not possible from age, also more commonly used"
    ),
    CalcFormulas(
        "wmwf",
        "Modified Weight Formula",
        "It is no need conversion between weight and pounds"
    )


)

@Composable
fun RadioButtonGroupWeight(onFormulaChange: (CalcFormulas) -> Unit) {
    val selectedValue = remember { mutableStateOf("Clark's Formula (Kg)") }

    val isSelectedItem: (String) -> Boolean = { selectedValue.value == it }
    val onChangeState: (CalcFormulas) -> Unit = {
        selectedValue.value = it.name
        onFormulaChange(it)
    }

    Column(
        Modifier
            .padding(8.dp)
    ) {
        //    Text(text = "Selected value: ${selectedValue.value.ifEmpty { "NONE" }}")
        itemsWeightFormulas.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .selectable(
                        selected = isSelectedItem(item.name),
                        onClick = { onChangeState(item) },
                        role = Role.RadioButton
                    )
                    .padding(8.dp)
            ) {

                Row(modifier = Modifier.padding(3.dp)) {
                    RadioButton(
                        selected = isSelectedItem(item.name),
                        onClick = null
                    )
                    Text(
                        text = item.name,
                        modifier = Modifier.fillMaxWidth()
                    )


                }


            }
        }

    }
}


@Composable
fun BottomSheetResultsWeight(
    result: String,
    childWeight: Int,
    adultDose: Double,
    formulaType: String
) {

    var formula = "Weight (kg)/ 70 × Adult Dose"
    var formulaCalculations = "${childWeight}/ 70 × ${adultDose} = ${result}"
    var formulaName = "Clark's Formula"

    if (formulaType.equals("wcfkg")) {
        formulaCalculations = "${childWeight} / 70 × ${adultDose} = ${result}"
        formula = "Weight (kg)/ 70 × Adult Dose"
        formulaName = "Clark's Formula"
    } else if (formulaType.equals("wcfp")) {
        formulaCalculations = "${childWeight} /150 × ${adultDose} = ${result}"
        formula = "weight (pounds) / 150 × adult dose"
        formulaName = "Clark's Formula"
    } else if (formulaType.equals("wmwf")) {
        formulaCalculations = "${childWeight}) / 50kg × ${adultDose} = ${result}"
        formula = "Weight (kg) / 50kg × Adult dose"
        formulaName = "Modified Weight Formula"

    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Icon(
            painter = painterResource(R.drawable.child_care_24px),
            contentDescription = "",
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .height(40.dp)
                .width(40.dp),
            tint = MaterialTheme.colorScheme.primary
        )


        Text(
            "Pediatric Dose",
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )

        Row(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 15.dp),
        ) {
            Text(
                "${result}",
                fontSize = 40.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
            )
            Text(
                "mg",
                modifier = Modifier
                    .padding(start = 5.dp)
                    .align(alignment = Alignment.Bottom),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraLight,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
//HorizontalDivider()
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        ) {
            Icon(
                painter = painterResource(R.drawable.calculate_24px),
                contentDescription = "",
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .height(40.dp)
                    .width(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Calculations",
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically),
                fontSize = 20.sp,
                //fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "${formulaName}",
            fontWeight = FontWeight.Light,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "${formula}",
            fontWeight = FontWeight.Light,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "${formulaCalculations}",
            fontWeight = FontWeight.Light,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )


    }
}


// Preview ====


@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalculateByWeightCompose_Preview() {

    CalculateByWeightCompose(
        adultDose = mutableStateOf("ds"),
        weightInput = mutableStateOf("d"),
        onFormulaChange = {

        },
        onCalculateButtonClick = {},
        onReset = {},
        finalResult = mutableStateOf("45")
    )
}

@Preview(name = "Results", showBackground = true)
@Composable
fun BottomSheetResultsWeightPREVIEW() {
    BottomSheetResultsWeight(
        result = "34",
        childWeight = 5,
        adultDose = 50.3,
        formulaType = "ds"
    )
}