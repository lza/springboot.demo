package org.leon.springboot.demo.services.qrcode;

import java.io.InputStream;

/**
 * Created by fran on 2016/7/22.
 */
public class QrStream {

    private InputStream inputStream;
    private long imageLength;

    public QrStream(InputStream inputStream, long imageLength) {
        this.inputStream = inputStream;
        this.imageLength = imageLength;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public long getImageLength() {
        return imageLength;
    }

    public void setImageLength(long imageLength) {
        this.imageLength = imageLength;
    }
}
