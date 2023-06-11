package com.guillermohernandezpomares.storenba.ui.login

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.guillermohernandezpomares.storenba.databinding.ActivityRegistroBinding
import com.guillermohernandezpomares.storenba.model.UsuarioInsertar
import com.guillermohernandezpomares.storenba.ui.MainActivity
import com.guillermohernandezpomares.storenba.utils.Constantes
import com.guillermohernandezpomares.storenba.utils.UsuarioRepo
import java.util.*


class RegistroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding
    private val usuarioRepo: UsuarioRepo = UsuarioRepo()
    private val autorizacion: FirebaseAuth = FirebaseAuth.getInstance()
    private var uri: Uri? = null
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val usuario = UsuarioInsertar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navegarLogin()
        binding.ivAddFoto.setOnClickListener {
            seleccionFoto()
        }
        binding.btnCrearCuenta.setOnClickListener {
            registrarUsuario(Constantes.USUARIO_URL_PREDETERMINADA)
        }
    }

    private fun seleccionFoto() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === RESULT_OK) {
            if (data != null) {
                binding.ivAddFoto.setImageURI(data.data)
                uri = data.data
                binding.btnCrearCuenta.setOnClickListener {
                    guardarImagenFirebase(uri)
                }
            }

        }
    }

    private fun guardarImagenFirebase(uri: Uri?) {
        if (uri != null) {

            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()


            val ref = storage.reference.child(
                Constantes.STORAGE_FOTOPERFIL + binding.etUsuarioRegistro.editText?.text.toString() +
                        "/" + UUID.randomUUID().toString()
            )

            ref.putFile(uri).addOnSuccessListener {
                Toast.makeText(this, "Imagen cargada", Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
                ref.downloadUrl.addOnSuccessListener { uri ->
                    // Poner la URL en el objeto de Usuario
                    registrarUsuario(uri.toString())
                }
            }
                .addOnFailureListener { e ->
                    // Error, la imagen no se cargó
                    progressDialog.dismiss()
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    // Listener de progreso para mostrar el porcentaje en el cuadro de diálogo
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                    progressDialog.setMessage("Cargando $progress%")
                }
        }

    }

    private fun registrarUsuario(url: String) {
        if (comprobacion()) {
            registroAutorizacion(url)
        }

    }

    private fun registroAutorizacion(url: String) {

        val email = binding.etUsuarioRegistro.editText?.text.toString()
        val password = binding.etContraseniaRegistro.editText?.text.toString()

        autorizacion.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                crearUsuario(url)
            } else {
                Log.e(TAG, "Error al registrar el usuario", task.exception)
            }
        }
    }


    private fun navegarLogin() {
        binding.tvIniciarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navegarMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun crearUsuario(url: String) {
        usuario.nombre = binding.etNombreRegistro.editText?.text.toString()
        usuario.apellidos = binding.etApellidosRegistro.editText?.text.toString()
        usuario.correo = binding.etUsuarioRegistro.editText?.text.toString()
        usuario.fotoUsuario = url
        usuarioRepo.insertUsuario(usuario)
        navegarMain()
    }

    private fun comprobacion(): Boolean {
        if (binding.etNombreRegistro.editText?.text.toString().isEmpty() ||
            binding.etApellidosRegistro.editText?.text.toString().isEmpty() ||
            binding.etContraseniaRegistro.editText?.text.toString().isEmpty() ||
            binding.etUsuarioRegistro.editText?.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "Los campos estan vacios", Toast.LENGTH_LONG).show()
            return false
        } else if (!validarCorreo(binding.etUsuarioRegistro.editText?.text.toString())) {
            Toast.makeText(this, "El correro es incorrecto", Toast.LENGTH_LONG).show()
            return false
        } else if (!validarContrasenya(binding.etContraseniaRegistro.editText?.text.toString())) {
            Toast.makeText(this, "La contraseña es incorrecta", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun validarContrasenya(password: String): Boolean {
        val patron = "^(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]+$".toRegex()
        return patron.matches(password)
    }


    private fun validarCorreo(correo: String): Boolean {

        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(correo).matches()

    }
}