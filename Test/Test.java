package Test;
import java.util.ArrayList;
import java.util.Collections;
import DataNode.DataNode;
import Util.Config;
import java.io.*;
import Util.FileOperator;
import java.util.HashMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Test {
	public static String byteArrayToHex(byte[] byteArray) {
		// ���ȳ�ʼ��һ���ַ����飬�������ÿ��16�����ַ�
		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
		// newһ���ַ����飬�������������ɽ���ַ����ģ�����һ�£�һ��byte�ǰ�λ�����ƣ�Ҳ����2λʮ�������ַ���2��8�η�����16��2�η�����
		char[] resultCharArray =new char[byteArray.length * 2];
		// �����ֽ����飬ͨ��λ���㣨λ����Ч�ʸߣ���ת�����ַ��ŵ��ַ�������ȥ
		int index = 0;
		for (byte b : byteArray) {
		    resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
		    resultCharArray[index++] = hexDigits[b& 0xf];
		 }
		 // �ַ�������ϳ��ַ�������
		 return new String(resultCharArray);
	}
	public static void main(String[] args) {
		/*
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] input = {(byte)0xB8, (byte)0xDF, (byte)0xC9};
			messageDigest.update(input);
			
			String s = byteArrayToHex(messageDigest.digest());
			System.out.println(s);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		/*
		try {
			FileOutputStream fop = new FileOutputStream("C:\\Users\\ThinkPad\\Desktop\\test.docx");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		System.out.println(Integer.toHexString((byte)(-1) & 0xFF));
		//HashMap mp = new HashMap();
		//System.out.println(mp.get(1));
		//Test test = new Test();
		//test.showURL();
		/*
		File file = new File("ThinkPad\\Desktop");
		System.out.println(file.isDirectory());
		System.out.println(file.toString());
		System.out.println(file.isFile());
		*/
		// TODO Auto-generated method stub
		/*
		ArrayList<DataNode> dataNodes = new ArrayList<DataNode>();
		dataNodes.add(new DataNode("DataNode1"));
		dataNodes.add(new DataNode("DataNode2"));
		dataNodes.add(new DataNode("DataNode3"));
		dataNodes.add(new DataNode("DataNode4"));
		dataNodes.get(0).setSize(10.5);
		dataNodes.get(1).setSize(7.4);
		dataNodes.get(2).setSize(14.5);
		dataNodes.get(3).setSize(5.1);
		Collections.sort(dataNodes);
		for(int i = 0; i < dataNodes.size(); i++) {
			System.out.println(dataNodes.get(i).getSize());
		}
		*/
		/*
		try {
			File file = new File("E:\\master1 spring\\�����ݴ���\\jdk api 1.8_google.CHM");
			System.out.println(file.getName());
			FileInputStream in = new FileInputStream("E:\\master1 spring\\�����ݴ���\\jdk api 1.8_google.CHM");
			FileOutputStream out = new FileOutputStream("E:\\master1 spring\\�����ݴ���\\jdk api 1.8_google_copy.CHM");
			byte[] buffer = new byte[1024];
			try {
				while(in.read(buffer) != -1) {
					out.write(buffer);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		/*
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			
			File file = new File("E:\\master1 spring\\�����ݴ���\\ʹ��˵��.docx");
			long fileSize = file.length();
			in = new FileInputStream("E:\\master1 spring\\�����ݴ���\\ʹ��˵��.docx");
			byte[] buffer = new byte[Config.BUFSIZE];
			long cnt = 0;
			int offset = 0;
			int n = 0;
			int read = 0;
			boolean flag = true;
			while(cnt < fileSize && flag) {
				out = new FileOutputStream("DataNode1" + "/" + 0 + "-" + offset);
				read = 0;
				try {
					while(read < Config.CHUNKSIZE && flag) {
						n = in.read(buffer);
						if(n == -1) {
							flag = false;
							break;
						}
						else {
							cnt += n;
							read += n;
							out.write(buffer);
						}										
						
					}								
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					offset++;
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		/*
		File file = new File("");
		System.out.println(file.isFile());
		*/
		
		
	}

}
