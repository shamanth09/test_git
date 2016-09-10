package com.mrkinnoapps.myordershopadmin.validtors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.service.UserService;


@Component
public class UserValidator implements Validator {

	private Pattern pattern;  
	private Matcher matcher;  
	@Autowired
	private AddressValidator addressValidator;
	@Autowired
	private UserService userService;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"  
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";  
	String MOBILE_PATTERN = "[0-9]{10}";  

	@Override
	public boolean supports(Class<?> cls) {

		return User.class.isAssignableFrom(cls);
	}

	@SuppressWarnings("static-access")
	@Override
	public void validate(Object obj, Errors err) {
		User user=(User)obj;   

		ValidationUtils.rejectIfEmptyOrWhitespace(err, "name",
				"required", "Required field");

		ValidationUtils.rejectIfEmptyOrWhitespace(err, "email",
				"required", "Required field");

		ValidationUtils.rejectIfEmptyOrWhitespace(err, "mobile",
				"required", "Required field");

		if(user.getEmail()!=null && !(user.getEmail().isEmpty()))
		{
			pattern=pattern.compile(EMAIL_PATTERN);
			matcher=pattern.matcher(user.getEmail());
			if(!matcher.matches())
			{
				err.rejectValue("email", "incorrect",  
						"Enter a correct email");  
			}
			else {
				
				if(user.getId()==null)
				{
					try {
						User user2=userService.getUserByMobieOrEmail(user.getEmail());
						if(user2!=null)
							err.rejectValue("email", "exist",  
									"Entered email Id exist");  
					} catch (EntityDoseNotExistException e) {
						System.out.println("entuty dosn't exist!");
					}
				}
				else
				{
					try {
						User user2=userService.getUserByMobieOrEmail(user.getEmail());
						
						if(user2!=null)
						{
						   if(!user2.getId().equals(user.getId()))
							   err.rejectValue("email", "exist",  
										"Entered email Id exist");  
						}
					} catch (EntityDoseNotExistException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}

		if(user.getMobile()!=null && !(user.getMobile().isEmpty()))
		{
			pattern=pattern.compile(MOBILE_PATTERN);
			matcher=pattern.matcher(user.getMobile());
			if(!matcher.matches())
			{
				err.rejectValue("mobile", "incorrect", "10 digit mobile number is required");
			}
			else{
				if(user.getId()==null)
				{
					try {
						User user2=userService.getUserByMobieOrEmail(user.getMobile());
						if(user2!=null)
							err.rejectValue("mobile", "exist",  
									"Entered mobile number exist");  
					} catch (EntityDoseNotExistException e) {
						System.out.println("entuty dosn't exist!");
					}
				}
				else
				{

					try {
						User user2=userService.getUserByMobieOrEmail(user.getMobile());
						
						if(user2!=null)
						{
						   if(!user2.getId().equals(user.getId()))
							   err.rejectValue("mobile", "exist",  
										"Entered mobile number exist");  
						}
					} catch (EntityDoseNotExistException e) {
						e.printStackTrace();
					}
					
				}
			}
		}

		try{
			err.pushNestedPath("address");
			ValidationUtils.invokeValidator(this.addressValidator,user.getAddress(), err);
		}
		finally {
			err.popNestedPath();
		}

	}

}
