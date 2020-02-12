package mx.edu.ittepic.ladm_u1_practica2_victorcamberos

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            if(radioButton.isChecked){
                guardarArchivoInterno()
            }else if (radioButton2.isChecked){
                if(ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){//preguntar si está denegado para solicitarlo
                    //ContextCompact pregunta si hay un permiso existente
                    //SI ENTRA ENTONCES AÚN NO SE OTORGAN LOS PERMISOS
                    //EL SIGUIENTE CÓDIGO LOS SOLICITA
                    //El arreglo se crea simepre aunque solo tenga un puro permiso como de leer menajes de texto
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0) //
                    //EL CÓDIGO DE ARRIBA ES EL EQUIVALENTE A OTORGAR PERMISOS EN MANIFEST
                }else{
                    guardarArchivoSD()
                }
            }
        }
        button2.setOnClickListener {
            if(radioButton.isChecked){
                leerArchivoInterno()
            }else if(radioButton2.isChecked){
                leerArchivoSD()
            }
        }
    }
    //FUNCIÓN PARA VER SI HAY MEMORIA EXTERNA
    fun noSD(): Boolean{
        var estado = Environment.getExternalStorageState()//CLASE ESTÁTICA ENFOCADO A LA MEMORIA EXTERNA
        if (estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }

    fun guardarArchivoInterno(){
        try{
            var flujoSalida = OutputStreamWriter(
                openFileOutput(editText2.text.toString()+".txt", Context.MODE_PRIVATE)) //solo un archivo puede meter datos al data a la vez
            var data = editText.text.toString()+"&"
            flujoSalida.write(data)
            flujoSalida.flush() //opcional equivalente al commit ()
            flujoSalida.close()

            mensaje("EXITO! se guardó correctamente")
            ponerTextos("")

        }catch( error : IOException){
            mensaje(error.message.toString())
        }
    }

    fun leerArchivoInterno(){
        try{
            var flujoEntrada = BufferedReader( //se usa BufferedReader para que no lea letra por letra (HACE REFERENCIA A MEMORIA PRINCIPAL)
                InputStreamReader(
                    openFileInput(editText2.text.toString()+".txt")))
            var data = flujoEntrada.readLine()
            var vector = data.split("&")

            ponerTextos(vector[0])
            flujoEntrada.close()

        }catch(error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun mensaje(m:String){
        AlertDialog.Builder(this)
            .setTitle("Atención")
            .setMessage(m)
            .setPositiveButton("OK"){d,i->}
            .show()
    }
    fun ponerTextos(t1:String){ //Método que está un poco de más
        editText.setText(t1)
    }
    fun guardarArchivoSD(){
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
        }
        try{
            //Referenciar a memoria externa
            var rutaSD = Environment.getExternalStorageDirectory() //la línea indica que está depreciado pero si funciona
            var datoArchivo = File(rutaSD.absolutePath,editText2.text.toString()+".txt")
            var flujoSalida = OutputStreamWriter(FileOutputStream(datoArchivo))//FileOutputStream se puede referenciar a la ruta que sea
            var data = editText.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush() //opcional equivalente al commit ()
            flujoSalida.close()

            mensaje("EXITO! se guardó correctamente")
            ponerTextos("")

        }catch( error : IOException){
            mensaje(error.message.toString())
        }
    }
    fun leerArchivoSD(){
        if(noSD()) {
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        try{
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,editText2.text.toString()+".txt")
            var flujoEntrada = BufferedReader( //se usa BufferedReader para que no lea letra por letra (HACE REFERENCIA A MEMORIA PRINCIPAL)
                InputStreamReader(
                    FileInputStream(datosArchivo)
                )
            )
            var data = flujoEntrada.readLine()
            var vector = data.split("&")

            ponerTextos(vector[0])
            flujoEntrada.close()

        }catch(error: IOException){
            mensaje(error.message.toString())
        }
    }
}
