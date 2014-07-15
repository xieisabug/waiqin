package com.sealion.serviceassistant.tools;

import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * 操作文件工具
 */
public class FileTools {
    private static final String TAG = FileTools.class.getSimpleName();
    private static String SDCARD_PATH = Environment.getExternalStorageDirectory() + "/";

    /**
     * 取得压缩包中的 文件列表(文件夹,文件自选)
     *
     * @param zipFileString  压缩包名字
     * @param bContainFolder 是否包括 文件夹
     * @param bContainFile   是否包括 文件
     * @return 文件list
     * @throws Exception
     */
    public static ArrayList<File> GetFileList(String zipFileString, boolean bContainFolder,
                                              boolean bContainFile) throws Exception {

        ArrayList<File> fileList = new ArrayList<File>();
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";

        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();

            if (zipEntry.isDirectory()) {
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(szName);
                if (bContainFolder) {
                    fileList.add(folder);
                }

            } else {
                File file = new File(szName);
                if (bContainFile) {
                    fileList.add(file);
                }
            }
        }// end of while

        inZip.close();

        return fileList;
    }

    /**
     * 返回压缩包中的文件InputStream
     *
     * @param zipFileString 压缩文件的名字
     * @param fileString    解压文件的名字
     * @return InputStream
     * @throws Exception
     */
    public static InputStream UpZip(String zipFileString, String fileString) throws Exception {
        ZipFile zipFile = new ZipFile(zipFileString);
        ZipEntry zipEntry = zipFile.getEntry(fileString);

        return zipFile.getInputStream(zipEntry);

    }

    /**
     * 编码转换
     * @param str 需要转码的字符串
     * @param newCharset 目标码
     * @return 转码后的字符串
     */
    public static String changeCharset(String str, String newCharset) {
        if (str != null) {
            try {
                // 用默认字符编码解码字符串。
                byte[] bs = str.getBytes();
                // 用新的字符编码生成字符串
                return new String(bs, newCharset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 解压文件到目录
     * @param archive 解压的文件
     * @param decompressDir 目标目录
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ZipException
     */
    public static void readByApacheZipFile(String archive, String decompressDir)
            throws IOException, FileNotFoundException, ZipException {
        BufferedInputStream bi;

        ZipFile zf = new ZipFile(archive);// 支持中文
        Enumeration e = zf.entries();

        while (e.hasMoreElements()) {
            ZipEntry ze2 = (ZipEntry) e.nextElement();

            String entryName = ze2.getName();
            String path = decompressDir + "/" + entryName;

            if (ze2.isDirectory()) {
                System.out.println("正在创建解压目录 - " + entryName);

                File decompressDirFile = new File(path);

                if (!decompressDirFile.exists()) {
                    decompressDirFile.mkdirs();
                }
            } else {
                System.out.println("正在创建解压文件 - " + entryName);
                String fileDir = path.substring(0, path.lastIndexOf("/"));
                File fileDirFile = new File(fileDir);

                if (!fileDirFile.exists()) {
                    fileDirFile.mkdirs();
                }

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
                        decompressDir + "/" + entryName));
                bi = new BufferedInputStream(zf.getInputStream(ze2));
                byte[] readContent = new byte[1024];
                int readCount = bi.read(readContent);
                while (readCount != -1) {
                    bos.write(readContent, 0, readCount);
                    readCount = bi.read(readContent);
                }
                bos.close();
            }

        }
        zf.close();
    }

    /**
     * 解压一个压缩文档 到指定位置
     *
     * @param zipFileString 压缩包的名字
     * @param outPathString 指定的路径
     * @throws Exception
     */
    public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));

        ZipEntry zipEntry;
        String szName = "";

        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();

            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                File file = new File(outPathString + File.separator + szName);
                if (file.createNewFile()) {
                    // get the output stream of the file
                    FileOutputStream out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[1024];
                    // read (len) bytes into buffer
                    while ((len = inZip.read(buffer)) != -1) {
                        // write (len) byte from buffer at the position 0
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                }
            }
        }// end of while

        inZip.close();

    }// end of func

    /**
     * 压缩文件,文件夹
     *
     * @param srcFileString 要压缩的文件/文件夹名字
     * @param zipFileString 指定压缩的目的和名字
     * @throws Exception
     */
    public static void ZipFolder(String srcFileString, String zipFileString) throws Exception {
        Log.v("XZip", "ZipFolder(String, String)");

        // 创建Zip包
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));

        // 打开要输出的文件
        File file = new File(srcFileString);

        // 压缩
        ZipFiles(file.getParent() + File.separator, file.getName(), outZip);

        // 完成,关闭
        outZip.finish();
        outZip.close();

    }// end of func

    /**
     * 压缩文件
     * @param folderString 文件夹路径
     * @param fileString 文件夹下文件名
     * @param zipOutputSteam zip文件的输出流
     * @throws Exception
     */
    private static void ZipFiles(String folderString, String fileString,
                                 ZipOutputStream zipOutputSteam) throws Exception {
        android.util.Log.v("XZip", "ZipFiles(String, String, ZipOutputStream)");

        if (zipOutputSteam == null)
            return;

        File file = new File(folderString + fileString);

        // 判断是不是文件
        if (file.isFile()) {

            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);

            int len;
            byte[] buffer = new byte[4096];

            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }

            zipOutputSteam.closeEntry();
        } else {

            // 文件夹的方式,获取文件夹下的子文件
            String fileList[] = file.list();

            // 如果没有子文件, 则添加进去即可
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }

            // 如果有子文件, 遍历子文件
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString, fileString + File.separator + fileList[i], zipOutputSteam);
            }// end of for

        }// end of if

    }// end of func

    @Override
    public void finalize() throws Throwable {

    }

    /**
     * 创建路径或者文件夹
     * @param path 需要创建的路径
     */
    public static void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Log.d(TAG, "create folder......" + path);
            boolean result = file.mkdir();
            if (result) {
                Log.d(TAG, "create folder success......");
            } else {
                Log.d(TAG, "create folder failure......");
            }
        } else {
            Log.d(TAG, "file folder is exist......");
        }
    }

    /**
     * 判断设备是否有sd卡
     * @return 是否有sd卡
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    // 删除文件夹
    // param folderPath 文件夹完整绝对路径

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            File myFilePath = new File(folderPath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一个路径下所有文件
     * @param path 文件路径
     * @return 删除是否成功
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return false;
        }
        String[] tempList = file.list();
        File temp;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取指定目录下的所有文件
     *
     * @param dirPath 指定路径
     * @return 文件名list
     */
    public static ArrayList<String> GetFileByDirectory(String dirPath) {
        ArrayList<String> list = new ArrayList<String>();
        File directory = new File(dirPath);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++) {
            list.add(files[i].getName());
        }
        return list;
    }

    /**
     * 上传文件
     * @param uploadUrl 上传请求URL
     * @param srcPath   上传文件的路径
     */
    public static void uploadFile(String uploadUrl, String srcPath) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary="
                    + boundary);

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + srcPath.substring(srcPath.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);

            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();

            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }

    }

    /**
     * 保存文件
     * @param path       保存路径
     * @param filename   文件名
     * @param localpath  不存在sd卡情况的本地路径
     * @param photoBytes 文件字节数组
     */
    public static String FileSave(String path, String filename, File localpath, byte[] photoBytes) {
        String filePath = SDCARD_PATH + path;
        Log.i(TAG, "readFile:createDir=" + SDCARD_PATH + path);
        if (!FileTools.hasSdcard()) {
            filePath = localpath + "/" + path;
        }

        File file = new File(filePath);
        if (file.exists()) {
            FileTools.delAllFile(filePath);
        }
        if (!file.exists()) {
            try {
                // 按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File imgfile = new File(file.getPath() + "/" + filename);
        if (!imgfile.exists()) {
            try {
                Log.d(TAG, "create new file");
                // 在指定的文件夹中创建文件
                imgfile.createNewFile();
            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imgfile);
            fos.write(photoBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return filePath;
    }

    /**
     * 通过Socket上传文件
     * @param address       地址
     * @param port          端口
     * @param uploadFile    上传的文件文件路径
     * @param order_num     工单号
     * @param file_type     文件类型 “image”:图片，“audio”:音频
     * @param fieldworkerid 员工id
     * @throws IOException
     */
    public static void UploadFileBySocket(String address, int port, File uploadFile, String order_num, String file_type, String fieldworkerid) throws IOException {
        String head = "Content-Length=" + uploadFile.length() + ";filename=" + uploadFile.getName()
                + ";orderid=" + order_num + ";fieldworkerid=" + fieldworkerid + ";filetype=" + file_type + "\r\n";
        Log.d(TAG, "head : " + head);
        Socket socket = new Socket(address, port);
        OutputStream outStream = socket.getOutputStream();
        outStream.write(head.getBytes());

        PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
        String response = StreamTool.readLine(inStream);
        String[] items = response.split(";");
        Log.d(TAG, "items : " + Arrays.toString(items));
        String responseid = items[0].substring(items[0].indexOf("=") + 1);
        String position = items[1].substring(items[1].indexOf("=") + 1);

        RandomAccessFile fileOutStream = new RandomAccessFile(uploadFile, "r");
        fileOutStream.seek(Integer.valueOf(position));
        byte[] buffer = new byte[1024];
        int len = -1;
        int length = Integer.valueOf(position);
        while ((len = fileOutStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
            length += len;
        }
        fileOutStream.close();
        outStream.close();
        inStream.close();
        socket.close();
    }
}

