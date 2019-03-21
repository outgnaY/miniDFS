package NameNode;
import java.io.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import Util.Pair;
import Util.FileTree;
import Util.Config;
import Util.Config.cmdType;
import DataNode.DataNode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NameNode implements Runnable {
	private HashMap<String, Pair<Integer, Long>> fileMap = null;
	private FileTree fileTree = null;
	private ArrayList<DataNode> dataNodes = null;
	private cmdType cmd;
	private int fidCnt;
	public byte[] buffer;
	public static String byteArrayToHex(byte[] byteArray) {
		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
		char[] resultCharArray =new char[byteArray.length * 2];
		int index = 0;
		for (byte b : byteArray) {
		    resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
		    resultCharArray[index++] = hexDigits[b& 0xf];
		 }
		 return new String(resultCharArray);
	}
	public NameNode() {
		fileMap = new HashMap<String, Pair<Integer, Long>>();
		fileTree = new FileTree();
		dataNodes = new ArrayList<DataNode>();
		fidCnt = 0;
		this.buffer = new byte[1024];
	}
	public void fidCntInc() {
		this.fidCnt++;
	}
	public int getFidCnt() {
		return this.fidCnt;
	}
	public void push(DataNode d) {
		dataNodes.add(d);
	}
	public String[] parseCmd() {
		System.out.print("miniDFS>");
		Scanner scan = new Scanner(System.in);
		String cmd = scan.nextLine();
		String[] paras = cmd.split(" ");
		return paras;
	}
	public boolean processCmd(String[] paras) {
		if(paras.length == 0) {
			System.out.println("You input a blank line.");
			return false;
		}
		else if(paras[0].equals("exit")) {
			if(paras.length != 1) {
				System.out.println("Usage:exit");
				return false;
			}
			else {
				this.cmd = cmdType.EXIT;
				return true;
			}
		}
		else if(paras[0].equals("list")) {
			if(paras.length != 1) {
				System.out.println("Usage:list");
				return false;
			}
			else {
				//fileTree.list();
				this.cmd = cmdType.LIST;
				return true;
			}
		}
		else if(paras[0].equals("locate")) {
			if(paras.length != 3) {
				System.out.println("Usage:locate FileID offset");
				return false;
			}
			else {
				this.cmd = cmdType.LOCATE;
				return true;
			}
			
		}
		else if(paras[0].equals("fetch")) {
			if(paras.length != 5) {
				System.out.println("Usage:fetch FileID offset destdir destname");
				return false;
			}
			else {
				this.cmd = cmdType.FETCH;
				return true;
			}
			
		}
		else if(paras[0].contentEquals("read")) {
			if(paras.length != 4) {
				System.out.println("Usage:read source destdir destname");
				return false;
			}
			else {
				this.cmd = cmdType.READ;
				return true;
			}
		}
		else if(paras[0].equals("write")) {
			if(paras.length != 3) {
				System.out.println("Usage:write source dest");
				return false;
			}
			else {
				this.cmd = cmdType.WRITE;
				return true;
			}
			
		}
		else {
			System.out.println("Wrong command.Usage:list | locate FileID offset | fetch FileID offset dest | write source dest");
			return false;
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(true) {			
			String[] paras = parseCmd();
			boolean flag = processCmd(paras);
			if(flag == false) {
				continue;
			}
			else {
				switch(this.cmd) {
					case EXIT:
						System.out.println("exit");
						System.exit(0);
						break;
					case LIST:
						System.out.println("list");
						this.fileTree.list();
						continue;
					case LOCATE:
						System.out.println("locate");
						for(int i = 0; i < Config.DATANUM; i++) {
							DataNode tempData = dataNodes.get(i);
							synchronized(tempData) {
								tempData.setCmdType(cmdType.LOCATE);
								tempData.setStatus(true);
								tempData.setFid(Integer.parseInt(paras[1]));
								tempData.setOffset(Integer.parseInt(paras[2]));
								tempData.notifyAll();
							}
						}
						break;
					case READ:
						System.out.println("read");
						File destdir = new File(paras[2]);
						File destfile = new File(paras[2] + "\\" + paras[3]);
						System.out.println(destfile);
						if(destdir.exists() != true) {
							System.out.println("File path is invalid. Read operation failed");
							continue;
						}
						else if(destfile.exists() == true) {
							System.out.println("Target file already exists. Read operation failed");
							continue;
						}
						else if(this.fileMap.get(paras[1]) == null) {
							System.out.println("Source file not found in miniDFS.");
							continue;
						}
						for(int i = 0; i < Config.DATANUM; i++) {
							DataNode tempData = dataNodes.get(i);
							synchronized(tempData) {
								tempData.setCmdType(cmdType.READ);
								tempData.setFid(this.fileMap.get(paras[1]).getKey());
								tempData.setBufSize(this.fileMap.get(paras[1]).getValue());
								tempData.setStatus(true);
								tempData.notifyAll();
							}
						}
												
						break;
					case FETCH:
						System.out.println("fetch");
						File destDir = new File(paras[3]);
						File destFile = new File(paras[3] + "\\" + paras[4]);
						if(destDir.exists() != true) {
							System.out.println("File path is invalid. Fetch operation failed");
							continue;
						}
						else if(destFile.exists() == true){			
							System.out.println("Target file already exists. Fetch operation failed");
							continue;
						}
						for(int i = 0; i < Config.DATANUM; i++) {
							DataNode tempData = dataNodes.get(i);
							synchronized(tempData) {
								tempData.setCmdType(cmdType.FETCH);
								tempData.setFid(Integer.parseInt(paras[1]));
								tempData.setOffset(Integer.parseInt(paras[2]));
								tempData.setStatus(true);
								tempData.notifyAll();
							}
						}
												
						break;
					case WRITE:
						System.out.println("write");
						File srcFile = new File(paras[1]);						
						if(!srcFile.exists()) {
							System.out.println("Source file not found.");
							continue;
						}
						else {
							boolean find = this.fileTree.insert(paras[2], true);
							if(find == true) {
								System.out.println("Create file error, " + paras[2] + " already exists");
								continue;
							}
							else {
								long fileSize = srcFile.length();
								Collections.sort(dataNodes);
								this.fileMap.put(paras[2], new Pair<Integer, Long>(this.fidCnt, fileSize));
								for(int i = 0; i < Config.REPNUM; i++) {
									DataNode tempData = dataNodes.get(i);
									synchronized(tempData) {
										tempData.setStatus(true);
										tempData.setPath(paras[1]);
										tempData.setFid(this.fidCnt);
										tempData.notifyAll();										
										tempData.setCmdType(cmdType.WRITE);
									}
								}
								fidCnt++;
							}
						}						
						break;
					default:
						break;
				}
			}
			
			for(int i = 0; i < Config.DATANUM; i++) {
				DataNode tempData = dataNodes.get(i);
				synchronized(tempData) {
					while(tempData.getStatus() != false) {
						try {
							tempData.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					System.out.println("Process datanode " + i + " finished");
				}
			}
			boolean notexist = true;
			MessageDigest messageDigest = null;
			String check = null;
			String pre = null;
			switch(this.cmd) {				
				case LOCATE:
					for(int i = 0; i < Config.DATANUM; i++) {
						DataNode tempData = dataNodes.get(i);
						if(tempData.getExist() == true) {
							notexist = false;
							System.out.println("Found file:FileID = " + paras[1] + " offset = " + paras[2] + " on the dataserver " + i);
						}						
					}
					if(notexist == true) {
						//Did not found file on any dataserver.
						System.out.println("File requested not found.");
					}					
					break;
				case FETCH:
					for(int i = 0; i < Config.DATANUM; i++) {
						DataNode tempData = dataNodes.get(i);
						if(tempData.getExist() == true) {
							System.out.println(i);
							notexist = false;
							FileOutputStream out = null;
							try {
								messageDigest = MessageDigest.getInstance("MD5");
								messageDigest.update(tempData.getBuffer());
								check = byteArrayToHex(messageDigest.digest());
								System.out.println(check);
								
							} catch (NoSuchAlgorithmException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} finally {
								if(pre != null && !pre.equals(check)) {
									System.out.println("Unequal checksum from different dataserver.");
								}
								pre = new String(check);
							}
							try {
								out = new FileOutputStream(paras[3] + "\\" + paras[4]);								
								try {
									out.write(tempData.getBuffer());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								try {
									out.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}								
							}							
							//break;
						}						
					}
					if(notexist == true) {
						//Did not found file on any dataserver.
						System.out.println("Source file not found in miniDFS.");
					}	
					break;
				case READ:
					for(int i = 0; i < Config.DATANUM; i++) {
						DataNode tempData = dataNodes.get(i);
						if(tempData.getExist() == true) {
							notexist = false;
							System.out.println(i);
							FileOutputStream out = null;
							try {
								messageDigest = MessageDigest.getInstance("MD5");
								messageDigest.update(tempData.getBuffer());
								check = byteArrayToHex(messageDigest.digest());
								System.out.println(check);
								
							} catch (NoSuchAlgorithmException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} finally {
								if(pre != null && !pre.equals(check)) {
									System.out.println("Unequal checksum from different dataserver.");
								}
								pre = new String(check);
							}
							try {
								out = new FileOutputStream(paras[2] + "\\" + paras[3]);
								
								try {
									out.write(tempData.getBuffer());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								try {
									out.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}								
							}							
							//break;
						}
						
					}
					break;
				case WRITE:
					System.out.println("File write success.");
					break;
				default:
					break;
			}			
			
		}
		
	}

}
