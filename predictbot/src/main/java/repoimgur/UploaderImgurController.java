package repoimgur;

import java.io.File;

public class UploaderImgurController {

	public static void main(String[] args) throws Exception {

		String fileString = "";

		System.out.println("Start uploader Imgur controller");

		File file = new File("/home/g/git/ImageTgUtils/ImageTgUtils/1.jpg");

		fileString = ImgurUploader.toBase64(file);

		ImgurUploader.getImgurContent("16b4bf413fefd65", fileString);

	}

}
