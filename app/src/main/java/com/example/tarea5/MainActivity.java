package com.example.tarea5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    InputImage image;
    TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResultado = (TextView) findViewById(R.id.txtResultado);
    }

    public void OpenGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                ImageView imageView = (ImageView) findViewById(R.id.imgCaptura);
                imageView.setImageURI(data.getData());
                image = InputImage.fromFilePath(this, data.getData());
                proceso();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void proceso() {
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

        labeler.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        txtResultado.setText("══════════════════");
                        for (ImageLabel label : labels) {
                            txtResultado.append("\nResultado: " + label.getIndex());
                            txtResultado.append("\nObjeto: " + label.getText());
                            txtResultado.append("\nConfianza: " + label.getConfidence());
                            txtResultado.append("\n══════════════════");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MainActivity.this,
                                ("Error: " + e.getMessage()), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}