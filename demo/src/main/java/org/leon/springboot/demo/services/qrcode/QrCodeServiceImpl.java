package org.leon.springboot.demo.services.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by fran on 2016/7/22.
 */
@Service
public class QrCodeServiceImpl implements QrCodeService {

    private static Logger log = LoggerFactory.getLogger(QrCodeServiceImpl.class);

    private static HashMap<EncodeHintType, String> hints = new HashMap<>();
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final String format = "png";
    private static final int width = 300;
    private static final int height = 300;

    static{
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
    }

    @Override
    public QrStream createQrInputStream(String content, String format, int width, int height) throws IOException, WriterException {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            return writeToInputStream(bitMatrix, format);
        } catch (Exception e) {
            log.error("createQrInputStream error! content["+ content +"]  format["+ format +"]", e);
            throw e;
        }
    }



    @Override
    public QrStream createQrInputStreamBase(String content) throws IOException, WriterException {
        return createQrInputStream(content, format, width, height);
    }

    public static QrStream writeToInputStream(BitMatrix matrix, String format) throws IOException {
        BufferedImage image = null;
        ByteArrayOutputStream bs = null;
        ImageOutputStream imOut = null;
        try{
            image = toBufferedImage(matrix);
            bs =new ByteArrayOutputStream();
            imOut =ImageIO.createImageOutputStream(bs);
            ImageIO.write(image, format ,imOut);
        }catch(Exception e){
            throw e;
        }finally{
            bs.close();
            imOut.close();
        }

        InputStream is =new ByteArrayInputStream(bs.toByteArray());
        QrStream stream = new QrStream(is, bs.size());
        return stream;
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
}
