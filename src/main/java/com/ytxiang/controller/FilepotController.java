package com.ytxiang.controller;

import java.util.List; 

import com.amazonaws.services.cloudfront.model.Method;
import com.ytxiang.bean.FileUploadForm;
import com.ytxiang.bean.UserBean;
import com.ytxiang.dto.S3FilePotDTO;
import com.ytxiang.dto.UserDTO;
import com.ytxiang.service.FilepotService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@Controller
public class FilepotController {

	@Autowired
	FilepotService filepotService;

    @RequestMapping("/")
    public String home(Model model) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null)
		    return "redirect:/signon";

	    boolean isAdmin = auth.getAuthorities().stream()
		    .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
	    S3FilePotDTO fpDto = isAdmin ? filepotService.getFileList() :
		    filepotService.getFileList(auth.getName());
	    model.addAttribute("files", fpDto.getFileList());

	    return isAdmin ? "admin" : "index";
    }

	@RequestMapping(value="/register", method = RequestMethod.GET)
    public String register(Model model) {
	return "register";
	}

	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String signUp(Model model, @ModelAttribute UserBean user) {
		try {
			filepotService.signUpUser(user);
			model.addAttribute("msg", "User " + user.getUsername() + " successfully registered.");
			model.addAttribute("account", user);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "register";
	}

	@PostMapping(value = "/file/upload")
	public String fileUpload(Model model,
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "notes", required = false) String notes) {
		if (file.isEmpty()) {
			model.addAttribute("error", "Please select a file to upload");
			return "index";
		}

		try {
			// Get the file and try to save it to S3
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			filepotService.upload(file, notes, userName);
			model.addAttribute("msg", "You successfully uploaded '" + file.getOriginalFilename() + "'");
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "index";
	}

	@RequestMapping(value="/file/{id}/update", method = RequestMethod.POST)
	@ResponseBody
	public String updateFile(@PathVariable("id") String id,
			/*
			@RequestParam(value = "file") MultipartFile file,
			@RequestParam(value = "notes") String notes
			 */
			@ModelAttribute FileUploadForm form) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			return "503 Authorization not granted";

		try {
			filepotService.upload(form.getFile(), Integer.parseInt(id), auth.getName(), form.getNotes());
		} catch (Exception e) {
			return e.getMessage();
		}
		return "";
	}

	@RequestMapping(value="/file/{id}/delete", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteFile(
			@PathVariable("id") String id) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		filepotService.deleteFile(Integer.parseInt(id), userName);
		return "";
	}

	@RequestMapping(value="/admin/file/delete", method = RequestMethod.POST)
	public String adminDeleteFiles(
			@RequestParam(value = "idChecked", required = true) List<String> idfiles) {
		if (idfiles != null) {
			for (String idfileStr : idfiles) {
				filepotService.deleteFile(Integer.valueOf(idfileStr));
			}
		}
		return "redirect:/";
	}

	@RequestMapping(value="logout")
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){
		new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/";
	}

}
