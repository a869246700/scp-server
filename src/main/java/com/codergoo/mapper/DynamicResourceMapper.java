package com.codergoo.mapper;

import com.codergoo.domain.DynamicResource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 动态资源持久层
 *
 * @author coderGoo
 * @date 2021/2/24
 */
@Mapper
@Repository
public interface DynamicResourceMapper {
    
    // 添加动态资源
    @Insert({
            "insert into scp_dynamic_resource(",
            "did, src, type",
            ")",
            "values(",
            "#{did}, #{src}, #{type}",
            ")"
    })
    Integer addDynamicResource(DynamicResource dynamicResource);
    
    // 移除动态资源
    @Delete("delete from scp_dynamic_resource where id = #{id}")
    Integer removeDynamicResource(Integer id);
    
    @Select("select * from scp_dynamic_resource where id = #{id}")
    DynamicResource findById(Integer id);
    
    // 根据动态did获取动态资源列表
    @Select("select * from scp_dynamic_resource where did = #{did}")
    List<DynamicResource> listByDid(Integer did);
}
