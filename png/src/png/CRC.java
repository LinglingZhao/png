package png;

//参考官网 ：https://www.w3.org/TR/PNG-CRCAppendix.html
public class CRC {
	public static int[] crc_table = new int[256];
	private boolean has_crc_table = false;// 含有crc表

	private void make_crc_table() {
		long c;
		for (int i = 0; i < 256; i++) {
			c = i;
			for (int k = 0; k < 8; k++) {
				if ((c & 1) != 0) {
					c = 0xedb88320L ^ (c >> 1);
				} else {
					c = c >> 1;
				}
			}
			crc_table[i] = (int) c;
		}
		has_crc_table = true;
	}

	private int update_crc(int crc, byte[] buf, int off, int len) {
		int c = crc;
		for (int i = off; i < len + off; i++) {
			c = crc_table[(int) ((c ^ (long) buf[i]) & 0xff)] ^ ((c >> 8) & 0xffffff);
		}
		return c;
	}

	public int crc(byte[] buf, int offset, int length) {
		if (!this.has_crc_table) {// 没有crc表，则创建一个
			make_crc_table();
		}
		return update_crc(0xffffffff, buf, offset, length) ^ 0xffffffff;
	}

}
