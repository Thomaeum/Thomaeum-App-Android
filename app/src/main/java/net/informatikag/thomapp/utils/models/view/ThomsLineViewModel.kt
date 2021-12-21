package net.informatikag.thomapp.utils.models.view

import android.app.Application
import net.informatikag.thomapp.MainActivity

class ThomsLineViewModel(application: Application):WordpressViewModel(application) {
    override val BASE_URL: String = MainActivity.THOMSLINE_BASE_URL
}