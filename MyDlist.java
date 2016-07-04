public class MyDlist extends DList {
	// constructor that creates an empty list
  	public MyDlist() { 
    	size = 0;
    	header = new DNode(null, null, null);	// create header
    	trailer = new DNode(null, header, null); // create trailer
    	header.setNext(trailer); // make header and trailer point to each 
	}
  	// constructor that creates a double linked list by reading from the input 
	public MyDlist(String f) throws IOException {
		// reading from the standard input 
		if(f.equals("stdin")) {
			Scanner s = new Scanner(System.in); 
			while(s.hasNextLine()) {
			    String str = s.nextLine();
			 	if(str.isEmpty())
			 		break;
			 	DNode newNode = new DNode(str, null, null);
			 	this.addLast(newNode);
			}
			s.close();
		}
		// reading from a text file
		else {
			File fip = new File(f); 
			Scanner s = new Scanner(fip);
			while(s.hasNextLine()) {
				DNode newNode = new DNode(s.next(), null, null);
				this.addLast(newNode);
			}
			s.close();
		}
	}
	// print the entire list
	public void printList() {
		DNode newNode = header.next;
			while(newNode != trailer) {
				System.out.println(newNode.element);
				newNode = newNode.getNext();
			}
		newNode = null;
	}
	// create an identical copy of a doubly linked list u 
	// and returns the resulting doubly linked list
	public static MyDlist cloneList(MyDlist u) {
		MyDlist w = new MyDlist();
		//w.header = u.header;
		DNode uNode = u.header.getNext();
		while(uNode.getNext() != null) {
			DNode wNode = new DNode(uNode.element, null, null);
			uNode = uNode.getNext();
			w.addLast(wNode);
		}
		//w.trailer = null;
		return w;
	}
	// connect the two list and return the new list
	// this method only requires a constant number of primitive operations
	public static MyDlist connecting(MyDlist u, MyDlist v) {
        u.getLast().setNext(v.getFirst());
        v.getFirst().setPrev(u.getLast());   
        v.header = null;
        u.trailer = v.trailer;   
        u.size = u.size + v.size;
        return u;
    }
	// figure out the union of u and v
	// first, connect the two lists into one new list, 
	// then remove the nodes that contain the same elements of the list
	// the big O :
	// assume that the length of the connected list is N
	// the initialization and return statements only requires a constant number of primitive operations
	// the first loop executes (N - 1) times, from node 1 to node N - 1 
	// the second loop executes (N - 2)times, from node 2 to node N
	// therefore, the number of primitive operations being c' * ( N - 1 ) * (N  - 2) + c''
	// that is, the running time of the algorithm is O(N * N)
	public static MyDlist union(MyDlist u, MyDlist v) {
		MyDlist w = connecting(u, v);
		//w.printList();
		DNode iNode = w.header.next;
		DNode jNode = iNode.next;
		//System.out.println(iNode.element + jNode.element);
		while(iNode.next != null) {
			//System.out.println("i");
			jNode = iNode.next;
			while (jNode.next != null) {
				//System.out.println("j");
				DNode tempNode = jNode.next;
				if(iNode.element.equals(jNode.element)) {
					//System.out.println("this is the if condition\n");
					w.remove(jNode);
				}
				jNode = tempNode;
			}
			iNode = iNode.next;
		}
		//w.printList();
		//System.out.println("end\n");
		return w;
	}
	// figure out the intersection of u and v
	// first, create a new empty list x 
	// then, compare node by node in both list u and list v
	// once a same element found in both u and v
	// then add it to the last of list x
	// assume that there are i Nodes in list u, and j nodes in list v
	// the initialization and return statements only requires a constant number of primitive operations
	// the first loop executes i times
	// and the second loop executes j times
	// therefore, the running time of the algorithm is O(i * j)
	public static MyDlist intersection(MyDlist u, MyDlist v) {
		MyDlist x = new MyDlist();
		DNode uNode = u.getFirst();	
		while(uNode.getNext() != null) {
			int flag = 0;
			DNode xNode = new DNode(uNode.element, null, null);
			DNode vNode = v.getFirst();
			while(vNode.getNext() != null) {
				if(uNode.element.equals(vNode.element)) {
					x.addLast(xNode);
					uNode = uNode.next;
					flag = 1;
					break;
				}
				vNode = vNode.next;
			}
			if(flag == 0)
				uNode = uNode.next;
		}
		//x.printList();
		//System.out.println("This is the flag\n");
		return x;
	}
}