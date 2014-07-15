package xmltransfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import service.DbService;

/**
 * 用于发出XML数据,根据MIS系统中的传输方式编写
 *
 * @author Administrator
 */
public class SendXMLUtil {
    private static int count = 0;
    private static Logger logger = Logger.getLogger(SendXMLUtil.class);

    public static int sendXML(String url, String xmlContent, String strDispatchId, String strFieldWorkerName, String rootPath, String logsPath, String filePath, int totalCount)
            throws IOException {
        int result = 0;
        try {
            URL gatewayUrl = new URL(url);// 建立链接
            HttpURLConnection httpURLConnection = (HttpURLConnection) gatewayUrl
                    .openConnection();
            // 设置连接属性
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            // 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
            byte[] requestStringBytes = (new sun.misc.BASE64Encoder()
                    .encode(xmlContent.getBytes("utf-8"))).getBytes("utf-8");

            // 设置请求属性
            httpURLConnection.setRequestProperty("Content-length", ""
                    + requestStringBytes.length);
            httpURLConnection
                    .setRequestProperty("accept", "text/xml;text/html");
            httpURLConnection.setRequestProperty("Content-type",
                    "text/xml;charset=utf-8");
            httpURLConnection.setRequestMethod("POST");
            // 建立输出流，并写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(requestStringBytes);
            outputStream.close();
            // 获得响应状态
            int responseCode = httpURLConnection.getResponseCode();
            StringBuffer responseBuffer = null;
            if (HttpURLConnection.HTTP_OK == responseCode) {
                responseBuffer = new StringBuffer();

                String readLine;

                // 处理响应流，必须与服务器响应流输出的编码一致
                BufferedReader responseReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection
                                .getInputStream(), "UTF-8")
                );
                while ((readLine = responseReader.readLine()) != null) {
                    responseBuffer.append(readLine).append("\n");
                }
                responseReader.close();
                System.out.println("responseBuffer:" + responseBuffer);
                logger.debug("responseBuffer.toString().equals(1) : " + responseBuffer.toString().trim().equals("1"));
                if (responseBuffer.toString().trim().equals("1")) {
                	logger.debug("发送成功");
                	logger.debug("成功的id为：" + strDispatchId);
                	
                    //记录发送成功条数
                    count++;
                    //update  DISPATCH_MARK
                    String sql = "UPDATE SM_DISPATCH SET USER_DEFINE20 = ? WHERE DISPATCH_ID=" + strDispatchId;
                    DbService.updateSm_tel_record_question(sql, "1");
                    DbService.deleteTempOrder(strDispatchId);//删除派工ID临时表
                }
            }
            //输出日志
//            writeLog(strFieldWorkerId, strFieldWorkerName, responseBuffer == null ? "" : responseBuffer.toString(), count, rootPath, logsPath, filePath, totalCount);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;


    }

    /**
     * 输出日志
     *
     * @param strFieldWorkerId 外勤人员id
     * @param strFieldWorkerName 外勤人员姓名
     * @param responseBuffer 返回的响应
     * @param count              成功发送的条数
     * @param rootPath           Log保存的根路径
     * @param logsPath           Log保存的路径
     * @param filePath           log4j配置文件的路径
     */
    private static void writeLog(String strFieldWorkerId, String strFieldWorkerName, String responseBuffer, int count, String rootPath, String logsPath, String filePath, int totalCount) {
        //System.out.println("logsPath："+logsPath);
        System.setProperty("webappHome", logsPath);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.setProperty("date", df.format(new Date()).substring(0, 10));
        // 从Servlet参数读取log4j的配置文件
        if (filePath != null) {
            PropertyConfigurator.configure(rootPath + filePath);
        }
        logger.info("工作者的ID是:" + strFieldWorkerId + "," + "工作者的名字是" + strFieldWorkerName + "," + "响应状态:" + responseBuffer + "," + " 总发送条数：" + totalCount + "," + "已经发送成功条数:" + count);
    }
}
