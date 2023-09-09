/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * DateTime: 2018/7/24 23:07
 **/
package com.wip.dao;

import com.wip.model.RelationShipDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章和项目关联表
 */
@Mapper
public interface RelationShipDao {

    /**
     * 根据meta编号获取关联
     * @param mid
     * @return
     */
    List<RelationShipDomain> getRelationShipByMid(Integer mid);
    List<RelationShipDomain> getRelationShipByMid2(Integer mid);

    /**
     * 根据meta编号删除关联
     * @param mid
     */
    void deleteRelationShipByMid(Integer mid);
    void deleteRelationShipByMid2(Integer mid);

    /**
     * 获取数量
     * @param cid
     * @param mid
     * @return
     */
    Long getCountById(@Param("cid") Integer cid, @Param("mid") Integer mid);

    /**
     * 获取教程数量
     * @param csid
     * @param mid
     * @return
     */
    Long getCountById2(@Param("csid") Integer csid, @Param("mid") Integer mid);

    /**
     * 添加
     * @param relationShip
     * @return
     */
    int addRelationShip(RelationShipDomain relationShip);
    int addRelationShip2(RelationShipDomain relationShip);

    /**
     * 根据文章编号删除关联
     * @param cid
     */
    void deleteRelationShipByCid(int cid);

    /**
     * 根据教程编号删除关联
     * @param csid
     */
    void deleteRelationShipByCsid(int csid);

    /**
     * 根据文章ID获取关联
     * @param cid
     */
    List<RelationShipDomain> getRelationShipByCid(Integer cid);

    /**
     * 根据教程ID获取关联
     * @param csid
     * @return
     */
    List<RelationShipDomain> getRelationShipByCsid(Integer csid);
}
