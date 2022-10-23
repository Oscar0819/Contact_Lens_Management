package com.eunwoo.contactlensmanagement.activity

import SignInIntentContract
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.eunwoo.contactlensmanagement.R
import com.eunwoo.contactlensmanagement.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    var launcher: ActivityResultLauncher<String>? = null

    private lateinit var auth: FirebaseAuth

    val binding: ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // LoginActivity -> 구글로그인 화면 -> LoginActivity로 돌아온 후 콜백 함수.
        // 구글로그인 화면을 통해 얻어온 tokenId를 이용해 Firebase 사용자 인증 정보로 교환하고
        // 해당 정보를 사용해 Firebase에 인증합니다.
        launcher = registerForActivityResult(SignInIntentContract()) { result: String? ->
            result?.let {
                firebaseAuthWithGoogle(it) // tokenId를 이용해 firebase에 인증하는 함수 호출.
            }
        }
    }

    // tokenId를 이용해 firebase에 인증하는 함수.
    fun firebaseAuthWithGoogle(idToken: String) {
        // it가 tokenId, credential은 firebase 사용자 인증 정보.
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        // Firebase 사용자 인증 정보(credential)를 사용해 Firebase에 인증.
        auth!!.signInWithCredential(credential).addOnCompleteListener(this@SignInActivity) { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(
                    this@SignInActivity,
                    getString(R.string.signin_complete),
                    Toast.LENGTH_SHORT
                ).show()
                // 테스트 필요
                finish()
            } else {
                println("firebaseAuthWithGoogle => ${task.exception}")
                Toast.makeText(
                    this@SignInActivity,
                    getString(R.string.signin_faile),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}