package com.example.webbongden.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
    private static final Cloudinary cloudinary;

    static {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dptmqc8lj",
                "api_key", "499861643587286",
                "api_secret", "MUbGOvtdEPIq07Wq9GM2cAf1lcI"
        ));
    }

    public static Cloudinary getCloudinary() {
        return cloudinary;
    }
}
