package Main;
import java.io.*;
import NameNode.NameNode;
import DataNode.DataNode;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println("Hello world!");
		NameNode n = new NameNode();
		DataNode d1 = new DataNode("DataNode1");
		DataNode d2 = new DataNode("DataNode2");
		DataNode d3 = new DataNode("DataNode3");
		DataNode d4 = new DataNode("DataNode4");
		n.push(d1);
		n.push(d2);
		n.push(d3);
		n.push(d4);
		Thread NameThread = new Thread(n);
		Thread DataThread1 = new Thread(d1);
		Thread DataThread2 = new Thread(d2);
		Thread DataThread3 = new Thread(d3);
		Thread DataThread4 = new Thread(d4);
		NameThread.start();
		DataThread1.start();
		DataThread2.start();
		DataThread3.start();
		DataThread4.start();
		/*
		String path = "a/b/c.txt";
		String[] aa = path.split("/");
		System.out.println(aa.length);
		for(int i = 0; i < aa.length; i++){
			System.out.println(aa[i] + i);
		}
		*/
	}

}
