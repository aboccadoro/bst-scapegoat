package structures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<T extends Comparable<T>> implements BSTInterface<T> {
	protected BSTNode<T> root;

	public boolean isEmpty() {
		return root == null;
	}

	public int size() {
		return subtreeSize(root);
	}

	protected int subtreeSize(BSTNode<T> node) {
		if (node == null)
			return 0;
		else
			return 1 + subtreeSize(node.getLeft()) + subtreeSize(node.getRight());
	}

	public boolean contains(T t) {
		if (t == null)
			throw new NullPointerException();
		return containsHelper(t, root);
	}
	
	public boolean containsHelper(T elem, BSTNode<T> node) {
		if (node == null)
			return false;
		else if (node.getData().equals(elem))
			return true;
		else if (elem.compareTo(node.getData()) > 0)
			 return containsHelper(elem, node.getRight());
		else
			return containsHelper(elem, node.getLeft());
	}

	public boolean remove(T t) {
		boolean result = contains(t);
		if (result)
			root = removeFromSubtree(root, t);
		return result;
	}

	private BSTNode<T> removeFromSubtree(BSTNode<T> node, T t) {
		// node must not be null
		int result = t.compareTo(node.getData());
		if (result < 0) {
			node.setLeft(removeFromSubtree(node.getLeft(), t));
			return node;
		} 
		else if (result > 0) {
			node.setRight(removeFromSubtree(node.getRight(), t));
			return node;
		} 
		else { // result == 0
			if (node.getLeft() == null)
				return node.getRight(); 
			else if (node.getRight() == null)
				return node.getLeft(); 
			else { // neither child is null
				T predecessorValue = getHighestValue(node.getLeft());
				node.setLeft(removeRightmost(node.getLeft()));
				node.setData(predecessorValue);
				return node;
			}
		}
	}

	private T getHighestValue(BSTNode<T> node) {
		// node must not be null
		if (node.getRight() == null)
			return node.getData();
		else
			return getHighestValue(node.getRight());
	}

	private BSTNode<T> removeRightmost(BSTNode<T> node) {
		// node must not be null
		if (node.getRight() == null)
			return node.getLeft(); 
		else {
			node.setRight(removeRightmost(node.getRight()));
			return node;
		}
	}

	public T get(T t) {
		if (t == null)
			throw new NullPointerException();
		return getHelper(t, root);
	}
	
	public T getHelper(T t, BSTNode<T> node) {
		if (node == null)
			return null;
		else if (node.getData().equals(t))
			return t;
		else if (t.compareTo(node.getData()) > 0)
			return getHelper(t, node.getRight());
		return getHelper(t, node.getLeft());
	}

	public void add(T t) {
		root = addToSubtree(t, root);
	}

	protected BSTNode<T> addToSubtree(T t, BSTNode<T> node) {
		if (node == null)
			return new BSTNode<T>(t, null, null);
		else if (t.compareTo(node.getData()) <= 0) {
			node.setLeft(addToSubtree(t, node.getLeft()));
			if (node.getLeft() != null)
				node.getLeft().setParent(node);
		}
		else {
			node.setRight(addToSubtree(t, node.getRight()));
			if (node.getRight() != null)
				node.getRight().setParent(node);
		}
		return node;
	}

	@Override
	public T getMinimum() {
		if (isEmpty())
			return null;
		return getMinimumHelper(root);
	}
	
	public T getMinimumHelper(BSTNode<T> node) {
		if (node.getLeft() == null)
			return node.getData();
		return getMinimumHelper(node.getLeft());
	}
	
	@Override
	public T getMaximum() {
		if (isEmpty())
			return null;
		return getMaximumHelper(root);
	}
	
	public T getMaximumHelper(BSTNode<T> node) {
		if (node.getRight() == null)
			return node.getData();
		return getMaximumHelper(node.getRight());
	}

	@Override
	public int height() {
		if (isEmpty())
			return -1;
		else if (size() == 1)
			return 0;
		return heightHelper(root) - 1;
	}
	
	public int heightHelper(BSTNode<T> root) {
		if (root == null)
			return 0;
		return 1 + Math.max(heightHelper(root.getLeft()), heightHelper(root.getRight()));
	}

	@Override
	public Iterator<T> preorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		preorderTraverse(queue, root);
		return queue.iterator();
	}
	
	public void preorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			queue.add(node.getData());
			preorderTraverse(queue, node.getLeft());
			preorderTraverse(queue, node.getRight());
		}
	}

	@Override
	public Iterator<T> inorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		inorderTraverse(queue, root);
		return queue.iterator();
	}
	
	private void inorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			inorderTraverse(queue, node.getLeft());
			queue.add(node.getData());
			inorderTraverse(queue, node.getRight());
		}
	}

	@Override
	public Iterator<T> postorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		postorderTraverse(queue, root);
		return queue.iterator();
	}
	
	public  void postorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			postorderTraverse(queue, node.getLeft());
			postorderTraverse(queue, node.getRight());
			queue.add(node.getData());
		}
	}

	@Override
	public boolean equals(BSTInterface<T> other) {
		if (other == null)
			throw new NullPointerException();
		else if (size() != other.size())
			return false;
		else if (!sameValues(other))
			return false;
		return true;
	}

	@Override
	public boolean sameValues(BSTInterface<T> other) {
		if (other == null)
			throw new NullPointerException();
		else if (isEmpty() && other.isEmpty())
			return true;
		Iterator<T> thisIter = inorderIterator();
		Iterator<T> otherIter = other.inorderIterator();
		return sameValuesHelper(thisIter, otherIter, thisIter.next(), otherIter.next());
	}
	
	public boolean sameValuesHelper(Iterator<T> thisIter, Iterator<T> otherIter, T next1, T next2) {
		boolean same = false;
		if (!thisIter.hasNext())
			same = true;
		else if (!otherIter.hasNext()) {
			if (!next2.getClass().equals(next1.getClass()))
				return false;
			return sameValuesHelper(thisIter, otherIter, thisIter.next(), next2);
		}
		else if (next1.getClass().equals(next2.getClass()))
			return sameValuesHelper(thisIter, otherIter, thisIter.next(), otherIter.next());
		return same;
	}

	@Override
	public boolean isBalanced() {
		if (isEmpty())
			return false;
		return Math.pow(2, height()) <= size() && size() < Math.pow(2, height() + 1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void balance() {
		T[] data = (T[])new Comparable[size()];
		Iterator<T> iter = inorderIterator();
		int count = 0;
		while (iter.hasNext()) {
			data[count] = iter.next();
			count++;
		}
		root = null;
		balanceHelper(data, 0, count - 1);
	}
	
	public void balanceHelper(T[] data, int low, int high) {
		if (low == high)
			add(data[low]);
		else if (low + 1 == high) {
			add(data[low]);
			add(data[high]);
		}
		else {
			add(data[(low + high)/2]);
			balanceHelper(data, low, (low + high)/2 - 1);
			balanceHelper(data, (low + high)/2 + 1, high);
		}
	}

	@Override
	public BSTNode<T> getRoot() {
		// DO NOT MODIFY
		return root;
	}

	public static <T extends Comparable<T>> String toDotFormat(BSTNode<T> root) {
		// DO NOT MODIFY
		// see project description for explanation

		// header
		int count = 0;
		String dot = "digraph G { \n";
		dot += "graph [ordering=\"out\"]; \n";
		// iterative traversal
		Queue<BSTNode<T>> queue = new LinkedList<BSTNode<T>>();
		queue.add(root);
		BSTNode<T> cursor;
		while (!queue.isEmpty()) {
			cursor = queue.remove();
			if (cursor.getLeft() != null) {
				// add edge from cursor to left child
				dot += cursor.getData().toString() + " -> "
						+ cursor.getLeft().getData().toString() + ";\n";
				queue.add(cursor.getLeft());
			} else {
				// add dummy node
				dot += "node" + count + " [shape=point];\n";
				dot += cursor.getData().toString() + " -> " + "node" + count
						+ ";\n";
				count++;
			}
			if (cursor.getRight() != null) {
				// add edge from cursor to right child
				dot += cursor.getData().toString() + " -> "
						+ cursor.getRight().getData().toString() + ";\n";
				queue.add(cursor.getRight());
			} else {
				// add dummy node
				dot += "node" + count + " [shape=point];\n";
				dot += cursor.getData().toString() + " -> " + "node" + count
						+ ";\n";
				count++;
			}

		}
		dot += "};";
		return dot;
	}
}