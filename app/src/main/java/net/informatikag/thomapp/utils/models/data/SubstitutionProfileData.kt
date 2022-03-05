package net.informatikag.thomapp.utils.models.data

class SubstitutionProfileData (
    val id: Int?,
    val grade: Int,
    val courseType: Boolean,
    val courseNumber: Int,
    val subject: String,
    val teacher: String
) {
    companion object {
        fun getSubstitutionPlanByID(id: Int): SubstitutionRequstResultData? {
            return null
        }
    }

    fun getSubstitutionPlan(): SubstitutionRequstResultData? = getSubstitutionPlanByID(this.id!!)

    fun register() {}
}