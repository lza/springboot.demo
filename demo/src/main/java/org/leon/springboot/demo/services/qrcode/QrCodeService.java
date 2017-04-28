package org.leon.springboot.demo.services.qrcode;

import com.google.zxing.WriterException;

import java.io.IOException;

/**
 * Created by fran on 2016/7/22.
 */
public interface QrCodeService {

    public QrStream createQrInputStream(String content, String format, int width, int height) throws IOException, WriterException;

    public QrStream createQrInputStreamBase(String content) throws IOException, WriterException;
}
