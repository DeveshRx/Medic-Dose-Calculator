package devesh.medic.dose.calculations

import android.util.Log
import kotlin.math.sqrt

val TAG = "Calculations Age"

// Calculate by Age =========
fun YoungsFormular(age: Int, adultDose: Double): Double {
    //[Age / (Age + 12)] x Recommended Adult Dose = Pediatric Dose
    val c1 = age + 12;
    val c2: Double = age.toDouble() / c1.toDouble();
    val final = c2 * adultDose

    Log.d(
        TAG,
        "YoungsFormular: age: ${age}, adultdose: ${adultDose} , c1:${c1} , c2:${c2} , final:${final}"
    )

    return Math.round(final * 1000.0) / 1000.0

}

fun DillingsFormula(age: Int, adultDose: Double): Double {
    // Age in years / 20 × adult dose
    val c1: Double = age.toDouble() / 20
    val final = c1 * adultDose

    Log.d(TAG, "DillingsFormula: age: ${age}, adultdose: ${adultDose} , c1:${c1} , final:${final}")

    return Math.round(final * 1000.0) / 1000.0
}

fun BastedoFormula(age: Int, adultDose: Double): Double {
    // Age (years) + 3/30 × adult dose
    val c1: Double = (3 / 30).toDouble();
    val c2 = age + c1
    val final = c2 * adultDose

    Log.d(
        TAG,
        "BastedoFormula: age: ${age}, adultdose: ${adultDose} , c1:${c1} , c2:${c2} , final:${final}"
    )

    return Math.round(final * 1000.0) / 1000.0
}

fun FriedFormula(age: Int, adultDose: Double): Double {
    //    [age (months)/150] × adult dose
    val c1: Double = age.toDouble() / 150
    val final = c1 * adultDose

    Log.d(TAG, "FriedFormula: age: ${age}, adultdose: ${adultDose} , c1 ${c1} , final: ${final}")

    return Math.round(final * 1000.0) / 1000.0
}


fun CrowlingFormula(age: Int, adultDose: Double): Double {
    // Age (years) +1 / 24 × adult dose
    val c1: Double = age.toDouble() + 1 / 24
    val final = c1 * adultDose

    Log.d(
        TAG,
        "CrowlingFormula: age: ${age}, adultdose: ${adultDose} , c1: ${c1} , fianl: ${final}"
    )

    return Math.round(final * 1000.0) / 1000.0
}

fun WebsterFormula(age: Int, adultDose: Double): Double {
    // Age (years) + 1 /Age (years)+7 * adult dose

    val c1 = age + 1
    val c2 = age + 7
    val c3 = c1.toDouble() / c2.toDouble()
    val final = c3 * adultDose

    Log.d(
        TAG,
        "WebsterFormula: age: ${age}, adultdose: ${adultDose} , c1: ${c1} , c2: ${c2} , c3:${c3} , final:${final}"
    )

    return Math.round(final * 1000.0) / 1000.0
}


// Calculate by Weight =========

fun ClarkFormulaKg(weight: Double, adultDose: Double): Double {
//    Weight (kg)/ 70 × adult dose

    val c1 = weight / 70
    val finalResult = c1 * adultDose
    return Math.round(finalResult * 1000.0) / 1000.0
}

fun ClarkFormulaPound(weight: Double, adultDose: Double): Double {
//    weight(pounds)/150* adult dose

    val c1 = weight / 150
    val finalResult = c1 * adultDose
    return Math.round(finalResult * 1000.0) / 1000.0
}

fun ModifiedWeightFormula(weight: Double, adultDose: Double): Double {
    // Weight kg/ 50kg *Adult dose
    val c1 = weight / 50
    val final = c1 * adultDose
    return Math.round(final * 1000.0) / 1000.0
}

/*fun MajidFormula(weight: Double, adultDose: Double): Double{
    // adult dose /70 (Adult weight) × weight (kg)
    val c1=adultDose/70
}*/

fun BSAMostellerFormula(childWeight: Double,childHeight: Double, adultSA: Double, adultDose: Double): Double {
    // SA:  √Weight (kg) × height (cm) /3600
    // Body surface area of child (m2)/ Adult S.A (1.73m²) × adult dose

    val e=(childWeight * childHeight) / 3600

        val childSA= sqrt(e)

    val c1 = childSA / adultSA
    val final = c1 * adultDose

    return Math.round(final * 1000.0) / 1000.0

}



