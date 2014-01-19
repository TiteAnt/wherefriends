package com.tite.system.comfc.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 该类定义了通用的文件夹操作类,包括文件下载方法, 压缩包的解压缩<br>
 * 文件内容的读取与写入等
 * 
 * @version 6.1.0 , 2010-10-26
 * @author jiaoyk
 * @since JDK1.6
 */
public class FileUtils {
	// 解压缩功能中使用到的字节数
	private static final int BUFFERSIZE = 1024;

	/**
	 * 根据HTTP访问路径下的文件下载至指定目录
	 * 
	 * @param downLoadURL
	 *            文件下载地址
	 * @param savePath
	 *            下载文件的保存地址
	 * @param fileName
	 *            下载后文件的名称, 如果该名称为null或空,则采用下载文件的名称
	 * @return 下载后的文件访问路径, 为空时表示下载失败
	 * */
	public static String downLoadByURL(String downLoadURL, String savePath,
			String fileName) {
		int byteread = 0;
		String strDownLoadPath = "";

		// 获取下载文件的名称
		String strFileName = "";
		// 若未指定下载文件的文件名,则采用被下载的文件名
		if (fileName != null && !"".equalsIgnoreCase(fileName.trim())) {
			strFileName = fileName;
		} else {
			String[] arrURL = downLoadURL.split("\\/");
			// http://10.5.45.5:7080/...
			// 分隔后,为["http:","","10.5.45.5:7080","..."],长度至少为4
			if (arrURL.length > 3) {
				strFileName = arrURL[arrURL.length - 1];
			} else {
				return "";
			}
		}

		// 检查文件保存路径是否存在,不存在则创建
		File downLoadPath = new File(savePath);
		if (!downLoadPath.exists()) {
			downLoadPath.mkdirs();
		}
		strDownLoadPath += savePath + File.separator + strFileName;

		URL url = null;

		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			url = new URL(downLoadURL);
			URLConnection conn = url.openConnection();
			inStream = conn.getInputStream();
			fs = new FileOutputStream(strDownLoadPath);

			byte[] buffer = new byte[1204];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}

			inStream.close();
			fs.close();

		} catch (IOException e) {
			strDownLoadPath = "";
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
				}
			}
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
				}
			}
		}

		return strDownLoadPath;
	}

	/**
	 * 将压缩包中的文件解压到指定目录下,当前只支持ZIP格式的压缩包
	 * 
	 * @param zipFilePath
	 *            压缩包文件磁盘访问路径
	 * @param unpackPath
	 *            解压的文件存放路径
	 * @return 所有解压文件的访问路径集合,该路径是压缩包内包含的路径,不是绝对路径
	 * */
	public static List<String> unpackZIPFile(String zipFilePath,
			String unpackPath) throws IOException {
		List<String> listFilePaths = new ArrayList<String>();

		InputStream is = null;
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		ZipFile zipFile = null;
		try {
			(new File(unpackPath)).mkdirs();
			File f = new File(zipFilePath);
			zipFile = new ZipFile(zipFilePath);
			if ((!f.exists()) && (f.length() <= 0)) {
				return listFilePaths;
			}
			String strPath = "";
			String gbkPath = "";
			String strtemp = "";
			String strTempPath = ""; // 记录压缩包中文件的相对路径
			File tempFile = new File(unpackPath);
			strPath = tempFile.getAbsolutePath();
			Enumeration<?> e = zipFile.getEntries();
			while (e.hasMoreElements()) {
				ZipEntry zipEnt = (ZipEntry) e.nextElement();
				gbkPath = zipEnt.getName();
				if (zipEnt.isDirectory()) {
					strtemp = strPath + File.separator + gbkPath;
					File dir = new File(strtemp);
					dir.mkdirs();
					continue;
				} else {
					// 读写文件
					is = zipFile.getInputStream(zipEnt);
					bis = new BufferedInputStream(is);
					strtemp = strPath + File.separator + gbkPath;
					strTempPath += gbkPath;

					// 建目录
					String strsubdir = gbkPath;
					for (int i = 0; i < strsubdir.length(); i++) {
						if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")
								|| strsubdir.substring(i, i + 1)
										.equalsIgnoreCase("\\")) {
							String temp = strPath + File.separator
									+ strsubdir.substring(0, i);
							File subdir = new File(temp);
							if (!subdir.exists()) {
								subdir.mkdir();
							}
						}
					}
					fos = new FileOutputStream(strtemp);
					bos = new BufferedOutputStream(fos);
					int c;
					while ((c = bis.read()) != -1) {
						bos.write((byte) c);
					}
					bos.close();
					fos.close();

					// 记录当前解压文件的相对访问路径
					listFilePaths.add(strTempPath);
					// 将临时记录相对路径的变量置空,用于记录下一个解压文件的相对路径
					strTempPath = "";
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (zipFile != null) {
				zipFile.close();
			}
			if (bos != null) {
				bos.close();
			}
			if (bis != null) {
				bis.close();
			}
			if ((fos != null)) {
				fos.close();
			}
			if (is != null) {
				is.close();
			}
		}

		return listFilePaths;
	}

	/**
	 * 将文件或文件夹进行压缩
	 * 
	 * @param inputFileName
	 *            被压缩的文件或文件夹磁盘访问路径
	 * @param zipFilename
	 *            压缩完成后文件的访问路径(包括压缩包的名称)
	 * @throws IOException
	 * */
	public static void zip(String inputFileName, String zipFilename)
			throws IOException {
		zip(new File(inputFileName), zipFilename);
	}

	/**
	 * 根据文件或文件夹对象执行压缩操作
	 * 
	 * @param inputFile
	 *            被压缩的文件或文件夹对象
	 * @param zipFilename
	 *            压缩完成后文件的访问路径(包括压缩包的名称)
	 * @throws IOException
	 * 
	 * */
	public static void zip(File inputFile, String zipFilename)
			throws IOException {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFilename));

		try {
			zip(inputFile, out, "");
		} catch (IOException e) {
			throw e;
		} finally {
			out.close();
		}
	}

	private static void zip(File inputFile, ZipOutputStream out, String base)
			throws IOException {
		out.setEncoding("GBK");
		if (inputFile.isDirectory()) {
			File[] inputFiles = inputFile.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < inputFiles.length; i++) {
				zip(inputFiles[i], out, base + inputFiles[i].getName());
			}

		} else {
			if (base.length() > 0) {
				out.putNextEntry(new ZipEntry(base));
			} else {
				out.putNextEntry(new ZipEntry(inputFile.getName()));
			}

			FileInputStream in = new FileInputStream(inputFile);
			try {
				int c;
				byte[] by = new byte[BUFFERSIZE];
				while ((c = in.read(by)) != -1) {
					out.write(by, 0, c);
				}
			} catch (IOException e) {
				throw e;
			} finally {
				in.close();
			}
		}
	}

	/**
	 * 循环删除文件夹
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
				file.delete();
			}
		}
	}
}
