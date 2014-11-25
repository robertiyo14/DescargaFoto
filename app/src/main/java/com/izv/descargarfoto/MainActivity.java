package com.izv.descargarfoto;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends Activity {

    private EditText etRuta, etNombre;
    private RadioButton rbPublica, rbPrivada;
    private String ruta;
    private String nombre;
    private ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etRuta = (EditText) findViewById(R.id.etRuta);
        etNombre = (EditText) findViewById(R.id.etNombre);
        rbPublica = (RadioButton) findViewById(R.id.rbPublica);
        rbPrivada = (RadioButton) findViewById(R.id.rbPrivada);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void guardar(View v) {
        HiloFacil hf = new HiloFacil();
        hf.execute();
        /*Bitmap img = null;
        Bitmap image = null;
        img = BitmapFactory.decodeFile();
        iv.setImageBitmap(img);*/
    }


    class HiloFacil extends AsyncTask<Object, Integer, String> {

        HiloFacil(String... p) {
            //Lo primero que se ejecuta.
        }

        @Override
        protected void onPreExecute() {
            //1º en ejecutarse despues del execute. Se ejecuta en la hebra UI.
            //trabajo previo.
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object[] params) {
            //2º en ejecutarse. En una hebra nueva.
            try {
                String dir = etRuta.getText().toString();
                URL url = new URL(dir);
                nombre = etNombre.getText().toString();
                // establecemos conexion
                URLConnection urlCon = url.openConnection();
                String tipo = dir.substring(dir.length()-3);
                if(tipo.equals("jpg")||tipo.equals("png")||tipo.equals("gif")){
                    // Se obtiene el inputStream de la foto web y se abre el fichero
                    // local.
                    InputStream is = urlCon.getInputStream();
                    if (rbPrivada.isChecked()) {
                        ruta = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() +"/"+ nombre+"."+tipo;
                        Log.v("Ruta: ",ruta);
                    } else {
                        ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()
                                +"/"+ nombre+"."+tipo;
                        Log.v("Ruta: ",ruta);
                    }
                    FileOutputStream fos = new FileOutputStream(ruta);

                    // Lectura de la foto de la web y escritura en fichero local
                    byte[] array = new byte[1000]; // buffer temporal de lectura.
                    int leido = is.read(array);
                    while (leido > 0) {
                        fos.write(array, 0, leido);
                        leido = is.read(array);
                    }

                    // cierre de conexion y fichero.
                    is.close();
                    fos.close();
                }else{
                    Log.v("Error: ","No se puede descargar ese tipo de archivos.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //3º en ejecutarse de forma intermitente para escribir en la UI.
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            //4º en ejecutarse al finalizar la hebra. En la hebra UI.
            super.onPostExecute(s);
        }

        @Override
        protected void onCancelled() {
            //si cancelo la hebra se llama a uno de estos dos métodos.
            super.onCancelled();
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }
    }
}
