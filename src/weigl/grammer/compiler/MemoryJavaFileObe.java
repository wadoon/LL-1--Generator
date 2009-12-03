package weigl.grammer.compiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

class MemoryJavaFileObject implements JavaFileObject {
	private ByteArrayOutputStream baos = new ByteArrayOutputStream(4 * 1024);
	private String name;

	public MemoryJavaFileObject(String name) {
		this.name = name;
	}

	public byte[] getClassByte()
	{
		try {
			baos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
	
	@Override
	public Modifier getAccessLevel() {
		return Modifier.PUBLIC;
	}

	@Override
	public Kind getKind() {
		return Kind.CLASS;
	}

	@Override
	public NestingKind getNestingKind() {
		return null;
	}

	@Override
	public boolean isNameCompatible(String simpleName, Kind kind) {
		System.out.println("MemoryJavaFileObject.isNameCompatible()");
		return true;
	}

	@Override
	public boolean delete() {
		System.out.println("MemoryJavaFileObject.delete()");
		return true;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors)
			throws IOException {
		System.out.println("MemoryJavaFileObject.getCharContent()");
		throw new IOException("read only!");
	}

	@Override
	public long getLastModified() {
		return System.currentTimeMillis();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public InputStream openInputStream() throws IOException {
		System.out.println("MemoryJavaFileObject.openInputStream()");
		throw new IOException("read only!");
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return baos;
	}

	@Override
	public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
		System.out.println("MemoryJavaFileObject.openReader()");
		throw new IOException("write only!");
	}

	@Override
	public Writer openWriter() throws IOException {
		System.out.println("MemoryJavaFileObject.openWriter()");
		return new OutputStreamWriter(openOutputStream());
	}

	@Override
	public URI toUri() {
		System.out.println("MemoryJavaFileObject.toUri()");
		return URI.create("memory://"+getName()+".class");
	}

}