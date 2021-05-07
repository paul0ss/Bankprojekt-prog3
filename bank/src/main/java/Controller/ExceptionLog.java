package Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

public class ExceptionLog {
	
	private File logFile = new File("ExceptionsLogs.txt");
	
	private static ExceptionLog log = new ExceptionLog();
	
	private ExceptionLog() {
		
	}
	
	public static ExceptionLog getExceptionLog() {
		return log;
	}
	
	public void logToFile(Exception e) {
		try {
			logFile.createNewFile();
			String exception = e.getStackTrace().toString();
			OutputStream os = new FileOutputStream(logFile);
			PrintStream ps = new PrintStream(os);
			ps.println(LocalDate.now().toString()+":   "+exception);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Logging fehlgeschlagen!");
		}
	}

}
