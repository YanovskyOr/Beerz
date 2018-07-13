package ory.ofir.beerz.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ory.ofir.beerz.MainActivity;
import ory.ofir.beerz.R;

public class LoginFragment extends Fragment {
    private EditText mEmailField;
    private EditText mPasswowrdField;
    private Button mLoginBtn;
    private Button mRegisterBrn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_menu, container, false);

        mAuth = FirebaseAuth.getInstance();

        mEmailField = view.findViewById(R.id.emailField);
        mPasswowrdField= view.findViewById(R.id.passwordField);
        mLoginBtn = view.findViewById(R.id.loginButton);
        mRegisterBrn = view.findViewById(R.id.registerButton);
        progressBar = view.findViewById(R.id.login_progressbar);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    //getActivity().getFragmentManager().popBackStackImmediate();
                    Log.d("TAG","Auth Changed.");
                }
            }
        };

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag","log in");
                startSignIn();
            }
        });

        mRegisterBrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag", "log in");
                startRegister();
            }
        });
        return view;
    }


    private void startSignIn() {
        progressBar.setVisibility(View.VISIBLE);
        String email = mEmailField.getText().toString();
        String password = mPasswowrdField.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(),"Fields are empty",Toast.LENGTH_LONG).show();
            Log.d("tag", "fields are empty");
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getActivity(),"Sign in failed",Toast.LENGTH_LONG).show();
                        Log.d("tag", "login failed");
                    }
                    else {
                        Log.d("tag", "login success");
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }

    private void startRegister() {
        progressBar.setVisibility(View.VISIBLE);
        String email = mEmailField.getText().toString();
        String password = mPasswowrdField.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(),"Fields are empty",Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getActivity(),"Registration in failed",Toast.LENGTH_LONG).show();
                        Log.d("tag", "register failed");
                    }
                    else {
                        Log.d("tag", "register success");


                        BeersListFragment fragment = new BeersListFragment();
                        FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                        tran.replace(R.id.main_container, fragment);
                        tran.commit();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}
