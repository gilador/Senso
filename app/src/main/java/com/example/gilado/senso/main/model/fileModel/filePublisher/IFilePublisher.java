package com.example.gilado.senso.main.model.fileModel.filePublisher;

import java.io.InputStream;

/**
 * Created by gilado on 9/30/2017.
 */

public interface IFilePublisher {
    void publish(InputStream reader, String fileName, String internalFileName);
}
