package test;

import java.io.File;


public class DeleteFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("/Users/qiaolong/test/文档1");
		file.delete();
	}

}
