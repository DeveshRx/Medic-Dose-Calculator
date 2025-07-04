package devesh.medic.dose

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds

import devesh.medic.dose.calculations.BastedoFormula
import devesh.medic.dose.calculations.ClarkFormulaKg
import devesh.medic.dose.calculations.ClarkFormulaPound
import devesh.medic.dose.calculations.CrowlingFormula
import devesh.medic.dose.calculations.DillingsFormula
import devesh.medic.dose.calculations.FriedFormula
import devesh.medic.dose.calculations.ModifiedWeightFormula
import devesh.medic.dose.calculations.WebsterFormula
import devesh.medic.dose.calculations.YoungsFormular
import devesh.medic.dose.screens.CalculateByAgeCompose
import devesh.medic.dose.screens.CalculateByWeightCompose
import devesh.medic.dose.ui.theme.MedicDoseCalculatorTheme

class MainActivity : ComponentActivity() {
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getString(R.string.app_flavour).equals("free")) {
            MobileAds.initialize(this)

        }

        setContent {
            MedicDoseCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen()
                }
            }
        }
    }

    var adultDose = mutableStateOf("")
    var ageInput = mutableStateOf("")
    var ageFormulaSelected: CalcFormulas? = null
    var finalResult = mutableStateOf("0.0000000")

    var weightFormulaSelected: CalcFormulas? = null
    var weightInput = mutableStateOf("")

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    @Preview(showBackground = true)
    @Composable
    fun Screen() {
        val navController = rememberNavController()

        var selectedItem = remember { mutableIntStateOf(0) }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val isSelectedItem: (String) -> Boolean = { currentDestination?.route == it }

        MedicDoseCalculatorTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier.shadow(
                            elevation = 5.dp,
                            //       spotColor = Color.DarkGray,
                        ),

                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),

                        title = {
                            Text("Medic Dose Calculator")
                        },
                        actions = {
                            /* IconButton(onClick = { /* do something */ }) {
                                 Icon(
                                     painter = painterResource(id = R.drawable.ic_android_black_24dp),
                                     contentDescription = "Localized description"
                                 )
                             }*/
                            IconButton(onClick = {

                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        SettingsActivity::class.java
                                    )
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.Settings,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                    )
                },
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),

                    ) {

                    Row(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .padding(5.dp)
                            .wrapContentWidth()
                    ) {

                        FilterChip(
                            selected = isSelectedItem("age"),
                            onClick = {
                                selectedItem.value = 0
                                navController.navigate("age") {

                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = { Text("Age") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = "Age",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            },
                            modifier = Modifier.padding(2.dp)

                        )
                        FilterChip(
                            selected = isSelectedItem("weight"),
                            onClick = {
                                selectedItem.value = 1
                                navController.navigate("weight") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }

                            },
                            label = { Text("Weight") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.scale_20px),
                                    contentDescription = "Weight",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            },
                            modifier = Modifier.padding(2.dp)
                        )

                        /*FilterChip(
                            selected = isSelectedItem("bsa"),

                            onClick = {
                                selectedItem.value = 2
                            },
                            label = { Text("Surface Area") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.scale_20px),
                                    contentDescription = "Body Surface Area",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            },
                            modifier = Modifier.padding(2.dp)
                        )*/

                    }

                    NavHost(
                        navController, startDestination = "age",

                        ) {
                        composable("age") {
                            CalculateByAgeCompose(
                                adultDose = adultDose,
                                ageInput = ageInput,
                                onFormulaChange = {
                                    Log.d(TAG, "onFormulaChange: " + it.toString())
                                    ageFormulaSelected = it
                                },
                                onCalculateButtonClick = { AgeCalculation() },
                                onReset = {
                                    adultDose.value = ""
                                    ageInput.value = ""
                                    finalResult.value = ""
                                    weightInput.value = ""
                                },
                                finalResult = finalResult
                            )
                        }
                        composable("weight") {
                            CalculateByWeightCompose(
                                adultDose = adultDose,
                                weightInput = weightInput,
                                onFormulaChange = {
                                    Log.d(TAG, "onFormulaChange: " + it.toString())
                                    weightFormulaSelected = it
                                },
                                onCalculateButtonClick = {
                                    WeightCalculation()
                                },
                                onReset = {

                                    adultDose.value = ""
                                    ageInput.value = ""
                                    finalResult.value = ""
                                    weightInput.value = ""

                                },
                                finalResult = finalResult
                            )
                        }

                    }


                    /* Box (modifier = Modifier.fillMaxSize()){
                         Text(text = "Result",
                             modifier = Modifier.align(Alignment.Center))
                     }*/

                    HorizontalDivider(modifier = Modifier.padding(top = 5.dp))

                    Spacer(modifier = Modifier.height(20.dp))

                    if (!LocalInspectionMode.current) {
                        AdMobBannerMediumRec()
                    }

                }
            }

        }


    }

    fun AgeCalculation() {

        var d = 0.00000
        if (ageFormulaSelected?.id.equals("yf")) {
            d = YoungsFormular(age = ageInput.value.toInt(), adultDose = adultDose.value.toDouble())
        } else if (ageFormulaSelected?.id.equals("df")) {
            d = DillingsFormula(
                age = ageInput.value.toInt(),
                adultDose = adultDose.value.toDouble()
            )

        } else if (ageFormulaSelected?.id.equals("cf")) {
            d = CrowlingFormula(
                age = ageInput.value.toInt(),
                adultDose = adultDose.value.toDouble()
            )

        } else if (ageFormulaSelected?.id.equals("ff")) {
            d = FriedFormula(
                age = ageInput.value.toInt(),
                adultDose = adultDose.value.toDouble()
            )

        } else if (ageFormulaSelected?.id.equals("bf")) {
            d = BastedoFormula(
                age = ageInput.value.toInt(),
                adultDose = adultDose.value.toDouble()
            )

        } else if (ageFormulaSelected?.id.equals("wf")) {
            d = WebsterFormula(
                age = ageInput.value.toInt(),
                adultDose = adultDose.value.toDouble()
            )

        } else {
            Log.d(TAG, "AgeCalculation: ageFormulaSelected  " + ageFormulaSelected?.id)

        }
        Log.d(TAG, "AgeCalculation: Result: " + d)
        finalResult.value = d.toString()


    }

    fun WeightCalculation() {

        var d = 0.00000
        if (weightFormulaSelected?.id.equals("wcfkg")) {
            d = ClarkFormulaKg(
                weight = weightInput.value.toDouble(),
                adultDose = adultDose.value.toDouble()
            )
        } else if (weightFormulaSelected?.id.equals("wcfp")) {
            d = ClarkFormulaPound(
                weight = weightInput.value.toDouble(),
                adultDose = adultDose.value.toDouble()
            )

        } else if (weightFormulaSelected?.id.equals("wmwf")) {
            d = ModifiedWeightFormula(
                weight = weightInput.value.toDouble(),
                adultDose = adultDose.value.toDouble()
            )

        } else {
            d = ClarkFormulaKg(
                weight = weightInput.value.toDouble(),
                adultDose = adultDose.value.toDouble()
            )

            Log.d(TAG, "WeightCalculation: weightFormulaSelected  " + weightFormulaSelected?.id)

        }
        Log.d(TAG, "WeightCalculation: Result: " + d)
        finalResult.value = d.toString()

    }


}






