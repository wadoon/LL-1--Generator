package weigl.grammar.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import javax.swing.text.Highlighter.HighlightPainter;

public class EditorScrollPane extends JScrollPane implements DocumentListener {

	private static final long serialVersionUID = -1856478938455202466L;

	private static final String[] KEYSSIGNS = {":", "€", "\\|"};

	protected Box m_linePanel = new Box(BoxLayout.Y_AXIS);
	protected JTextArea m_textComponent;
	protected Font m_currentFont;

	private DefaultHighlighter dh = new DefaultHighlighter();

	private static final HighlightPainter CONSTANTS = new DefaultHighlighter.DefaultHighlightPainter(
			Color.LIGHT_GRAY);
	private DefaultHighlighter.DefaultHighlightPainter TERMINAL = new DefaultHighlighter.DefaultHighlightPainter(
			new Color(232, 242, 254));
	private DefaultHighlighter.DefaultHighlightPainter NONTERMINAL = new DefaultHighlighter.DefaultHighlightPainter(
			Color.ORANGE);

	public EditorScrollPane(JTextArea component) {
		m_textComponent = component;
		m_currentFont = component.getFont();

		m_linePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

		setViewportView(m_textComponent);
		setRowHeaderView(m_linePanel);
		component.getDocument().addDocumentListener(this);

		m_textComponent.setHighlighter(dh);

		for (int j = 1; j < 50; j++)
			m_linePanel.add(createLabel(j));
		update();
	}

	public void changedUpdate(DocumentEvent e) {
		update();
	}

	public void insertUpdate(DocumentEvent e) {
		update();
	}

	public void removeUpdate(DocumentEvent e) {
		update();
	}

	public void update() {
		m_currentFont=m_textComponent.getFont();
		updateHighlighter();
	}

	private void updateHighlighter() {
		dh.removeAllHighlights();
		hightlightWords(KEYSSIGNS, CONSTANTS);
		hightlightWords("([A-Z]+)", NONTERMINAL);
		hightlightWords("([^A-Z ]+)", TERMINAL);
	}

	private void hightlightWords(String[] words, HighlightPainter hlcolor) {
		hightlightWords("(" + EditorScrollPane.join(words, "|") + ")", hlcolor);
	}

	private void hightlightWords(String regex, HighlightPainter hlcolor) {
		Pattern p = Pattern.compile(regex, Pattern.UNICODE_CASE
				| Pattern.MULTILINE);
		Matcher m = p.matcher(m_textComponent.getText());

		while (m.find()) {
			try {
				dh.addHighlight(m.start(), m.end(), hlcolor);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	private Component createLabel(int i) {
		Formatter f = new Formatter((Appendable) null).format("%03d", i);
		JLabel lbl = new JLabel(f.out().toString());
		lbl.setFont(m_currentFont);
		return lbl;
	}

	public static String join(String[] words, String sep) {
		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (; i < words.length - 1; i++)
			sb.append(words[i]).append(sep);
		sb.append(words[words.length - 1]);

		return sb.toString();
	}

}