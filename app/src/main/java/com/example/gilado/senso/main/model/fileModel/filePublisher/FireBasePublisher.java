package com.example.gilado.senso.main.model.fileModel.filePublisher;

import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;

/**
 * Created by gilado on 9/30/2017.
 */

public class FireBasePublisher implements IFilePublisher {
    private StorageReference mStorageRef;
    private String TAG = FireBasePublisher.class.getSimpleName();

    public FireBasePublisher() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void publish(InputStream readerStream, String serverFileName, String internalFileName) {
        UploadTask uploadTask = mStorageRef.child(serverFileName).putStream(readerStream);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Log.d(TAG, "putStream->onSuccess");
            File    file    = new File(internalFileName);
            boolean deleted = file.delete();
            Log.d(TAG, "putStream->onSuccess " + ", internal file deletion status: " + deleted);

        }).addOnFailureListener(exception -> Log.d(TAG, "putStream->onFailure"));

    }
}
