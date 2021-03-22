package challenges.find_the_middle_of_a_list_in_a_single_pass;



/**
 * Created by sousaJ on 19/10/2020
 * in package - challenges.find_the_middle_of_a_list_in_a_single_pass
 **/
public class LinkedList {

    Node head;

    public static void main(String[] args) {
       LinkedList llist = new LinkedList();
        for (int i = 10; i > 5; --i) {
            llist.push(i);
            llist.printNext();
            llist.printMiddle(llist.head);
        }

    }

    public void push(int data){
         Node node = new Node(data);
         node.next = head;
         head = node;
    }

    public void printNext(){
         Node n = head;
         while (n != null){
             System.out.println(n.data + " ");
             n = n.next;
         }
    }

    public void printMiddle(Node head){
         Node slow_ptr  = head;
         Node fast_ptr = head;

         if(head != null) {
             while (fast_ptr != null && fast_ptr.next != null) {
                    fast_ptr = fast_ptr.next.next;
                    slow_ptr = slow_ptr.next;
             }
             System.out.println("The middle element is: " + slow_ptr.data);
         }
    }

     static class Node {
        int data;
        Node next;

         public Node(int data) {
            this.data = data;
        }
    }

}
