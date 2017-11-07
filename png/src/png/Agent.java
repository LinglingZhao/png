package png;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Agent {// 中介类，提供了几种png操作的方法

	private Png png;// png对象实例

	public Agent() {
		this.png = new Png();
	}

	// 设置想要转成png的目标文件
	public void setData(String pathName) {
		// 读取文件
		byte[] data = Utils.readFile(pathName);

		int size = data.length;// 文件字节大小
		// 图片的宽高
		int width = (int) Math.sqrt((size + 4) / 3) + 1;
		int high = width;
		// 处理过后的字节数组
		byte[] Data = new byte[3 * width * high];
		// 前4个字节，存储原文件的大小
		int tsize = size;
		for (int i = 3; i >= 0; i--) {
			Data[i] = (byte) (tsize & 0xff);
			tsize = tsize >> 8;
		}
		// 读取文件字节数组
		for (int i = 4; i < size + 4; i++) {
			Data[i] = data[i - 4];
		}
		// 填充 0xff 将Data数组填满
		for (int i = size + 4; i < Data.length; i++) {
			Data[i] = (byte) 0xff;
		}

		// 按png所需格式处理
		/*
		 * 0x00 RGB RGB RGB RGB .... 0x00 RGB RGB RGB RGB .... .... 每行以0x00开头
		 * 每个RGB三个字节大小
		 */
		ByteArrayOutputStream baos = new ByteArrayOutputStream(Data.length);
		for (int i = 0; i < high; i++) {
			baos.write(0x00);// 行首加 0x00
			for (int j = 0; j < width; j++) {
				// 每三个字节作为一个RGB
				baos.write(Data[i * width * 3 + j * 3 + 0]);
				baos.write(Data[i * width * 3 + j * 3 + 1]);
				baos.write(Data[i * width * 3 + j * 3 + 2]);
			}
		}
		// zlib压缩
		byte[] zlibData = Utils.zlib(baos.toByteArray());
		try {
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 设置IHDR数据块
		png.getIHDR().setWidth(width);
		png.getIHDR().setHigh(high);
		png.getIHDR().setChunkData();
		// 设置IDAT数据块
		png.getIDAT().setLength(zlibData.length);
		png.getIDAT().setChunkData(zlibData);
		System.out.println("转入png完成");
	}

	// 从png图片读取 文件
	public byte[] readPng(String pathName) {
		// 不是 .png后缀，就抛出异常
		if (!pathName.substring(pathName.length() - 3).equals("png")) {
			throw new IllegalArgumentException("该文件不是png图片");
		}

		byte[] data = Utils.readFile(pathName);

		// 读取图片 宽度
		int width = 0;
		for (int i = 16; i < 20; i++) {
			width = width << 8;
			width = width | (data[i] & 0xff);
		}
		// 读取图片 高度
		int high = 0;
		for (int i = 20; i < 24; i++) {
			high = high << 8;
			high = high | (data[i] & 0xff);
		}
		// 未解压的数据
		/*
		 * 因为IDAT的数据块数据 从41开始 到结尾-16个字节 41 = PNG_HEAD + IHDR + IDAT前两部分 8 + 25 + 8 25 =
		 * 4 + 4 + 13 + 4 length IHDR标识 IHDR数据 CRC校验码 16 = IEND + IDAT_CRC 12 + 4
		 */
		byte[] zlibData = new byte[data.length - 16 - 41];
		for (int i = 41; i < data.length - 16; i++) {
			zlibData[i - 41] = data[i];
		}

		// 解压后的数据
		byte[] unzlibData = Utils.unzlib(zlibData);

		ArrayList<Byte> tmp = new ArrayList<>();
		// 去除每行起始标识 0x00
		for (int i = 0; i < high; i++) {
			for (int j = 0; j < width * 3; j++) {
				tmp.add(unzlibData[i * (3 * width + 1) + j + 1]);
			}
		}

		int dataSize = 0;// 原数据的长度
		// 前四个字节为 原数据长度
		for (int i = 0; i < 4; i++) {
			dataSize = dataSize << 8;
			dataSize = dataSize | (tmp.get(i) & 0xff);
		}

		byte[] sourceData = new byte[dataSize];
		// 读取原数据
		for (int i = 4; i < dataSize + 4; i++) {
			sourceData[i - 4] = tmp.get(i);
		}
		System.out.println("读取png完成");
		return sourceData;
	}

	// 将setData的数据写为png图片
	public void writePng(String pathName) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			// 写入png格式头
			baos.write(Png.PNG_HEAD);
			// 写入IHDR数据块
			baos.write(png.getIHDR().getBytes());
			// 写入IDAT数据块
			baos.write(png.getIDAT().getBytes());
			// 写入png格式尾
			baos.write(Png.IEND);
			// 写入文件
			Utils.writeFile(pathName, baos.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {// 关闭资源
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("写入png完成");
	}

}
