package net.informatikag.thomapp.utils.models.data

import org.json.JSONObject

data class SubstitutionRequstResultData (
    val hash: String,
    val entryData: Array<SubstitutionEntryData>
) {
    constructor(jsonObject: JSONObject) : this(
        jsonObject.getString("hash"),
        Array<SubstitutionEntryData>(jsonObject.getJSONArray("entries").length()) {
            SubstitutionEntryData(jsonObject.getJSONArray("entries").getJSONObject(it))
        },
    )
}
