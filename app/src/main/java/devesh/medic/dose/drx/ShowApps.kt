package devesh.medic.dose.drx

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.google.gson.Gson
import devesh.medic.dose.ui.theme.MedicDoseCalculatorTheme

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import kotlin.concurrent.thread

val TAG = "DRX_Apps"
val client = OkHttpClient()
var gson = Gson()
val DRX_URL = "https://deveshrx.com/api/getallapps"

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun DeveshRxAppsList() {

    val appsList = mutableStateListOf<AppItem>()


var mContext=LocalContext.current;
    getReq(
        url = DRX_URL,
        httpResponse = {
            val resString: String = it.body!!.string()
            Log.d(TAG, "PostReq: ${resString}")
            val al = gson.fromJson(resString, Array<AppItem>::class.java)
            Log.d(TAG, "appsList: ${al}")
al.shuffle()
            appsList.addAll(al)

        }
    )

    MedicDoseCalculatorTheme {

        Row(modifier = Modifier.padding(5.dp)) {
            Text(text = "DeveshRx Apps & Games",
                fontWeight = FontWeight.Medium,
                fontSize = 28.sp)
        }

        LazyColumn(
            modifier = Modifier.heightIn(max = 20000.dp) // set the maximum height possible in your case
        ) {
            items(appsList) { app ->
                Surface(onClick = {
                    val url = app.appurl
                    val urlIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(url)
                    )
                    mContext.startActivity(urlIntent,null)
                }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        AppItem(name = app.name, imageURL = app.icon)


                    }
               //     HorizontalDivider()

                }

            }
        }


    }


}


@Composable
fun AppItem(name: String, imageURL: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        //https://ik.imagekit.io/mkg3bafgvs3/  DeveshRx/deveshrx_com/games/raven_sky/icon512px
        AsyncImage(
//            model = "https://ik.imagekit.io/mkg3bafgvs3/tr:w-100/DeveshRx/deveshrx_com/games/raven_sky/icon512px",
            model = "https://ik.imagekit.io/mkg3bafgvs3/tr:w-100/${imageURL}",

            contentDescription = "",
            modifier = Modifier.align(Alignment.CenterVertically)

        )
        Text(
            text = "${name}", modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 5.dp)
        )
    }

}

fun getReq(url: String, httpResponse: (res: Response) -> Unit) {


    Log.d(TAG, "PostReq: URL:${url}, postBody")
    thread {
        //  val requestBody = post_body.toRequestBody()

        val request = Request.Builder()
            .url(url)
            //   .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->

            // val resString:String=response.body!!.string()
            //Log.d(TAG, "PostReq: ${resString}")

            httpResponse(response)
            /*
                            if (!response.isSuccessful) throw IOException("Unexpected code $response")

                            for ((name, value) in response.headers) {
                                println("$name: $value")
                            }

                            println(response.body!!.string())*/
        }

    }


}
