package com.example.cbr_manager.ui.create_client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.cbr_manager.NavigationActivity;
import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.client.Client;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class PhotoFragment extends Fragment {
    static final int PICK_IMAGE_CODE = 1;
    private static final APIService apiService = APIService.getInstance();
    Button cameraButton;
    View view;
    ImageView imageView;
    File filePhotoUpload;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        view = inflater.inflate(R.layout.activity_create_client_photo, container, false);

        cameraButton = view.findViewById(R.id.takePhotoButton);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE);
            }
        });
        //TODO: Add Camera functionality

        Button submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSurvey();
            }
        });
        Button prevButton = view.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateClientActivity) getActivity()).setViewPager(4);
            }
        });

        return view;
    }

    private void submitSurvey() {
        Intent intent = new Intent(getActivity(), NavigationActivity.class);
        startActivity(intent);
        Client client = ((CreateClientActivity) getActivity()).getClient();

        if (filePhotoUpload != null){
           apiService.clientService.uploadClientPhoto(filePhotoUpload).enqueue(new Callback<Client>() {
               @Override
               public void onResponse(Call<Client> call, Response<Client> response) {
                   try {
                       String error = response.errorBody().string();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   System.out.println("ok");
               }

               @Override
               public void onFailure(Call<Client> call, Throwable t) {

               }
           });
        } else {
            Call<Client> call = apiService.clientService.createClientManual(client);
            call.enqueue(new Callback<Client>() {
                @Override
                public void onResponse(Call<Client> call, Response<Client> response) {
                    if (response.isSuccessful()) {
                        Snackbar.make(view, "Successfully created the client.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Snackbar.make(view, "Failed to create the client.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }

                @Override
                public void onFailure(Call<Client> call, Throwable t) {
                    Snackbar.make(view, "Failed to create the client. Please try again", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_CODE && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            Bitmap bitmap = getBitMap(path);

            imageView = getView().findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);

            filePhotoUpload = saveToBitMap(bitmap);
        }
    }

    public File saveToBitMap(Bitmap bitmap) {
        File outputDir = getContext().getCacheDir();
        File file = null;
        try {
            file = File.createTempFile("prefix", "png", outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public Bitmap getBitMap(Uri path) {
        try {
            return MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
