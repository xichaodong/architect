package com.tristeza.rabbitmq.task.enums;

/**
 * @author chaodong.xi
 * @date 2020/7/8 3:01 下午
 */
public enum ElasticJobType {
    SIMPLE("SimpleJob", "简单类型job"),
    DATAFLOW("DataflowJob", "流式类型job"),
    SCRIPT("ScriptJob", "脚本类型job");

    private String type;
    private String desc;

    ElasticJobType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
