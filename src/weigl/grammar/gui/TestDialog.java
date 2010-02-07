package weigl.grammar.gui;


import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTree;

import weigl.grammar.ll.rt.AST;

public class TestDialog {
	public static  void showFrame(AST ast) {
		JFrame frame = new JFrame("AST");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTree tree = new JTree(new AstJTreeAdapter(ast));
		frame.add(tree);
		frame.pack();
		frame.setVisible(true);
	}
}
