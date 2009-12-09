package weigl.grammar.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import weigl.grammar.GrammarParserException;
import weigl.grammar.ParserWrapper;
import weigl.grammar.ProductionGrammerParser;

/**
 * 
 *
 *
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date   07.12.2009
 * @version 1
 *
 */
public class GrammarApplet extends JApplet {
	private static final long serialVersionUID = -5382319120075825086L;

	private JTextArea txtGrammar = new JTextArea();
	private JTextArea txtJavaSource = new JTextArea();

	private JButton btnCompile = new JButton("Compile");

	private JLabel lblInput = new JLabel("Eingabe: ");
	private JTextField txtInput = new JTextField(20);

	private JTree treeParse = new JTree(new Object[] {});

	private JTextArea txtLogging = new JTextArea();
	private JScrollPane scrLogging = new JScrollPane(txtLogging);
	
	private ParserWrapper parser;

	@Override
	public void init() {
		setLayout(new BorderLayout());
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		add(split);

		txtJavaSource.setEditable(false);

		JTabbedPane pane = new JTabbedPane();
		pane.addTab("GrammarInput", txtGrammar);
		pane.addTab("JavaSource", new JScrollPane(txtJavaSource));

		pane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				generateSource();
			}
		});
		pane.setSelectedIndex(0);

		split.setLeftComponent(pane);
		JPanel center = new JPanel(new BorderLayout());
		center.add(btnCompile, BorderLayout.NORTH);
		
		btnCompile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				compile();
			}
		});
		
		JPanel p = new JPanel(new BorderLayout(10,10));
		JPanel pinput = new JPanel(new BorderLayout());
		pinput.add(lblInput, BorderLayout.WEST);
		pinput.add(txtInput, BorderLayout.CENTER);
		pinput.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		txtInput.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				checkInput();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				checkInput();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				checkInput();
			}
		});

		p.add(pinput, BorderLayout.NORTH);
		p.add(createBorder(new JScrollPane(treeParse), "Parse Tree"));
		center.add(p);
		split.setRightComponent(center);

		add(createBorder(scrLogging,"Logging"), BorderLayout.SOUTH);
		
		
		status("Welcome to another LL(1)-Parser factory");
		status("Alexander Weigl <weigla@fh-trier.de> -- Dezember 2009");
		status("Creates parser generators after the script in Theoretische Informatik!");
	}

	public void success(JComponent c) {
		c.setBorder(BorderFactory.createLineBorder(Color.GREEN));
	}

	public void error(JComponent c) {
		c.setBorder(BorderFactory.createLineBorder(Color.RED));
	}

	public void status(String line) {
		txtLogging.append(line+'\n');
		scrLogging.getVerticalScrollBar().setValue(
				scrLogging.getVerticalScrollBar().getMaximum() 
				);
				
	}

	public void generateSource() {
		try {
			txtJavaSource.setText(ProductionGrammerParser
					.generateSource(txtGrammar.getText()));
			success(txtGrammar);
			status("SOURCE GENERATOR: run successful");
		} catch (GrammarParserException e) {
			status("SOURCE GENERATOR: " + e.getMessage());
			error(txtGrammar);
		}
	}

	public void compile() {
		generateSource();
		String s = txtJavaSource.getText();
		status("Compiling...");
		try {
			parser = new ParserWrapper(s);
		} catch (Exception e) {
			e.printStackTrace();
			// JOptionPane.showMessageDialog(this, e.getMessage(),
			// "Error at compiling", JOptionPane.ERROR_MESSAGE);
			status("COMPILER ERROR:  " + e.getMessage());
		}
	}
	
	private JComponent createBorder(JComponent c, String title)
	{
		JPanel pane = new JPanel(new BorderLayout());
		pane.add(c);
		pane.setBorder(BorderFactory.createTitledBorder(title));
		return pane;
	}

	public void checkInput() {
		if (parser != null) {
			String input = txtInput.getText();
			try {
				parser.run(input);
				treeParse.setModel(new AstJTreeAdapter(parser.getParseTree()));
				success(txtInput);
				treeParse.expandRow(treeParse.getRowCount()-1);
			} catch (IllegalStateException e) {
				error(txtInput);
				status("Input string is not well-known for the given grammar");
				status(e.getMessage());
				treeParse.setModel(null);
			}
		} else {
			error(txtInput);
			status("no parser was compiled");
		}
	}
}
