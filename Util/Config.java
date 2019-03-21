package Util;

public class Config {
	public static final int REPNUM = 3;
	public static final int DATANUM = 4;
	public static final int CHUNKSIZE = 2 * 1024 * 1024;
	public static final int BUFSIZE = 1024;
	public enum cmdType{
		EXIT, LIST, LOCATE, FETCH, READ, WRITE;
	}
}
