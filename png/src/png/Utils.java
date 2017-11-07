package png;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Utils {

	public static byte[] zlib(byte[] data) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
		Deflater compresser = new Deflater();
		compresser.reset();
		compresser.setInput(data);
		compresser.finish();
		byte[] buffer = new byte[1024 * 1024];
		while (!compresser.finished()) {
			int len = compresser.deflate(buffer);
			baos.write(buffer, 0, len);
		}
		byte[] zlibData = baos.toByteArray();

		try {
			compresser.end();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return zlibData;
	}

	public static byte[] unzlib(byte[] zlibData) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(zlibData.length);
		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(zlibData);
		byte[] buffer = new byte[1024 * 1024];
		while (!decompresser.finished()) {
			try {
				int len = decompresser.inflate(buffer);
				baos.write(buffer, 0, len);
			} catch (DataFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		byte[] unzlibData = baos.toByteArray();

		try {
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return unzlibData;
	}

	public static void writeFile(String pathName, byte[] data) {
		File file = new File(pathName);
		OutputStream os = null;
		BufferedOutputStream bos = null;
		try {
			// 获取输出字节流
			os = new FileOutputStream(file);
			// 将字节流包装成，带缓冲区的字节流
			bos = new BufferedOutputStream(os);
			// 写入数据
			bos.write(data);
			// 刷新缓冲
			bos.flush();

			// 关闭资源
			if (bos != null)
				bos.close();
			if (os != null)
				os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static byte[] readFile(String pathName) {
		File file = new File(pathName);
		InputStream is = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		byte[] data = null;
		try {
			// 获取输入流
			is = new FileInputStream(file);
			// 将输入流包装成，带缓冲的输入流
			bis = new BufferedInputStream(is);
			baos = new ByteArrayOutputStream();
			// 缓冲数组
			byte[] buffer = new byte[1024 * 1024];// 大小为 1M
			// 读入数据
			while (bis.available() > 0) {
				int len = bis.read(buffer);
				baos.write(buffer, 0, len);
			}
			data = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {// 关闭资源
			try {
				if (baos != null)
					baos.close();
				if (bis != null)
					bis.close();
				if (is != null)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}
}
