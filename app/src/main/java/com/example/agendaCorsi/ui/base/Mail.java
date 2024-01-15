package com.example.agendaCorsi.ui.base;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.access.CredenzialeDAO;

import java.util.Date;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail extends javax.mail.Authenticator {
    private static Mail INSTANCE = null;

    private static String _user;
    private static String _pass;
    private String[] _to;
    private static String _from;
    private static String _port;
    private static String _socketFactoryPort;
    private static String _host;
    private static String _subject;
    private static String _body;
    private static boolean _auth;
    private static boolean _debuggable;
    private static Multipart _multipart;

    public static PropertyReader propertyReader;
    public static Properties properties;

    public static Mail getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Mail();
            propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
            properties = propertyReader.getMyProperties("config.properties");

            _host = properties.getProperty("EMAIL_HOST");
            _port = properties.getProperty("EMAIL_PORT");
            _socketFactoryPort = properties.getProperty("EMAIL_SOCKET_FACTORY_PORT");

            _user = CredenzialeDAO.getInstance().getUtenteCorrente();
            _pass = CredenzialeDAO.getInstance().getPasswordCorrente();
            _from = "";             // email del mittente
            _subject = "";          // oggetto della mail
            _body = "";             // corpo della mail

            _debuggable = false;    // debug mode (on / off) - default off
            _auth = true;           // smtp authentication - default on
            _multipart = new MimeMultipart();

            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap(properties.getProperty("EMAIL_CAP_TEXT_HTML"));
            mc.addMailcap(properties.getProperty("EMAIL_CAP_TEXT_XML"));
            mc.addMailcap(properties.getProperty("EMAIL_CAP_TEXT_PLAIN"));
            mc.addMailcap(properties.getProperty("EMAIL_CAP_MULTIPART"));
            mc.addMailcap(properties.getProperty("EMAIL_CAP_RCF822"));
            CommandMap.setDefaultCommandMap(mc);
        }
        return INSTANCE;
    }

    private Mail() {}

    public boolean send() throws Exception {
        Properties props = _setProperties();

        if(!_user.equals("") && !_pass.equals("") && _to.length > 0 && !_from.equals("") && !_subject.equals("") && !_body.equals("")) {
            Session session = Session.getInstance(props, this);

            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(_from));

            InternetAddress[] addressTo = new InternetAddress[_to.length];
            for (int i = 0; i < _to.length; i++) {
                addressTo[i] = new InternetAddress(_to[i]);
            }
            msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

            msg.setSubject(_subject);
            msg.setSentDate(new Date());

            // creo il messaggio multipart
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(_body);
            _multipart.addBodyPart(messageBodyPart);

            // imposto il contenuto del messaggio
            msg.setContent(_multipart);

            // invio la mail
            Transport.send(msg);

            return true;
        } else {
            return false;
        }
    }

    public void addAttachment(String filename) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);

        _multipart.addBodyPart(messageBodyPart);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(_user, _pass);
    }

    private Properties _setProperties() {
        Properties properties = new Properties();

        properties.put("mail.smtp.host", _host);

        if(_debuggable) {
            properties.put("mail.debug", "true");
        }

        if(_auth) {
            properties.put("mail.smtp.auth", "true");
        }

        properties.put("mail.smtp.port", _port);
        properties.put("mail.smtp.socketFactory.port", _socketFactoryPort);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");

        return properties;
    }

    // getters e setters

    public String getBody() {
        return _body;
    }

    public void setBody(String _body) {
        this._body = _body;
    }

    public void setTo(String[] toArr) {
        this._to = toArr;
    }

    public void setFrom(String string) {
        this._from = string;
    }

    public void setSubject(String string) {
        this._subject = string;
    }
}
