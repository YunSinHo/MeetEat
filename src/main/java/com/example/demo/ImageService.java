package com.example.demo;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public class ImageService {
    
     // 이미지 삭제 메소드
     public void deleteImage(String filePath) {
        try {
            File file = new File("src/main/resources/static" + filePath);
            if (file.exists()) {
                if (file.isFile()) {
                    if (file.delete()) {
                        System.out.println("File deleted successfully: " + filePath);
                    } else {
                        throw new IOException("Failed to delete file: " + filePath);
                    }
                } else {
                    System.out.println("Not a file: " + filePath);
                }
            } else {
                System.out.println("File not found: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
