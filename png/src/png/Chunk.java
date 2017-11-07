package png;

public class Chunk {// 数据块
	private int length;// 数据块长度
	private String ChunkTypeCode;// 数据块类型码
	private byte[] ChunkData;// 数据块数据

	// 将数据块以 byte数组的形式返回
	public byte[] getBytes() {
		int length = this.length;
		byte[] bytes = new byte[ChunkData.length + 12];
		// 写入数据块长度
		for (int i = 3; i >= 0; i--) {
			bytes[i] = (byte) (length & 0xff);
			length = length >> 8;
		}
		// 写入数据块标识 （标识字符的ascii码值）
		for (int i = 0; i < 4; i++) {
			bytes[i + 4] = (byte) (this.ChunkTypeCode.charAt(i) & 0xff);
		}
		// 写入数据块数据
		for (int i = 0; i < ChunkData.length; i++) {
			bytes[i + 8] = this.ChunkData[i];
		}
		// 写入crc校验码
		int crc = new CRC().crc(bytes, 4, ChunkData.length + 4);
		for (int i = bytes.length - 1; i >= bytes.length - 4; i--) {// crc校验码
			bytes[i] = (byte) (crc & 0xff);
			crc = crc >> 8;
		}
		return bytes;
	}

	// set 和 get 方法
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getChunkTypeCode() {
		return ChunkTypeCode;
	}

	public void setChunkTypeCode(String chunkTypeCode) {
		ChunkTypeCode = chunkTypeCode;
	}

	public byte[] getChunkData() {
		return ChunkData;
	}

	public void setChunkData(byte[] chunkData) {
		ChunkData = chunkData;
	}

}
