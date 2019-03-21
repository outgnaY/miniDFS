package Util;


public class FileTree {
	public static void main(String[] args) {
		//FileTree class test.
		FileTree fileTree = new FileTree();
		fileTree.insert("a/b/c.txt", true);		
		fileTree.insert("a/b/test.pdf", true);
		fileTree.insert("foo/ss", false);
		fileTree.insert("a/test.pdf", true);
		fileTree.insert("a/b/c.txt", true);
		fileTree.list();
	}
	private Node root;
	public FileTree() {
		this.root = new Node("/");
	}
	public void list() {
		list(root);
	}
	private void list(Node n) {
		if(n != null) {
			System.out.println(n.val + ' ' + n.fileflag);
			list(n.firstChild);
			list(n.nextSibling);
		}		
	}
	public boolean insert(String path, boolean fileflag) {
		String[] patharr = path.split("/");
		Node tempNode = root.firstChild;
		Node lastNode = root;
		boolean find = true;
		for(int i = 0; i < patharr.length; i++) {
			while(tempNode != null && !tempNode.val.equals(patharr[i])) {
				tempNode = tempNode.nextSibling;
			}
			if(tempNode == null) {
				find = false;
				while(i < patharr.length) {
					if(i == patharr.length - 1) {
						Node newNode = new Node(patharr[i], true);
						Node fc = lastNode.firstChild;
						lastNode.firstChild = newNode;
						newNode.nextSibling = fc;
						lastNode = lastNode.firstChild;
					}
					else {
						Node newNode = new Node(patharr[i], false);
						Node fc = lastNode.firstChild;
						lastNode.firstChild = newNode;
						newNode.nextSibling = fc;
						lastNode = lastNode.firstChild;
					}
					i++;					
				}
				break;
			}
			else if(tempNode.val.equals(patharr[i])) {
				lastNode = tempNode;
				tempNode = tempNode.firstChild;
			}
		}
		if(!find) {
			System.out.println("Node insert done");
		}
		else {
			System.out.println("File already exists");
		}
		return find;
	}

}
class Node {
	Node firstChild;
	Node nextSibling;
	String val;
	boolean fileflag;
	public Node(String val) {
		firstChild = null;
		nextSibling = null;
		this.val = val;
		this.fileflag = false;
	}
	public Node(String val, boolean fileflag) {
		firstChild = null;
		nextSibling = null;
		this.val = val;
		this.fileflag = fileflag;
	}
}