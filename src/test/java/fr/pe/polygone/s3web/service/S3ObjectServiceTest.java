package fr.pe.polygone.s3web.service;

import fr.pe.polygone.s3web.repository.S3ObjectRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
public class S3ObjectServiceTest {

    @InjectMocks
    private S3ObjectService s3ObjectService;

    @Mock
    private S3ObjectRepository s3ObjectRepository;

    @Test
    public void retrieveAllFilesFromS3BucketCallRepository() {
        String bucketName = "test-bucket";

        s3ObjectService.retrieveAllFilesFromS3Bucket(bucketName);

        Mockito.verify(s3ObjectRepository).getFilesNames(bucketName);
    }

    @Test
    public void saveFileIntoS3BucketSuccessfull() {
        String bucketName = "test-bucket";

        try {
            byte[] body = "body".getBytes();
            InputStream is = Mockito.mock(InputStream.class);
            Mockito.when(is.readAllBytes()).thenReturn(body);

            MultipartFile file = Mockito.mock(MultipartFile.class);
            Mockito.when(file.getOriginalFilename()).thenReturn("test-file.pdf");
            Mockito.when(file.getContentType()).thenReturn("PDF");
            Mockito.when(file.getInputStream()).thenReturn(is);

            s3ObjectService.saveFileIntoS3Bucket(file, bucketName);

            Mockito.verify(s3ObjectRepository).save(bucketName, "test-file.pdf", body, "PDF");

        } catch (IOException e) {
            fail("Unexpected failure");
        }
    }

}
