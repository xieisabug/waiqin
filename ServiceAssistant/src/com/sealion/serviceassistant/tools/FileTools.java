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
 * �����ļ�����
 */
public class FileTools {
    private static final String TAG = FileTools.class.getSimpleName();
    private static String SDCARD_PATH = Environment.getExternalStorageDirectory() + "/";

    /**
     * ȡ��ѹ�����е� �ļ��б�(�ļ���,�ļ���ѡ)
     *
     * @param zipFileString  ѹ��������
     * @param bContainFolder �Ƿ���� �ļ���
     * @param bContainFile   �Ƿ���� �ļ�
     * @return �ļ�list
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
     * ����ѹ�����е��ļ�InputStream
     *
     * @param zipFileString ѹ���ļ�������
     * @param fileString    ��ѹ�ļ�������
     * @return InputStream
     * @throws Exception
     */
    public static InputStream UpZip(String zipFileString, String fileString) throws Exception {
        ZipFile zipFile = new ZipFile(zipFileString);
        ZipEntry zipEntry = zipFile.getEntry(fileString);

        return zipFile.getInputStream(zipEntry);

    }

    /**
     * ����ת��
     * @param str ��Ҫת����ַ���
     * @param newCharset Ŀ����
     * @return ת�����ַ���
     */
    public static String changeCharset(String str, String newCharset) {
        if (str != null) {
            try {
                // ��Ĭ���ַ���������ַ�����
                byte[] bs = str.getBytes();
                // ���µ��ַ����������ַ���
                return new String(bs, newCharset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * ��ѹ�ļ���Ŀ¼
     * @param archive ��ѹ���ļ�
     * @param decompressDir Ŀ��Ŀ¼
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ZipException
     */
    public static void readByApacheZipFile(String archive, String decompressDir)
            throws IOException, FileNotFoundException, ZipException {
        BufferedInputStream bi;

        ZipFile zf = new ZipFile(archive);// ֧������
        Enumeration e = zf.entries();

        while (e.hasMoreElements()) {
            ZipEntry ze2 = (ZipEntry) e.nextElement();

            String entryName = ze2.getName();
            String path = decompressDir + "/" + entryName;

            if (ze2.isDirectory()) {
                System.out.println("���ڴ�����ѹĿ¼ - " + entryName);

                File decompressDirFile = new File(path);

                if (!decompressDirFile.exists()) {
                    decompressDirFile.mkdirs();
                }
            } else {
                System.out.println("���ڴ�����ѹ�ļ� - " + entryName);
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
     * ��ѹһ��ѹ���ĵ� ��ָ��λ��
     *
     * @param zipFileString ѹ����������
     * @param outPathString ָ����·��
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
     * ѹ���ļ�,�ļ���
     *
     * @param srcFileString Ҫѹ�����ļ�/�ļ�������
     * @param zipFileString ָ��ѹ����Ŀ�ĺ�����
     * @throws Exception
     */
    public static void ZipFolder(String srcFileString, String zipFileString) throws Exception {
        Log.v("XZip", "ZipFolder(String, String)");

        // ����Zip��
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));

        // ��Ҫ������ļ�
        File file = new File(srcFileString);

        // ѹ��
        ZipFiles(file.getParent() + File.separator, file.getName(), outZip);

        // ���,�ر�
        outZip.finish();
        outZip.close();

    }// end of func

    /**
     * ѹ���ļ�
     * @param folderString �ļ���·��
     * @param fileString �ļ������ļ���
     * @param zipOutputSteam zip�ļ��������
     * @throws Exception
     */
    private static void ZipFiles(String folderString, String fileString,
                                 ZipOutputStream zipOutputSteam) throws Exception {
        android.util.Log.v("XZip", "ZipFiles(String, String, ZipOutputStream)");

        if (zipOutputSteam == null)
            return;

        File file = new File(folderString + fileString);

        // �ж��ǲ����ļ�
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

            // �ļ��еķ�ʽ,��ȡ�ļ����µ����ļ�
            String fileList[] = file.list();

            // ���û�����ļ�, ����ӽ�ȥ����
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }

            // ��������ļ�, �������ļ�
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString, fileString + File.separator + fileList[i], zipOutputSteam);
            }// end of for

        }// end of if

    }// end of func

    @Override
    public void finalize() throws Throwable {

    }

    /**
     * ����·�������ļ���
     * @param path ��Ҫ������·��
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
     * �ж��豸�Ƿ���sd��
     * @return �Ƿ���sd��
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    // ɾ���ļ���
    // param folderPath �ļ�����������·��

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // ɾ����������������
            File myFilePath = new File(folderPath);
            myFilePath.delete(); // ɾ�����ļ���
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ɾ��һ��·���������ļ�
     * @param path �ļ�·��
     * @return ɾ���Ƿ�ɹ�
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
                delAllFile(path + "/" + tempList[i]);// ��ɾ���ļ���������ļ�
                delFolder(path + "/" + tempList[i]);// ��ɾ�����ļ���
                flag = true;
            }
        }
        return flag;
    }

    /**
     * ��ȡָ��Ŀ¼�µ������ļ�
     *
     * @param dirPath ָ��·��
     * @return �ļ���list
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
     * �ϴ��ļ�
     * @param uploadUrl �ϴ�����URL
     * @param srcPath   �ϴ��ļ���·��
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
     * �����ļ�
     * @param path       ����·��
     * @param filename   �ļ���
     * @param localpath  ������sd������ı���·��
     * @param photoBytes �ļ��ֽ�����
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
                // ����ָ����·�������ļ���
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File imgfile = new File(file.getPath() + "/" + filename);
        if (!imgfile.exists()) {
            try {
                Log.d(TAG, "create new file");
                // ��ָ�����ļ����д����ļ�
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
     * ͨ��Socket�ϴ��ļ�
     * @param address       ��ַ
     * @param port          �˿�
     * @param uploadFile    �ϴ����ļ��ļ�·��
     * @param order_num     ������
     * @param file_type     �ļ����� ��image��:ͼƬ����audio��:��Ƶ
     * @param fieldworkerid Ա��id
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

