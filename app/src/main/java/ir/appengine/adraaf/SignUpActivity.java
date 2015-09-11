package ir.appengine.adraaf;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import utils.ConnectToServer;
import utils.Session;

public class SignUpActivity extends AppCompatActivity {

    TextView textViewSignUp;
    EditText editTextUsername;
    EditText editTextPassword;

    Typeface typefaceYekan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textViewSignUp = (TextView) findViewById(R.id.activity_sign_up_text_view_register);
        editTextUsername = (EditText) findViewById(R.id.activity_sign_up_edit_text_username);
        editTextPassword = (EditText) findViewById(R.id.activity_sign_up_edit_text_password);

        typefaceYekan = Typeface.createFromAsset(getAssets(), "B Yekan.ttf");

        textViewSignUp.setTypeface(typefaceYekan);
        editTextUsername.setTypeface(typefaceYekan);
        editTextPassword.setTypeface(typefaceYekan);

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> params = new HashMap<>();
                params.put("email", String.valueOf(editTextUsername.getText()));
                params.put("password", String.valueOf(editTextPassword.getText()));
                ConnectToServer.post(
                        SignUpActivity.this,
                        true,
                        "ارتباط با سرور",
                        "ثبت نام",
                        ConnectToServer.baseUri + "api/users",
                        params,
                        false,
                        new ConnectToServer.PostListener() {
                            @Override
                            public void ResponseListener(String response) {
                                try {
                                    JSONObject result = new JSONObject(response);
                                    Session session = new Session(getApplicationContext());
                                    session.insertOrUpdateUserID(result.getString("user_id"));
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void ErrorListener(VolleyError error) {
                                //Try to login
                                ConnectToServer.get(
                                        SignUpActivity.this,
                                        true,
                                        "ارتباط با سرور",
                                        "ثبت نام",
                                        ConnectToServer.baseUri + "api/users?email=" + String.valueOf(editTextUsername.getText()) + "&password=" + String.valueOf(editTextPassword.getText()),
                                        false,
                                        new ConnectToServer.GetListener() {
                                            @Override
                                            public void ResponseListener(String response) {
                                                try {
                                                    JSONObject result = new JSONObject(response);
                                                    Session session = new Session(getApplicationContext());
                                                    session.insertOrUpdateUserID(result.getString("user_id"));
                                                    finish();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void ErrorListener(VolleyError error) {

                                            }
                                        }
                                );
                            }
                        }
                );
            }
        });
    }
}
