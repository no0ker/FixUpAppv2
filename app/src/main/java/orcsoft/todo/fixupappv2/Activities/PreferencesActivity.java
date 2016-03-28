package orcsoft.todo.fixupappv2.Activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.PreferenceChange;
import org.androidannotations.annotations.PreferenceScreen;
import org.androidannotations.annotations.sharedpreferences.Pref;

import orcsoft.todo.fixupappv2.R;
import orcsoft.todo.fixupappv2.SharedPreferences.Prefs_;

@PreferenceScreen(R.xml.settings)
@EActivity
public class PreferencesActivity extends PreferenceActivity{
    @Pref
    protected Prefs_ prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getPreferenceManager().setSharedPreferencesName("Prefs");
    }

    @PreferenceChange({R.string.pref_auth_login, R.string.pref_auth_pass})
    void authChanged() {
        prefs.accessToken().put("");
    }
}
