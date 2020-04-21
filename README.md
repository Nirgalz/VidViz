# Video assets management tool

Developed specifically for a VFX artist to facilitate his workflow, but generalist enough to be used in various cases where one wants to visualize and control multiple videos at the same time, or simply better manage a video library.
As of now, it is primarily designed to be used as a local tool.

![alt text](https://nirgalz.github.io/assets/static/vidviz2.07cc2b7.aaf37fd635beb47678d1b0698144f74b.jpg "vidviz")

### features :
##### Videos :
* Control multiple videos at once : play/pause, video speed, auto-loop, show/hide controls.
* Select/unselect. 
    * Hide/view videos
    * Download videos associated json files. 
    * Delete videos and json files.
* Contextual menu to open file in system browser or download json.

##### Files :
* Upload files or use them directly using the file system. The app will update its content.
* Upload json files associated with each video to get the metadata of that video you selected.
* Automatic encoding of video thumbnails for better encoding experience.
* Search, filter, edit and delete your folders.

### possible evolutions
* Online version with security
* SAAS service
...


### Install
For windows :
[Download the latest release.](https://github.com/Nirgalz/VidViz/releases)

* run in cmd : 
```
<jar path>/java -jar backend-0.2.1-SNAPSHOT.jar
```
   
* Visit [http://localhost:8080/](http://localhost:8080/)

I haven't tried it yet on linux, there may be a few tweaks to do to get it to work.

### Dev environment

Clone the repo and run
```
mvn install
```

### Reminder 

Don't deploy it live, there is no security and anyone accessing the url would have total control over your files.
It is meant to be used on a local computer only for now.
