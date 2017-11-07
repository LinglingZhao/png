package png;

public class Png {
	// png 开头标识
	public final static byte[] PNG_HEAD = { (byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a };
	// png 结尾标识
	public final static byte[] IEND = { 0x00, 0x00, 0x00, 0x00, 0x49, 0x45, 0x4e, 0x44, (byte) 0xae, 0x42, 0x60,
			(byte) 0x82 };

	private ChunkIHDR IHDR;
	private ChunkIDAT IDAT;

	public Png() {
		this.IHDR = new ChunkIHDR();
		this.IDAT = new ChunkIDAT();
	}

	public ChunkIHDR getIHDR() {
		return IHDR;
	}

	public void setIHDR(ChunkIHDR iHDR) {
		IHDR = iHDR;
	}

	public ChunkIDAT getIDAT() {
		return IDAT;
	}

	public void setIDAT(ChunkIDAT iDAT) {
		IDAT = iDAT;
	}

}
