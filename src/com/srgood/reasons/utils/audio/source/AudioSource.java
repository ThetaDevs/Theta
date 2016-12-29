package com.srgood.reasons.utils.audio.source;


import com.srgood.reasons.utils.audio.Stream.AudioStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

/**
 * Created by srgood on 12/28/2016.
 */
public interface AudioSource {
    String getSource();

    AudioInfo getInfo();

    AudioStream asStream();

    File asFile(String path, boolean deleteIfExists) throws FileAlreadyExistsException, FileNotFoundException;
}
