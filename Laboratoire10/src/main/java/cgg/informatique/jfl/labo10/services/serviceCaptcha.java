package cgg.informatique.jfl.labo10.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import cgg.informatique.jfl.labo10.utilitaires.Captcha;

public class serviceCaptcha {
	
	final static ArrayList<String> listeCaptcha = new ArrayList<String>( Arrays.asList(
			"aBc123",
			"D1spar5", 
			"LaM0rd1ta",
			"Gr00v6",
			"C4p7C8A",
			"A51osC4rl0s", 
			"S4yCh33s3",
			"t1t1T4t4"));
	;
	
	@Path("/getCaptcha")
	@GET
	public Captcha getCaptcha64(String pCaptcha) {
		Captcha captcha = new Captcha( pCaptcha   , 20);
		
	    return captcha;
	}
	
	public String getCaptchaStr() {
		return listeCaptcha.get(new Random().nextInt( listeCaptcha.size() ) );
	}

}


