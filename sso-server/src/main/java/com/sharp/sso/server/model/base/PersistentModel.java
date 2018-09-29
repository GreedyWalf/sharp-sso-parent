package com.sharp.sso.server.model.base;

import java.io.Serializable;

/**
 * 持久化基类
 *
 * @author QinYupeng
 * @since 2018-09-26 19:57:28
 */
public class PersistentModel implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
