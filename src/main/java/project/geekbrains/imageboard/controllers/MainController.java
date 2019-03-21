package project.geekbrains.imageboard.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.geekbrains.imageboard.entities.Message;
import project.geekbrains.imageboard.entities.MessageGroup;
import project.geekbrains.imageboard.entities.MessageImage;
import project.geekbrains.imageboard.entities.Section;
import project.geekbrains.imageboard.services.BoardService;
import project.geekbrains.imageboard.services.ImageSaverService;


import java.security.acl.Group;
import java.util.List;

@Controller
public class MainController {

    private BoardService boardService;
    private ImageSaverService imageSaverService;

    @Autowired
    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }

    @Autowired
    public void setImageSaverService(ImageSaverService imageSaverService) {
        this.imageSaverService = imageSaverService;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        List<Section> allSections = boardService.getAllSections();
        model.addAttribute("sections", allSections);
        model.addAttribute("newsection", new Section());
        return "index";
    }

    @RequestMapping("/threads")
    public String threads(Model model, @RequestParam("section_id") Long section_id) {
        List<MessageGroup> allGroups = boardService.getAllGroups(section_id);
        model.addAttribute("groups", allGroups);

        model.addAttribute("section_id", section_id);

        Message newMessage = new Message();
        newMessage.setUsername("Anonymous");
        model.addAttribute("message", newMessage);
        return "threads";
    }

    @RequestMapping("/one-thread")
    public String oneThread(Model model, @RequestParam("id") Long id) {
        model.addAttribute("thread_id", id);
        MessageGroup group = boardService.getGroupById(id);
        List<Message> threadMessages = group.getMessages();
        model.addAttribute("messages", threadMessages);
        Message newMessage = new Message();
        newMessage.setUsername("Anonymous");
        model.addAttribute("addmessage", newMessage);
        model.addAttribute("section_id", group.getSection().getId());
        return "one-thread";
    }

    @RequestMapping(value = "/add-new-thread", method = RequestMethod.POST)
    public String addNewThread(Model model,
                               @ModelAttribute("message") Message message,
                               @ModelAttribute("section_id") Long section_id,
                               @RequestParam(value = "file", required = false) MultipartFile file) {

        if(!file.isEmpty()) {
            String img_filename = imageSaverService.saveFile(file);
            MessageImage image = new MessageImage();
            image.setFilename(img_filename);
            image.setMessage(message);
            message.setImage(image);
        }
        boardService.createNewGroup(message, section_id);
        return "redirect:/one-thread?id=" + message.getGroup().getId();
    }

    @RequestMapping(value = "/add-message", method = RequestMethod.POST)
    public String addMessage(Model model,
                             @ModelAttribute("addmessage") Message message,
                             @ModelAttribute("thread_id") Long thread_id,
                             @RequestParam(value = "file", required = false) MultipartFile file) {

        // Long id = (Long)model.asMap().get("thread_id");
        if (!file.isEmpty()) {
            String img_filename = imageSaverService.saveFile(file);
            MessageImage image = new MessageImage();
            image.setFilename(img_filename);
            image.setMessage(message);
            message.setImage(image);
        }

        boardService.addMessage(thread_id, message);
        return "redirect:/one-thread?id=" + thread_id;
        // return "redirect:/threads";
    }

    @RequestMapping(value = "/add-new-section", method = RequestMethod.POST)
    public String addNewSection(Model model, @ModelAttribute("newsection") Section section) {

        section.setMessage_counter(new Long(1));

        boardService.addSection(section);
        return "redirect:/";
    }

}
