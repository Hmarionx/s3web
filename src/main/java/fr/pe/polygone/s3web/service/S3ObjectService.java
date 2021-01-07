package fr.pe.polygone.s3web.service;

import fr.pe.polygone.s3web.repository.S3ObjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * S3 service
 */
@Service
@Slf4j
public class S3ObjectService {

    @Autowired
    private S3ObjectRepository s3ObjectRepository;

    /**
     * Retrieve all file from S3 bucket
     *
     * @param bucketName
     */
    public List<String> retrieveAllFilesFromS3Bucket(String bucketName) {
        log.info("Retrieving all files in bucket : " + bucketName);
        return s3ObjectRepository.getFilesNames(bucketName);
    }

    /**
     * Allows to register new file into a specified S3 bucket
     *
     * @param file
     * @param bucketName
     * @throws IOException
     */
    public void saveFileIntoS3Bucket(MultipartFile file, String bucketName) throws IOException {
        log.info("Saving file : " + file.getName());

        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        InputStream is = file.getInputStream();
        s3ObjectRepository.save(bucketName, fileName, is.readAllBytes(), fileType);
    }

}
