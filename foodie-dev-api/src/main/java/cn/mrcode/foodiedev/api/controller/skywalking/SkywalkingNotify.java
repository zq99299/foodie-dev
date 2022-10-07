package cn.mrcode.foodiedev.api.controller.skywalking;

import java.util.List;

/**
 * Skywalking web hook 通知内容
 *
 * @author mrcode
 * @date 2022/10/7 14:23
 */
public class SkywalkingNotify {
    private Integer scopeId;
    private String scope;
    private String id0;
    private String id1;
    private String ruleName;
    private String alarmMessage;
    private Long startTime;
    private List<Tag> tags;

    public class Tag {
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public Integer getScopeId() {
        return scopeId;
    }

    public void setScopeId(Integer scopeId) {
        this.scopeId = scopeId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getId0() {
        return id0;
    }

    public void setId0(String id0) {
        this.id0 = id0;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getAlarmMessage() {
        return alarmMessage;
    }

    public void setAlarmMessage(String alarmMessage) {
        this.alarmMessage = alarmMessage;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "SkywalkingNotify{" +
                "scopeId=" + scopeId +
                ", scope='" + scope + '\'' +
                ", id0='" + id0 + '\'' +
                ", id1='" + id1 + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", alarmMessage='" + alarmMessage + '\'' +
                ", startTime=" + startTime +
                ", tags=" + tags +
                '}';
    }
}