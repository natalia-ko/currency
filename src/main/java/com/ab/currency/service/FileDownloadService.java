package com.ab.currency.service;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FileDownloadService {

    byte[] download(String url) throws IOException;
}
