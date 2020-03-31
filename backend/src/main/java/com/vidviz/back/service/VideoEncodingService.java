package com.vidviz.back.service;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.FFmpegResult;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import com.github.kokorin.jaffree.ffprobe.FFprobe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VideoEncodingService {

    private Path BIN = Paths.get("ffmpeg/bin");

    public void encodeVideo(Path folder, String fileName) throws IOException {

        Path outputPath;
        Path source = Paths.get("uploads/"+folder.getFileName() + "/" + fileName);
        if (!Files.exists(Paths.get("uploads/"+folder.getFileName() + "/thumbnails"))) {
            Files.createDirectory(Paths.get("uploads/"+folder.getFileName() + "/thumbnails"));
        }
        outputPath = Paths.get("uploads/"+folder.getFileName() + "/thumbnails/" + fileName);

        FFmpegResult result = FFmpeg.atPath(BIN)
                .addInput(UrlInput.fromPath(source))
                .addOutput(UrlOutput.toPath(outputPath)
                        .copyAllCodecs().setFrameSize(400,400)
                )
                .execute();
    }
}
