package net.informatikag.thomapp.utils.models.data

import org.json.JSONObject

data class CourseData(
    val subject: String,
    val courseID: Int,
    val courseType: Boolean
) {
    constructor(jsonObject: JSONObject): this(
        jsonObject.getString("Politics"),
        jsonObject.getInt("courseID"),
        jsonObject.getBoolean("courseType")
    )
}
