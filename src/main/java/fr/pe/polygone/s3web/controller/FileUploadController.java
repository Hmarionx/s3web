package fr.pe.polygone.s3web.controller;

import fr.pe.polygone.s3web.service.S3ObjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class FileUploadController {

    @Autowired
    private S3ObjectService s3ObjectService;

    @Value("${aws.s3-bucket.name}")
    private String bucketName;

    /**
     * Hompe page endpoint
     *
     * @param model
     * @return
     */
    @GetMapping("/bucket/home")
    public String homePage(Model model) {
        List<String> files = s3ObjectService.retrieveAllFilesFromS3Bucket(bucketName);
        model.addAttribute("files", files);
        return "home";
    }

    /**
     * Upload page Endpoint
     *
     * @param model
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/bucket/upload", method = RequestMethod.POST)
    public String uploadFile(Model model, @RequestParam("file") MultipartFile file) throws IOException {

        log.info("uploading the file : " + file.getName());
        if (!file.isEmpty()) {
            s3ObjectService.saveFileIntoS3Bucket(file, bucketName);
        }
        return "redirect:/bucket/home";
    }

}
