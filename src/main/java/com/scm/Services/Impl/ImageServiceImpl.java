package com.scm.Services.Impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.Helper.AppConstants;
import com.scm.Services.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageServiceImpl  implements ImageService{


    private Cloudinary cloudinary;

   public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }



    @Override
    public String uploadImage(MultipartFile contactImage, String filename) {

      
       

        try {
            byte[] data  = new byte[contactImage.getInputStream().available()];
         
          contactImage.getInputStream().read(data);

          cloudinary.uploader().upload(data, ObjectUtils.asMap(
            "public_id", filename));


            log.info("Image uploaded successfully. Public ID: {}", filename);

            return this.getUrlFromPublicId(filename);

        } catch (IOException e) {
            log.error("Image upload failed: {}", e.getMessage());
            throw new RuntimeException("Image upload failed", e);

           // return null;
        }
   
    }

    @Override
    public String getUrlFromPublicId(String publicId) {
       
        return cloudinary
        .url()
        .transformation(
            new Transformation<>()
            .width(AppConstants.CONTACT_IMAGE_WIDTH)
            .height(AppConstants.CONTACT_IMAGE_HEIGHT)
            .crop(AppConstants.CONTACT_IMAGE_CROP))
        
        .generate(publicId);
    }

}
