package png;

public class ChunkIHDR extends Chunk {
	private int width = 0;
	private int high = 0;
	private byte[] chunkData = new byte[13];

	public ChunkIHDR() {
		// IHDR数据块数据 的长度，一般为13
		setLength(13);
		setChunkTypeCode("IHDR");
		for (int i = 0; i < 8; i++) {// 宽高，默认为0
			chunkData[i] = 0x00;
		}
		// 色深
		chunkData[8] = 0x08;// 真彩色图像
		// 颜色类型
		chunkData[9] = 0x02;// 真彩色图像
		// 以下值默认为 0
		chunkData[10] = 0x00;
		chunkData[11] = 0x00;
		chunkData[12] = 0x00;
	}

	//
	public void setChunkData() {
		if (width <= 0 || high <= 0) {
			throw new ExceptionInInitializerError("图片的宽高必须大于0");
		}
		int width = this.width;
		for (int i = 3; i >= 0; i--) {
			chunkData[i] = (byte) (width & 0xff);
			width = width >> 8;
		}
		int high = this.high;
		for (int i = 3; i >= 0; i--) {
			chunkData[i + 4] = (byte) (high & 0xff);
			high = high >> 8;
		}
		super.setChunkData(chunkData);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	public byte[] getChunkData() {
		return chunkData;
	}
}
