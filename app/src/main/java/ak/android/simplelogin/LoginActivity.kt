package ak.android.simplelogin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 100
        private val TAG = LoginActivity::class.java.simpleName
    }

    // Add auth provider(s) here
    private val providers = listOf(AuthUI.IdpConfig.GoogleBuilder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        supportActionBar?.hide()

        btn_google.setOnClickListener {
            // Log in only processed if button clicked
            createSignInIntent()
        }
    }

    private fun createSignInIntent() {
        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers).build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully logged in
                // TODO: Do something after logged in
                val user = FirebaseAuth.getInstance().currentUser
                user?.let {
                    val name = it.displayName ?: it.email
                    Toast.makeText(this, "Welcome, $name", Toast.LENGTH_SHORT).show()

                    // As an example, create an activity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            } else {
                // Log in failed
                Log.e(TAG, "Error when sign in. Code: ${response?.error?.errorCode}")
            }
        }
    }
}
