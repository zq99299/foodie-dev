package cn.mrcode.rabbit.common.serializer.impl;

import cn.mrcode.rabbit.common.serializer.Serializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Jackson 的序列化和反序列化
 *
 * @author mrcode
 * @date 2021/10/26 22:12
 */
public class JacksonSerializer implements Serializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonSerializer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // 禁用缩进功能：不打印出漂亮的格式化字符串
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        // 遇到未知属性时，是否抛出异常
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 可以启用以使用反斜杠引用机制接受所有字符的引用的功能：如果未启用，则只能转义 JSON 规范明确列出的字符（请参阅 JSON 规范以获取这些字符的小列表）
        mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        // 确定解析器是否允许在解析的内容中使用 Java/C++ 样式注释（'/'+'*' 和 '//' 变体）的功能。
        // 由于 JSON 规范没有将注释作为合法结构提及，因此这是一个非标准特性； 然而，在野外这被广泛使用。 因此，解析器默认禁用功能，必须显式启用。
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        // 允许解析器将一组“非数字”(NaN) 标记识别为合法浮点值的功能（类似于许多其他数据格式和编程语言源代码允许它）
        mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        // 确定解析器是否允许 JSON 整数以附加（可忽略）零开头的功能（例如：000001）。 如果启用，则不会引发异常，并且会默默忽略额外的空值（并且不包含在通过getText公开的文本表示中）。
        // 由于 JSON 规范不允许前导零，因此这是一项非标准功能，因此默认情况下禁用
        mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        // 确定解析器是否允许使用单引号（撇号、字符“\”）来引用字符串（名称和字符串值）的功能。 如果是这样，这是对其他可接受的标记的补充。 但不是 JSON 规范）。
        // 由于 JSON 规范要求对字段名称使用双引号，因此这是一项非标准功能，因此默认情况下禁用。
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 确定解析器是否允许使用不带引号的字段名称的功能（Javascript 允许，但 JSON 规范不允许）。
        // 由于 JSON 规范要求对字段名称使用双引号，因此这是一项非标准功能，因此默认情况下禁用。
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 确定解析器是否允许 JSON 字符串包含未加引号的控制字符（值小于 32 的 ASCII 字符，包括制表符和换行符）的功能。 如果 feature 设置为 false，则在遇到此类字符时会引发异常。
        // 由于 JSON 规范要求引用所有控制字符，因此这是一项非标准功能，因此默认情况下禁用。
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    private final JavaType type;

    /**
     * @param type 传递一个 class ，反序列化时，会将消息反序列化为该 class 对应的对象
     */
    private JacksonSerializer(Type type) {
        this.type = mapper.getTypeFactory().constructType(type);
    }

    public static JacksonSerializer createParametricType(Class<?> cls) {
        return new JacksonSerializer(cls);
    }

    @Override
    public byte[] serializerRaw(Object data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            LOGGER.error("序列化出错", e);
        }
        return null;
    }

    @Override
    public String serialize(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            LOGGER.error("序列化出错", e);
        }
        return null;
    }

    @Override
    public <T> T deserialize(String content) {
        try {
            return mapper.readValue(content, type);
        } catch (JsonProcessingException e) {
            LOGGER.error("反序列化出错", e);
        }
        return null;
    }

    @Override
    public <T> T deserialize(byte[] content) {
        try {
            return mapper.readValue(content, type);
        } catch (IOException e) {
            LOGGER.error("反序列化出错", e);
        }
        return null;
    }
}
