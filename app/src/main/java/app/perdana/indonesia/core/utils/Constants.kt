package app.perdana.indonesia.core.utils

import app.perdana.indonesia.BuildConfig


object Constants {
    const val USER_PROFILE = "USER_PROFILE"
    const val USER_ROLE = "USER_ROLE"
    const val TOKEN = "TOKEN"
    const val IS_INTRO_DISPLAYED = "IS_INTRO_DISPLAYED"
    const val PKM_GROUP_ID = "PKM_GROUP_ID"
    const val PKM_GROUP_OBJECT = "PKM_GROUP_OBJECT"
    const val USER_MEKAAR = "USER_MEKAAR"
    const val USER_PHONE = "USER_PHONE"
    const val IS_INTERNET_AVAILABLE = "IS_INTERNET_AVAILABLE"
    const val SHARED_PREFERENCES_NAME = BuildConfig.APPLICATION_ID + ".sp"

    object UserRole {
        const val ARCHER = "archer"
        const val CLUB_SATUAN_MANAGER = "club-satuan-manager"
    }
}