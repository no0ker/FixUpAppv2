package orcsoft.todo.fixupappv2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;

import orcsoft.todo.fixupappv2.Exceptions.NetException;
import orcsoft.todo.fixupappv2.Operations;
import orcsoft.todo.fixupappv2.R;
import orcsoft.todo.fixupappv2.SharedPreferences.Prefs_;
import orcsoft.todo.fixupappv2.Utils.NetHelper_;

@EActivity(R.layout.login_activity)
public class LogOnActivity extends AppCompatActivity {
    @Pref
    protected Prefs_ prefs;

    @ViewById(R.id.login)
    EditText editTextLogin;

    @ViewById(R.id.password)
    EditText editTextPassword;

    @ViewById(R.id.url)
    EditText editTextUrl;

    @ViewById(R.id.parent_layout)
    LinearLayout parentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!StringUtils.isEmpty(prefs.accessToken().get())) {
            MenuActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
        }
    }

    @AfterViews
    protected void setDefaultData() {
        Map<String, ?> data = prefs.getSharedPreferences().getAll();
        editTextLogin.setText(prefs.login().get());
        editTextPassword.setText("");
        editTextUrl.setText(prefs.server().get());
    }

    @Background
    @Click(R.id.button_login)
    protected void buttonLoginClick() {
        try {
            prefs.login().put(editTextLogin.getText().toString());
            prefs.password().put(editTextPassword.getText().toString());
            prefs.server().put(editTextUrl.getText().toString());
            String accessToken = NetHelper_.getInstance_(this).auth();
            prefs.accessToken().put(accessToken);
            MenuActivity_.intent(this)
                    .flags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .extra(Operations.ORDERS_FRAGMENT_WITH_RELOAD, Operations.YES).start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NetException e) {
            e.printStackTrace();
            Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }
}
