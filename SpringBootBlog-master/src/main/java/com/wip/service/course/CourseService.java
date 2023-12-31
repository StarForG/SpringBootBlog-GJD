package com.wip.service.course;

import com.github.pagehelper.PageInfo;
import com.wip.dto.cond.ContentCond;
import com.wip.model.CourseDomain;
import com.wip.model.MetaDomain;

import java.util.List;

/**
 * @author gjd
 * @ClassName CourseService
 * @description: 教程相关Service接口
 * @date 2023年09月08日
 * @version: 1.0
 */
public interface CourseService {

    /**
     * 添加教程
     * @param courseDomain
     */
    void addCourse(CourseDomain courseDomain);

    /**
     * 根据编号获取教程
     * @param csid
     * @return
     */
    CourseDomain getCourseById(Integer csid);

    /**
     * 更新教程
     * @param course
     */
    void updateCourseById(CourseDomain course);

    /**
     * 根据条件获取教程列表
     * @param contentCond
     * @param page
     * @param limit
     * @return
     */
    PageInfo<CourseDomain> getCourseByCond(ContentCond contentCond, int page, int limit);

    /**
     * 删除教程
     * @param csid
     */
    void deleteCourseById(Integer csid);

    /**
     * 添加教程点击量
     * @param courseDomain
     */
    void updateCourseByCsid(CourseDomain courseDomain);

    /**
     * 通过分类获取教程
     * @param category
     * @return
     */
    List<CourseDomain> getCourseByCategory(String category);

    /**
     * 通过标签获取教程
     * @param tags
     * @return
     */
    List<CourseDomain> getCourseByTags(MetaDomain tags);
}
