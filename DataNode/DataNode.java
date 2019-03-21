package DataNode;
import java.io.*;
import Util.FileOperator;
import Util.Config;
import Util.Config.cmdType;
import java.util.Arrays;
public class DataNode implements Runnable, Comparable<DataNode> {
	private String name;
	private double size;
	private boolean status;
	private cmdType cmd;
	private int offset;
	private int fid;
	private boolean exist;
	private String path;
	private byte[] buffer;
	private long bufSize;
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public void setBufSize(long bufSize) {
		this.bufSize = bufSize;
	}
	public long getBufSize() {
		return this.bufSize;
	}
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
	public byte[] getBuffer() {
		return this.buffer;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return this.path;
	}
	public double getSize() {
		return this.size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public int getFid() {
		return this.fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	
	public cmdType getCmdType() {
		return this.cmd;
	}
	public void setCmdType(cmdType cmd) {
		this.cmd = cmd;
	}
	public boolean getExist() {
		return this.exist;
	}
	public DataNode(String name) {
		this.name = name;
		this.status = false;
		this.size = 0.0;
		this.buffer = null;
		//delete old directory
		File f = new File(this.getClass().getResource("/").getPath());
		String temp = f.getParent().toString();
		//replace space and \
		temp = temp.replaceAll("%20", " ");
		temp = temp.replaceAll("\\\\", "/");
		FileOperator fop = new FileOperator();
		//delete old directory
	    fop.DeleteFolder(temp + "/" + this.name);  	    
	    //create new directory
	    File datadir = new File(name);
		if(!datadir.exists()) {
			datadir.mkdir();
			System.out.println("Create directory£º"+ datadir); 
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub		
		while(true) {
			synchronized(this) {
				while(this.status == false) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(Thread.currentThread().getName() + "waked.");
				if(this.cmd == cmdType.LOCATE) {
					File file = new File(this.name + "/" + this.fid + "-" + this.offset);
					if(!file.exists()) {
						this.exist = false;
					}
					else {
						this.exist = true;
					}
				}
				else if(this.cmd == cmdType.FETCH) {
					FileInputStream in = null;
					String fileName = this.name + "/" +this.fid + "-" + this.offset;
					File file = new File(fileName);
					this.buffer = new byte[(int)file.length()];
					if(!file.exists()) {
						this.exist = false;
					}
					else {
						this.exist = true;
						try {
							in = new FileInputStream(fileName);
							try {
								in.read(this.buffer);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
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
					}
				}
				else if(this.cmd == cmdType.READ) {
					FileInputStream in = null;
					this.buffer = new byte[(int)this.bufSize];
					int start = 0;
					int offset;
					String fileName;
					while(start < this.bufSize) {
						offset = start / Config.CHUNKSIZE;
						fileName = this.name + "/" + this.fid + "-" + offset;
						//System.out.println(fileName);
						File file = new File(this.name + "/" + this.fid + "-" + this.offset);
						if(!file.exists()) {
							this.exist = false;
							break;
						}
						this.exist = true;					
						try {							
							in = new FileInputStream(fileName);
							try {
								in.read(this.buffer, start, Math.min(Config.CHUNKSIZE, (int)(this.bufSize - start)));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							start += Config.CHUNKSIZE;
							try {
								in.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}						
					}					
				}
				else if(this.cmd == cmdType.WRITE) {
					
					FileInputStream in = null;
					FileOutputStream out = null;
					try {
						File file = new File(this.path);
						long fileSize = file.length();
						this.size += fileSize / 1024.0 / 1024;
						in = new FileInputStream(this.path);
						byte[] buffer = new byte[Config.BUFSIZE];
						long cnt = 0;
						int offset = 0;
						int n = 0;
						int read = 0;
						boolean flag = true;
						while(cnt < fileSize && flag) {
							out = new FileOutputStream(this.name + "/" + this.fid + "-" + offset);
							read = 0;
							Arrays.fill(buffer, (byte)(-1));
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
										if(cnt >= fileSize) {
											out.write(buffer, 0, n);
										}
										else {
											out.write(buffer);
										}										
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
				}				
				this.status = false;
				this.notifyAll();
				
			}
			
		}
		
	}	
	@Override
	public int compareTo(DataNode d) {
		if(this.size > d.getSize()) {
			return 1;
		}
		else if(this.size == d.getSize()) {
			return 0;
		}
		else {
			return -1;
		}
	}
	
	
}
