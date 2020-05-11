package com.touchus.publicutils.utils;

import a_vcard.android.syncml.pim.vcard.VCardParser_V21;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailManager {
    public static final String ACTION_FEEDBACK_MAIL = "com.unibroad.mail";
    public static final String FEEDBACK_KEY = "feedback_result";
    private static final String KEY_MAIL_AUTH = "mail.smtp.auth";
    private static final String KEY_MAIL_HOST = "mail.smtp.host";
    private static final String SENDER_NAME = "bug@unibroad.com";
    private static final String SENDER_PASS = "BUGzero0";
    private static final String VALUE_MAIL_AUTH = "true";
    private static final String VALUE_MAIL_HOST = "smtp.exmail.qq.com";
    /* access modifiers changed from: private */
    public static Context mContext;

    public static MailManager getInstance(Context context) {
        mContext = context;
        return InstanceHolder.instance;
    }

    private MailManager() {
    }

    /* synthetic */ MailManager(MailManager mailManager) {
        this();
    }

    private static class InstanceHolder {
        /* access modifiers changed from: private */
        public static MailManager instance = new MailManager((MailManager) null);

        private InstanceHolder() {
        }
    }

    class MailTask extends AsyncTask<Void, Void, Boolean> {
        private MimeMessage mimeMessage;

        public MailTask(MimeMessage mimeMessage2) {
            this.mimeMessage = mimeMessage2;
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(Void... voids) {
            Bundle bundle = new Bundle();
            try {
                Transport.send(this.mimeMessage);
                bundle.putBoolean(MailManager.FEEDBACK_KEY, true);
                Intent intent = new Intent();
                intent.setAction(MailManager.ACTION_FEEDBACK_MAIL);
                intent.putExtras(bundle);
                MailManager.mContext.sendBroadcast(intent);
                return Boolean.TRUE;
            } catch (MessagingException e) {
                e.printStackTrace();
                bundle.putBoolean(MailManager.FEEDBACK_KEY, false);
                Intent intent2 = new Intent();
                intent2.setAction(MailManager.ACTION_FEEDBACK_MAIL);
                intent2.putExtras(bundle);
                MailManager.mContext.sendBroadcast(intent2);
                return Boolean.FALSE;
            }
        }
    }

    public void sendMail(String title, String content) {
        new MailTask(createMessage(title, content)).execute(new Void[0]);
    }

    public void sendMailWithFile(String title, String content, String filePath) {
        MimeMessage mimeMessage = createMessage(title, content);
        appendFile(mimeMessage, filePath);
        new MailTask(mimeMessage).execute(new Void[0]);
    }

    public void sendMailWithMultiFile(String title, String content, List<String> pathList) {
        MimeMessage mimeMessage = createMessage(title, content);
        appendMultiFile(mimeMessage, pathList);
        new MailTask(mimeMessage).execute(new Void[0]);
    }

    private Authenticator getAuthenticator() {
        return new Authenticator() {
            /* access modifiers changed from: protected */
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailManager.SENDER_NAME, MailManager.SENDER_PASS);
            }
        };
    }

    private MimeMessage createMessage(String title, String content) {
        Properties properties = System.getProperties();
        properties.put(KEY_MAIL_HOST, VALUE_MAIL_HOST);
        properties.put(KEY_MAIL_AUTH, VALUE_MAIL_AUTH);
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(properties, getAuthenticator()));
        try {
            mimeMessage.setFrom(new InternetAddress(SENDER_NAME));
            mimeMessage.setRecipients(Message.RecipientType.TO, (Address[]) new InternetAddress[]{new InternetAddress(SENDER_NAME)});
            mimeMessage.setSubject(title);
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(content, "text/html");
            textPart.setText(content, VCardParser_V21.DEFAULT_CHARSET);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            mimeMessage.setContent(multipart);
            mimeMessage.setSentDate(new Date());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return mimeMessage;
    }

    private void appendFile(MimeMessage message, String filePath) {
        try {
            MimeBodyPart filePart = new MimeBodyPart();
            filePart.attachFile(filePath);
            ((Multipart) message.getContent()).addBodyPart(filePart);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e2) {
            e2.printStackTrace();
        }
    }

    private void appendMultiFile(MimeMessage message, List<String> pathList) {
        try {
            Multipart multipart = (Multipart) message.getContent();
            for (String path : pathList) {
                MimeBodyPart filePart = new MimeBodyPart();
                filePart.attachFile(path);
                multipart.addBodyPart(filePart);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e2) {
            e2.printStackTrace();
        }
    }
}
