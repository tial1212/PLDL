package cgg.informatique.jfl.labo10.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import cgg.informatique.jfl.labo10.utilitaires.Captcha;

public class serviceCaptcha {
	
	/**
	 * An array of random captcha
	 */
	final static ArrayList<String> listeCaptcha = new ArrayList<String>( Arrays.asList(
			"aBc 123",
			"D1spar5", 
			"La M0rd1ta",
			"Gr00v6",
			"C4p7 C8A",
			"A51osC4rl0s", 
			"S4yCh33s3",
			"l1k31W0uld",
			"T00 g00d 4 U",
			"1F0nl6",
			"Bac0n B1t",
			"t1t1 T4t4"));
	;
	
	
	/**
	 * Get a Base 64 captcha of adesired string.
	 * @param pCaptcha , the text to be transformed into an image.
	 * @return captcha64
	 */
	@Path("/getCaptcha")
	@GET
	public static String getCaptcha64(String pCaptcha) {
		Captcha captcha = new Captcha( pCaptcha   , 20);
		return captcha.getCatpcha();
	}
	
	/**
	 * Get a random captcha from a static list.
	 * @return captchaStr
	 */
	public static String getRdmCaptchaStr() {
		return listeCaptcha.get(new Random().nextInt( listeCaptcha.size() ) );
	}

}


