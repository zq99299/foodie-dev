package cn.mrcode.rabbit.task.enums;

import lombok.Getter;

/**
 * @author mrcode
 * @date 2021/11/28 16:35
 */
public enum ElasticJobTypeEnum {
    SIMPLE("SimpleJob", "简单类型job"),
    DATAFLOW("DataflowJob", "流式类型iob"),
    SCRIPT("ScriptJob", "做本类型job");
    @Getter
    private String type;
    @Getter
    private String desc;

    private ElasticJobTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
