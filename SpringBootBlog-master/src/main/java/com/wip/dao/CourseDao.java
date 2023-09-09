package com.wip.dao;

import com.github.pagehelper.PageInfo;
import com.wip.dto.cond.ContentCond;
import com.wip.model.CourseDomain;
import com.wip.model.RelationShipDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gjd
 * @ClassName CourseDao
 * @description: 教程Dao接口
 * @date 2023年09月09日
 * @version: 1.0
 */
@Mapper
public interface CourseDao {

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
     * @param courseDomain
     */
    void updateCourseById(CourseDomain courseDomain);

    /**
     * 根据条件获取教程列表
     * @param contentCond
     * @return
     */
    List<CourseDomain> getCourseByCond(ContentCond contentCond);

    /**
     * 删除教程
     * @param csid
     */
    void deleteCourseById(Integer csid);

    /**
     * 获取教程总数
     * @return
     */
    Long getCourseCount();

    /**
     * 通过分类获取教程
     * @param category
     * @return
     */
    List<CourseDomain> getCourseByCategory(@Param("category") String category);

    /**
     * 通过标签获取教程
     * @param csid
     * @return
     */
    List<CourseDomain> getCourseByTags(List<RelationShipDomain> csid);

}
