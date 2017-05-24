package project.bluesign.service.settings;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static project.bluesign.service.settings.Settings.ALBUM;
import static project.bluesign.service.settings.Settings.FACIAL_RECOGNITION_ENABLED;
import static project.bluesign.service.settings.Settings.ID;
import static project.bluesign.service.settings.Settings.PIN;
import static project.bluesign.service.settings.Settings.REGISTRATION_COMPLETE;

public class SettingsService {

    private SharedPreferences preferences;

    public SettingsService(Context context) {
        preferences = getDefaultSharedPreferences(context);
    }

    public String getId() {
        return preferences.getString(ID.toString(), "id");
    }

    public String getPin() {
        return preferences.getString(PIN.toString(), "pin");
    }

    public boolean saveId(String id) {
        return preferences.edit().putString(ID.toString(), id).commit();
    }

    public boolean savePin(String pin) {
        return preferences.edit().putString(PIN.toString(), pin).commit();
    }

    public boolean saveAlbum(byte[] album) {
        return preferences.edit().putString(ALBUM.toString(), Arrays.toString(album)).commit();
    }

    public byte[] loadAlbum() {
        String albumString = preferences.getString(ALBUM.toString(), null);

        byte[] albumBytes;
        if (albumString != null) {
            String[] splitStringArray = albumString.substring(1, albumString.length() - 1).split(", ");

            albumBytes = new byte[splitStringArray.length];
            for (int i = 0; i < splitStringArray.length; i++) {
                albumBytes[i] = Byte.parseByte(splitStringArray[i]);
            }
            return albumBytes;
        }
        return null;
    }

    public boolean registrationComplete(Boolean finished) {
        return preferences.edit().putBoolean(REGISTRATION_COMPLETE.toString(), finished).commit();
    }

    public boolean resetSettings() {
        return preferences.edit().clear().commit();
    }

    public boolean facialRecognitionEnabled(Boolean enabled) {
        return preferences.edit().putBoolean(FACIAL_RECOGNITION_ENABLED.toString(), enabled).commit();
    }

    public boolean isFacialRecognitionEnabled() {
        return preferences.getBoolean(FACIAL_RECOGNITION_ENABLED.toString(), false);
    }

    public boolean isRegistrationComplete() {
        return preferences.getBoolean(REGISTRATION_COMPLETE.toString(), false);
    }

    public boolean isPinCorrect(String pin) {
        return preferences.getString(PIN.toString(), "pin").equals(pin);
    }
}
