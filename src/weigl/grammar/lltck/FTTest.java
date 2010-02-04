package weigl.grammar.lltck;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FTTest {
	public static void main(String[] args) throws TemplateException, IOException {
		Configuration cfg = new Configuration();
		cfg.setObjectWrapper(DefaultObjectWrapper.BEANS_WRAPPER);
		cfg.setDirectoryForTemplateLoading(new File("."));
		Template tpl = cfg.getTemplate("Test.ftl");
		SimpleHash sh = new SimpleHash();
		sh.put("theObject", 
				new BeansWrapper().wrap(new TestObject("test", 234)));
		tpl.process(sh, new OutputStreamWriter(System.out));
	}

	static class TestObject {
		private String name;
		private int price;

		public TestObject(String name, int price) {
			this.name = name;
			this.price = price;
		}

		// JavaBean properties
		// Note that public fields are not visible directly;
		// you must write a getter method for them.
		public String getName() {
			return name;
		}

		public int getPrice() {
			return price;
		}

		// A method
		public double sin(double x) {
			return Math.sin(x);
		}
	}
}
