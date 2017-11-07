package png;

public class Test {
	public static void main(String[] args) {
		Agent agent = new Agent();
		// 设置1.mp3
		agent.setData("1.mp3");
		// 写入1.png
		agent.writePng("1.png");
		// 从1.png 读取到 2.mp3
		Utils.writeFile("2.mp3", agent.readPng("1.png"));
	}
}
