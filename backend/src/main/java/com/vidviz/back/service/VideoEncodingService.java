package com.vidviz.back.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.StringValue;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.mp4.media.Mp4VideoDirectory;
import ws.schild.jave.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoEncodingService {

    private Path BIN = Paths.get("ffmpeg/bin");

    public void encodeVideo(Path folder, String fileName) throws IOException {

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

        HashMap<String, Integer> metadata = getMetadata(source);
        originalWidth = metadata.get("originalWidth");
        originalHeight = metadata.get("originalHeight");

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

    private HashMap<String, Integer> getMetadata(File source) {
        HashMap<String, Integer> values = new HashMap<String, Integer>();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(source);

            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if (tag.getTagName().equals("Width")) {
                        values.put("originalWidth", Integer.parseInt(tag.getDescription().substring(0, tag.getDescription().indexOf(" "))));
                    }
                    if (tag.getTagName().equals("Height")) {
                        values.put("originalHeight", Integer.parseInt(tag.getDescription().substring(0, tag.getDescription().indexOf(" "))));
                    }
                }
            }
        } catch (ImageProcessingException | IOException e) {
            e.printStackTrace();
        }
        return values;
    }
}
