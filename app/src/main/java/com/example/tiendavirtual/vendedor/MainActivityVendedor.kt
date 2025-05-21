package com.example.tiendavirtual.vendedor

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.tiendavirtual.R
import com.example.tiendavirtual.databinding.ActivityMainVendedorBinding
import com.example.tiendavirtual.vendedor.Bottom_Nav_Fragments_Vendedor.FragmentMisproductosV
import com.example.tiendavirtual.vendedor.Nav_Fragments_Vendedor.FragmentInicioV
import com.example.tiendavirtual.vendedor.Nav_Fragments_Vendedor.FragmentMiTiendaV
import com.example.tiendavirtual.vendedor.Nav_Fragments_Vendedor.FragmentReseniasV
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityVendedor : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainVendedorBinding
    private var firebaseAuth : FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainVendedorBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        binding.navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(FragmentInicioV())
        binding.navigationView.setCheckedItem(R.id.op_inio_v)


    }

    private fun cerrarSesion() {
        firebaseAuth?.signOut()
        startActivity(Intent(applicationContext, LoginVendedorActivity::class.java))
        finish()
        Toast.makeText(applicationContext, "Saliste de la aplicación", Toast.LENGTH_SHORT).show()
    }

    private fun comprobarSesion() {
        if (firebaseAuth!!.currentUser== null) {
            startActivity(Intent(applicationContext,LoginVendedorActivity::class.java))
        }else{
            Toast.makeText(applicationContext, "Bienvenido ${firebaseAuth?.currentUser?.email}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.op_inio_v -> {
                replaceFragment(FragmentInicioV())
            }
            R.id.op_mi_tienda_v -> {
                replaceFragment(FragmentMiTiendaV())
            }
            R.id.op_resenia_v -> {
                replaceFragment(FragmentReseniasV())
            }
            R.id.op_cerrar_sesion-> {

                cerrarSesion()

            }
            R.id.op_mis_productos_v -> {
                replaceFragment(FragmentMisproductosV())
            }
            R.id.op_mis_ordenes_v->{
                replaceFragment(FragmentMisproductosV())
            }

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}