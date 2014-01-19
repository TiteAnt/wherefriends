package com.tite.system.comfc.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.tite.system.comfc.utility.FileUtils;

public class FileUtilsTest {

	@Test
	public void testDownLoadByURL() {
		//从百度空间上下载一张图片
		String url = "http://a.hiphotos.baidu.com/album/w=575/sign=610703f0d009b3deebbfe46ff9be6cd3/738b4710b912c8fcf4875255fd039245d6882135.jpg?psign=f4875255fd039245d688d43f8794a4c27c1ed21b0ef4050d";
		String dirPath = System.getProperty("user.dir").concat(File.separator).concat("src\\test\\resources\\zipfiles");
		String fileName = "Jordan.jpg";
		
		String filePath = FileUtils.downLoadByURL(url, dirPath, fileName);
		assertNotNull(filePath);
		assertFalse("".equalsIgnoreCase(filePath.trim()));
		File file = new File(filePath);
		assertTrue(file.exists());
		assertEquals(file.getAbsolutePath(), dirPath + File.separator + fileName);
		//删除测试数据
		assertTrue(file.delete());
	}

	@Test
	public void testUnpackZIPFile() {
		String dirPath = System.getProperty("user.dir").concat(File.separator).concat("src\\test\\resources\\unpackfiles");
		List<String> filePaths = null;
		try {
			filePaths = FileUtils.unpackZIPFile(dirPath + File.separator + "解压.zip", dirPath);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(filePaths);
		assertEquals(2, filePaths.size());
		//删除解压出来的测试数据
		File file = new File(dirPath + File.separator + filePaths.get(0));
		assertTrue(file.delete());
		file = new File(dirPath + File.separator + filePaths.get(1));
		assertTrue(file.delete());
	}

	@Test
	public void testZipStringString() {
		String dirPath = System.getProperty("user.dir").concat(File.separator).concat("src\\test\\resources");
		String zipFileName = "unpackfiles" + File.separator +"测试用例数据.zip";
		//压缩文件
		try {
			FileUtils.zip(dirPath + File.separator + "zipfiles", dirPath + File.separator + zipFileName);
			File zipFile = new File(dirPath + File.separator + zipFileName);
			assertTrue(zipFile.exists());
			//删除测试数据
			zipFile.delete();
			
			//测试将单个文件压缩
			FileUtils.zip(dirPath + File.separator + "zipfiles" + File.separator + "测试文件.xlsx", dirPath + File.separator + zipFileName);
			zipFile = new File(dirPath + File.separator + zipFileName);
			assertTrue(zipFile.exists());
			//删除测试数据
			zipFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testZipFileString() {
		String dirPath = System.getProperty("user.dir").concat(File.separator).concat("src\\test\\resources");
		String zipFileName = "unpackfiles" + File.separator +"测试用例数据.zip";
		//压缩文件
		try {
			FileUtils.zip(new File(dirPath + File.separator + "zipfiles"), dirPath + File.separator + zipFileName);
			File zipFile = new File(dirPath + File.separator + zipFileName);
			assertTrue(zipFile.exists());
			//删除测试数据
			zipFile.delete();
			
			//测试将单个文件压缩
			FileUtils.zip(new File(dirPath + File.separator + "zipfiles" + File.separator + "测试文件.xlsx"), dirPath + File.separator + zipFileName);
			zipFile = new File(dirPath + File.separator + zipFileName);
			assertTrue(zipFile.exists());
			//删除测试数据
			zipFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
}
