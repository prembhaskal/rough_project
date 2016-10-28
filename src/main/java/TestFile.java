import java.io.File;

public class TestFile {


	public static void main(String[] args) {
		File file = new File("D:\\project\\own\\our_stuff\\backup.log*");

		System.out.println(file.exists());
	}
}
