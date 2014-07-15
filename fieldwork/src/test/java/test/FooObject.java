package test;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public  class FooObject implements com.hazelcast.nio.DataSerializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7575016952877202642L;

		private String areaCode;

		private String uniqueId;

		private Integer value;


		
		
		public FooObject() {
			super();
		}

		public String getAreaCode() {
			return areaCode;
		}

		public void setAreaCode(String areaCode) {
			this.areaCode = areaCode;
		}

		public String getUniqueId() {
			return uniqueId;
		}

		public void setUniqueId(String uniqueId) {
			this.uniqueId = uniqueId;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

		@Override
		public void writeData(DataOutput out) throws IOException {
			System.out.println("11");
			out.writeUTF(areaCode);
			out.writeUTF(uniqueId);
			out.writeInt(value);
		}

		@Override
		public void readData(DataInput in) throws IOException {
			System.out.println("22");
			this.areaCode = in.readUTF();
			this.uniqueId = in.readUTF();
			this.value = in.readInt();

		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("FooObject [areaCode=").append(areaCode)
					.append(", uniqueId=").append(uniqueId).append(", value=")
					.append(value).append("]");
			return builder.toString();
		}
		
		public static FooObject create(String areaCode, String uniqueId, Integer value){
			FooObject foo=new FooObject();
			foo.areaCode = areaCode;
			foo.uniqueId = uniqueId;
			foo.value = value;
			return foo;
		}
		
		
		public static void main(String[]args) throws UnsupportedEncodingException{
//			String utf8 = new String("好".getBytes( "UTF-8"));  
//			System.out.println(utf8);  
			System.out.println(System.getProperty("file.encoding"));
			String utf8="好";
			System.out.println("utf8 bytes:"+Arrays.toString(utf8.getBytes()));
			String unicode = new String(utf8.getBytes(),"UTF-8");   
			System.out.println(unicode);  
			String gbk = new String(unicode.getBytes("GBK"));  
			System.out.println("gbk bytes"+Arrays.toString(gbk2utf8(gbk)));
//			System.out.println(new String(gbk.getBytes("iso-8859-1"),"GBK"));
			
			  
			System.out.println(gbk);  
		}
		
		public static byte[] gbk2utf8(String chenese) {
			char c[] = chenese.toCharArray();
			byte[] fullByte = new byte[3 * c.length];
			for (int i = 0; i < c.length; i++) {
				int m = (int) c[i];
				String word = Integer.toBinaryString(m);

				StringBuffer sb = new StringBuffer();
				int len = 16 - word.length();
				for (int j = 0; j < len; j++) {
					sb.append("0");
				}
				sb.append(word);
				sb.insert(0, "1110");
				sb.insert(8, "10");
				sb.insert(16, "10");

				String s1 = sb.substring(0, 8);
				String s2 = sb.substring(8, 16);
				String s3 = sb.substring(16);

				byte b0 = Integer.valueOf(s1, 2).byteValue();
				byte b1 = Integer.valueOf(s2, 2).byteValue();
				byte b2 = Integer.valueOf(s3, 2).byteValue();
				byte[] bf = new byte[3];
				bf[0] = b0;
				fullByte[i * 3] = bf[0];
				bf[1] = b1;
				fullByte[i * 3 + 1] = bf[1];
				bf[2] = b2;
				fullByte[i * 3 + 2] = bf[2];

			}
			return fullByte;
		}


	}




