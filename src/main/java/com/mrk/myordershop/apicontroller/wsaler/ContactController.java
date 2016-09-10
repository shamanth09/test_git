package com.mrk.myordershop.apicontroller.wsaler;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonView;
import com.mrk.myordershop.assembler.ContactAssembler;
import com.mrk.myordershop.assembler.OrderSearchAssembler;
import com.mrk.myordershop.assembler.linkprovider.ContactLinkProvider;
import com.mrk.myordershop.bean.Contact;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.SearchFilter;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.SearchResource;
import com.mrk.myordershop.bean.dto.View;
import com.mrk.myordershop.bean.dto.filter.ContactFilter;
import com.mrk.myordershop.constant.ContactGroup;
import com.mrk.myordershop.constant.view.Alert;
import com.mrk.myordershop.exception.DuplicateContactsException;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.resource.Resource;
import com.mrk.myordershop.security.oauth.resolver.Owner;
import com.mrk.myordershop.service.ContactService;
import com.mrk.myordershop.util.VCFReader;

import io.swagger.annotations.Api;

@Controller("apiWsalerContactController")
@RequestMapping("/api/v1/ws/contacts")
@Api(value="Contacts Details", tags={"Contacts Details"})
public class ContactController {

	final Logger log = Logger.getLogger(ContactController.class);

	@Autowired
	private ContactService contactService;
	@Autowired
	private OrderSearchAssembler searchAssembler;
	@Autowired
	private ContactAssembler contactAssembler;

	@Autowired
	private VCFReader vcfReader;

	@RequestMapping(value = "/{group}/import", method = RequestMethod.POST)
	public String uploadVcf(@Owner Wholesaler currentUser,
			@PathVariable String group, MultipartFile source,
			RedirectAttributes map) throws DuplicateContactsException {
		ContactGroup contactGroup = ContactGroup.valueOf(group.toUpperCase());
		if (source != null && source.getOriginalFilename().endsWith(".vcf")
				&& contactGroup != null) {
			try {
				List<Contact> contacts = vcfReader.read(source.getBytes());
				int count = contactService.addContacts(contacts, currentUser,
						contactGroup);
				map.addFlashAttribute("alert", new Alert(Alert.TYPE_SUCCESS,
						count + " Contacts Imported..."));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return "redirect:./import";
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Resource> save(@RequestBody Contact contact,
			@Owner Wholesaler currentUser) {
		contactService.addContact(contact, currentUser);
		return new ResponseEntity<Resource>(
				contactAssembler.toResource(contact), HttpStatus.OK);
	}

	@JsonView(View.ContactDetail.class)
	@RequestMapping(value = "/{contactId}", method = RequestMethod.PUT)
	public ResponseEntity<Contact> update(
			@PathVariable("contactId") Integer contactId,
			@RequestBody Contact contact, @Owner Wholesaler currentUser)
			throws EntityDoseNotExistException {
		contactService.update(contactId, contact, currentUser);
		return new ResponseEntity<Contact>(contact, HttpStatus.OK);
	}

	@JsonView(View.ContactDetail.class)
	@RequestMapping(value = "/{contactId}", method = RequestMethod.GET)
	public ResponseEntity<Resource> get(@PathVariable Integer contactId,
			@Owner Wholesaler currentUser) throws EntityDoseNotExistException {
		return new ResponseEntity<Resource>(
				contactAssembler.toResource(contactService.get(contactId,
						currentUser)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{contactId}", method = RequestMethod.DELETE)
	public ResponseEntity<Resource> delete(@PathVariable Integer contactId,
			@Owner Wholesaler currentUser) throws EntityDoseNotExistException {
		contactService.delete(contactId, currentUser);
		return new ResponseEntity<Resource>(HttpStatus.OK);
	}

	@JsonView(View.ContactBasic.class)
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> get(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String mobile,
			@RequestParam(required = false) String firmName,
			@ModelAttribute ContactFilter contactFilter,
			@Owner Wholesaler currentUser,
			@PageableDefault(value = 10) Pageable pageable,
			PagedResourcesAssembler<Contact> pagedAssembler) {
		Page<Contact> contacts = contactService.getContact(currentUser,
				contactFilter, pageable);
		return new ResponseEntity<PagedResources>(pagedAssembler.toResource(
				contacts, contactAssembler,
				ContactLinkProvider.get(contactFilter)), HttpStatus.OK);
	}

	@RequestMapping(value = "/mobile", method = RequestMethod.GET)
	public ResponseEntity getByMobile(@RequestParam String mobile,
			@Owner Wholesaler currentUser) throws EntityDoseNotExistException {
		Contact contact = contactService.getByMobile(mobile);
		return new ResponseEntity(contactAssembler.toResource(contact),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> searchInField(
			@RequestParam(required = false) SearchIn searchIn,
			@RequestParam(required = false) String query,
			@ModelAttribute SearchFilter searchFilter,
			@Owner Wholesaler currentUser,
			PagedResourcesAssembler<SearchResource> pagedResourcesAssembler) {
		List<SearchResource> contacts = contactService.search(currentUser,
				searchFilter);
		return new ResponseEntity<PagedResources>(
				pagedResourcesAssembler
						.toResource(new PageImpl<SearchResource>(contacts),
								searchAssembler),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/search/{field}", method = RequestMethod.GET)
	public ResponseEntity<PagedResources> search(@Owner Wholesaler currentUser,
			@PathVariable String field, @RequestParam String query,
			@PageableDefault(value = 20) Pageable pageable,
			PagedResourcesAssembler<Contact> pagedAssembler) {
		Page<Contact> contacts = null;
		if (field.equals("mobile")) {
			contacts = contactService.findByMobileNo(currentUser, query,
					pageable);
			return new ResponseEntity<PagedResources>(
					pagedAssembler.toResource(contacts), HttpStatus.OK);
		} else if (field.equals("name")) {
			contacts = contactService.findByName(currentUser, query, pageable);
			return new ResponseEntity<PagedResources>(
					pagedAssembler.toResource(contacts), HttpStatus.OK);
		}
		return new ResponseEntity<PagedResources>(HttpStatus.NOT_FOUND);
	}
}
