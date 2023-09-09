package com.wip.service.course.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.constant.ErrorConstant;
import com.wip.constant.Types;
import com.wip.constant.WebConst;
import com.wip.dao.CommentDao;
import com.wip.dao.CourseDao;
import com.wip.dao.RelationShipDao;
import com.wip.dto.cond.ContentCond;
import com.wip.exception.BusinessException;
import com.wip.model.CommentDomain;
import com.wip.model.CourseDomain;
import com.wip.model.MetaDomain;
import com.wip.model.RelationShipDomain;
import com.wip.service.course.CourseService;
import com.wip.service.meta.MetaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gjd
 * @ClassName CourseServiceImpl
 * @description: TODO
 * @date 2023年09月08日
 * @version: 1.0
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private MetaService metaService;

    @Autowired
    private RelationShipDao relationShipDao;

    @Autowired
    private CommentDao commentDao;

    /**
     * 添加教程
     * @param courseDomain
     */
    @Transactional
    @Override
    @CacheEvict(value = {"courseCache", "courseCaches"}, allEntries = true, beforeInvocation = true)
    public void addCourse(CourseDomain courseDomain) {

        if (null == courseDomain)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);

        if (StringUtils.isBlank(courseDomain.getTitle()))
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_CAN_NOT_EMPTY);

        if (courseDomain.getTitle().length() > WebConst.MAX_TITLE_COUNT)
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_IS_TOO_LONG);

        if (StringUtils.isBlank(courseDomain.getContent()))
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_CAN_NOT_EMPTY);

        if (courseDomain.getContent().length() > WebConst.MAX_CONTENT_COUNT)
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_IS_TOO_LONG);

        // 取到标签和分类
        String tags = courseDomain.getTags();
        String categories = courseDomain.getCategories();

        // 添加教程
        courseDao.addCourse(courseDomain);

        // 添加分类和标签
        int csid = courseDomain.getCsid();
        metaService.addMetas2(csid, tags, Types.TAG.getType());
        metaService.addMetas2(csid, categories, Types.CATEGORY.getType());
    }

    /**
     * 通过ID获取课程
     * @param csid
     * @return
     */
    @Override
    @Cacheable(value = "courseCache", key = "'courseById_' + #p0")
    public CourseDomain getCourseById(Integer csid) {
        if (null == csid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return courseDao.getCourseById(csid);
    }

    /**
     * 更新课程
     * @param courseDomain
     */
    @Override
    @Transactional
    @CacheEvict(value = {"courseCache", "courseCaches"}, allEntries = true, beforeInvocation = true)
    public void updateCourseById(CourseDomain courseDomain) {
        // 标签和分类
        String tags = courseDomain.getTags();
        String categories = courseDomain.getCategories();

        // 更新文章
        courseDao.updateCourseById(courseDomain);
        int csid = courseDomain.getCsid();
        relationShipDao.deleteRelationShipByCsid(csid);
        metaService.addMetas2(csid,tags,Types.TAG.getType());
        metaService.addMetas2(csid,categories,Types.CATEGORY.getType());
    }

    /**
     * 根据条件获取教程列表
     * @param contentCond
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    @Cacheable(value = "courseCaches", key = "'coursesByCond_' + #p1 + 'type_' + #p0.type")
    public PageInfo<CourseDomain> getCourseByCond(ContentCond contentCond, int pageNum, int pageSize) {
        if (null == contentCond)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        PageHelper.startPage(pageNum,pageSize);
        List<CourseDomain> contents = courseDao.getCourseByCond(contentCond);
        PageInfo<CourseDomain> pageInfo = new PageInfo<>(contents);
        return pageInfo;
    }

    /**
     * 删除教程
     * @param csid
     */
    @Override
    @Transactional
    @CacheEvict(value = {"courseCache", "courseCaches"}, allEntries = true, beforeInvocation = true)
    public void deleteCourseById(Integer csid) {
        if (null == csid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        // 删除教程
        courseDao.deleteCourseById(csid);

        // 同时要删除该教程下的所有评论
        List<CommentDomain> comments = commentDao.getCommentByCsId(csid);
        if (null != comments && comments.size() > 0) {
            comments.forEach(comment -> {
                commentDao.deleteComment(comment.getCoid());
            });
        }

        // 删除标签和分类关联
        List<RelationShipDomain> relationShips = relationShipDao.getRelationShipByCsid(csid);
        if (null != relationShips && relationShips.size() > 0) {
            relationShipDao.deleteRelationShipByCsid(csid);
        }
    }

    /**
     * 更新教程
     * @param course
     */
    @Override
    @CacheEvict(value = {"courseCache", "courseCaches"}, allEntries = true, beforeInvocation = true)
    public void updateCourseByCsid(CourseDomain course) {
        if (null != course && null != course.getCsid()) {
            courseDao.updateCourseById(course);
        }
    }

    /**
     * 通过分类获取教程
     * @param category
     * @return
     */
    @Override
    @Cacheable(value = "courseCache", key = "'courseByCategory_' + #p0")
    public List<CourseDomain> getCourseByCategory(String category) {
        if (null == category)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return courseDao.getCourseByCategory(category);
    }

    /**
     * 通过标签获取教程
     * @param tags
     * @return
     */
    @Override
    @Cacheable(value = "courseCache", key = "'courseByCategory_'+ #p0")
    public List<CourseDomain> getCourseByTags(MetaDomain tags) {
        if (null == tags)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        List<RelationShipDomain> relationShip = relationShipDao.getRelationShipByMid2(tags.getMid());
        if (null != relationShip && relationShip.size() > 0) {
            return courseDao.getCourseByTags(relationShip);
        }
        return null;
    }
}
