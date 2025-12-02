package com.example.a2048;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private int [][] matriz_tablero = new int[4][4], matriz_anterior = new int[4][4];
    GridLayout pantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GestureDetectorCompat gDetector = new GestureDetectorCompat(this, new MyGestureListener());
        pantalla = findViewById(R.id.cuadricula);

        pantalla.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gDetector.onTouchEvent(event);
            }
        });

        inicializar_matriz();
        generar_numero();
        mostrar_tablero();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                Log.d("Macia", "Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d("Macia", "Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d("Macia", "Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d("Macia", "Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d("Macia", "Movement occurred outside bounds of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(@NonNull MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            float difX, difY;

            difX = e2.getX() - e1.getX();
            difY = e2.getY() - e1.getY();

            if (Math.abs(Math.abs(difX) - Math.abs(difY)) > 200){
                if (Math.abs(difX) > Math.abs(difY)){
                    if (difX > 0){
                        goRight();
                    } else {
                        goLeft();
                    }
                } else {
                    if (difY > 0){
                        goDown();
                    } else {
                        goUp();
                    }
                }
            }
            return true;
        }
    }

    public void goRight(){
        Log.d("Macia", "goRight: ");
        for (int fila = 0; fila < matriz_tablero.length; fila++) {
            for (int columna = matriz_tablero.length-2; columna < 0; columna--) {
                for (int iteraciones = 0; iteraciones < matriz_tablero.length-columna-1; iteraciones++) {
                    if (matriz_tablero[fila][columna+iteraciones+1] == 0){
                        matriz_tablero[fila][columna+iteraciones+1] = matriz_tablero[fila][columna+iteraciones];
                        matriz_tablero[fila][columna+iteraciones] = 0;
                    }
                }
            }
        }
        mostrar_tablero();
    }

    public void goLeft(){

    }

    public void goUp(){

    }

    public void goDown(){

    }

    public void mostrar_tablero (){
        String nombre_casilla;
        TextView casilla;

        for (int fila = 0; fila < matriz_tablero.length; fila++) {
            for (int columna = 0; columna < matriz_tablero.length; columna++) {
                nombre_casilla = "casilla" + (4*fila+columna);
                casilla = findViewById(getResources().getIdentifier(nombre_casilla, "id", getPackageName()));
                if (matriz_tablero[fila][columna] == 0){
                    casilla.setText(" ");
                } else {
                    casilla.setText(String.valueOf(matriz_tablero[fila][columna]));
                }
            }
        }
    }

    public void generar_numero(){
        boolean generado = false;
        int numero_aleatorio, fila, columna;

        while (!generado){
            numero_aleatorio = (int) (Math.random()*16);
            fila = numero_aleatorio/matriz_tablero.length;
            columna =  numero_aleatorio%matriz_tablero.length;

            if (matriz_tablero[fila][columna] == 0){
                generado = true;
                matriz_tablero[fila][columna] = 2;
            }
        }
    }

    public void inicializar_matriz(){
        for (int fila = 0; fila < matriz_tablero.length; fila++) {
            for (int columna = 0; columna < matriz_tablero.length; columna++) {
                matriz_tablero[fila][columna] = 0;
            }
        }
    }
}