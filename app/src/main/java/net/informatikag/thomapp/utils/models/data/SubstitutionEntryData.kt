package net.informatikag.thomapp.utils.models.data

import org.json.JSONObject

data class SubstitutionEntryData(
    val start: Int,
    val range: Int,
    val regularCourse: CourseData,
    val changedCourse: CourseData,
    val teacher: String,
    val room: String,
    val annotations: String,
    val type: String
) {
    constructor(jsonObject: JSONObject): this(
        jsonObject.getInt("start"),
        jsonObject.getInt("range"),
        CourseData(jsonObject.getJSONObject("regularCourse")),
        CourseData(jsonObject.getJSONObject("changedCourse").getJSONObject("subject")),
        jsonObject.getJSONObject("changedCourse").getString("teacher"),
        jsonObject.getJSONObject("changedCourse").getString("room"),
        jsonObject.getString("annotations"),
        jsonObject.getString("type")
    )

    //TODO this just provides a dummy version, this should be removed ASAP
    constructor(): this(1,2,
        CourseData("Computer Scienece",0,true),
        CourseData("Computer Scienece",1,true),
        "Zimmermann","N002","LUL Text","EVA"
    )
}
