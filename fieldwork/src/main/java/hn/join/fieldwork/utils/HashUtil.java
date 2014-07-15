package hn.join.fieldwork.utils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Md5Hash;
/**
 * HASH编码工具类
 * @author chenjinlong
 *
 */
public class HashUtil {

	public static String encodeMd5Hex(String plainText) {
		Hash hash = new Md5Hash(plainText);
		return hash.toHex();

	}
	
	public static byte[] decodeBase64(byte[] base64Data){
		return Base64.decode(base64Data);
	}
	
	
	public static void main(String[] args){
		System.out.println(encodeMd5Hex("111111"));
	}

}
