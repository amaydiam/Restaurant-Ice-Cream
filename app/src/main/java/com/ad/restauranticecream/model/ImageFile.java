package com.ad.restauranticecream.model;

import java.io.File;

/**
 * Created by Amay on 7/11/2017.
 */

public class ImageFile {
    File imageFile;

    public ImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public File getImageFile() {
        return imageFile;
    }

}
