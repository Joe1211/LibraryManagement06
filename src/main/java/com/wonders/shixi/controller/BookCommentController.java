package com.wonders.shixi.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wonders.shixi.pojo.*;
import com.wonders.shixi.service.IBookCommentService;
import com.wonders.shixi.service.impl.BookCommentServiceImpl;
import com.wonders.shixi.service.impl.ReaderServiceImpl;
import com.wonders.shixi.util.RestMsg;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/api/bookcomment")
public class BookCommentController {

    @Autowired
    IBookCommentService bookCommentService;
    ReaderServiceImpl readerService;

    /**
     * 查找该图书所有评论
     * @param bookId
     * @return
     */
    @RequestMapping("/select")
    @ResponseBody
    public List<BookCommentModel> select(int bookId, HttpSession session){
        Reader reader = (Reader) session.getAttribute("reader");
        int readerId = reader.getReaderId();
        List<BookCommentModel> list=bookCommentService.selectAllById(bookId,readerId);
        return list;
    }

    /**
     * 图书评论
     * @param bookId
     * @param readerId
     * @param say
     * @return
     */
    @ApiOperation(value = "添加评论信息", httpMethod = "post")
    @ApiImplicitParams({
            @ApiImplicitParam(name="bookId",value="图书Id",required=true,dataType="int",paramType = "query"),
            @ApiImplicitParam(name="ReaderId",value="读者Id",required=true,dataType="int",paramType = "query"),
            @ApiImplicitParam(name="say",value="评论信息",required=true,dataType="String",paramType = "query")
    })
    @RequestMapping(value="/insert",method= RequestMethod.POST)
    @ResponseBody
    public int insertComment(Integer bookId, Integer readerId,String say){
//        System.out.println(bookId+" "+readerId+" "+say);
        BookComment record=new BookComment();
        record.setBookId(bookId);
        record.setReaderId(readerId);
        record.setComment(say);
        System.out.println(record);
        int num=bookCommentService.intsertComment(record);
        System.out.println(num);
        return num;
    }

    /**
     * 查询所有评论
     * @return
     */
    @GetMapping("/comment")
    @ResponseBody
    public RestMsg<Object> selectCommentAll(@RequestParam(required = false,defaultValue = "1",value = "pn")Integer pn){
        RestMsg<Object> rm = new RestMsg<>();
        //在查询之前传入当前页，然后多少记录
        PageHelper.startPage(pn,10);
        //startPage后紧跟的这个查询就是分页查询
        List<BookComment> list = bookCommentService.selectCommentAll();
        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        PageInfo pageInfo = new PageInfo<>(list,5);
        //pageINfo封装了分页的详细信息，也可以指定连续显示的页数
        if (list.size()!=0){
            rm.setResult(pageInfo);
            return rm.successMsg();
        }else {
            return rm.errorMsg("没有评论");
        }
    }

    @GetMapping("/delete")
    @ResponseBody
    public RestMsg<Object> deleteComment(String id){
        RestMsg<Object> rm = new RestMsg<>();
        int i = bookCommentService.deleteComment(Integer.parseInt(id));
        if (i!=0){
            return rm.successMsg("删除评论成功！");
        }else {
            return  rm.errorMsg("删除评论失败！");
        }
    }

    /**
     * 给评论点赞
     * @param id
     */
    @PostMapping("/like")
    @ResponseBody
    public RestMsg<Object> addLike(@RequestParam("id")int id,HttpSession session){
        RestMsg<Object> rm = new RestMsg<>();
        Reader r = (Reader)session.getAttribute("reader");
        int rid = (int)r.getReaderId();
        BookCommentLike bcl = new BookCommentLike();
        bcl.setBookCommentId(id);
        bcl.setReaderId(rid);
        int i = bookCommentService.addLike(bcl);
        if (i>0){
            return rm.successMsg("点赞成功");
        }
        return rm.errorMsg("点赞失败");
    }

    /**
     * 取消点赞
     */
    @PostMapping("/deletelike")
    @ResponseBody
    public RestMsg<Object> deletelike(int id,HttpSession session){
        RestMsg<Object> rm = new RestMsg<>();
        Reader reader = (Reader)session.getAttribute("reader");
        int rid = reader.getReaderId();
        int i = bookCommentService.deleteLike(id,rid);
        if (i>0){
            return rm.successMsg("点赞成功");
        }
        return rm.errorMsg("点赞失败");
    }
}
