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
    private boolean finish = false;
    private int high_score = 2;
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
        siguiente();
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

            if (Math.abs(Math.abs(difX) - Math.abs(difY)) > 50){
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
        for (int fila = 0; fila < matriz_tablero.length; fila++) {
            for (int columna = matriz_tablero.length-2; columna >= 0; columna--) {
                for (int iteraciones = 0; iteraciones < matriz_tablero.length-columna-1; iteraciones++) {
                    if (matriz_tablero[fila][columna+iteraciones+1] == 0){
                        matriz_tablero[fila][columna+iteraciones+1] = matriz_tablero[fila][columna+iteraciones];
                        matriz_tablero[fila][columna+iteraciones] = 0;
                    }
                }
            }
        }
        siguiente();
    }

    public void goLeft(){
        for (int fila = 0; fila < matriz_tablero.length; fila++) {
            for (int columna = 1; columna < matriz_tablero.length; columna++) {
                for (int iteraciones = 0; iteraciones < columna; iteraciones++) {
                    if (matriz_tablero[fila][columna-iteraciones-1] == 0){
                        matriz_tablero[fila][columna-iteraciones-1] = matriz_tablero[fila][columna-iteraciones];
                        matriz_tablero[fila][columna-iteraciones] = 0;
                    }
                }
            }
        }
        siguiente();
    }

    public void goUp(){
        for (int columna = 0; columna < matriz_tablero.length; columna++) {
            for (int fila = 1; fila < matriz_tablero.length; fila++) {
                for (int iteraciones = 0; iteraciones < fila; iteraciones++) {
                    if (matriz_tablero[fila-iteraciones-1][columna] == 0){
                        matriz_tablero[fila-iteraciones-1][columna] = matriz_tablero[fila-iteraciones][columna];
                        matriz_tablero[fila-iteraciones][columna] = 0;
                    }
                }
            }
        }
        siguiente();
    }

    public void goDown(){
        for (int columna = 0; columna < matriz_tablero.length; columna++) {
            for (int fila = matriz_tablero.length-2; fila >= 0; fila--) {
                for (int iteraciones = 0; iteraciones < matriz_tablero.length-fila-1; iteraciones++) {
                    if (matriz_tablero[fila+iteraciones+1][columna] == 0){
                        matriz_tablero[fila+iteraciones+1][columna] = matriz_tablero[fila+iteraciones][columna];
                        matriz_tablero[fila+iteraciones][columna] = 0;
                    }
                }
            }
        }
        siguiente();
    }

    public void siguiente(){
        boolean continuar;

        actualizar_high_score();

        if (!finish && high_score==2048){
            finish = true;
            game_complete();
        }

        continuar = generar_numero();
        if (!continuar){
            game_over();
        }

        mostrar_tablero();
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

    public boolean generar_numero(){
        boolean lleno = true;
        int numero_aleatorio, fila, columna;

        for (int i = 0; i < matriz_tablero.length; i++) {
            for (int j = 0; j < matriz_tablero.length; j++) {
                if (matriz_tablero[i][j] == 0){
                    lleno = false;
                    break;
                }
            }
        }

        if (lleno){
            return false;
        }

        while (true){
            numero_aleatorio = (int) (Math.random()*16);
            fila = numero_aleatorio/matriz_tablero.length;
            columna =  numero_aleatorio%matriz_tablero.length;

            if (matriz_tablero[fila][columna] == 0){
                matriz_tablero[fila][columna] = 2;
                return true;
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

    public void actualizar_high_score(){
        for (int fila = 0; fila < matriz_tablero.length; fila++) {
            for (int columna = 0; columna < matriz_tablero.length; columna++) {
                if (matriz_tablero[fila][columna] > high_score){
                    high_score = matriz_tablero[fila][columna];
                    TextView panel_high_score = findViewById(R.id.high_score);
                    panel_high_score.setText(String.valueOf(high_score));
                }
            }
        }

    }

    public void game_over(){

    }

    public void game_complete(){

    }
}