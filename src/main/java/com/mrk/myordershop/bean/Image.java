package com.mrk.myordershop.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.codec.Base64;

import com.mrk.myordershop.constant.ActiveFlag;

@Entity
@Table(name = "MOS_IMAGE")
public class Image implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 7134511248145749117L;

	public enum Scale {
		LARGE(1000, 1000);
		private int height;
		private int width;

		private Scale(int height, int width) {
			this.height = height;
			this.width = width;
		}
	};

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Enumerated(EnumType.STRING)
	@Column(name = "ACTIVE_FLAG")
	private ActiveFlag activeFlag;

	@Column(name = "IMAGE", columnDefinition = "LONGBLOB")
	private byte[] imageArray;

	public Image() {
	}

	public Image(byte[] imageByte) {
		this.imageArray = imageByte;
	}

	public String getProductImageByte() {
		return new String(Base64.encode(this.imageArray));
	}

//	public byte[] getImageArray(Scale scale) {
//		ByteArrayInputStream inputStream = new ByteArrayInputStream(this.imageArray);
//		ByteArrayOutputStream bos = null;
//		try {
//			BufferedImage originalImage = ImageIO.read(inputStream);
//			java.awt.Image img = originalImage.getScaledInstance(scale.width, scale.height, BufferedImage.SCALE_SMOOTH);
//			BufferedImage bufferedImage = new BufferedImage(scale.width, scale.height, originalImage.getType());
//			Graphics2D gc = bufferedImage.createGraphics();
//			gc.drawImage(img, 0, 0, null);
//			gc.dispose();
//			gc.setComposite(AlphaComposite.Src);
//
//			gc.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//			gc.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//			gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//			bos = new ByteArrayOutputStream();
//			ImageIO.write(bufferedImage, "png", bos);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		// return bos.toByteArray();
//		return imageArray;
//	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	public byte[] getImageArray() {
		return imageArray;
	}

	public void setImageArray(byte[] imageArray) {
		this.imageArray = imageArray;
	}

}
