package br.ufmg.coltec.topicos_e04_backgroundtasks;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button downloadBtn = findViewById(R.id.btn_download);
        EditText txtLink = findViewById(R.id.txt_img_link);
        imgView = findViewById(R.id.img_picture);
        progressBar = findViewById(R.id.progress_bar);

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Exibe a barra de progresso enquanto a imagem está sendo baixada
                progressBar.setVisibility(View.VISIBLE);

                // Esconde a imagem durante o download
                imgView.setVisibility(View.GONE);
                String imageUrl = txtLink.getText().toString();

                // Inicia uma nova Thread para o download da imagem
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Baixa a imagem usando a classe ImageDownloader
                            Bitmap img = ImageDownloader.download(imageUrl);

                            // Atualiza a UI na thread principal após o download
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    if (img != null) {
                                        imgView.setImageBitmap(img);
                                        imgView.setVisibility(View.VISIBLE);
                                    } else {
                                        Log.e("MainActivity", "Falha ao carregar a imagem.");
                                    }
                                }
                            });
                        } catch (IOException e) {
                            Log.e("MainActivity", "Erro ao baixar a imagem: " + e.getMessage());

                            // Esconder a barra de progresso em caso de erro
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}