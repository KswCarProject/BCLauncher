package org.apache.harmony.awt.datatransfer;

import java.awt.Image;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import org.apache.harmony.awt.internal.nls.Messages;

public final class DataProxy implements Transferable {
    public static final Class[] charsetTextClasses = {byte[].class, ByteBuffer.class, InputStream.class};
    public static final Class[] unicodeTextClasses = {String.class, Reader.class, CharBuffer.class, char[].class};
    private final DataProvider data;
    private final SystemFlavorMap flavorMap = SystemFlavorMap.getDefaultFlavorMap();

    public DataProxy(DataProvider data2) {
        this.data = data2;
    }

    public DataProvider getDataProvider() {
        return this.data;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        String mimeType = String.valueOf(flavor.getPrimaryType()) + "/" + flavor.getSubType();
        if (flavor.isFlavorTextType()) {
            if (mimeType.equalsIgnoreCase("text/html")) {
                return getHTML(flavor);
            }
            if (mimeType.equalsIgnoreCase(DataProvider.TYPE_URILIST)) {
                return getURL(flavor);
            }
            return getPlainText(flavor);
        } else if (flavor.isFlavorJavaFileListType()) {
            return getFileList(flavor);
        } else {
            if (flavor.isFlavorSerializedObjectType()) {
                return getSerializedObject(flavor);
            }
            if (flavor.equals(DataProvider.urlFlavor)) {
                return getURL(flavor);
            }
            if (mimeType.equalsIgnoreCase("image/x-java-image") && Image.class.isAssignableFrom(flavor.getRepresentationClass())) {
                return getImage(flavor);
            }
            throw new UnsupportedFlavorException(flavor);
        }
    }

    public DataFlavor[] getTransferDataFlavors() {
        ArrayList<DataFlavor> result = new ArrayList<>();
        String[] natives = this.data.getNativeFormats();
        for (String flavorsForNative : natives) {
            for (DataFlavor f : this.flavorMap.getFlavorsForNative(flavorsForNative)) {
                if (!result.contains(f)) {
                    result.add(f);
                }
            }
        }
        return (DataFlavor[]) result.toArray(new DataFlavor[result.size()]);
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        DataFlavor[] flavors = getTransferDataFlavors();
        for (DataFlavor equals : flavors) {
            if (equals.equals(flavor)) {
                return true;
            }
        }
        return false;
    }

    private Object getPlainText(DataFlavor f) throws IOException, UnsupportedFlavorException {
        if (!this.data.isNativeFormatAvailable("text/plain")) {
            throw new UnsupportedFlavorException(f);
        }
        String str = this.data.getText();
        if (str != null) {
            return getTextRepresentation(str, f);
        }
        throw new IOException(Messages.getString("awt.4F"));
    }

    private Object getFileList(DataFlavor f) throws IOException, UnsupportedFlavorException {
        if (!this.data.isNativeFormatAvailable("application/x-java-file-list")) {
            throw new UnsupportedFlavorException(f);
        }
        String[] files = this.data.getFileList();
        if (files != null) {
            return Arrays.asList(files);
        }
        throw new IOException(Messages.getString("awt.4F"));
    }

    private Object getHTML(DataFlavor f) throws IOException, UnsupportedFlavorException {
        if (!this.data.isNativeFormatAvailable("text/html")) {
            throw new UnsupportedFlavorException(f);
        }
        String str = this.data.getHTML();
        if (str != null) {
            return getTextRepresentation(str, f);
        }
        throw new IOException(Messages.getString("awt.4F"));
    }

    private Object getURL(DataFlavor f) throws IOException, UnsupportedFlavorException {
        if (!this.data.isNativeFormatAvailable("application/x-java-url")) {
            throw new UnsupportedFlavorException(f);
        }
        String str = this.data.getURL();
        if (str == null) {
            throw new IOException(Messages.getString("awt.4F"));
        }
        URL url = new URL(str);
        if (f.getRepresentationClass().isAssignableFrom(URL.class)) {
            return url;
        }
        if (f.isFlavorTextType()) {
            return getTextRepresentation(url.toString(), f);
        }
        throw new UnsupportedFlavorException(f);
    }

    private Object getSerializedObject(DataFlavor f) throws IOException, UnsupportedFlavorException {
        String nativeFormat = SystemFlavorMap.encodeDataFlavor(f);
        if (nativeFormat == null || !this.data.isNativeFormatAvailable(nativeFormat)) {
            throw new UnsupportedFlavorException(f);
        }
        byte[] bytes = this.data.getSerializedObject(f.getRepresentationClass());
        if (bytes == null) {
            throw new IOException(Messages.getString("awt.4F"));
        }
        try {
            return new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (ClassNotFoundException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    private String getCharset(DataFlavor f) {
        return f.getParameter("charset");
    }

    private Object getTextRepresentation(String text, DataFlavor f) throws UnsupportedFlavorException, IOException {
        if (f.getRepresentationClass() == String.class) {
            return text;
        }
        if (f.isRepresentationClassReader()) {
            return new StringReader(text);
        }
        if (f.isRepresentationClassCharBuffer()) {
            return CharBuffer.wrap(text);
        }
        if (f.getRepresentationClass() == char[].class) {
            char[] chars = new char[text.length()];
            text.getChars(0, text.length(), chars, 0);
            return chars;
        }
        String charset = getCharset(f);
        if (f.getRepresentationClass() == byte[].class) {
            return text.getBytes(charset);
        }
        if (f.isRepresentationClassByteBuffer()) {
            return ByteBuffer.wrap(text.getBytes(charset));
        }
        if (f.isRepresentationClassInputStream()) {
            return new ByteArrayInputStream(text.getBytes(charset));
        }
        throw new UnsupportedFlavorException(f);
    }

    private Image getImage(DataFlavor f) throws IOException, UnsupportedFlavorException {
        if (!this.data.isNativeFormatAvailable("image/x-java-image")) {
            throw new UnsupportedFlavorException(f);
        }
        RawBitmap bitmap = this.data.getRawBitmap();
        if (bitmap != null) {
            return createBufferedImage(bitmap);
        }
        throw new IOException(Messages.getString("awt.4F"));
    }

    private boolean isRGB(RawBitmap b) {
        return b.rMask == 16711680 && b.gMask == 65280 && b.bMask == 255;
    }

    private boolean isBGR(RawBitmap b) {
        return b.rMask == 255 && b.gMask == 65280 && b.bMask == 16711680;
    }

    private BufferedImage createBufferedImage(RawBitmap b) {
        int[] offsets;
        if (b == null || b.buffer == null || b.width <= 0 || b.height <= 0) {
            return null;
        }
        ColorModel cm = null;
        WritableRaster wr = null;
        if (b.bits != 32 || !(b.buffer instanceof int[])) {
            if (b.bits == 24 && (b.buffer instanceof byte[])) {
                int[] bits = {8, 8, 8};
                if (isRGB(b)) {
                    offsets = new int[3];
                    offsets[1] = 1;
                    offsets[2] = 2;
                } else if (!isBGR(b)) {
                    return null;
                } else {
                    offsets = new int[3];
                    offsets[0] = 2;
                    offsets[1] = 1;
                }
                byte[] buffer = (byte[]) b.buffer;
                cm = new ComponentColorModel(ColorSpace.getInstance(1000), bits, false, false, 1, 0);
                wr = Raster.createInterleavedRaster(new DataBufferByte(buffer, buffer.length), b.width, b.height, b.stride, 3, offsets, (Point) null);
            } else if ((b.bits == 16 || b.bits == 15) && (b.buffer instanceof short[])) {
                int[] masks = {b.rMask, b.gMask, b.bMask};
                short[] buffer2 = (short[]) b.buffer;
                cm = new DirectColorModel(b.bits, b.rMask, b.gMask, b.bMask);
                wr = Raster.createPackedRaster(new DataBufferUShort(buffer2, buffer2.length), b.width, b.height, b.stride, masks, (Point) null);
            }
        } else if (!isRGB(b) && !isBGR(b)) {
            return null;
        } else {
            int[] masks2 = {b.rMask, b.gMask, b.bMask};
            int[] buffer3 = (int[]) b.buffer;
            ColorModel directColorModel = new DirectColorModel(24, b.rMask, b.gMask, b.bMask);
            wr = Raster.createPackedRaster(new DataBufferInt(buffer3, buffer3.length), b.width, b.height, b.stride, masks2, (Point) null);
            cm = directColorModel;
        }
        if (cm == null || wr == null) {
            return null;
        }
        return new BufferedImage(cm, wr, false, (Hashtable) null);
    }
}
