package az.code.auctionbackend.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.uploadcare.api.Client;
import com.uploadcare.api.File;
import com.uploadcare.upload.FileUploader;
import com.uploadcare.upload.UploadFailureException;
import com.uploadcare.upload.Uploader;

@Service
@Log4j2
public class UploadcareService {

    @Value("${uploadcare.publicKey}")
    private String publicKey;
    @Value("${uploadcare.secretKey}")
    private String secretKey;
    private Client client;

    @PostConstruct
    private void init() {
        client = new Client(publicKey, secretKey);
        log.info("UploadCare public key is " + client.getPublicKey());
    }

    public String sendFile(java.io.File file) {

        String uploadedFileId = null;

        try {

            Uploader uploader = new FileUploader(client, file);
            File uploadedFile = uploader.upload();
            uploadedFileId = uploadedFile.getFileId();

        } catch (UploadFailureException e){
            log.warn(e);
        }
        return uploadedFileId;
    }
}
