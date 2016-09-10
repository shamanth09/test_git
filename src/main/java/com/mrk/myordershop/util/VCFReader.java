package com.mrk.myordershop.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mrk.myordershop.bean.Contact;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Email;
import ezvcard.property.FormattedName;
import ezvcard.property.Telephone;

@Component
public class VCFReader {

	public List<Contact> read(byte[] bs) {

		try {
			ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bs);
			return parse(arrayInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public List<Contact> parse(InputStream stream) throws IOException {

		List<VCard> vCards = Ezvcard.parse(stream).all();
		List<Contact> contacts = new ArrayList<Contact>();
		for (Object object : vCards) {
			VCard card = (VCard) object;
			Contact contact = new Contact();

			FormattedName formattedName = card.getFormattedName();
			// Gender gender = card.getGender();
			// Birthday birthday = card.getBirthday();
			List<Email> emails = card.getEmails();
			// List<Photo> photos = card.getPhotos();
			List<Telephone> telephones = card.getTelephoneNumbers();

			if (formattedName != null) {
				contact.setName(formattedName.getValue());

				// if (gender != null) {
				// contact.setSex(gender.getGender());
				// }
				// if (birthday != null) {
				// contact.setBirthDay(birthday.getText());
				// }
				if (emails != null && emails.size() > 0) {
					for (Email email : emails) {
						contact.setEmail(email.getValue());
					}
				}
				// if (photos != null && photos.size() > 0) {
				// for (Photo photo : photos) {
				// contact.setImage(photo.getData());
				// }
				// }
				if (telephones != null && telephones.size() > 0) {
					String mobileNo = telephones.get(0).getText()
							.replace("-", "").replace(" ", "").replace("+91", "").replace("(", "").replace(")", "").trim();
					if (mobileNo.startsWith("0"))
						mobileNo = mobileNo.substring(1);
					if (mobileNo.length() != 10)
						continue;

					contact.setMobile(mobileNo);
				}else {
					continue;
				}
				contacts.add(contact);
			}
		}

		return contacts;

	}
}
