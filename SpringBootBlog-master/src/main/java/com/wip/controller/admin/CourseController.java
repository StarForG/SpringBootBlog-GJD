package com.wip.controller.admin;

import com.github.pagehelper.PageInfo;
import com.wip.constant.LogActions;
import com.wip.constant.Types;
import com.wip.controller.BaseController;
import com.wip.dto.cond.ContentCond;
import com.wip.dto.cond.MetaCond;
import com.wip.model.ContentDomain;
import com.wip.model.CourseDomain;
import com.wip.model.MetaDomain;
import com.wip.service.course.CourseService;
import com.wip.service.log.LogService;
import com.wip.service.meta.MetaService;
import com.wip.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author gjd
 * @ClassName CourseController
 * @description: 教程控制器
 * @date 2023年09月08日
 * @version: 1.0
 */
@Api("教程管理")
@Controller
@RequestMapping("/admin/course")
public class CourseController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private MetaService metaService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LogService logService;

    /**
     * 跳转教程管理界面
     * @param request
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation("教程页")
    @GetMapping(value = "")
    public String index_cs(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "页数", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
            int page,
            @ApiParam(name = "limit", value = "每页数量", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "15")
            int limit
    ) {
        PageInfo<CourseDomain> courses = courseService.getCourseByCond(new ContentCond(), page, limit);
        request.setAttribute("courses",courses);
        return "admin/course_list";
    }

    /**
     * 跳转发布新教程界面
     * @param request
     * @return
     */
    @ApiOperation("发布新教程页")
    @GetMapping(value = "/pub_course")
    public String newCourse(HttpServletRequest request) {
        MetaCond metaCond = new MetaCond();
        metaCond.setType(Types.CATEGORY.getType());
        List<MetaDomain> metas = metaService.getMetas(metaCond);
        request.setAttribute("categories",metas);
        return "admin/course_edit";
    }

    /**
     * 编辑教程
     * @param csid
     * @param request
     * @return
     */
    @ApiOperation("教程编辑页")
    @GetMapping(value = "/{csid}")
    public String editArticle(
            @ApiParam(name = "csid", value = "教程编号", required = true)
            @PathVariable
            Integer csid,
            HttpServletRequest request
    ) {
        CourseDomain course = courseService.getCourseById(csid);
        request.setAttribute("courses", course);
        MetaCond metaCond = new MetaCond();
        metaCond.setType(Types.CATEGORY.getType());
        List<MetaDomain> categories = metaService.getMetas(metaCond);
        request.setAttribute("categories", categories);
        request.setAttribute("active", "course");
        return "admin/course_edit";
    }

    /**
     * 编辑保存教程
     * @param request
     * @param csid
     * @param title
     * @param titlePic
     * @param slug
     * @param content
     * @param type
     * @param status
     * @param tags
     * @param categories
     * @param allowComment
     * @return
     */
    @ApiOperation("编辑保存教程")
    @PostMapping("/modify_cs")
    @ResponseBody
    public APIResponse modifyCourse(
            HttpServletRequest request,
            @ApiParam(name = "csid", value = "教程主键", required = true)
            @RequestParam(name = "csid", required = true)
            Integer csid,
            @ApiParam(name = "title", value = "标题", required = true)
            @RequestParam(name = "title", required = true)
            String title,
            @ApiParam(name = "titlePic", value = "标题图片", required = false)
            @RequestParam(name = "titlePic", required = false)
            String titlePic,
            @ApiParam(name = "slug", value = "内容缩略名", required = false)
            @RequestParam(name = "slug", required = false)
            String slug,
            @ApiParam(name = "content", value = "内容", required = true)
            @RequestParam(name = "content", required = true)
            String content,
            @ApiParam(name = "type", value = "教程类型", required = true)
            @RequestParam(name = "type", required = true)
            String type,
            @ApiParam(name = "status", value = "教程状态", required = true)
            @RequestParam(name = "status", required = true)
            String status,
            @ApiParam(name = "tags", value = "标签", required = false)
            @RequestParam(name = "tags", required = false)
            String tags,
            @ApiParam(name = "categories", value = "分类", required = false)
            @RequestParam(name = "categories", required = false, defaultValue = "默认分类")
            String categories,
            @ApiParam(name = "allowComment", value = "是否允许评论", required = true)
            @RequestParam(name = "allowComment", required = true)
            Boolean allowComment
    ) {
        CourseDomain courseDomain = new CourseDomain();
        courseDomain.setTitle(title);
        courseDomain.setCsid(csid);
        courseDomain.setTitlePic(titlePic);
        courseDomain.setSlug(slug);
        courseDomain.setContent(content);
        courseDomain.setType(type);
        courseDomain.setStatus(status);
        courseDomain.setTags(tags);
        courseDomain.setCategories(categories);
        courseDomain.setAllowComment(allowComment ? 1: 0);
        courseService.updateCourseById(courseDomain);

        return APIResponse.success();
    }

    @ApiOperation("发布新教程")
    @PostMapping(value = "/publish")
    @ResponseBody
    public APIResponse publishCourse(
            @ApiParam(name = "title", value = "标题", required = true)
            @RequestParam(name = "title", required = true)
            String title,
            @ApiParam(name = "titlePic", value = "标题图片", required = false)
            @RequestParam(name = "titlePic", required = false)
            String titlePic,
            @ApiParam(name = "slug", value = "内容缩略名", required = false)
            @RequestParam(name = "slug", required = false)
            String slug,
            @ApiParam(name = "content", value = "内容", required = true)
            @RequestParam(name = "content", required = true)
            String content,
            @ApiParam(name = "type", value = "教程类型", required = true)
            @RequestParam(name = "type", required = true)
            String type,
            @ApiParam(name = "status", value = "教程状态", required = true)
            @RequestParam(name = "status", required = true)
            String status,
            @ApiParam(name = "categories", value = "教程分类", required = false)
            @RequestParam(name = "categories", required = false, defaultValue = "默认分类")
            String categories,
            @ApiParam(name = "tags", value = "教程标签", required = false)
            @RequestParam(name = "tags", required = false)
            String tags,
            @ApiParam(name = "allowComment", value = "是否允许评论", required = true)
            @RequestParam(name = "allowComment", required = true)
            Boolean allowComment
    ) {
        CourseDomain courseDomain = new CourseDomain();
        courseDomain.setTitle(title);
        courseDomain.setTitlePic(titlePic);
        courseDomain.setSlug(slug);
        courseDomain.setContent(content);
        courseDomain.setType(type);
        courseDomain.setStatus(status);
        courseDomain.setHits(1);
        courseDomain.setCommentsNum(0);
        // 只允许博客教程有分类，防止作品被收入分类
        courseDomain.setTags(type.equals(Types.ARTICLE.getType()) ? tags : null);
        courseDomain.setCategories(type.equals(Types.ARTICLE.getType()) ? categories : null);
        courseDomain.setAllowComment(allowComment ? 1 : 0);

        // 添加教程
        courseService.addCourse(courseDomain);

        return APIResponse.success();
    }

    @ApiOperation("删除教程")
    @PostMapping("/delete_cs")
    @ResponseBody
    public APIResponse deleteCourse(
            @ApiParam(name = "csid", value = "教程ID", required = true)
            @RequestParam(name = "csid", required = true)
            Integer csid,
            HttpServletRequest request
    ) {
        // 删除教程
        courseService.deleteCourseById(csid);
        // 写入日志
        logService.addLog(LogActions.DEL_ARTICLE.getAction(), csid+"",request.getRemoteAddr(),this.getUid(request));
        return APIResponse.success();
    }


}
