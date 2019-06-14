package com.gox.partner.utils

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.util.Log
import com.gox.base.data.Constants
import com.gox.base.data.Constants.FareType.DISTANCE_TIME
import com.gox.base.data.Constants.FareType.FIXED
import com.gox.base.data.Constants.FareType.HOURLY
import com.gox.partner.R
import com.gox.partner.models.SubServicePriceCategoriesResponse
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CommonMethods {

    companion object {
        fun getDefaultFileName(context: Context): File {
            val imageFile: File?
            val isSDPresent = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
            imageFile = (if (isSDPresent) File(Environment.getExternalStorageDirectory().toString() +
                    File.separator + Constants.TEMP_FILE_NAME + System.currentTimeMillis() + ".png")
            else File(context.filesDir.toString() + File.separator +
                    Constants.TEMP_FILE_NAME + System.currentTimeMillis() + ".png"))
            return imageFile
        }

        fun validatePhone(phone: String): Boolean {
            return if (phone.length >= 10) {
                Log.e("valid", "---$phone")
                true
            } else false
        }

        fun getLocalTimeStamp(dateStr: String, request: String): String {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            df.timeZone = TimeZone.getTimeZone("UTC")
            var date: Date? = null
            val localTime = ""
            var calendar: Calendar? = null
            var strDate = ""
            try {
                date = df.parse(dateStr)
                calendar = Calendar.getInstance(TimeZone.getDefault())
                calendar!!.time = date!!
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val dayOfTheWeek = SimpleDateFormat("EEEE").format(date) // Thursday
            val day = SimpleDateFormat("dd").format(date) // 20
            val monthString = SimpleDateFormat("MMM").format(date) // Jun
            val monthNumber = SimpleDateFormat("MM").format(date) // 06
            val hours = SimpleDateFormat("hh").format(date) // 12
            val min = SimpleDateFormat("mm").format(date) // 60
            val amPm = SimpleDateFormat("a").format(date) // 60

            val year = calendar!!.get(Calendar.YEAR)

            Log.d("Date", day + "-" + dayOfTheWeek + "-" + monthString + "-" + monthNumber +
                    "-" + year + "/" + hours + ":" + min + ":" + amPm)

            if (request == "Req_Date_Month") return "$day $monthString"
            if (request == "Req_time") return "$hours:$min $amPm"

            val dayMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val strMonth = SimpleDateFormat("MMM").format(calendar.time)

            if (strMonth != null) strDate = Integer.toString(dayMonth) + "-" +
                    strMonth + "-" + Integer.toString(year)

            return strDate

        }

        fun getFare(activity: Activity, servicePrices: SubServicePriceCategoriesResponse.Servicescityprice,
                    providerService: List<SubServicePriceCategoriesResponse.ProviderServices>): String =
                when (servicePrices.fare_type) {
                    HOURLY -> {
                        if (providerService.isNotEmpty())
                            providerService[0].per_mins + " " + activity.getString(R.string.per_hour)
                        else
                            servicePrices.per_mins + " " + activity.getString(R.string.per_hour)
                    }
                    FIXED -> {
                        if (providerService.isNotEmpty())
                            providerService[0].base_fare + " " + activity.getString(R.string.fixed)
                        else
                            servicePrices.base_fare + " " + activity.getString(R.string.fixed)
                    }
                    DISTANCE_TIME -> {
                        if (providerService.isNotEmpty())
                            providerService[0].per_mins + " " + activity.getString(R.string.per_hour)
                        else
                            servicePrices.per_mins + " " + activity.getString(R.string.per_min)
                    }
                    else -> ""
                }
    }
}
