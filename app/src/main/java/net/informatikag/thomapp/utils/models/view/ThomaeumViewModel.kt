package net.informatikag.thomapp.utils.models.view

import android.app.Application
import net.informatikag.thomapp.MainActivity

class ThomaeumViewModel(application: Application):WordpressViewModel(application) {
    override val BASE_URL: String = MainActivity.THOMAEUM_BASE_URL
}