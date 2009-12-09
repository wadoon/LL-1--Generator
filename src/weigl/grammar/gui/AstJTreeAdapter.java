package weigl.grammar.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import weigl.grammar.rt.AST;
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
		AST.Leaf l = searchFor(parent);
		if (l instanceof AST.Node) {
			AST.Node node = (AST.Node) l;
			return node.getElements().get(index);
		}
		return null;
	}

	private AST.Leaf searchFor(Object parent) {
		Stack<AST.Node> stack = new Stack<AST.Node>();
		stack.push(ast.getRoot());

		if (ast.getRoot().equals(parent))
			return ast.getRoot();

		while (stack.size() != 0) {
			AST.Node node = stack.pop();
			for (AST.Leaf n : node.getElements()) {
				if (parent.equals(n))
					return n;
				if (n instanceof AST.Node)
					stack.push((Node) n);
			}
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		AST.Leaf l = searchFor(parent);
		if (l instanceof AST.Node) {
			AST.Node node = (AST.Node) l;
			return node.getElements().size();
		}
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		AST.Leaf l = searchFor(parent);
		if (l instanceof AST.Node) {
			AST.Node node = (AST.Node) l;
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
		AST.Leaf l = searchFor(node);
		if (l instanceof AST.Node) {
			return ((AST.Node) l).getElements().size() == 0;
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
