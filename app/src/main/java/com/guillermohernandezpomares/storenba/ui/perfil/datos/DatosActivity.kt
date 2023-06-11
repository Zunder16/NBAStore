package com.guillermohernandezpomares.storenba.ui.perfil.datos

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.guillermohernandezpomares.storenba.databinding.ActivityDatosBinding
import com.guillermohernandezpomares.storenba.model.UsuarioInsertar
import com.guillermohernandezpomares.storenba.ui.MainActivity
import com.guillermohernandezpomares.storenba.utils.Constantes
import com.guillermohernandezpomares.storenba.utils.UsuarioRepo
import com.guillermohernandezpomares.storenba.utils.Utiles
import java.util.*

class DatosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDatosBinding
    private var uri: Uri? = null
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val usuario = UsuarioInsertar()
    private val usuarioRepo: UsuarioRepo = UsuarioRepo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setValoresPerfil()
        binding.ivModificarFoto.setOnClickListener {
            seleccionFoto()
        }
        binding.btnModificar.setOnClickListener {
            modificarUsuario(Constantes.DATOS_USUARIO!!.fotoUsuario)
        }
    }

    private fun setValoresPerfil() {
        val usuario = Constantes.DATOS_USUARIO
        if (usuario != null) {
            binding.etNombreModificar.editText?.setText(usuario.nombre)
            binding.etApellidoModificar.editText?.setText(usuario.apellidos)

            Glide.with(this).load(usuario.fotoUsuario).into(binding.ivModificarFoto)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return Utiles.onCreateOptionsMenu(menuInflater, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return Utiles.onOptionsItemSelected(this, item)
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
                binding.ivModificarFoto.setImageURI(data.data)
                uri = data.data
                binding.btnModificar.setOnClickListener {
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
                Constantes.STORAGE_FOTOPERFIL + Constantes.DATOS_USUARIO!!.correo +
                        "/" + UUID.randomUUID().toString()
            )

            ref.putFile(uri).addOnSuccessListener {
                Toast.makeText(this, "Imagen cargada", Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
                ref.downloadUrl.addOnSuccessListener { uri ->
                    // Poner la URL en el objeto de Usuario
                    modificarUsuario(uri.toString())
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
    private fun modificarUsuario(url: String) {
        if (comprobacion()) {
            usuarioModificar(url)
        }

    }

    private fun usuarioModificar(url: String) {
        usuario.nombre = binding.etNombreModificar.editText!!.text.toString()
        usuario.apellidos = binding.etApellidoModificar.editText!!.text.toString()
        usuario.correo = Constantes.DATOS_USUARIO!!.correo
        usuario.fotoUsuario = url
        usuarioRepo.modifyUsuario(Constantes.DATOS_USUARIO!!.id, usuario)
        navegarMain()
    }
    private fun navegarMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun comprobacion(): Boolean {
        if (binding.etNombreModificar.editText?.text.toString().isEmpty() ||
            binding.etApellidoModificar.editText?.text.toString().isEmpty()
        ){
            Toast.makeText(this, "Los campos estan vacios", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

}