package com.example.movieexplorer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieexplorer.databinding.FragmentProfileBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ProfileFragment extends Fragment implements dialogListener{
    FragmentProfileBinding fragmentProfileBinding;
    ActivityResultLauncher<Intent> imagePickerActivityResult;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater,container,false);
        imagePickerActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null){
                    Uri imageUri = result.getData().getData();
                    // Save new photo to parse
                    try {
                        saveImageToParse(imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Replace old photo with new photo
                    Glide.with(requireContext())
                            .load(imageUri)
                            .into(fragmentProfileBinding.ivCurrUserProfileImage);
                }
            }
        });
        return fragmentProfileBinding.getRoot();
    }

    private void saveImageToParse(Uri imageUri)throws IOException {

        InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
        byte[] inputData = getBytes(inputStream);

        ParseFile file = new ParseFile("user_profile_photo", inputData);
        file.saveInBackground();

        ParseUser user = ParseUser.getCurrentUser();
        user.put("profilePhoto", file);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getContext(), "Saved File Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private byte[] getBytes(InputStream inputStream)throws IOException{
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ParseUser currUser = ParseUser.getCurrentUser();
        fragmentProfileBinding.tvCurrUserName.setText(currUser.getUsername());
        fragmentProfileBinding.tvCurrUserEmail.setText(currUser.getEmail());
        ParseFile image = currUser.getParseFile("profilePhoto");
        Glide.with(getContext()).load(image == null? null : image.getUrl()).placeholder(R.drawable.defaultimage).override(200,200).into(fragmentProfileBinding.ivCurrUserProfileImage);
        fragmentProfileBinding.btnUploadNewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                imagePickerActivityResult.launch(photoPickerIntent);
            }
        });
        fragmentProfileBinding.btnNameChange.setOnClickListener(view1 -> {
            dialogfragment dialogWindow = new dialogfragment();
            dialogWindow.show(getChildFragmentManager(), "dialogWindow");

        });
    }


    @Override
    public void onFinishChangeUsername(String newUsername) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("username", newUsername);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                fragmentProfileBinding.tvCurrUserName.setText(newUsername);
                Toast.makeText(getContext(), "some stuff would go here", Toast.LENGTH_SHORT).show();
            }
        });
    }
}