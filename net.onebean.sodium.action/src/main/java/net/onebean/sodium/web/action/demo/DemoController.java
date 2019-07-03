package net.onebean.sodium.web.action.demo;


import net.onebean.util.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;

@RequestMapping("demo")
@Controller
public class DemoController {

    @RequestMapping("form")
    public String form(){
        return "demo/form";
    }

    @RequestMapping("richtext")
    public String richtext(Model model){
        return "demo/richtext";
    }

    @RequestMapping("calendar")
    public String calendar(Model model){
        return "demo/calendar";
    }

    @RequestMapping("tables")
    public String tables(Model model){
        return "demo/tables";
    }

    @RequestMapping("chart")
    public String chart(Model model){
        return "demo/chart";
    }

    @RequestMapping("tablelist")
    public String tableList(Model model){
        return "demo/tablelist";
    }

    @RequestMapping("tablelistimg")
    public String tableListImg(Model model){
        return "demo/tablelistimg";
    }

    @RequestMapping("upload")
    public String upload(Model model){
        return "demo/upload";
    }

    @RequestMapping("tree")
    public String tree(Model model){
        model.addAttribute("orgId",1);
        model.addAttribute("parentId",1);
        model.addAttribute("id",1);
        model.addAttribute("sysUserId",1);
        return "demo/tree";
    }

    @RequestMapping("datapicker")
    public String datapicker(Model model){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        model.addAttribute("startDate",DateUtils.getDateStrByTimestamp(timestamp));
        model.addAttribute("endDate",DateUtils.getDateStrByTimestampNextDay(timestamp));
        return "demo/datapicker";
    }


}
