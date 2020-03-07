package controller.DJX;

import com.mysql.jdbc.Blob;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import po.DJX.*;
import service.DJX.DManagerService;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class GlySignInCheck {


    @Autowired
    private DManagerService service;

    /*跳转到后台登陆页面*/
    @RequestMapping("/Msignup")
    public String toSignUpGly() {
        return "sign-up-gly";
    }

    /*接收ajax信息并放入session
     * 再跳转到后台首页
     * */
    @RequestMapping("/houtai-index")
    public String houtaiIndex(String username, HttpServletRequest request) {
        request.getSession().setAttribute("username", username);
        return "redirect:houtai-xinxitongji";
    }


    /*验证管理员信息*/
    @ResponseBody
    @RequestMapping("/ManagerCheck")
    public String managerCheck(String name, String password) {
//        System.out.println("name="+name+"  password="+password);
        DManager manager = new DManager();
        manager.setA_name(name);
        manager.setA_password(password);
        String nowTime = getNowTime();
        System.out.println(nowTime);
        System.out.println(nowTime + "---成功调用管理员验证程序......");
//        System.out.println(manager.toString());
        return service.managerCheck(manager) ? "1" : "2";
    }

    //    跳转到信息统计页面
    @RequestMapping("/houtai-xinxitongji")
    public String toHoutai(HttpServletRequest request) {
        //验证登陆session
        String username = (String) request.getSession().getAttribute("username");
        request.getSession().setAttribute("username", username);
        //实现数据查询逻辑
        //用户总数、评论总数、书籍总数
        //近一段时间评论数量
//        书籍按类型分类
//        用户性别统计

//        获取用户总数
        int userNum = service.getUserNum();
        int bookNum = service.getBookNum();
        int commentNum = service.getCommentNum();
        int huitieNum = service.getHuitieNum();
        /*获取书籍类型总数*/
        List<DBookType> typeList = service.selectBookType();
        /*根据不同的书籍类型的id获取对应的数量*/
        for (DBookType bookType : typeList) {
            bookType.setTypeNum(service.getTypeNum(bookType.getT_id()));
        }
        /*将整个po类传递到jsp*/
        request.getSession().setAttribute("typeList", typeList);
        request.getSession().setAttribute("userNum", userNum);
        request.getSession().setAttribute("bookNum", bookNum);
        request.getSession().setAttribute("commentNum", commentNum + huitieNum);
        String nowTime = getNowTime();
        /*获取用户性别的男女数量并传递到前端页面*/
        int manNum = service.getUserNanNum();
        int womanNum = service.getUserNvNum();
        request.getSession().setAttribute("manNum", manNum);
        request.getSession().setAttribute("womanNum", womanNum);
        System.out.println(nowTime + "---成功调用后台跳转程序......");
        return "houtai-xinxitongji";
    }


//    /*跳转到用户管理页面*/
//    @RequestMapping("/houtai-yonghuguanli")
//    public String toHouTaiYongHuGuanLi(HttpServletRequest request) {
//        String username = (String) request.getSession().getAttribute("username");
//        request.getSession().setAttribute("username", username);
//
//        return "houtai-yonghuguanli";
//    }

    /*跳转到添加书籍页面*/
    /*获取书籍类型的列表*/
    @RequestMapping("/houtai-tianjiashuji")
    public String toHouTianJiaShuJi(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        request.getSession().setAttribute("username", username);
        List<DBookType> bookTypeList = service.selectBookType();
//        System.out.println("BookType="+bookTypeList);
        request.getSession().setAttribute("bookTypeList", bookTypeList);
        return "houtai-tianjiashuji";
    }

//    /*跳转到书籍管理页面*/
//    @RequestMapping("/houtai-shujiguanli")
//    public String toHouTaiShuJiFuanLi(HttpServletRequest request) {
//        String username = (String) request.getSession().getAttribute("username");
//        request.getSession().setAttribute("username", username);
//        return "houtai-shujiguanli";
//    }

//    /*跳转到举报信息管理页面*/
//    @RequestMapping("/houtai-jubaoxinxiguanli")
//    public String toHouTaiJuBaoXinXiGuanLi(HttpServletRequest request) {
//        String username = (String) request.getSession().getAttribute("username");
//        request.getSession().setAttribute("username", username);
//        return "houtai-jubaoxinxiguanli";
//    }


    /*跳转到添加书籍类型的界面*/
    @RequestMapping("/houtai-tianjiashujileixing")
    public String toHoutaiTianjiaShujiLeixing(HttpServletRequest request) {
        request.getSession().setAttribute("username", request.getSession().getAttribute("username"));
        return "houtai-tianjiashujileixing";
    }

    /* * 添加书籍类型
     * 添加成功之后跳转到添加书籍类型页面
     *
     * */
    @RequestMapping("/addBookType")
    public String addBookType(String t_type, HttpServletRequest request) {
        request.getSession().setAttribute("username", request.getSession().getAttribute("username"));
        service.addBookType(t_type);
        System.out.println(">>>添加成功");
        return "redirect:/houtai-tianjiashujileixing";
    }

    /*判断类型是否重复*/
    @ResponseBody
    @RequestMapping("/sameType")
    public String sameType(String t_type, HttpServletRequest request) {
        request.getSession().setAttribute("username", request.getSession().getAttribute("username"));
        DBookType bookType = new DBookType();
        bookType.setT_type(t_type);
//        System.out.println("t_type="+t_type);
        int f = service.sameType(bookType);
        System.out.println(">>>sameType方法已运行......");
        return String.valueOf(f);
    }

    /*获取当前系统时间*/
    public String getNowTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(format.format(date));
        return format.format(date);
    }

    /*
     * 添加书籍
     * */
    /*
     *
     * 添加书籍两步：
     * 1、书籍表的内容直接存储进数据库，通过action方式
     * 2、书籍的类型通过ajax返回到后台存储到book_type表
     *
     */
    @ResponseBody
    @RequestMapping("/addBook")
    public String addBook(DBook book, HttpServletRequest request) throws IOException {
        request.getSession().setAttribute("username", request.getSession().getAttribute("username"));
        System.out.println(">>>");
//        System.out.println(book.toString());
//        System.out.println(">>>b_cover="+book.getB_cover());
        service.addBook(book);
        System.out.println("书籍信息添加成功......");
//        return "redirect:/houtai-tianjiashuji";
        return "ok";
    }

    /*添加书籍的对应类型*/
    /*先根据书籍的名称或者是其他的信息，获取到书籍的id，然后将书籍的id和类型依次存储到book_type表*/
    @RequestMapping("/saveBookType")
    public String saveBookType(HttpServletRequest request, String book_name, String b_type) {
        request.getSession().setAttribute("username", request.getSession().getAttribute("username"));
//        System.out.println(">>>");
//        System.out.println(b_type);
        int b_id = service.selectBookIdByBookName(book_name);

//        System.out.println("book_name="+book_name+",b_id="+b_id);

        String[] type = b_type.split(",");
        for (String s : type) {
            DBook_Type book_type = new DBook_Type();
            book_type.setBook_id(b_id);
            book_type.setType_id(Integer.parseInt(s));
            service.saveBookType(book_type);
        }
        System.out.println(">>>");
        System.out.println("书籍对应类型存储成功......");
        return "redirect:/houtai-tianjiashuji";
    }

    /*判断书名是否有重复*/
    @ResponseBody
    @RequestMapping("/sameBookName")
    public String sameBookName(String bookName) {
        boolean f = service.sameBookName(bookName);
        return f ? "0" : "1";
    }

    /*获取书籍列表*/
    /*同时跳转到书籍列表界面*/
    @RequestMapping("/houtai-shujiguanli")
    public String selectAllBook(HttpServletRequest request, Integer pageNumber) {
        request.getSession().setAttribute("username", request.getSession().getAttribute("username"));
        Integer pg = pageNumber;
        if ("".equals(String.valueOf(pg)) || pg == null) {
            pg = 1;
        }
        List<DBook> bookList = service.selectAllBook((pg - 1) * 10);
        int totalSize = service.getBookNum() / 10;
        if (service.getBookNum() % 10 != 0) {
            totalSize++;
        }
        List<DBookList> bookLists = new ArrayList<>();
        for (DBook book : bookList) {
            List<String> bookType = getTypeByBookId(book.getB_id());
//            String typeList = StringUtils.strip(bookType.toString(),"[]");
//            String base64 = convertBlobToBase64String((Blob) book.getB_cover());
            String res = new String((byte[]) book.getB_cover());
//            System.out.println(res);
            book.setB_cover(res);
            DBookList book_list = new DBookList();
            book_list.setTypeList(bookType);
            book_list.setBook(book);
            bookLists.add(book_list);
        }
        request.getSession().setAttribute("totalSize", totalSize);
        request.getSession().setAttribute("bookList", bookLists);
        request.getSession().setAttribute("pageNumber", pg);
        System.out.println(">>>");
        System.out.println("跳转到书籍管理界面成功......");
        return "houtai-shujiguanli";
    }

    /*根据书籍的id获取类型的id*/
    /*类型的id是一个数组*/
    /*根据类型的ids获取一个类型的String列表*/
    public List<String> getTypeByBookId(Integer b_id) {
        List<Integer> type_id = service.getTypeId(b_id);
        List<String> typeList = new ArrayList<>();
        for (Integer i : type_id) {
            String t_type = service.getTypeByTypeId(i);
            typeList.add(t_type);
        }
        return typeList;
    }

    /*获取所有的bookType类型并跳转到书籍类型管理页面*/
    @RequestMapping("/houtai-shujileixingguanli")
    public String toHouTaiShuJiLeiXingGuanLi(HttpServletRequest request, Integer pageNumber) {
        Integer pg = pageNumber;
        if ("".equals(String.valueOf(pg)) || pg == null) {
            pg = 1;
        }
        request.getSession().setAttribute("username", request.getSession().getAttribute("username"));
        List<DBookType> bookTypes = service.selectAllBookType((pg - 1) * 15);
        /*获取list的大小以便前端分页*/
        /*前端页面的大小为每页15条数据，先将总的页数计算好，再传给前端*/
        int typeSum = service.selectTypeSum();
        int totalSize = typeSum / 15;
        if (typeSum % 15 != 0) {
            totalSize++;
        }
//        System.out.println(totalSize);
        System.out.println(">>>");
//        System.out.println(totalSize);
        request.getSession().setAttribute("typeList", bookTypes);
        request.getSession().setAttribute("totalSize", totalSize);
        request.getSession().setAttribute("pageNumber", pg);
        System.out.println("跳转到书籍类型页面方法执行成功......");
        return "houtai-shujileixingguanli";
    }

    /*获取书籍的id，将书籍信息获取到编辑页面*/
    @RequestMapping("/EditBook")
    public String editBook(Integer b_id, HttpServletRequest request) {
        request.getSession().setAttribute("username", request.getSession().getAttribute("username"));
        List<DBook> booklist = service.selectAllBook(b_id);
        DBookList bookList = new DBookList();
        DBook book = booklist.get(0);
        String res = new String((byte[]) book.getB_cover());
        book.setB_cover(res);
        bookList.setBook(book);
        List<String> bookType = getTypeByBookId(b_id);
        bookList.setTypeList(bookType);
        List<DBookType> bookTypes = service.selectBookType();
        request.getSession().setAttribute("bookTypeList", bookTypes);
        request.getSession().setAttribute("bookInfo", bookList);
        return "houtai-bianjishuji";
    }


    /*根据类型id删除类型*/
    @RequestMapping("/deleteType")
    public String deleteType(Integer t_id) {
        service.deleteType(t_id);
        System.out.println(">>>");
        System.out.println("删除类型成功......");
        return "redirect:/houtai-shujileixingguanli";
    }

    /*处理书籍的编辑信息*/
    /*根据书籍id删除相关的书籍--类型对应信息*/
    /*更新书籍的基本信息（书籍的类型在原有的地方添加）*/
    /*该方法在存储书籍类型之前实现*/
    @ResponseBody
    @RequestMapping("/EditBookPro")
    public String editBookPro(Integer b_id, DBook book) {
        service.deleteBookType(b_id);
        service.updateBookInfo(book);
        System.out.println(">>>");
        System.out.println("书籍更新成功......");
        return "ok";
    }

    /*获取用户的所有的信息*/
    /*跳转到用户管理界面*/
    @RequestMapping("/houtai-yonghuguanli")
    public String toYongHuGuanLi(HttpServletRequest request) {
        request.getSession().setAttribute("username", request.getSession().getAttribute("username"));
        List<DUserinfo> userinfos = service.getAllUserInfo();
        request.getSession().setAttribute("userInfo", userinfos);
        return "houtai-yonghuguanli";
    }

    /*用户禁言*/
    @ResponseBody
    @RequestMapping("/Forbid")
    public String userForbid(Integer u_id) {
        service.forbidUser(u_id);
        System.out.println(">>>");
        System.out.println(u_id + "禁言");
        return "ok";
    }

    /*解除用户禁言*/
    @ResponseBody
    @RequestMapping("/UnForbid")
    public String unForbid(Integer u_id) {
        service.unForbidUser(u_id);
        System.out.println(">>>");
        System.out.println(u_id + "解除禁言");
        return "OK";
    }

    /*根据书籍的id删除书籍*/
    @ResponseBody
    @RequestMapping("/deleteBook")
    public String deleteBook(Integer b_id) {
        service.deleteBook(b_id);
        System.out.println(">>>");
        System.out.println("书籍删除成功......");
        return "OK";
    }

    /*跳转到用户登录界面*/
    @RequestMapping("/USignUp")
    public String userSignUp() {
        return "sign-up-yh";
    }

    /*判断数据库中是否有该用户的相关信息*/
    /*通过用户名和用户密码判断是否有记录存在*/
    @ResponseBody
    @RequestMapping("/CheckUserName")
    public String checkUserName(DUserinfo userinfo){
        Integer userNum = service.checkUserInfo(userinfo);
        System.out.println(">>>");
        System.out.println("判断用户名成功......");
//        System.out.println(userNum);
        return String.valueOf(userNum);
    }


    /*用于登录界面和用户登陆后的主界面之间的跳转*/
    /*隐藏用户名等信息*/
    /*根据用户名获取用户id*/
    /*将用户名和用户id一起传递到jsp*/
    @RequestMapping("/toHomeUser")
    public String toHomeUserPre(String userName, HttpServletRequest request) {
        Integer u_id = service.getUidByUname(userName);
        request.getSession().setAttribute("userId",u_id);
        request.getSession().setAttribute("userName",userName);
        System.out.println(userName);
        System.out.println(">>>");
        System.out.println("跳转到首页成功......");
        return "home_page";
    }

//    /*跳转到用户首页*/
//    @RequestMapping("/HomeUser")
//    public String toHomeUser(HttpServletRequest request) {
////        request.getSession().setAttribute("userId",request.getSession().getAttribute("userId"));
////        request.getSession().setAttribute("userName",request.getSession().getAttribute("username"));
////        System.out.println(request.getSession().getAttribute("username"));
//        System.out.println(">>>");
////        System.out.println("跳转到首页成功......");
////        return "home_user";
////    }

    /*判断用户名是否重复*/
    /*如果已存在返回的是1，否则返回的是0*/
    @ResponseBody
    @RequestMapping("/sameUserName")
    public String sameuserName(String u_name) {
        System.out.println(">>>");
        System.out.println("查询是否有相同的用户名......");
        return String.valueOf(service.sameUserName(u_name));
    }

    /*添加用户信息*/
    /*添加成功之后，跳转到用户登录界面*/
    @ResponseBody
    @RequestMapping("/addUserInfo")
    public String addUserInfo(DUserinfo userinfo) {
        userinfo.setU_address(null);
        userinfo.setU_forbid("0");
        userinfo.setU_sex(null);
        service.addUser(userinfo);
        System.out.println(">>>");
        System.out.println("添加用户信息成功......");
        return "OK";
    }

    /*跳转到comment读后感列表页面*/
    /*获取所有的读后感*/
    /*获取bookType类型，同时将bookType类型存入session*/
    /*获取session里面的userId和userName*/
    @RequestMapping("/comment")
    public String toComment(Integer book_id,Integer pageNum,HttpServletRequest request) {
        request.getSession().setAttribute("userId", request.getSession().getAttribute("userId"));
        request.getSession().setAttribute("username", request.getSession().getAttribute("userName"));
        /*获取所有长评*/
        if ("".equals(String.valueOf(pageNum))||pageNum==null) {
            pageNum=1;
        }
        List<DLongComm> longCommList = service.getAllLongComm( book_id, (pageNum-1)*6);
        request.getSession().setAttribute("longCommList", longCommList);
        /*获取所有书籍类型并存储到session*/
        List<DBookType> bookTypeList = service.selectBookType();
        request.getSession().setAttribute("bookTypeList", bookTypeList);
        /*获取长评总数*/
        /*每页6条数据*/
        int longCommNum = service.getLongCommNum();
        int totalPage = longCommNum/6;
        if (longCommNum % 6 != 0) {
            totalPage++;
        }
        request.getSession().setAttribute("totalPage",totalPage);
        request.getSession().setAttribute("pageNum",pageNum);
        request.getSession().setAttribute("book_id",book_id);
        return "comment";
    }

    /*添加长评*/
    @RequestMapping("/addLongComm")
    public String addLongComm(DLongComm comm) {
        String lc_time = getNowTime();
        comm.setLc_click(0);
        comm.setLc_content(0);
        comm.setLc_time(lc_time);
        System.out.println(">>>");
        System.out.println("LongComm"+comm.toString());
        return "/comment";
    }
}
