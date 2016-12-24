package project.bluesign.service.settings;

import android.content.Context;

import project.bluesign.service.ContextService;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static project.bluesign.service.settings.Settings.ID;
import static project.bluesign.service.settings.Settings.PIN;
import static project.bluesign.service.settings.Settings.REGISTRATION_COMPLETE;

public class SettingsService extends ContextService{

    Context mcontext;

    public SettingsService(Context context) {
        mcontext = context;
    }

    public String getId() {
        return getDefaultSharedPreferences(mcontext).getString(ID.toString(), "id");
    }

    public String getPin() {
        return getDefaultSharedPreferences(mcontext).getString(PIN.toString(), "pin");
    }

    public boolean saveId(String id) {
        return getDefaultSharedPreferences(mcontext).edit().putString(ID.toString(), id).commit();
    }

    public boolean savePin(String pin) {
        return getDefaultSharedPreferences(mcontext).edit().putString(PIN.toString(), pin).commit();
    }

    public boolean registrationComplete(Boolean finished) {
        return getDefaultSharedPreferences(mcontext).edit().putBoolean(REGISTRATION_COMPLETE.toString(), finished).commit();
    }

    public boolean resetSettings() {
        return getDefaultSharedPreferences(mcontext).edit().clear().commit();
    }

    public boolean isRegistrationComplete() {
        return getDefaultSharedPreferences(mcontext).getBoolean(REGISTRATION_COMPLETE.toString(), false);
    }
}
