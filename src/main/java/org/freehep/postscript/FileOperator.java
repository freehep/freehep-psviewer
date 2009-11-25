// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.freehep.util.io.ASCII85InputStream;
import org.freehep.util.io.ASCII85OutputStream;
import org.freehep.util.io.ASCIIHexInputStream;
import org.freehep.util.io.ASCIIHexOutputStream;
import org.freehep.util.io.DCTInputStream;
import org.freehep.util.io.FlateInputStream;
import org.freehep.util.io.FlateOutputStream;
import org.freehep.util.io.RunLengthInputStream;
import org.freehep.util.io.RunLengthOutputStream;
import org.freehep.util.io.StandardFileFilter;

/**
 * File Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class FileOperator extends PSOperator {

	public static Class<?>[] operators = { FileFile.class, Filter.class,
			CloseFile.class, Read.class, Write.class, ReadHexString.class,
			WriteHexString.class, ReadString.class, WriteString.class,
			ReadLine.class, BytesAvailable.class, Flush.class, FlushFile.class,
			ResetFile.class, Status.class, Run.class, CurrentFile.class,
			DeleteFile.class, RenameFile.class, FilenameForAll.class,
			SetFilePosition.class, FilePosition.class, Print.class,
			FileEqual.class, FileEqualEqual.class, Stack.class, PStack.class };

	@Override
	public boolean execute(OperandStack os) {
		throw new RuntimeException("Cannot execute class: " + getClass());
	}
}

class FileFile extends FileOperator {
	{
		operandTypes = new Class[] { PSString.class, PSString.class };
	}

	@Override
	public String getName() {
		return "file";
	}

	@Override
	public boolean execute(OperandStack os) {
		String access = os.popString().getValue();
		String filename = os.popString().getValue();
		try {
			if (filename.equals("%stdin")) {
				os.push(new PSInputFile(System.in, os.getDSC()));

			} else if (filename.equals("%stdout")) {
				os.push(new PSOutputFile(System.out));

			} else if (filename.equals("%stderr")) {
				os.push(new PSOutputFile(System.err));

			} else if (filename.equals("%lineedit")
					|| filename.equals("%statementedit")) {
				System.err
						.println("%lineedit and %statementedit not supported.");
				error(os, new Undefined());

			} else {
				// regular file
				boolean rw = (access.length() > 1) && (access.charAt(1) == '+');
				boolean write = (access.length() > 0)
						&& (access.charAt(0) == 'w');
				boolean append = (access.length() > 0)
						&& (access.charAt(0) == 'a');

				if (rw) {
					os.push(new PSRandomAccessFile(filename, write, append, os
							.isSecure()));
				} else {
					if (write || append) {
						os.push(new PSOutputFile(filename, append, os
								.isSecure()));
					} else {
						os.push(new PSInputFile(filename, os.getDSC()));
					}
				}
			}
		} catch (FileNotFoundException e) {
			error(os, new UndefinedFileName());
		} catch (IOException e) {
			error(os, new InvalidFileAccess());
		}
		return true;
	}
}

class Filter extends FileOperator {
	{
		operandTypes = new Class[] { PSName.class };
	}

	public boolean encode(OperandStack os, String filterName)
			throws IOException {
		if (filterName.equals("RunLengthEncode")) {
			// special parameter
			int runLength = os.popInteger().getValue();
			PSDictionary dict;
			if (os.checkType(PSDictionary.class)) {
				dict = os.popDictionary();
			}
			PSDataTarget target = os.popDataTarget();
			// FIXME: runLength parameter is ignored
			os.push(new PSOutputFile(new RunLengthOutputStream(target
					.getOutputStream())));

			error(os, new Undefined());
			return true;

		} else {
			// no parameters, optional dictionary
			PSDictionary dict;
			if (os.checkType(PSDictionary.class)) {
				dict = os.popDictionary();
			}
			PSDataTarget target = os.popDataTarget();
			if (filterName.equals("ASCIIHexEncode")) {
				os.push(new PSOutputFile(new ASCIIHexOutputStream(target
						.getOutputStream())));

			} else if (filterName.equals("ASCII85Encode")) {
				os.push(new PSOutputFile(new ASCII85OutputStream(target
						.getOutputStream())));

			} else if (filterName.equals("LZWEncode")) {
				error(os, new Undefined());
				return true;

			} else if (filterName.equals("FlateEncode")) {
				os.push(new PSOutputFile(new FlateOutputStream(target
						.getOutputStream())));

			} else if (filterName.equals("CCITTFaxEncode")) {
				error(os, new Undefined());
				return true;

			} else if (filterName.equals("NullEncode")) {
				os.push(new PSOutputFile(target.getOutputStream()));

			} else {
				error(os, new Undefined());
				return true;
			}
		}

		return true;
	}

	public boolean decode(OperandStack os, String filterName)
			throws IOException {
		if (filterName.equals("SubFileDecode")) {
			// special parameters
			error(os, new Undefined());
			return true;
		} else {
			// no parameters, optional dictionary
			PSDictionary dict;
			if (os.checkType(PSDictionary.class)) {
				dict = os.popDictionary();
			}
			PSDataSource source = os.popDataSource();
			if (filterName.equals("ASCIIHexDecode")) {
				os.push(new PSInputFile(new ASCIIHexInputStream(source
						.getInputStream()), source.getDSC()));

			} else if (filterName.equals("ASCII85Decode")) {
				os.push(new PSInputFile(new ASCII85InputStream(source
						.getInputStream()), source.getDSC()));

			} else if (filterName.equals("LZWDecode")) {
				error(os, new Undefined());
				return true;

			} else if (filterName.equals("FlateDecode")) {
				os.push(new PSInputFile(new FlateInputStream(source
						.getInputStream()), source.getDSC()));

			} else if (filterName.equals("RunLengthDecode")) {
				os.push(new PSInputFile(new RunLengthInputStream(source
						.getInputStream()), source.getDSC()));

			} else if (filterName.equals("CCITTFaxDecode")) {
				error(os, new Undefined());
				return true;

			} else if (filterName.equals("DCTDecode")) {
				os.push(new PSInputFile(new DCTInputStream(source
						.getInputStream()), source.getDSC()));
				return true;

			} else if (filterName.equals("ReusableStreamDecode")) {
				// Level 3
				error(os, new Undefined());
				return true;

			} else {
				error(os, new Undefined());
				return true;
			}
		}

		if (!os.checkType(PSDataSource.class)) {
			error(os, new TypeCheck());
			return true;
		}

		return true;
	}

	@Override
	public boolean execute(OperandStack os) {
		String filterName = os.popName().getValue();
		try {
			return filterName.endsWith("Encode") ? encode(os, filterName)
					: decode(os, filterName);
		} catch (ClassCastException e) {
			error(os, new TypeCheck());
			return true;
		} catch (IOException e) {
			error(os, new IOError(e));
			return true;
		}
	}

}

class CloseFile extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSFile file = os.popFile();
		try {
			file.close();
		} catch (IOException e) {
			error(os, new IOError(e));
		}
		return true;
	}
}

class Read extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSFile file = os.popFile();
		try {
			int b = file.read();
			if (b < 0) {
				os.push(false);
			} else {
				os.push(b);
				os.push(true);
			}
		} catch (IOException e) {
			error(os, new IOError(e));
		}
		return true;
	}
}

class Write extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class, PSInteger.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSInteger b = os.popInteger();
		PSFile file = os.popFile();
		try {
			file.write(b.getValue() & 0xFF, os.isSecure());
		} catch (IOException e) {
			error(os, new IOError(e));
		}
		return true;
	}
}

class ReadHexString extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class, PSString.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSString string = os.popString();
		PSFile file = os.popFile();
		if (string.size() <= 0) {
			error(os, new RangeCheck());
		} else {
			try {
				InputStream in = new ASCIIHexInputStream(((PSInputFile) file)
						.getInputStream(), true);
				for (int i = 0; i < string.size(); i++) {
					int b = in.read();
					if (b < 0) {
						os.push(string.subString(0, i));
						os.push(false);
						return true;
					} else {
						string.set(i, b & 0xFF);
					}
				}
				os.push(string.subString(0));
				os.push(true);
			} catch (IOException e) {
				error(os, new IOError(e));
			} catch (ClassCastException e) {
				error(os, new InvalidAccess());
			}
		}
		return true;
	}
}

class WriteHexString extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class, PSString.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSString string = os.popString();
		PSFile file = os.popFile();
		try {
			if (!os.isSecure()) {
				throw new IOException();
			}
			OutputStream out = new ASCIIHexOutputStream(((PSOutputFile) file)
					.getOutputStream());
			for (int i = 0; i < string.size(); i++) {
				out.write(string.get(i));
			}
		} catch (IOException e) {
			error(os, new IOError(e));
		} catch (ClassCastException e) {
			error(os, new InvalidAccess());
		}
		return true;
	}
}

class ReadString extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class, PSString.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSString string = os.popString();
		PSFile file = os.popFile();
		if (string.size() <= 0) {
			error(os, new RangeCheck());
		} else {
			try {
				for (int i = 0; i < string.size(); i++) {
					int b = file.read();
					if (b < 0) {
						os.push(string.subString(0, i));
						os.push(false);
						return true;
					} else {
						string.set(i, b & 0xFF);
					}
				}
				os.push(string.subString(0));
				os.push(true);
			} catch (IOException e) {
				error(os, new IOError(e));
			}
		}
		return true;
	}
}

class WriteString extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class, PSString.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSString string = os.popString();
		PSFile file = os.popFile();
		try {
			for (int i = 0; i < string.size(); i++) {
				file.write(string.get(i) & 0xFF, os.isSecure());
			}
		} catch (IOException e) {
			error(os, new IOError(e));
		}
		return true;
	}
}

class ReadLine extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class, PSString.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSString string = os.popString();
		PSFile file = os.popFile();
		try {
			String line = file.readLine();
			if (line == null) {
				os.push(string.subString(0, 0));
				os.push(false);
				return true;
			}

			if (string.size() < line.length()) {
				error(os, new RangeCheck());
			} else {
				for (int i = 0; i < line.length(); i++) {
					string.set(i, line.charAt(i));
				}
				os.push(string.subString(0, line.length()));
				os.push(true);
			}
		} catch (IOException e) {
			error(os, new IOError(e));
		}
		return true;
	}
}

class BytesAvailable extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSFile file = os.popFile();
		try {
			os.push(file.available());
		} catch (IOException e) {
			error(os, new IOError(e));
		}
		return true;
	}
}

class Flush extends FileOperator {

	@Override
	public boolean execute(OperandStack os) {
		System.out.flush();
		if (System.out.checkError()) {
			error(os, new IOError(new IOException("Cannot flush System.out")));
		}
		return true;
	}
}

class FlushFile extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSFile file = os.popFile();
		try {
			file.flush();
		} catch (IOException e) {
			error(os, new IOError(e));
		}
		return true;
	}
}

class ResetFile extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSFile file = os.popFile();
		try {
			file.reset();
		} catch (IOException e) {
			error(os, new IOError(e));
		}
		return true;
	}
}

class Status extends FileOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSFile.class)) {
			PSFile file = os.popFile();
			os.push(file.isValid());
		} else if (os.checkType(PSString.class)) {
			PSString name = os.popString();
			File file = new File(name.getValue());
			if (file.exists() && file.isFile()) {
				os.push(file.length() / 1024); // pages
				os.push(file.length()); // bytes
				os.push(file.lastModified()); // referenced
				os.push(file.lastModified()); // created
				os.push(true);
			} else {
				os.push(false);
			}
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class Run extends FileOperator implements LoopingContext {
	{
		operandTypes = new Class[] { PSString.class };
	}

	// FIXME: does not handle InvalidExit
	@Override
	public boolean execute(OperandStack os) {
		String name = os.popString().getValue();
		try {
			PSFile file = new PSInputFile(name, os.getDSC());
			file.setExecutable();
			os.execStack().pop();
			os.execStack().push(file);
			return false;
		} catch (FileNotFoundException e) {
			error(os, new UndefinedFileName());
		} catch (IOException e) {
			error(os, new IOError(e));
		}
		return true;
	}
}

class CurrentFile extends FileOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.execStack().getCurrentFile());
		return true;
	}
}

class DeleteFile extends FileOperator {
	{
		operandTypes = new Class[] { PSString.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (!os.isSecure()) {
			error(os, new InvalidFileAccess());
			return true;
		}

		PSString name = os.popString();
		if (name.get(0) == '%') {
			error(os, new UndefinedFileName());
		} else {
			File file = new File(name.getValue());
			if (!file.delete()) {
				error(os, new IOError(new IOException("Cannot delete file "+file)));
			}
		}
		return true;
	}
}

class RenameFile extends FileOperator {
	{
		operandTypes = new Class[] { PSString.class, PSString.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (!os.isSecure()) {
			error(os, new InvalidFileAccess());
			return true;
		}

		PSString name2 = os.popString();
		PSString name1 = os.popString();
		if ((name1.get(0) == '%') || (name2.get(0) == '%')) {
			error(os, new UndefinedFileName());
		} else {
			File file1 = new File(name1.getValue());
			File file2 = new File(name2.getValue());
			if (!file1.exists()) {
				error(os, new UndefinedFileName());
			} else {
				if (!file1.renameTo(file2)) {
					error(os, new IOError(new IOException("Could not rename "+file1+" to "+file2)));
				}
			}
		}
		return true;
	}
}

class FilenameForAll extends FileOperator implements LoopingContext {

	private int index;
	private String[] files;
	private PSPackedArray proc;
	private PSString scratch;

	public FilenameForAll() {
	}

	private FilenameForAll(String[] f, PSPackedArray p, PSString s) {
		files = f;
		proc = p;
		scratch = s;
		index = 0;
	}

	// FREEHEP-128: incomplete
	@Override
	public boolean execute(OperandStack os) {
		if (proc == null) {
			if (!os.checkType(PSString.class, PSPackedArray.class,
					PSString.class)) {
				error(os, new TypeCheck());
				return true;
			}

			PSString s = os.popString();
			PSPackedArray p = os.popPackedArray();
			PSString template = os.popString();

			String dir = template.getValue();
			if (dir.startsWith("%")) {
				System.err.println("%device%file currently not supported.");
				error(os, new Undefined());
				return true;
			}

			File directory = new File((dir.lastIndexOf('/') < 0) ? "./" : dir);
			FileFilter filter = new StandardFileFilter(dir);
			File[] matchedFiles = directory.listFiles(filter);

			Collection<String> matchedFileNames = new ArrayList<String>();
			for (int i = 0; i < matchedFiles.length; i++) {
				matchedFileNames.add(matchedFiles[i].getName());
			}

			String[] f = matchedFileNames.toArray(new String[matchedFileNames.size()]);
			os.execStack().pop();
			os.execStack().push(new FilenameForAll(f, p, s));
			return false;
		}

		if (index >= files.length) {
			return true;
		}

		PSString name = scratch.set(files[index]);
		os.push(name);
		os.execStack().push(proc);
		index++;
		return true;
	}
}

class SetFilePosition extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class, PSInteger.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSInteger pos = os.popInteger();
		PSFile file = os.popFile();
		try {
			file.seek(pos.getValue());
		} catch (IOException e) {
			error(os, new IOError(e));
		}
		return true;
	}
}

class FilePosition extends FileOperator {
	{
		operandTypes = new Class[] { PSFile.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSFile file = os.popFile();
		try {
			os.push(file.getFilePointer());
		} catch (IOException e) {
			error(os, new IOError(e));
		}
		return true;
	}
}

class Print extends FileOperator {
	{
		operandTypes = new Class[] { PSString.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSString s = os.popString();
		System.out.print(s.getValue());
		return true;
	}
}

// FIXME: should be built-in function
class FileEqual extends FileOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public String getName() {
		return "=";
	}

	@Override
	public boolean execute(OperandStack os) {
		// FIXME: should call CvS
		// there should maybe be a separate string value for objects
		PSObject o = os.popObject();
		System.out.println(o.toString());
		return true;
	}
}

// FIXME: should be built-in function
class FileEqualEqual extends FileOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public String getName() {
		return "==";
	}

	@Override
	public boolean execute(OperandStack os) {
		PSObject o = os.popObject();
		System.out.println(o.toString());
		return true;
	}
}

class Stack extends FileOperator {

	@Override
	public boolean execute(OperandStack os) {
		// FIXME: should call = operator
		os.printStack();
		return true;
	}
}

class PStack extends FileOperator {

	@Override
	public boolean execute(OperandStack os) {
		// FIXME: should call == operator
		os.printStack();
		return true;
	}
}
