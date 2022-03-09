package net.informatikag.thomapp.utils.models.data

import android.content.Context
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import net.informatikag.thomapp.MainActivity
import org.json.JSONArray
import org.json.JSONObject

class SubstitutionProfileData (
    var id: Int?,
    val profileEntries: Array<SubstitutionProfileEntryData>
) {
    var lastResultData: SubstitutionRequstResultData? = null

    companion object {
        fun getSubstitutionByID(
            id: Int,
            context: Context,
            callback: (results: SubstitutionRequstResultData?, error: VolleyError?) -> Unit
        ){
            var url = MainActivity.SUBSTITUTION_PLAN_BASE_URL + "/substitution"
            if (id >= 0) url += "?profile=$id"

            Volley.newRequestQueue(context).add(
                JsonObjectRequest(url,
                    { response ->
                        try {
                            callback(SubstitutionRequstResultData(response), null)
                        } catch (e: Exception) {
                            callback(null, null)
                        }
                    },
                    { volleyError ->
                        callback(null, volleyError)
                    }
                )
            )
        }
    }

    // Gets the
    fun getSubstitution(context:Context, loadFullPlan: Boolean, callback: (SubstitutionRequstResultData?, VolleyError?) -> Unit) {
        getSubstitutionByID(if(loadFullPlan || id == null) -1 else id!!, context) { results, error ->
            lastResultData = results
            callback(results, error)
        }
    }

    // TODO test this. This will be used when the users can add their own profiles, so at the moment that isn't really possible
    fun register(context:Context, callback: (VolleyError?) -> Unit) {
        // If the Profile already is registerd, you don't have to do it again
        if (id != null) return

        val url = MainActivity.SUBSTITUTION_PLAN_BASE_URL + "/substitution/register"
        val body = JSONArray()

        // Build a JSON object for each entry of the Profile and add it to the Body of the request
        for (profileEntry in profileEntries) {
            val jsonEntry = JSONObject()

            if(profileEntry.grade       != null) jsonEntry.put("grade",         profileEntry.grade)
            if(profileEntry.courseType  != null) jsonEntry.put("courseType",    profileEntry.courseType)
            if(profileEntry.courseNumber!= null) jsonEntry.put("courseNumber",  profileEntry.courseNumber)
            if(profileEntry.subject     != null) jsonEntry.put("subject",       profileEntry.subject)
            if(profileEntry.teacher     != null) jsonEntry.put("teacher",       profileEntry.teacher)

            body.put(jsonEntry)
        }

        // Make the request
        Volley.newRequestQueue(context).add(
            JsonObjectRequest(url,
                { response ->
                    this.id = response.getInt("id")
                    callback(null)
                },
                { volleyError -> callback(volleyError) }
            )
        )
    }

    data class SubstitutionProfileEntryData (
        val grade: Int?,
        val courseType: Boolean?,
        val courseNumber: Int?,
        val subject: String?,
        val teacher: String?
    )
}