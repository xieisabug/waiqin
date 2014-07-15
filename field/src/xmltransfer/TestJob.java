package xmltransfer;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements org.quartz.Job
{
	
	private static int totalCount;
	private static String rootPath;
	private static String logsPath;
	private static String filePath;
	public TestJob(String rootPath,String logsPath,String filePath)
	{
		//totalCount++;
		this.rootPath = rootPath;
		this.logsPath = logsPath;
		this.filePath = filePath;
	}
	
	public TestJob()
	{
		totalCount++;
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			WorkCardXmlUtil.CreateWorkCardOrderXml(rootPath,logsPath,filePath,totalCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
