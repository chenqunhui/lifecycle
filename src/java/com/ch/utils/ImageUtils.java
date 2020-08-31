/** 
 * Project Name:advertise-common <br>
 * File Name:ImageUtils.java <br>
 * Copyright (c) 2017, babytree-inc.com All Rights Reserved. 
 */
package com.ch.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @date 2017年8月14日 下午1:59:33
 */
public class ImageUtils {
	public static ImageInfo getImageInfo(InputStream input){
		ImageInfo info = new ImageInfo();
		try {
			process(input, info);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}
	
	private static void process(InputStream is, ImageInfo info) throws IOException {
		processStream(is, info);
	}

	private static void processStream(InputStream is, ImageInfo info) throws IOException {
		int c1 = is.read();
		int c2 = is.read();
		int c3 = is.read();

		if (c1 == 'G' && c2 == 'I' && c3 == 'F') { // GIF
			is.skip(3);
			info.setWidth(readInt(is, 2, false));
			info.setHeight(readInt(is, 2, false));
			info.setFormat("gif");
		} else if (c1 == 0xFF && c2 == 0xD8) { // JPG
			while (c3 == 255) {
				int marker = is.read();
				int len = readInt(is,2,true);
				if (marker == 192 || marker == 193 || marker == 194) {
					is.skip(1);
					info.setHeight(readInt(is, 2, true));
					info.setWidth(readInt(is, 2, true));
					info.setFormat("jpeg");
					break;
				}
				is.skip(len - 2);
				c3 = is.read();
			}
		} else if (c1 == 137 && c2 == 80 && c3 == 78) { // PNG
			is.skip(15);
			info.setWidth(readInt(is, 2, true));
			is.skip(2);
			info.setHeight(readInt(is, 2, true));
			info.setFormat("png");
		} else if (c1 == 66 && c2 == 77) { // BMP
			is.skip(15);
			info.setWidth(readInt(is, 2, false));
			is.skip(2);
			info.setHeight(readInt(is, 2, false));
			info.setFormat("bmp");
		} else {
			int c4 = is.read();
			if ((c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42)
					|| (c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0)) { //TIFF
				boolean bigEndian = c1 == 'M';
				int ifd = 0;
				int entries;
				ifd = readInt(is,4,bigEndian);
				is.skip(ifd - 8);
				entries = readInt(is,2,bigEndian);
				for (int i = 1; i <= entries; i++) {
					int tag = readInt(is,2,bigEndian);
					int fieldType = readInt(is,2,bigEndian);
					long count = readInt(is,4,bigEndian);
					int valOffset;
					if ((fieldType == 3 || fieldType == 8)) {
						valOffset = readInt(is,2,bigEndian);
						is.skip(2);
					} else {
						valOffset = readInt(is,4,bigEndian);
					}
					if (tag == 256) {
						info.setWidth(valOffset);
					} else if (tag == 257) {
						info.setHeight(valOffset);
					}
					if (info.getWidth() != 0 && info.getHeight() != 0) {
						info.setFormat("tiff");
						break;
					}
				}
			}
		}
		if (info.getFormat() == null) {
			throw new IOException("Unsupported image type");
		}
	}
	
	private static int readInt(InputStream is, int noOfBytes, boolean bigEndian) throws IOException {
		int ret = 0;
		int sv = bigEndian ? ((noOfBytes - 1) * 8) : 0;
		int cnt = bigEndian ? -8 : 8;
		for(int i=0;i<noOfBytes;i++) {
			ret |= is.read() << sv;
			sv += cnt;
		}
		return ret;
	}

	public static  void main(String[] args) throws FileNotFoundException{
		ImageInfo i = getImageInfo(new FileInputStream(new File("C:\\Users\\wayne\\Pictures\\sss.gif")));
		System.out.println(i.getFormat());
		System.out.println(i.getHeight());
		System.out.println(i.getWidth());
	}

	public static class ImageInfo{
		private int width;
		
		private int height;
		
		private String format;

		public ImageInfo() {
			super();
		}

		public ImageInfo(int width, int height, String format) {
			super();
			this.width = width;
			this.height = height;
			this.format = format;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}
	}
}
