package weigl.grammar.gui;

import java.util.LinkedList;

import java.util.Stack;

import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import weigl.grammar.lltck.rt.TokenDefinition;
import weigl.grammar.lltck.rt.interfaces.AST;
import weigl.grammar.lltck.rt.interfaces.Leaf;
import weigl.grammar.lltck.rt.interfaces.Node;

public class AstTokenJTreeAdapter<E extends TokenDefinition<E>> implements TreeModel
{
    private List<TreeModelListener> listeners = new LinkedList<TreeModelListener>();
    private AST<E>                  ast;

    public AstTokenJTreeAdapter(AST<E> a)
    {
        ast = a;
    }

    private Leaf<E> searchFor(Object parent)
    {
        Stack<Node<E>> stack = new Stack<Node<E>>();
        stack.push(ast.getRoot());
        while (stack.size() >= 1)
        {
            Node<E> n = stack.pop();
            for (Leaf<E> c : n.getElements())
            {
                if (c == parent)
                    return c;
                if (c.hasChildren())
                    stack.push((Node<E>) c);
            }
        }
        throw new RuntimeException("object not found");
    }

    @Override
    public void addTreeModelListener(TreeModelListener l)
    {
        listeners.add(l);
    }

    @Override
    public Object getChild(Object parent, int index)
    {
        return null;
    }

    @Override
    public int getChildCount(Object parent)
    {
        try
        {
            Node<E> n = (Node<E>) searchFor(parent);
            return n.getElements().size();
        } catch (ClassCastException e)
        {
            return 0;
        }
    }

    @Override
    public int getIndexOfChild(Object parent, Object child)
    {
        Node<E> n = (Node<E>) searchFor(parent);
        return n.getElements().indexOf(child);
    }

    @Override
    public Object getRoot()
    {
        return ast.getRoot();
    }

    @Override
    public boolean isLeaf(Object node)
    {
        return !searchFor(node).hasChildren();
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l)
    {
        listeners.remove(l);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue)
    {
        System.err.println("not implemented");
    }

}
