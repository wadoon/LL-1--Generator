package weigl.grammar.gui;

import java.util.LinkedList;

import java.util.List;
import java.util.Stack;

import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import weigl.grammar.rt.AST;
import weigl.grammar.rt.AST.Leaf;
import weigl.grammar.rt.AST.Node;

/**
 * Decorator for an AST to an {@link TreeModel} for using in {@link JTree}.
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 07.12.2009
 * @version 1
 * 
 */
public class AstJTreeAdapter implements TreeModel {

	private List<TreeModelListener> listeners = new LinkedList<TreeModelListener>();

	private AST ast;

	public AstJTreeAdapter(AST ast) {
		this.ast = ast;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(l);
	}

	@Override
	public Object getChild(Object parent, int index) {
		Leaf l = searchFor(parent);
		if (l.getClass() == Node.class) {
			Node node = (Node) l;
			return node.getElements().get(index);
		}
		return null;
	}

	private Leaf searchFor(Object parent) {
		Stack<Node> stack = new Stack<Node>();
		stack.push(ast.getRoot());

		if (ast.getRoot().equals(parent))
			return ast.getRoot();

		while (stack.size() != 0) {
			Node node = stack.pop();
			for (Leaf n : node.getElements()) {
				if (parent.equals(n))
					return n;
				if (n instanceof Node)
					stack.push((Node) n);
			}
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		Leaf l = searchFor(parent);
		if (l instanceof Node) {
			Node node = (Node) l;
			return node.getElements().size();
		}
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		Leaf l = searchFor(parent);
		if (l instanceof Node) {
			Node node = (Node) l;
			return node.getElements().indexOf(child);
		}
		return -1;
	}

	@Override
	public Object getRoot() {
		return ast.getRoot();
	}

	@Override
	public boolean isLeaf(Object node) {
		Leaf l = searchFor(node);
		if (l instanceof Node) {
			return ((Node) l).getElements().size() == 0;
		}
		return true;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// EMPTY
	}
}
