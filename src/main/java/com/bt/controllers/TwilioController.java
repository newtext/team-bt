package com.bt.controllers;

import com.bt.dao.PhoneNumberDao;
import com.bt.dao.PhoneNumberDaoImpl;
import com.bt.util.TwilioUtil;
import com.twilio.Twilio;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

@RestController
public class TwilioController {

    @Autowired
    TwilioUtil twilioUtil;

    @RequestMapping(value="/twilio/message",method= RequestMethod.POST)
    public void getSMS(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Twilio.init(twilioUtil.getAccountSid(),twilioUtil.getAuthToken());

        String fromNumber = req.getParameter("From");

        String imageUrl = req.getParameter("MediaUrl0");
        System.out.println(req.getParameter(imageUrl));

        String base64 = null;

        System.out.println(fromNumber);


        if (imageUrl != null){
            URLConnection conn;
            try {
                conn = new URL(imageUrl).openConnection();

                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                byte[] imageBytes = IOUtils.toByteArray(conn);
                base64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
            } catch (IOException e) {
                System.out.println("image not found" + e);
            }
        }

        System.out.println(base64);
        postImages(fromNumber, base64);
    }

    public void postImages(String fromNumber, String base64){
        String phoneNumber = fromNumber.substring(2);
        ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
        PhoneNumberDao phDao = (PhoneNumberDaoImpl) ac.getBean("phoneNumberDaoImpl");
        phDao.postImageFromNumber(phoneNumber,base64);
    }


}
