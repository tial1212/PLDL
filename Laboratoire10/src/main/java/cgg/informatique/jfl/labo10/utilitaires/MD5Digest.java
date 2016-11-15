
/** **************************************************************
 * Copyright 2013 Code Strategies                                *
 * This code may be freely used and distributed in any project.  *
 * However, please do not remove this credit if you publish this *
 * code in paper or electronic form, such as on a web site.      *
 * FROM :http://www.avajava.com/tutorials/lessons/               *
 *             how-do-i-generate-an-md5-digest-for-a-string.html *
/************************************************************** **/


package cgg.informatique.jfl.labo10.utilitaires;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Digest {

	/**
	 * Get
	 * @param pOriginal
	 */
	public static String getMD5(String pOriginal) {
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(pOriginal.getBytes());
			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}
			
			System.out.println("original:" + pOriginal);
			System.out.println("digested(hex):" + sb.toString() );
			return sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Can't get MD5 unstance");
			e.printStackTrace();
			return"";
		}
	}

}
