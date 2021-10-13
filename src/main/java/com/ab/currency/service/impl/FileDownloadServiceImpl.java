package com.ab.currency.service.impl;

import com.ab.currency.service.FileDownloadService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class FileDownloadServiceImpl implements FileDownloadService {

    @Override
    public byte[] download(String imageUrl) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        URL url = new URL(imageUrl);
        try (InputStream is = url.openStream()) {
            byte[] bytebuffer = new byte[4096];
            int n;
            while ((n = is.read(bytebuffer)) > 0) {
                bos.write(bytebuffer, 0, n);
            }
        }
        return bos.toByteArray();
    }
}
