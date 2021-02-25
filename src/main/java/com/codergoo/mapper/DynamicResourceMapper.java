package com.codergoo.mapper;

import com.codergoo.domain.DynamicResource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
}
