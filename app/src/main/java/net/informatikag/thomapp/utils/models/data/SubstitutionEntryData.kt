package net.informatikag.thomapp.utils.models.data

import org.json.JSONObject

data class SubstitutionEntryData(
    val date: Long,
    val start: Int,
    val duration: Int,
    val regularCourse: CourseData,
    val changedCourse: CourseData,
    val teacher: String,
    val room: String,
    val annotations: String,
    val type: String
) {
    constructor(jsonObject: JSONObject): this(
        jsonObject.getLong("date"),
        jsonObject.getInt("start"),
        jsonObject.getInt("duration"),
        CourseData(jsonObject.getJSONObject("regularCourse")),
        CourseData(jsonObject.getJSONObject("changedCourse").getJSONObject("subject")),
        jsonObject.getJSONObject("changedCourse").getString("teacher"),
        jsonObject.getJSONObject("changedCourse").getString("room"),
        jsonObject.getString("annotations"),
        jsonObject.getString("type")
    )
}
