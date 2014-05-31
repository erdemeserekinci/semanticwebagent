package com.galaksiya.semanticweb.agent.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class Emailer {

	private ArrayList<String> recipients;

	private static Logger logger = Logger.getLogger("Mailer");
	private String password;

	public Emailer(String password) {
		this.password = password;
	}

	/**
	 * Sets mail destinations for further mailing progress.
	 * 
	 * @return Returns generated list.
	 */
	// TODO : get developers from ldap server..
	public ArrayList<String> setMailDestinations() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("erdemeserekinci@gmail.com");
		list.add("zgul.cabuk@gmail.com");
		list.add("pinargocebe@galaksiya.com");
		list.add("berkayakdal@gmail.com");
		return list;
	}

	/**
	 * Send an e-mail as plain text.
	 * 
	 * @param from
	 *            - mail's source.
	 * @param to
	 *            - mail's destination.
	 * @param subject
	 *            - mail's subject.
	 * @param text
	 *            - mail's content.
	 * @throws MessagingException
	 *             if it fails to send the mail.
	 * @throws AddressException
	 *             id any of the internet addresses (from or to) is not valid.
	 */
	public void send(final String from, String to, String subject, String text)
			throws MessagingException, AddressException {

		// Set mail properties and mail session.
		Properties props = setProperties();
		Session mailSession = setMailSession(from, props);

		Message simpleMessage = new MimeMessage(mailSession);

		InternetAddress fromAddress = setMailAddress(from);
		InternetAddress toAddress = setMailAddress(to);

		transportPlainTextMail(subject, text, simpleMessage, fromAddress,
				toAddress);
	}

	/**
	 * Creates a {@link MimeMessage}, attaches connected.jpg, check.png,
	 * failed.png and sends the message.
	 * 
	 * @param from
	 *            - this e-mail address will send the message.
	 * @param to
	 *            - this will be the receiver address.
	 * @param subject
	 *            - subject of the message.
	 * @param content
	 *            - content of the message (String of prepared HTML code).
	 * @throws MessagingException
	 *             if it fails to send the mail.
	 */
	public void sendHTMLMessage(final String from, String to, String subject,
			String content) throws MessagingException {
		Properties props = setProperties();

		Session mailSession = setMailSession(from, props);

		MimeMessage mimeMessage = new MimeMessage(mailSession);

		Multipart multiPart = new MimeMultipart("mixed");

//		// Attach images.
//		if (content.contains(URLVocabulary.CONNECTION_BROKEN_IMG)) {
//			multiPart.addBodyPart(attachImage("./img/not_connected.jpg", "<"
//					+ URLVocabulary.CONNECTION_BROKEN_IMG + ">"));
//			multiPart.addBodyPart(attachImage("./img/failed.png", "<"
//					+ URLVocabulary.FAILED_IMG + ">"));
//		} else
//			multiPart.addBodyPart(attachImage("./img/connected.jpg", "<"
//					+ URLVocabulary.CONNECTION_SUCCESSFULL_IMG + ">"));
//		multiPart.addBodyPart(attachImage("./img/check.png", "<"
//				+ URLVocabulary.CHECK_IMG + ">"));
//
//		new ChartViewer();
//		multiPart.addBodyPart(attachImage("./report.jpg", "<"
//				+ URLVocabulary.REPORT_CHART_IMG + ">"));

		// Create HTML part.
		BodyPart htmlPart = new MimeBodyPart();

		htmlPart.setContent(content, "text/html");
		multiPart.addBodyPart(htmlPart);
		mimeMessage.setContent(multiPart);

		// Prepare content.
		logger.debug("HTML content generated.");

		InternetAddress fromAddress = setMailAddress(from);
		InternetAddress toAddress = setMailAddress(to);
		logger.debug("from-to Mail addresses created.");

		transportHTMLFormattedMailMessage(subject, mimeMessage, fromAddress,
				toAddress);
		logger.info("send...");
	}

	protected BodyPart attachImage(String filePath, String attachmentName)
			throws MessagingException {
		BodyPart imgPartOne = new MimeBodyPart();
		DataSource ds1 = new FileDataSource(filePath);
		imgPartOne.setDataHandler(new DataHandler(ds1));

		imgPartOne.setHeader("Content-ID", attachmentName);
		return imgPartOne;
	}

	/**
	 * Generates an {@link InternetAddress} from given string.
	 * 
	 * @param address
	 *            - the string which will be converted to an
	 *            {@link InternetAddress}.
	 * @return Returns generated {@link InternetAddress}.
	 * @throws AddressException
	 *             if given string is not a valid address.
	 */
	private InternetAddress setMailAddress(String address)
			throws AddressException {
		InternetAddress toAddress = null;
		try {
			toAddress = new InternetAddress(address);
		} catch (AddressException e) {
			logger.fatal("CHECK MAIL ADDRESSES!!!");
			throw e;
		}
		return toAddress;
	}

	/**
	 * Sets mail session.
	 * 
	 * @param from
	 *            - mail's source.
	 * @param props
	 *            - the properties which will be used while sending mail.
	 * @return Returns mailSession.
	 */
	private Session setMailSession(final String from, Properties props) {
		Session mailSession = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {

					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, password);
					}
				});
		return mailSession;
	}

	/**
	 * Sets mail properties.
	 * 
	 * @return Returns properties.
	 */
	private Properties setProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		return props;
	}

	/**
	 * Transports the plain text mail message.
	 * 
	 * @param subject
	 *            - subject of message.
	 * @param text
	 *            - content of message.
	 * @param simpleMessage
	 *            - the message itself.
	 * @param fromAddress
	 *            - source of the message.
	 * @param toAddress
	 *            - destination of the message.
	 * @throws MessagingException
	 *             if the message could not send.
	 */
	private void transportPlainTextMail(String subject, String text,
			Message simpleMessage, InternetAddress fromAddress,
			InternetAddress toAddress) throws MessagingException {
		try {
			simpleMessage.setFrom(fromAddress);
			simpleMessage.setRecipient(RecipientType.TO, toAddress);
			simpleMessage.setSentDate(new Date());
			simpleMessage.setSubject(subject);
			simpleMessage.setText(text);
			Transport.send(simpleMessage);

		} catch (MessagingException e) {
			logger.fatal("E-MAIL CANNOT SENDED!!!");
			throw e;
		}
	}

	/**
	 * Transports the HTML formatted message.
	 * 
	 * @param subject
	 *            - subject of mail.
	 * @param mimeMessage
	 *            - the message itself.
	 * @param fromAddress
	 *            - source of mail.
	 * @param toAddress
	 *            - destination of mail.
	 * @throws MessagingException
	 *             if the message could not send.
	 */
	private void transportHTMLFormattedMailMessage(String subject,
			MimeMessage mimeMessage, InternetAddress fromAddress,
			InternetAddress toAddress) throws MessagingException {
		mimeMessage.setFrom(fromAddress);
		mimeMessage.setRecipient(Message.RecipientType.TO, toAddress);
		mimeMessage.setSentDate(new Date());
		mimeMessage.setSubject(subject);
		Transport.send(mimeMessage);
	}

	/**
	 * Sends information message to all members of given list.
	 * 
	 * @param mailDestinations
	 *            - The list of mail destinations.
	 * @throws MessagingException
	 *             when it fails to send an e-mail.
	 * @throws AddressException
	 *             when one of given addresses is not a valid mail address.
	 */
	public void sendMailsTo(ArrayList<String> mailDestinations, String content) {
		for (int index = 0; index < mailDestinations.size(); index++) {
			try {
				sendHTMLMessage("muse@galaksiya.com",
						mailDestinations.get(index), "Web Page Test Result",
						content);
			} catch (AddressException e1) {
				// Triggers when any member of recipients is not a valid
				// address.
				logger.fatal(e1.getMessage(), e1);
				throw new RuntimeException(e1.getMessage());

			} catch (MessagingException e1) {
				// Triggers when message could not be sended.
				logger.fatal(e1.getMessage(), e1);
				throw new RuntimeException(e1.getMessage());

			}
			logger.info("Mail send to : " + mailDestinations.get(index));
		}
	}

	/**
	 * @return Returns ArrayList<String> of message recipients.
	 */
	public ArrayList<String> getRecipients() {
		return recipients;
	}
}
