package structures;

public class ScapegoatTree<T extends Comparable<T>> extends BinarySearchTree<T> {

	private int upperBound;
	
	/**
	 * Adds an element to the tree.
	 * 
	 * The modified tree must still obey the BST rule, though it might not be
	 * balanced.
	 * 
	 * In addition to obeying the BST rule, the resulting tree must also obey
	 * the scapegoat rule. 
	 * 
	 * This method must only perform rebalancing of subtrees when indicated
	 * by the scapegoat rule; do not unconditionally call balance() 
	 * after adding, or you will receive no credit. 
	 * See the project writeup for details.
	 * 
	 * @param element
	 * @throws NullPointerException if element is null
	 */
	@Override
	public void add(T element) {
		if(element == null)
			throw new NullPointerException();
		super.add(element);
		upperBound++;
		if ((double)height() > Math.log((double)upperBound)/Math.log(3.0/2.0)) {
			BSTNode<T> currNode = find(element);
			while (((double)subtreeSize(currNode)/(double)subtreeSize(currNode.getParent())) <= (double)(2.0/3.0)) {
				currNode = currNode.getParent();
			}
			BSTNode<T> goat = null;
			if (currNode != root)
				goat = currNode.getParent();
			else 
				goat = currNode;
			BinarySearchTree<T> tree = new BinarySearchTree<T>();
			tree.root = currNode;
			tree.balance();
			if (goat.getData().compareTo(tree.root.getData()) > 0) {
				goat.setLeft(tree.getRoot());
				goat.getLeft().setParent(goat);
			}
			else {
				goat.setRight(tree.getRoot());
				goat.getRight().setParent(goat);
			}
		}
	}
	
	public BSTNode<T> find(T t) {
		if (t == null)
			throw new NullPointerException();
		return findHelper(t, root);
	}
	
	public BSTNode<T> findHelper(T t, BSTNode<T> node) {
		if (node == null)
			return null;
		else if (node.getData().equals(t))
			return node;
		else if (t.compareTo(node.getData()) < 0)
			return findHelper(t, node.getLeft());
		return findHelper(t, node.getRight());
	}
	
	/**
	 * Attempts to remove one copy of an element from the tree, returning true
	 * if and only if such a copy was found and removed.
	 * 
	 * The modified tree must still obey the BST rule, though it might not be
	 * balanced.
	 * 
	 * In addition to obeying the BST rule, the resulting tree must also obey
	 * the scapegoat rule.
	 * 
	 * This method must only perform rebalancing of subtrees when indicated
	 * by the scapegoat rule; do not unconditionally call balance() 
	 * after removing, or you will receive no credit. 
	 * See the project writeup for details.

	 * @param element
	 * @return true if and only if an element removed
	 * @throws NullPointerException if element is null
	 */
	@Override
	public boolean remove(T t) {
		if (super.remove(t)) {
            if (2*size() < upperBound) {
                balance();
                upperBound = size();
            }
            return true;
        }
        return false;
    }

}
