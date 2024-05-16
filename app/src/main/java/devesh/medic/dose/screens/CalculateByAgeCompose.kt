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

val TAG = "CalcAgeCompose"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculateByAgeCompose(
    ageInput: MutableState<String>,
    adultDose: MutableState<String>,
    onFormulaChange: (CalcFormulas) -> Unit,
    onCalculateButtonClick: () -> Unit,
    onReset: () -> Unit,
    finalResult: MutableState<String>
) {
    val mContext = LocalContext.current
    var text by remember { ageInput }
    var text1 by remember { adultDose }
    var selectedFormula = remember {
        mutableStateOf("yf")
    }

    val sheetState = rememberModalBottomSheetState()
    //val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val supportText = remember {
        mutableStateOf("Child's Age in Years")
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
                label = { Text("Age") },
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

            RadioButtonGroupAge(onFormulaChange = {
                onFormulaChange(it)
                selectedFormula.value = it.id
                if (it.id.equals("ff")) {
                    supportText.value = "Child's Age in Months"
                } else {
                    supportText.value = "Child's Age in Years"
                }
            })

            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
                Button(
                    onClick = {
                        if (ageInput.value == "" || adultDose.value == "") {
                            Toast
                                .makeText(mContext, "Please Enter Age or Dose", Toast.LENGTH_SHORT)
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
                    BottomSheetAgeResults(
                        result = finalResult.value,
                        childAge = ageInput.value.toInt(),
                        adultDose = adultDose.value.toDouble(),
                        formulaType = selectedFormula.value
                    )
                }
            }

        }


    }

}


val formula_items = listOf(

    CalcFormulas(
        "yf",
        "Young's Formula", "Can be applied quickly approach a situation in which the patients" +
                "weight is unknown and easy to remember because young refers to" +
                "age"
    ),
    CalcFormulas(
        "df",
        "Dilling's Formula",
        "It is the simplest and easiest and formula for child dose calculation"
    ),
    CalcFormulas(
        "cf",
        "Cowling's Formula",
        "the most safe and accurate techniques of pediatric dosage calculation"
    ),
    CalcFormulas(
        "ff",
        "Fried's Formula (for infants)", "Commonly used for neonates"
    ),
    CalcFormulas(
        "bf",
        "Bastedo's Formula",
        "It is one of the child dose calculation formula as an optional"
    ),
    CalcFormulas("wf", "Webster Formula", "Commonly used in older children")

)

@Composable
fun RadioButtonGroupAge(onFormulaChange: (CalcFormulas) -> Unit) {
    val selectedValue = remember { mutableStateOf("Young's Formula") }

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
        formula_items.forEach { item ->
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
fun BottomSheetAgeResults(result: String, childAge: Int, adultDose: Double, formulaType: String) {

    var formula = "[Age / (Age + 12)] × Adult Dose "
    var formulaCalculations = "[${childAge} / (${childAge} + 12)] × ${adultDose} = ${result}\n"
    var formulaName = "Young's Formula"

    if (formulaType.equals("yf")) {
        formulaCalculations = "[${childAge} / (${childAge} + 12)] × ${adultDose} = ${result}\n"
        formula = "[Age / (Age + 12)] × Adult Dose "
        formulaName = "Young's Formula"
    } else if (formulaType.equals("df")) {
        formulaCalculations = "${childAge} / 20 × ${adultDose} = ${result}\n"
        formula = "Age in years / 20 × adult dose\n"
        formulaName = "Dilling's Formula"
    } else if (formulaType.equals("cf")) {
        formulaCalculations = "( ${childAge} + 1 ) / 24 × ${adultDose} = ${result}\n"
        formula = "(Age (years) + 1) / 24 × adult dose\n"
        formulaName = "Cowling's Formula"

    } else if (formulaType.equals("ff")) {
        formulaCalculations = "[${childAge} / 150] × ${adultDose} = ${result}\n"
        formula = "[age (months) / 150] × adult dose\n"
        formulaName = "Fried's Formula (for infants)"

    } else if (formulaType.equals("bf")) {
        formulaCalculations = "${childAge} + 3 / 30 × ${adultDose} = ${result}\n"
        formula = "Age (years) + 3/30 × adult dose\n"
        formulaName = "Bastedo's Formula"

    } else if (formulaType.equals("wf")) {
        formulaCalculations = "${childAge} + 1 / ${childAge} + 7 × ${adultDose} = ${result}\n"
        formula = "Age (years) + 1 /Age (years)+7 × adult dose\n"
        formulaName = "Webster Formula"

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
fun CalculateByAgeCompose_Preview() {

    CalculateByAgeCompose(
        adultDose = mutableStateOf("ds"),
        ageInput = mutableStateOf("d"),
        onFormulaChange = {

        },
        onCalculateButtonClick = {},
        onReset = {},
        finalResult = mutableStateOf("45")
    )
}

@Preview(name = "Results", showBackground = true)
@Composable
fun BottomSheetPREVIEW() {
    BottomSheetAgeResults(
        result = "34",
        childAge = 5,
        adultDose = 50.3,
        formulaType = "ds"
    )
}