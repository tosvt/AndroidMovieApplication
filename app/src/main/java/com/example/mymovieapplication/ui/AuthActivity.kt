package com.example.mymovieapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.mymovieapplication.databinding.ActivityAuthBinding
import java.util.concurrent.Executor

class AuthActivity : AppCompatActivity() {
    lateinit var  binding: ActivityAuthBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgFinger.setOnClickListener {
            checkDeviceHasBiomentric()
        }

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt= BiometricPrompt(this,executor,object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@AuthActivity, "Ошибка входа: $errString", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                //Toast.makeText(this@AuthActivity, "Auth success!!!", Toast.LENGTH_LONG).show()
                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                startActivity(intent)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@AuthActivity, "Вход не удался", Toast.LENGTH_LONG).show()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Вход в приложение")
            .setSubtitle(" ")
            .setNegativeButtonText(" ")
            .build()
        binding.btnLogin.setOnClickListener{
            biometricPrompt.authenticate(promptInfo)
        }
    }

    fun checkDeviceHasBiomentric(){
        val biometricManager = androidx.biometric.BiometricManager.from(this)
        when(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)){
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can auth using bio")
                binding.tvMsg.text = "Вход возможен, нажмите на кнопку ниже"
                binding.btnLogin.isEnabled=true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE-> {
                Log.d("MY_APP_TAG", "Bio feautures are currently unavailable")
                binding.tvMsg.text = "Вход невозможен"
                binding.btnLogin.isEnabled=false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)

                }
                binding.btnLogin.isEnabled=false

                startActivityForResult(enrollIntent, 100)
            }
        }
    }
}