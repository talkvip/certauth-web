/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanaldiyar.projects.certauth.web.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

/**
 *
 * @author kazim
 */
public class SecurityManager {

    private SecurityManager() {
    }

    private void init() {
        try {
            SecureRandom seed_sr = SecureRandom.getInstance("SHA1PRNG");
            SecureRandom key_sr = SecureRandom.getInstance("SHA1PRNG");
            key_sr.setSeed(seed_sr.generateSeed(64));

            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, key_sr);
            aesKey = kgen.generateKey();
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Error at init in security filter", ex);
        }

    }
    private static SecurityManager securityManager = null;

    public static SecurityManager getSecurityManager() {
        if (securityManager == null) {
            securityManager = new SecurityManager();
            securityManager.init();
        }
        return securityManager;
    }
    Logger logger = Logger.getLogger(SecurityManager.class);
    SecretKey aesKey;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String encrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
            return Hex.encodeHexString(encrypted);
        } catch (Exception ex) {
            Logger.getLogger(SecurityFilter.class).error("Error at encrypt in security filter", ex);
        }
        return null;
    }

    private String decrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] decrypted = cipher.doFinal(Hex.decodeHex(text.toCharArray()));
            return new String(decrypted, "UTF-8");
        } catch (Exception ex) {
            Logger.getLogger(SecurityFilter.class).error("Error at encrypt in security filter", ex);
        }
        return null;
    }

    public void checkAndRefleshSecurityToken() { 
        logger.debug("Security Manager is processing the request");
        HttpServletRequest request=RequestResponseContext.getHttpServletRequest(); 
        HttpServletResponse response=RequestResponseContext.getHttpServletResponse();
        
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("___securityToken")) {
                    logger.debug("Security Cookie founded");
                    String token = decrypt(cookie.getValue());
                    if (token != null) {
                        try {
                            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                            String[] parts = token.split("\\|");
                            Date lastAccess = simpleDateFormat.parse(parts[2]);
                            Date now = calendar.getTime();

                            if (now.getTime() < lastAccess.getTime()) {
                                calendar.add(Calendar.MINUTE, 15);
                                lastAccess = calendar.getTime();
                                String cookie_value = parts[0] + "|" + parts[1] + "|" + simpleDateFormat.format(lastAccess);
                                Cookie new_cookie = new Cookie("___securityToken", encrypt(cookie_value));
                                new_cookie.setMaxAge(15 * 60);
                                response.addCookie(new_cookie);
                            } else {
                                cookie.setMaxAge(0);
                            }

                        } catch (ParseException ex) {
                            logger.debug("Error at security manager", ex);
                        }

                        break;
                    } else {
                        logger.warn("Error at cookie decryption, may be a hijack!");
                    }
                    break;
                }
            }
        }
    }

    public void setSecurityToken(String username, String[] roles) {
        HttpServletResponse response=RequestResponseContext.getHttpServletResponse();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.add(Calendar.MINUTE, 15);
        Date lastAccess = calendar.getTime();
        String role = stringJoin(",", roles);

        String cookie_value = username + "|" + role + "|" + simpleDateFormat.format(lastAccess);
        Cookie new_cookie = new Cookie("___securityToken", encrypt(cookie_value));
        new_cookie.setMaxAge(15 * 60);
        response.addCookie(new_cookie);
    }

    private String stringJoin(String delim, String... strings) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < strings.length - 1; i++) {
            sb.append(strings[i]).append(delim);
        }
        sb.append(strings[strings.length - 1]);

        return sb.toString();
    }

    public boolean isAuthenticated() {
        logger.debug("Security Manager is processing the request");
        HttpServletRequest request=RequestResponseContext.getHttpServletRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("___securityToken")) {
                    logger.debug("Security Cookie founded");
                    String token = decrypt(cookie.getValue());
                    if (token != null) {
                        try {
                            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                            String[] parts = token.split("\\|");
                            Date lastAccess = simpleDateFormat.parse(parts[2]);
                            Date now = calendar.getTime();

                            if (now.getTime() < lastAccess.getTime()) {
                                return true;
                            } else {
                                return false;
                            }

                        } catch (ParseException ex) {
                            logger.debug("Error at security manager", ex);
                        }

                        break;
                    } else {
                        logger.warn("Error at cookie decryption, may be a hijack!");
                    }
                    break;
                }
            }
        }
        return false;
    }

    public boolean isInRole(String role) {
        logger.debug("Security Manager is processing the request");
        HttpServletRequest request=RequestResponseContext.getHttpServletRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("___securityToken")) {
                    logger.debug("Security Cookie founded");
                    String token = decrypt(cookie.getValue());
                    if (token != null) {
                        String[] parts = token.split("\\|");
                        String[] roles = parts[2].split(",");
                        for (String r : roles) {
                            if (r.equals(role)) {
                                return true;
                            }
                        }
                        return false;
                    } else {
                        logger.warn("Error at cookie decryption, may be a hijack!");
                    }
                    break;
                }
            }
        }
        return false;
    }
}
