package weigl.grammar.compiler;

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

/**
 * 
 *
 *
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date   07.12.2009
 * @version 1
 *
 */
class MemoryJavaFileObject implements JavaFileObject {
	private ByteArrayOutputStream baos = new ByteArrayOutputStream(4 * 1024);
	private String name;

	public MemoryJavaFileObject(String name) {
		this.name = name;
	}

	/**
	 * @return the catched class bytes
	 */
	public byte[] getClassByte()
	{
		try {
			baos.flush();
		} catch (IOException e) {}
		return baos.toByteArray();
	}
	
	/**
	 * {@inheritDoc}
	 * <br>always {@link Modifier#PUBLIC}
	 */
	@Override
	public Modifier getAccessLevel() {
		return Modifier.PUBLIC;
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * always {@link Kind#CLASS} 
	 */
	@Override
	public Kind getKind() {
		return Kind.CLASS;
	}

	/**
	 * {@inheritDoc}
	 * <br>always null
	 */
	@Override
	public NestingKind getNestingKind() {
		return null;
	}

	/**
	 * {@inheritDoc}<br>
	 * always true
	 */
	@Override
	public boolean isNameCompatible(String simpleName, Kind kind) {
		return true;
	}

	@Override
	public boolean delete() {
		return true;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors)
			throws IOException {
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
		throw new IOException("read only!");
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return baos;
	}

	@Override
	public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
		throw new IOException("write only!");
	}

	@Override
	public Writer openWriter() throws IOException {
		return new OutputStreamWriter(openOutputStream());
	}

	@Override
	public URI toUri() {
		return URI.create("memory://"+getName()+".class");
	}
}