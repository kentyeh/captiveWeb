package captive.context;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Kent Yeh
 */
@Controller
public class EchoController {

    @RequestMapping("/echo/{msg}")
    public String echo1(@PathVariable String msg, Model model) {
        model.addAttribute("msg", msg);
        return "echo";
    }
    /**
     * value 不能使用 echo，在測試中會與回傳view name的 echo 混淆
     * @param msg
     * @param model
     * @return 
     */
    @RequestMapping(value = "/echom", params = {"msg"}, produces = "text/html;charset=UTF-8")
    public String echo2(@RequestParam String msg, Model model) {
        model.addAttribute("msg", msg);
        return "echo";
    }
}
