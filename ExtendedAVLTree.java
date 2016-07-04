package net.datastructures;

import java.util.Comparator;
import java.util.ArrayList;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ExtendedAVLTree<K, V> extends AVLTree<K, V> {
	public AVLTree<K, V> avltree;	
	//constructor
	public ExtendedAVLTree(Comparator<K> c) {
		super(c);
	}
	public ExtendedAVLTree() {
		super();
	}
	//if the tree is empty
	public boolean isEmpty() {
		if (root == null)
			return true;
		else
			return false;
	}
	//clone the nodes from the original tree to a new tree, and returns the new tree
	public AVLTree<K, V> copyNode(AVLTree<K, V> tree, Position<Entry<K, V>> p1, Position<Entry<K, V>> p2) {
		if (p1.element() != null) {
			if (avltree.hasLeft(p1)) 
				copyNode(tree, avltree.left(p1), tree.insertLeft(p2, avltree.left(p1).element()));
			if (avltree.hasRight(p1)) 
				copyNode(tree, avltree.right(p1), tree.insertRight(p2, avltree.right(p1).element()));
		}
		return tree;
	}
	//creates an identical copy of the AVL tree, and returns a reference to the new tree
	public static <K, V> AVLTree<K, V> clone(AVLTree<K, V> tree) {
		ExtendedAVLTree<K, V> cloneTree = new ExtendedAVLTree<K, V>();
		AVLTree<K, V> ref = new AVLTree<K, V>();
		cloneTree.avltree = tree;
		if (cloneTree.isEmpty())
			System.out.println("Empty tree.\n");
		else {
			Position<Entry<K, V>> p = ref.addRoot(tree.root().element());
			ref = cloneTree.copyNode(ref, tree.root(), p);
		}
		return ref;
	}
	//inOrder traversal to copy all the nodes from a tree
	public static <K, V> void inOrderAdd(AVLTree<K, V> tree, Position<Entry<K, V>> node, ArrayList<Entry<K, V>> array) {
			if(node.element() == null)
				return;
			else {
					inOrderAdd(tree, tree.left(node), array);
				array.add(node.element());
					inOrderAdd(tree, tree.right(node), array);
			}
	}
	//merges two AVL trees, tree1 and tree2, into a new tree
	//assuming that tree1 has m nodes and tree2 has n nodes
	//the time complexity of the method is O(m + n)
	//since copying nodes from tree1 to array1 by inOrder traversal takes O(m) time
	//copying nodes from tree2 to array2 by inOrder traversal takes O(n) time
	//therefore, copying nodes from trees to arrays takes O(m + n) time
	//then, comparing the elements from array1 and array2 one by one 
	//and add the element to array3 takes O(m + n) time
	//finally, insert elements from array3 into a new tree takes O(m + n) time
	//therefore, the time complexity of this algorithm is O(3(m + n))
	//that is, O(m + n)
	public static <K, V> AVLTree<K, V> merge(AVLTree<K, V> tree1, AVLTree<K, V> tree2) {
		AVLTree<K, V> tree3 = new AVLTree<K, V>();
		ArrayList<Entry<K, V>> array1 = new ArrayList<Entry<K, V>>(100);
		ArrayList<Entry<K, V>> array2 = new ArrayList<Entry<K, V>>(100);
		ArrayList<Entry<K, V>> array3 = new ArrayList<Entry<K, V>>(100);
		Position<Entry<K, V>> node1 = tree1.root();
		Position<Entry<K, V>> node2 = tree2.root();
		inOrderAdd(tree1, node1, array1);
		inOrderAdd(tree2, node2, array2);
		int i = 0;
		int j = 0;
		int size1 = array1.size();
		int size2 = array2.size();
		while(i != size1 && j != size2) {
			if((int)array1.get(i).getKey() <= (int)array2.get(j).getKey()) {
				array3.add(array1.get(i));
				i++;
			}
			else if((int)array1.get(i).getKey() > (int)array2.get(j).getKey()) {
				array3.add(array2.get(j));
				j++;
			}
		}
		while(i == size1 && j != size2) {
			array3.add(array2.get(j));
			j++;
		}
		while(j == size2 && i != size1) {
			array3.add(array1.get(i));
			i++;
		}
		//for(int k = 0; k < array3.size(); k++) {
			//int s = (int)array3.get(k).getKey();
			//System.out.println(s);
		//}
		tree3.addRoot(array3.get(array3.size() / 2));
		insertNode(tree3, array3, tree3.root(), array3.size() / 2, 0, array3.size() - 1);
		return tree3;
	}
	//recursively insert the left node and the right node to one new node
	public static <K, V> void insertNode (AVLTree<K, V> tree, ArrayList<Entry<K, V>> array, Position<Entry<K, V>> p, int middle, int first, int last) {
		int leftlast = middle - 1;
		int rightfirst = middle + 1;
		if(first <= leftlast) {
			int newnode = first + (leftlast - first + 1) / 2;
			tree.insertLeft(p, array.get(newnode));
			if(newnode < first + 1) 
				tree.insertLeft(tree.left(p), null);
			if(newnode > leftlast - 1) 
    			tree.insertRight(tree.left(p), null);
			insertNode(tree, array, tree.left(p), newnode, first, leftlast);
		}
		if(rightfirst <= last) {
			int newnode = rightfirst + (last - rightfirst + 1) / 2;
    		tree.insertRight(p, array.get(newnode));
    		if (newnode < rightfirst + 1) 
    			tree.insertLeft(tree.right(p), null);
    		if (newnode > last - 1) 
    			tree.insertRight(tree.right(p), null);
    		insertNode(tree, array, tree.right(p), newnode, rightfirst, last);
    	}
		tree.setHeight(p);
	}
	//prints the AVL Tree
	public static <K, V> void print(AVLTree<K, V> tree) {
		DrawTree<K, V> frame = new DrawTree<K, V>(tree);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.tabbedPane.setSelectedIndex(0);
	}
	// the main function
}

class DrawTree<K, V> extends JFrame {
	JTabbedPane tabbedPane;
	public DrawTree(AVLTree<K, V> tree) {
		setTitle("AVL Tree");
		tabbedPane = new JTabbedPane();
		PaintPanel<K, V> PanelLayer = new PaintPanel<K, V>(tree);
		tabbedPane.addTab("AVL tree", null, PanelLayer, "AVL Tree");
		getContentPane().add(tabbedPane);
		setVisible(true);
		setSize(1200, 1200);
		setLocation(10, 10);
	}
}

class PaintPanel<K, V> extends JPanel {
	public AVLTree<K, V> avlTree;
	public PaintPanel(AVLTree<K, V> t) {
		this.avlTree = t;
	}
	void printAVLTree(Position<Entry<K, V>> p, int distance, int height, Graphics g, int x, int y) {
		int radius = 20;
		if (p.element() != null) {
			g.drawOval(x, y, radius, radius);
			g.drawString(p.element().getKey().toString(), x + 4, y + 15);
			if (avlTree.hasLeft(p)) {
				g.drawLine(x + radius / 2, y + radius, x - distance + radius / 2, y + 100);
				printAVLTree(avlTree.left(p), (int) (distance / 2), ++height, g, x - distance, y + 100);
				--height;
			}
			if (avlTree.hasRight(p)) {
				g.drawLine(x + radius / 2, y + radius, x + distance + radius / 2, y + 100);
				printAVLTree(avlTree.right(p), (int) (distance / 2), ++height, g, x + distance, y + 100);
				--height;
			}
		} 
		else if (p.element() == null) {
			g.drawRect(x, y, radius, radius);
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Position<Entry<K, V>> newRoot = avlTree.root();
		if (newRoot != null) {
			printAVLTree(newRoot, 150, 0, g, getWidth() / 2, 20);
		}
	}
}
