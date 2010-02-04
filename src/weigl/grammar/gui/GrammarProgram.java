package weigl.grammar.gui;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import weigl.grammar.GrammarParserException;
import weigl.grammar.ParserWrapper;
import weigl.grammar.ProductionGrammerParser;
import weigl.gui.LogListRenderer;
import weigl.gui.LogListRenderer.Level;
import weigl.gui.editor.EditorComponent;
import weigl.gui.editor.EditorFrame;
import weigl.gui.editor.EditorScrollPane.Highlight;

/**
 * 
 * 
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 07.12.2009
 * @version 1
 * 
 */
public class GrammarProgram extends JFrame {
	private static final long serialVersionUID = -5382319120075825086L;

	static Color ERROR = new Color(200, 0, 20).brighter().brighter();
	static Color SUCCESS = new Color(0, 200, 20);

	public static final String GRAMMAR_EXPRESSION = "E: TZ\n" + "Z: +TZ|€\n"
			+ "T: FX\n" + "X: *FX|€\n" + "F: (E)|I\n" + "I: aY|bY\n"
			+ "Y: aY|bY|0Y|1Y|€\n";
	
	private JTextArea txtGrammar = new JTextArea(GRAMMAR_EXPRESSION);
	private EditorScrollPane editGrammar = new EditorScrollPane(txtGrammar);

	private JButton btnCompile = new JButton("Compile");
	private JButton btnJavaSource = new JButton("Show Java Source");

	private JLabel lblInput = new JLabel("Eingabe: ");
	private JTextField txtInput;

	private JTree treeParse = new JTree(new Object[] {});

	private DefaultListModel lstLoggingModel = new DefaultListModel();
	private JList lstLogging = new JList(lstLoggingModel);
	private JScrollPane scrLogging = new JScrollPane(lstLogging);

	private ParserWrapper parser;

	private JLabel lblLeafWord;

	public final static String[] JAVA_KEYWORDS = { "if", "while", "private",
			"public", "protected", "do", "void", "int", "double", "true",
			"else", "\\{", "\\}", "final", "false", "boolean", "float", "long",
			"for", "extends", "\\(", "\\)", "import", "package", "implements",
			"return", "class", "new", "volatile", "transient", "try", "catch",
			"throws", "throw" };

	private static final String NAME = "LL(1)-Parsergenerator";
	private static final String VERSION = "0.3";

	public GrammarProgram() {
		lookandfeel();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout(5, 5));

		LogListRenderer.install(lstLoggingModel);
		lstLogging.setCellRenderer(new LogListRenderer());

		scrLogging.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent e) {
						e.getAdjustable().setValue(
								e.getAdjustable().getMaximum());
					}
				});

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		txtGrammar.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 22));

		JPanel pWest = new JPanel(new BorderLayout());
		JComponent p = createBorder(editGrammar, "Grammar Input:");

		JPanel pWestSouth = new JPanel(new GridLayout(1, 2, 10, 10));
		pWestSouth.add(btnJavaSource);
		pWestSouth.add(btnCompile);
		pWest.add(p);
		pWest.add(pWestSouth, BorderLayout.SOUTH);

		split.setLeftComponent(pWest);

		btnJavaSource.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String s = ProductionGrammerParser
							.generateSource(txtGrammar.getText());
					EditorFrame frame = new EditorFrame(s);
					EditorComponent ec = frame.getEditorComponent();

					ec.setKeywords(JAVA_KEYWORDS);
					ec.addHighlight(new Highlight(new Color(250, 200, 0, 160),
							JAVA_KEYWORDS));
					frame.setSize(400, 400);
					frame.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		btnCompile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				compile();
			}
		});

		txtInput = new JTextField(20);

		lblLeafWord = new JLabel("");

		JPanel center = new JPanel(new BorderLayout(10, 10));
		JPanel pinput = new JPanel(new BorderLayout());

		pinput.add(createGrid(lblInput, new JLabel("Leaf word:")),
				BorderLayout.WEST);
		pinput.add(createGrid(txtInput, lblLeafWord), BorderLayout.CENTER);

		pinput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		txtInput.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				change(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				change(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				change(e);
			}

			private void change(DocumentEvent e) {
				checkInput(txtInput.getText());
			}
		});

		center.add(pinput, BorderLayout.NORTH);
		center.add(createBorder(new JScrollPane(treeParse), "Parse Tree"));
		split.setRightComponent(center);

		add(createBorder(scrLogging, "Logging"), BorderLayout.SOUTH);
		add(split, BorderLayout.CENTER);

		status("Welcome to another LL(1)-Parser factory");
		status("Alexander Weigl <weigla@fh-trier.de> -- Dezember 2009");
		status("Creates parser generators after the script in theoretical computer science!");
	}

	private void lookandfeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager

			// .setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Component createGrid(JComponent... c) {
		JPanel p = new JPanel(new GridLayout(0, 1));
		for (JComponent j : c)
			p.add(j);
		return p;
	}

	public void success(JComponent c) {
		c.setBackground(SUCCESS);
	}

	public void error(JComponent c) {
		c.setBackground(ERROR);
	}

	public void status(String line) {
		status(line, Level.INFO);
	}

	public void status(String line, Level level) {
		lstLoggingModel.addElement(LogListRenderer.createMessage(line, level));
		scrLogging.getVerticalScrollBar().setValue(
				scrLogging.getVerticalScrollBar().getMaximum());
	}

	public void generateSource() {
		try {
			ProductionGrammerParser.generateSource(txtGrammar.getText());
			success(editGrammar);
			status("source generator ran successfully");
		} catch (GrammarParserException e) {
			status("source generator: " + e.getMessage());
			error(editGrammar);
		}
	}

	public void compile() {
		status("Compiling...");
		try {
			String s = ProductionGrammerParser.generateSource(txtGrammar
					.getText());
			parser = new ParserWrapper(s);
		} catch (Exception e) {
			e.printStackTrace();
			// JOptionPane.showMessageDialog(this, e.getMessage(),
			// "Error at compiling", JOptionPane.ERROR_MESSAGE);
			status("error at compiling  " + e.getMessage());
		}
	}

	private JComponent createBorder(JComponent c, String title) {
		JPanel pane = new JPanel(new BorderLayout());
		pane.add(c);
		pane.setBorder(BorderFactory.createTitledBorder(title));
		return pane;
	}

	public void checkInput(String input) {
		if (parser != null) {
			try {
				parser.run(input);
				treeParse.setModel(new AstJTreeAdapter(parser.getParseTree()));
				String leafWord = parser.getParseTree().getLeafWord();
				lblLeafWord.setText(leafWord);

				if (leafWord.equals(input)) {
					success(txtInput);
					status("word match!", Level.SUCCESS);
				} else {
					error(txtInput);
					status("word mismatch!", Level.ERROR);
				}
			} catch (IllegalStateException e) {
				status("Input string is not well-known for the given grammar",
						Level.ERROR);
				status(e.getMessage(), Level.ERROR);
				error(txtInput);
				treeParse.setModel(null);
			}
		} else {
			error(txtInput);
			status("no parser was compiled", Level.WARNING);
		}
	}
	
	public static void main(String[] args) {
		GrammarProgram gp = new GrammarProgram();
		gp.setTitle(NAME + " - "+VERSION);
		gp.pack();
		gp.setVisible(true);
	}
}
