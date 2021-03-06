/*
+--------------------------------------------------------------------------
|   
|   ========================================
|    
|   
|
+---------------------------------------------------------------------------
*/
package uzblog.base.upload.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import uzblog.base.context.AppContext;
import uzblog.base.lang.BlogException;
import uzblog.base.upload.FileRepo;
import uzblog.base.utils.FileNameUtils;
import uzblog.base.utils.ImageUtils;

/**
 * 
 */
public abstract class AbstractFileRepo implements FileRepo {
	private static Logger log = LoggerFactory.getLogger(AbstractFileRepo.class);

	@Autowired
	protected AppContext appContext;

	// 文件允许格式
	protected String[] allowFiles = { ".gif", ".png", ".jpg", ".jpeg", ".bmp" };

	protected void validateFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new BlogException("文件不能为空");
		}

		if (!checkFileType(file.getOriginalFilename())) {
			throw new BlogException("文件格式不支持");
		}
	}

	/**
	 * 文件类型判断
	 *
	 * @param fileName
	 * @return
	 */
	protected boolean checkFileType(String fileName) {
		Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
		while (type.hasNext()) {
			String ext = type.next();
			if (fileName.toLowerCase().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	protected String getExt(String filename) {
		int pos = filename.lastIndexOf(".");
		return filename.substring(pos + 1);
	}

	protected void checkDirAndCreate(File file) {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
	}

	@Override
	public String store(MultipartFile file, String basePath) throws IOException {
		validateFile(file);

		String path = basePath + FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));
		File temp = new File(getRoot() + path);
		checkDirAndCreate(temp);
		file.transferTo(temp);
		return path;
	}

	@Override
	public String storeScale(MultipartFile file, String basePath, int maxWidth) throws Exception {
		validateFile(file);

		String path = basePath + FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));
		// 根据临时文件生成略缩图
		String dest = getRoot() + path;
		ImageUtils.scaleImageByWidth(file, dest, maxWidth);
		return path;
	}

	@Override
	public String storeScale(MultipartFile file, String basePath, int width, int height) throws Exception {
		validateFile(file);

		String path = basePath + FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));

		// 根据临时文件生成略缩图
		String dest = getRoot() + path;
		ImageUtils.scale(file, dest, width, height);
		return path;
	}

	@Override
	public int[] imageSize(String storePath) {
		String root = getRoot();

		File dest = new File(root + storePath);
		int[] ret = new int[2];

		try {
			// 读入文件
			BufferedImage src = ImageIO.read(dest);
			int w = src.getWidth();
			int h = src.getHeight();

			ret = new int[] { w, h };

		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	@Override
	public void deleteFile(String storePath) {
		File file = new File(getRoot() + storePath);

		// 文件存在, 且不是目录
		if (file.exists() && !file.isDirectory()) {
			file.delete();
			log.info("fileRepo delete " + storePath);
		}
	}

	@Override
	public String store(URL url, String basePath) throws IOException {
		String path = basePath + FileNameUtils.genPathAndFileName("png");
		File temp = new File(getRoot() + path);
		checkDirAndCreate(temp);
		FileUtils.copyURLToFile(url, temp);
		return path;
	}

	@Override
	public String storeScale(URL url, String basePath, int maxWidth) throws Exception {
		String path = basePath + FileNameUtils.genPathAndFileName("png");
		ImageUtils.scaleImageByWidth(url, getRoot() + path, maxWidth);
		return path;
	}

	@Override
	public String storeScale(URL url, String basePath, int width, int height) throws Exception {
		String path = basePath + FileNameUtils.genPathAndFileName("png");
		// 根据临时文件生成略缩图
		String dest = getRoot() + path;
		ImageUtils.scale(url, dest, width, height);
		return path;
	}
}
