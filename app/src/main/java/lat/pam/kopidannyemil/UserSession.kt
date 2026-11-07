package lat.pam.kopidannyemil

import android.content.Context
import androidx.core.content.edit

object UserSession {
    private const val PREF_NAME = "kopi_nyemil_prefs"
    private const val KEY_FULL_NAME = "key_full_name"
    private const val KEY_USERNAME = "key_username"

    fun saveUser(context: Context, name: String, username: String? = null) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_FULL_NAME, name)
            username?.let { putString(KEY_USERNAME, it) }
        }
    }

    fun getUserName(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val name = prefs.getString(KEY_FULL_NAME, "")?.trim()
        return if (!name.isNullOrEmpty()) name else "Nama"
    }

    fun getFirstName(context: Context): String {
        val fullName = getUserName(context)
        return fullName.split("\\s+".toRegex()).firstOrNull { it.isNotBlank() } ?: "Nama"
    }

    fun clear(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { clear() }
    }
}
