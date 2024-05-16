package ru.mirea.borodkinada.mireaproject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.mirea.borodkinada.mireaproject.databinding.FragmentCameraBinding;


public class CameraFragment extends Fragment {

    private boolean isWork = false;
    private Uri imageUri;
    private FragmentCameraBinding fragmentCameraBinding;
    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
        isWork = permissions.containsValue(true);
    });


    private List<Uri> imageUris = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentCameraBinding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = fragmentCameraBinding.getRoot();

        int	cameraPermissionStatus = ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.CAMERA);
        int	storagePermissionStatus = ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if	(cameraPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus
                ==	PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            requestPermissionLauncher.launch(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE});
        }


        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    fragmentCameraBinding.imageView.setImageURI(imageUri);
                    imageUris.add(imageUri);
                }
            }
        };

        ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback);

        fragmentCameraBinding.takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (isWork) {
                    try {
                        File photoFile = createImageFile();
                        String authorities = requireContext().getPackageName() + ".mireaprovider";
                        imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        cameraActivityResultLauncher.launch(cameraIntent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(requireContext(), "Нет разрешений", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fragmentCameraBinding.savePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imageUris.isEmpty()) {
                    createCollage();
                } else {
                    Toast.makeText(requireContext(), "Нет сохраненных фотографий для создания коллажа", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";

        File storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }


    private void createCollage() {
        Bitmap collageBitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(collageBitmap);


        int imageSize = 400;


        int startX = 0;
        int startY = 0;

        for (Uri imageUri : imageUris) {
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);

                canvas.drawBitmap(scaledBitmap, startX, startY, null);

                if (startX + imageSize * 2 <= collageBitmap.getWidth()) {
                    startX += imageSize;
                } else {
                    startX = 0;
                    startY += imageSize;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        fragmentCameraBinding.imageView.setImageBitmap(collageBitmap);
    }

}
