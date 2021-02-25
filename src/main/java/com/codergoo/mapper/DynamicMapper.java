package com.codergoo.mapper;

import com.codergoo.domain.Dynamic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 动态持久层
 *
 * @author coderGoo
 * @date 2021/2/24
 */
@Mapper
@Repository
public interface DynamicMapper {
    
    // 添加动态
    @Insert({
            "insert into scp_dynamic(",
            "id, uid, content, type, camouflage, tag, permissions, status, address, show_address, time",
            ")",
            "values(",
            "#{id}, #{uid}, #{content}, #{type}, #{camouflage}, #{tag}, #{permissions}, #{status}, #{address}, #{showAddress}, #{time}",
            ")"
    })
    Integer addDynamic(Dynamic dynamic);
    
    // 获取最大的id
    @Select("select max(id) from scp_dynamic")
    Integer getMaxId();
}
