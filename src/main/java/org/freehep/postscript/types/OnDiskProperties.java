// Copyright FreeHEP 2009
package org.freehep.postscript.types;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.AccessControlException;

/**
 * 
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public class OnDiskProperties {

	private char equals = '=';
	private char separator = ':';
	private char pad = '_';
	private BufferedOutputStream bos;
	private String file;
	private RandomAccessFile raf;
	
	private int recordLength;
	private long size = -1;

	public OnDiskProperties(String file, int recordLength, boolean write)
			throws FileNotFoundException {
		this.file = file;
		if (write) {
			bos = new BufferedOutputStream(new FileOutputStream(file));
		} else {
			try {
				if (new File(file).exists()) {
					raf = new RandomAccessFile(file, "r");
				}
			} catch (AccessControlException ace) {
				// ignore, will use input stream
			}
		}
		if (recordLength < 2)
			throw new IllegalArgumentException(
					"OnDiskProperties: minimum recordlength is 2");
		this.recordLength = recordLength + 1;
	}

	public void add(String key, String value) throws IOException {
		if (bos == null)
			throw new IllegalStateException(getClass() + ": opened for reading");
		StringBuffer record = new StringBuffer();
		record.append(key);
		record.append("=");
		record.append(value);
		record.append(separator);
		while (record.length() < recordLength - 1) {
			record.append(pad);
		}
		bos.write(record.toString().getBytes());
		bos.write('\n');
	}

	public String getProperty(String key) throws IOException {
		if (bos != null) {
			throw new IllegalStateException(getClass() + ": opened for writing");
		}
		byte[] record = binarySearch(key.getBytes());
		if (record == null)
			return null;

		int equalsIndex = 1;
		while ((record[equalsIndex] != equals) && (equalsIndex < record.length)) {
			equalsIndex++;
		}
		if (equalsIndex >= record.length)
			throw new IOException("Record without equal '" + equals + "' sign.");
		equalsIndex++;

		int length = 0;
		while ((record[equalsIndex + length] != separator)
				&& (equalsIndex + length) < record.length) {
			length++;
		}
		if (equalsIndex >= record.length)
			throw new IOException("Record without separator '" + separator
					+ "' sign.");

		return new String(record, equalsIndex, length);
	}

	public long size() throws IOException {
		if (bos != null)
			throw new IllegalStateException(getClass() + ": opened for writing");
		if (size >= 0) return size;
		
		if (raf != null) {
			size = raf.length() / recordLength;
			return size;
		}
				
		BufferedInputStream bis = new BufferedInputStream(getClass().getResourceAsStream(file));
		int length = 0;
		int s;
		byte[] buffer = new byte[1000000];
		while ((s = bis.read(buffer)) >= 0) {
			length += s;
		}
		bis.close();	
		size = length / recordLength;
		return size;
	}

	/**
	 * @throws IOException
	 * 
	 */
	public void close() throws IOException {
		if (bos != null)
			bos.close();
		if (raf != null)
			raf.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		raf.close();
	}

	private byte[] binarySearch(byte[] name) throws IOException {
		byte[] record = new byte[recordLength];
		long low = 0;
		long high = size() - 1;

		while (low <= high) {
			long mid = (low + high) >>> 1;
			if (raf != null) {
				raf.seek(mid * recordLength);
				raf.read(record, 0, recordLength);
			} else {
				BufferedInputStream bis = new BufferedInputStream(getClass().getResourceAsStream(file));
				bis.skip(mid * recordLength);
				bis.read(record, 0, recordLength);
				bis.close();
			}
			// compare(record, name)
			for (int j = 0; j < name.length; j++) {
				if (record[j] < name[j]) {
					low = mid + 1;
					break;
				} else if (record[j] > name[j]) {
					high = mid - 1;
					break;
				}
			}
			if (record[name.length - 1] == name[name.length - 1]) {
				return record; // key found
			}
		}
		return null; // key not found
	}
}
