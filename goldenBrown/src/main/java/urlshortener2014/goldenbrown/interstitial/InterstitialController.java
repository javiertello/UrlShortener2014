package urlshortener2014.goldenbrown.interstitial;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
* Class that provide a web service that consist of prove if an URL can be reachable using
* the HEAD petition to the URL and avoid redirections. Also the Class has a Time Out of
* 5 seconds, after that if the page isn't reachable the answer is a Not Found HTTP.
* @author: Jorge,Javi,Gabi
* @version: 08/01/2015
*/
@RestController
public class InterstitialController {
	//Parameter that calculate the time out of the URL
	static final int WAITING_TIME_SEC = 10;
	
	private static final Logger logger = LoggerFactory.getLogger(InterstitialController.class);
	
	@Autowired
	private VelocityEngine velocityEngine;

	@RequestMapping(value = "/interstitial", method = RequestMethod.GET)
	public ResponseEntity<String> getInterstitial(@RequestParam("targetURL") String targetURL, 
			@RequestParam("interstitialURL") String interstitialURL){
		Map<String, Object> model = null;
		String html_text = null;
		
		logger.info("Generating interstitial, target: "+targetURL +", sponsor: "+interstitialURL);
		model = new HashMap<String, Object>();
		model.put("targetURL", targetURL);
		model.put("interstitialURL", interstitialURL);
		model.put("waitingTime", WAITING_TIME_SEC);
		html_text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				"interstitial.html", "UTF-8", model);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		ResponseEntity<String> re = new ResponseEntity<>(html_text, headers, HttpStatus.OK); 
		return re;
	}
}
