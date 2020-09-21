/*
 * Name: Isaac Blackwood
 * Class: SE 3345.002
 * Date: 9/18/2020
 * Description:
 * 	This is a linked list implementation that can hold any type using Java Generics.
 * 	I implemented the required functions as well as some others just for fun/practice.
 * 	This was originally project seperated into several files, but I condensed it into a
 * 	single .java file because the project description indicated that it should be.
 */

import static java.lang.System.out;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.NoSuchElementException;

public class GenLinkedList <T> implements Iterable<Node<T>>
{
	//Constants
	private static final String EMPTY = "[Empty]";
	
	//Data members
	private Node<T> head = null;
	
	//Methods
	//Constructors
	public GenLinkedList() {}
	public GenLinkedList(GenLinkedList<T> genLinkedList) //copy constructor
	{
		for(Node<T> current : genLinkedList)
		{
			addEnd(current.data());
		}
	}
	public GenLinkedList(List<T> list)
	{
		for(T currentData : list)
		{
			addEnd(currentData);
		}
	}
	
	//Display
	public String toString()
	{
		if (isEmpty())
		{
			return EMPTY;
		}
		
		//else step through the list and add each node to the StringBuilder
		StringBuilder stringBuilder = new StringBuilder(head.toString());
		Node<T> current = head;
		while(current.hasNext())
		{
			current = current.next();
			stringBuilder.append(current);
		}
		stringBuilder.append("null");
		return stringBuilder.toString();
	}
	
	//Getters
	public Node<T> head()
	{
		return head;
	}
	public Node<T> tail()
	{
		return getNode(size()-1);
	}
	public T get(int index)
	{
		return getNode(index).data();
	}
	public Node<T> getNode(int index) //returns the node at index
	{
		if ((index >= size()) || (index < 0))
		{
			//the index is out of bounds
			throw new IllegalArgumentException("Out of bounds.");
		}
		//the arguments were valid, so find the node
		Node<T> current = head;
		for(int i = 0; i < index; i++)
		{
			current = current.next();
		}
		
		//get the node
		return current;
	}
	public boolean isEmpty()
	{
		if (null == head)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	public int size()
	{
		//if the list is empty return 0
		if(isEmpty())
		{
			return 0;
		}
		
		//Traverse the list
		Node<T> current = head;
		int size = 1;
		while (current.hasNext())
		{
			size++;
			current = current.next();
		}
		return size;
	}
	
	//Setters
	public Node<T> set(int index, T data)
	{
		//get the node at index
		//if the index was invalid an out of bounds error should be thrown from get()
		Node<T> current = getNode(index);
		
		//set the data
		current.data(data);	
		return current;
	}
	
	//Add nodes
	public Node<T> addFront(Node<T> node)
	{
		if(null != node) 
		{
			Node<T> oldHead = head;
			this.head = node;
			head.next(oldHead);
			return head;
		}
		else
		{
			throw new IllegalArgumentException("Null cannot be added as a new node.");
		}
	}
	public Node<T> addFront(T data)
	{
		return addFront(new Node<T>(data));
	}
	public Node<T> addEnd(Node<T> node)
	{
		if (isEmpty()) 
		{
			//set the node as the head
			return head = node;
		}
		
		//else traverse to the end of the list
		Node<T> current = head;
		while (current.hasNext())
		{
			current = current.next();
		}
		
		//If the node has a next, remove it
		if (node.hasNext())
		{
			node.next(null);
		}
		
		//Add the node
		return current.next(node);
	}
	public Node<T> addEnd(T data)
	{
		return addEnd(new Node<T>(data));
	}
	public Node<T> set(int index, Node<T> node) //sets the data of the node at index
	{
		if (null == node)
		{
			throw new IllegalArgumentException("Null cannot be added as a new node.");
		}
		return set(index, node.data());
	}
	public void insertList(List<T> list, int index)
	{
		GenLinkedList<T> genLinkedList = new GenLinkedList<T>(list);
		insertList(genLinkedList, index);
	}
	public void insertList(GenLinkedList<T> genLinkedList, int index)
	{
		Node<T> nodeAfterInsertedList = getNode(index);//This performs bounds checking on index, so it is convenient to call immediately
		
		//create a copy of each node to be inserted
		GenLinkedList<T> list = new GenLinkedList<T>(genLinkedList);
		
		//connect the new list's tail
		list.tail().next(nodeAfterInsertedList);
		
		//connect the new list's head
		if (0 == index)
		{
			//special case, list will be the new head
			head = list.head();
			return;
		}
		Node<T> parent = getNode(index - 1);
		parent.next(list.head());
	}
	
	//Remove nodes
	public Node<T> removeFront()
	{
		if (isEmpty())
		{
			//do nothing the list is empty
			return null;
		}
		
		//pop the head by setting it to the next node
		Node<T> oldHead = head;
		head = head.next();
		return oldHead;
	}
	public Node<T> removeEnd()
	{
		if (isEmpty())
		{
			//do nothing the list is empty
			return null;
		}
		else if (null == head.next())
		{
			//the head is the last/only node
			Node<T> oldHead = head;
			head = null;
			return oldHead;
		}
		
		//traverse the list until the second to last node
		Node<T> current = head;
		while(current.next().hasNext())
		{
			current = current.next();
		}
		
		//remove the last node
		Node<T> oldEnd = current.next();
		current.next(null);
		return oldEnd;
	}
	public void removeMatching(T value) //removes all copies of this value from the list
	{
		//make a stack of all indices of the matching elements
		Stack<Integer> stack = new Stack<>();
		int i = 0;
		for (Node<T> current : this)
		{
			if (current.data().equals(value))
			{
				stack.push(i);
			}
			i++;
		}
		//remove each of the matching elements
		while (!stack.isEmpty())
		{
			int index = stack.pop();
			remove(index);
		}
	}
	public Node<T> remove(int index)
	{
		if (0 == index)
		{
			return removeFront();
		}
		//get the parent and link it to the next of the node to be deleted
		Node<T> parentOfNodeToDelete = getNode(index - 1);
		Node<T> nodeToDelete = getNode(index);
		parentOfNodeToDelete.next(nodeToDelete.next());
		return nodeToDelete;
	}
	public void erase(int index, int numOfElements)
	{
		//prechecks
		boolean outOfBounds = (index < 0 || index >= size() || numOfElements < 0 || (index + numOfElements) > size());
		if (outOfBounds)
		{
			throw new IllegalArgumentException("Out of bounds.");
		}
		if (0 == numOfElements)
		{
			//do nothing
			return;
		}
		
		//starting with the last element, remove each until were done (work backwards because the indices shift forward when their parents are deleted)
		for(int i = (index + numOfElements - 1); i >= index; i--)
		{
			remove(index);
		}
	}
	
	//List manipulation
	public void swap(int index1, int index2) //Swaps the nodes at each index NOTE: I wanted to implement this as a proper node swap, not a swap of the data within
	{
		//get the nodes at each index
		//bounds checking is performed in the .get() function
		Node<T> node1 = getNode(index1);
		Node<T> node2 = getNode(index2);
		
		//check if same index
		if (index1 == index2)
		{
			//same node do nothing
			return;
		}
		
		//switch the parents of each (using get() so must be before switching the next members)
		if ((index1 != 0) && (index2 != 0))
		{
			//general case
			Node<T> parent1 = getNode(index1 - 1);
			Node<T> parent2 = getNode(index2 - 1);
			parent1.next(node2);
			parent2.next(node1);
		}
		else 
		{
			//special cases: one node is the head
			if (0 == index1) //node1 was head
			{
				Node<T> parent2 = getNode(index2 - 1);
				parent2.next(node1);
				//set node2 as new head
				head = node2;
			}
			if (0 == index2) //node2 was head
			{
				Node<T> parent1 = getNode(index1 - 1);
				parent1.next(node2);
				//set node1 as new head
				head = node1;
			}
		}

		//switch the next nodes of each
		Node<T> next1 = node1.next();
		node1.next(node2.next());
		node2.next(next1);		
	}
	public void shift(int shiftAmount) //rotates the list towards the head by shiftAmount such that the node at index shiftAmount will become the new head
	{
		if (0 == shiftAmount)
		{
			//do nothing
			return;
		}
		//attach the current tail to the current head to make the list into a circle
		//get() will perform bounds checking
		Node<T> oldTail = getNode(size()-1);
		oldTail.next(head);
		
		//get the new head and its parent
		Node<T> oldParentOfNewHead = getNode(shiftAmount - 1); //cannot go out of bounds we checked if shiftAmount was 0
		Node<T> newHead = getNode(shiftAmount);
		
		//remove the connection to the new head
		oldParentOfNewHead.next(null);
		
		//set the new head
		head = newHead;
	}
	

	//Iterable
	@Override
	public Iterator<Node<T>> iterator() 
	{
		return new GenLinkedListIterator<T>(this);
	}

	//Main
	public static void main(String[] args) 
	{
		out.println("Create a new empty GenLinkedList, list1");
		GenLinkedList<Integer> list1 = new GenLinkedList<>();
		out.println(list1);

		out.println("addEnd(1)");
		list1.addEnd(1);
		out.println(list1);

		out.println("addEnd(2)");
		list1.addEnd(2);
		out.println(list1);
		
		out.println("addFront(3)");
		list1.addFront(3);
		out.println(list1);
		
		out.println("Create a new empty GenLinkedList, list2");
		GenLinkedList<Integer> list2 = new GenLinkedList<>();
		out.println(list2);
		
		out.println("addFront(3)");
		list2.addFront(3);
		out.println(list2);
		
		out.println("set(1, 16) (an exception should be thrown)");
		try
		{
			list2.set(1, 16);
		}
		catch (Exception e) 
		{
			out.println(e);
		}
		out.println(list2);
		
		out.println("set(0, 16)");
		list2.set(0, 16);
		out.println(list2);

		out.println("list1 again");
		out.println("set(1,13)");
		list1.set(1, 13);
		out.println(list1);
		
		out.println("get(1)");
		out.println(list1.get(1));
		
		out.println("swap(1, 16) (an exception should be thrown)");
		try
		{
			list1.swap(1, 16);
		}
		catch (Exception e) 
		{
			out.println(e);
		}
		out.println(list2);
		
		out.println("swap(1,2)");
		list1.swap(1, 2);
		out.println(list1);
		
		out.println("swap(0,2)");
		list1.swap(0, 2);
		out.println(list1);
		
		out.println("swap(1,1)");
		list1.swap(1, 1);
		out.println(list1);
		
		out.println("removeFront()");
		list1.removeFront();
		out.println(list1);
		
		out.println("removeEnd()");
		list1.removeEnd();
		out.println(list1);
		
		out.println("add some values");
		list1.addEnd(23);
		list1.addEnd(16);
		list1.addEnd(54);
		list1.addEnd(23);
		list1.addEnd(74);
		list1.addEnd(75);
		list1.addEnd(32);
		list1.addEnd(16);
		out.println(list1);
		
		out.println("removeMatching(16)");
		list1.removeMatching(16);
		out.println(list1);
		
		out.println("erase(2,2)");
		list1.erase(2,2);
		out.println(list1);
		
		out.println("create new LinkedList, javaList");
		List<Integer> javaList = new LinkedList<>();
		javaList.add(1);
		javaList.add(2);
		javaList.add(3);
		javaList.add(4);
		out.println(javaList);
		
		out.println("insertList(javaList, 0)");
		list1.insertList(javaList, 0);
		out.println(list1);		
		
		out.println("insertList(javaList, 7)");
		list1.insertList(javaList, 7);
		out.println(list1);	
	}
}

/* Description:
 * 	This is the implementation for the iterator of the GenLinkedList class above. 
 */
class GenLinkedListIterator<T> implements Iterator<Node<T>>
{
	//
	private Node<T> current;
	
	//Methods
	//Constructor
	public GenLinkedListIterator(GenLinkedList<T> genLinkedList) 
	{
		current = new Node<T>();
		current.next(genLinkedList.head());
	}
	@Override
	public boolean hasNext() 
	{
		return current.hasNext();
	}
	@Override
	public Node<T> next() 
	{
		if (null == current.next())
		{
			throw new NoSuchElementException();
		}
		current = current.next();
		return current;
	}
}

/* Description:
 * 	This is the data holding node for a linked list implementation that can hold any type using Java Generics.
 */
class Node<T> 
{ 
	//Constants
	private static final String EMPTY_NODE = "[Empty Node]-->";
	
	//Data members
	private T data = null;
	private Node<T> next = null;
	
	//Methods
	//Constructors
	public Node() {}
	public Node(T data)
	{
		this.data = data(data);
	}
	public Node(T data, Node<T> next)
	{
		this.data = data(data);
		this.next = next(next);
	}
	public Node(Node<T> node) //copy constructor. NOTE: shallow copy behavior (copy data only)
	{
		this.data = node.data();
	}
	
	//Object
	@Override
	public String toString()
	{
		if (isEmpty())
		{
			return EMPTY_NODE;
		}
		return data.toString() + "-->";
	}
	@Override
	public boolean equals(Object o)
	{
		if (null == o)
		{
			return false;
		}
		else if (o instanceof Node<?>)
		{
			//cast o to Node
			Node<?> node = (Node<?>) o;
			if (node.data().equals(data))
			{
				//the data and types were the same
				return true;
			}
		}
		return false;
	}
	
	//Getters
	public T data()
	{
		return data;
	}
	public Node<T> next()
	{
		return next;
	}
	
	//Setters
	public T data(T data)
	{
		return this.data = data;
	}
	public Node<T> next(Node<T> next)
	{
		return this.next = next;
	}
	
	//isEmpty and hasNext()
	public boolean isEmpty()
	{
		if (null == data)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean hasNext()
	{
		if (null != next)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
}

