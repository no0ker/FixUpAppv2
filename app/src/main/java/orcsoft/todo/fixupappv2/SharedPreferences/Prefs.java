package orcsoft.todo.fixupappv2.SharedPreferences;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface Prefs {
    public String accessToken();

    @DefaultString("rustam@mail.ru")
    public String login();

    @DefaultString("123")
    public String password();

    @DefaultString("http://fixup.forteapps.com/api")
    public String server();

    public int jobId();
}
