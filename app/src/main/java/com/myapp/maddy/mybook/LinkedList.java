package com.myapp.maddy.mybook;

import android.widget.Toast;

/**
 * Created by Maddy on 5/2/2020.
 */

public class LinkedList {

    Node head;

    public void insertDataAscending(TaskByTime task) {
        Node newNode = new Node();
        newNode.task = task;
        newNode.next = null;
        System.out.println(task);
        if(head == null){
            head = newNode;
        }
		/*else{
			Node n = head;
			while(n.next!=null){
				n = n.next;
			}
			n.next = newNode;
		}*/
        else{
            Node n = head;
            Node prev = head;
            while(n.task.id < task.id && n.next!=null){
                prev = n;
                n = n.next;
                System.out.print(prev.task.data+"->");
            }
            if(n.task.id>task.id && n == head){
                newNode.next = n;
                head = newNode;
            }
            else if(n.task.id>=task.id){
                newNode.next = n;
                prev.next = newNode;
            }
            else if(n.next==null){
                n.next = newNode;
            }
            System.out.println("\n");
        }
    }

    public String print(){
        String text = "";
        try {
            Node print = head;
            while (print.next != null) {
                text += "\n" + print.task.id + " - " + print.task.data;
                print = print.next;
            }
            text += "\n" + print.task.id + " - " + print.task.data;
        }catch(Exception e){
            text = "No records found";
        }
        return text;
    }

}
