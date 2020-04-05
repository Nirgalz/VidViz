package com.vidviz.back.service;

import ws.schild.jave.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VideoEncodingService {

    private Path BIN = Paths.get("ffmpeg/bin");

    public void encodeVideo(Path folder, String fileName) throws IOException, EncoderException {

        Path outputPath;
        //Path source = Paths.get("uploads/"+folder.getFileName() + "/" + fileName);
        if (!Files.exists(Paths.get("videos/" + folder.getFileName() + "/thumbnails"))) {
            Files.createDirectory(Paths.get("videos/" + folder.getFileName() + "/thumbnails"));
        }
        outputPath = Paths.get("videos/" + folder.getFileName() + "/thumbnails/" + fileName);

        File source = new File("videos/" + folder.getFileName() + "/" + fileName);
        File target = new File("videos/" + folder.getFileName() + "/thumbnails/" + fileName);

        float originalWidth = 0;
        float originalHeight = 0;
        int width = 0;
        int height = 0;

        MultimediaObject multimediaObject = new MultimediaObject(source);
        MultimediaInfo infos= multimediaObject.getInfo();

        originalWidth = infos.getVideo().getSize().getWidth();
        originalHeight = infos.getVideo().getSize().getHeight();

        float ratio = originalWidth / originalHeight;

        if (ratio == 1) {
            width = 500;
            height = 500;
        } else {
            height = 1024;
            width = 768;
        }


        /* Step 2. Set Audio Attrributes for conversion*/
//        AudioAttributes audio = new AudioAttributes();
//        audio.setCodec("aac");
//// here 64kbit/s is 64000
//        audio.setBitRate(64000);
//        audio.setChannels(2);
//        audio.setSamplingRate(44100);

        /* Step 3. Set Video Attributes for conversion*/
        VideoAttributes video = new VideoAttributes();
        video.setCodec("h264");
        video.setX264Profile(VideoAttributes.X264_PROFILE.BASELINE);
// Here 160 kbps video is 160000
        video.setBitRate(160000);
// More the frames more quality and size, but keep it low based on devices like mobile
        video.setFrameRate(30);
        video.setSize(new VideoSize(width, height));

        /* Step 4. Set Encoding Attributes*/
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp4");
//        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);

        /* Step 5. Do the Encoding*/
        try {
            Encoder encoder = new Encoder();

            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (Exception e) {
            /*Handle here the video failure*/
            e.printStackTrace();
        }
    }
}
