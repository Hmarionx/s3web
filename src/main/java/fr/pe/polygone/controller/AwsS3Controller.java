package fr.pe.polygone.controller;

import fr.pe.polygone.service.AwsS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class AwsS3Controller {
    @Value("${aws.bucketName}")
    private String awsABucketName;

    @Autowired
    private AwsS3Service awsS3Service;

    @GetMapping("/")
    public String redirectHome() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getHomePage(Model model) {
        List<String> allFileNameInBucket = awsS3Service.getAllFileNameInBucket(awsABucketName);
        model.addAttribute("s3FileNames", allFileNameInBucket);
        return "home";
    }

    @PostMapping("/upload")
    public String uploadToS3(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("tmp", "tmp");
        multipartFile.transferTo(file);
        awsS3Service.upload(file, multipartFile.getOriginalFilename(), awsABucketName);
        return "redirect:/home";
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("filename") String fileName) {
        ByteArrayOutputStream byteArrayOutputStream = awsS3Service.downloadFile(fileName, awsABucketName);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(byteArrayOutputStream.toByteArray());
    }

    @GetMapping("/delete/{filename}")
    public String deleteFile(@PathVariable("filename") String fileName) {
        awsS3Service.deleteFromBucket(awsABucketName, fileName);
        return "redirect:/home";
    }

}